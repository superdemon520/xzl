package cn.xinzhili.api.doctor.service;

import cn.xinzhili.api.doctor.bean.AdviceCategory;
import cn.xinzhili.api.doctor.bean.ConsultationInfo;
import cn.xinzhili.api.doctor.bean.DoctorApiMessage;
import cn.xinzhili.api.doctor.bean.DoctorMessage;
import cn.xinzhili.api.doctor.bean.InquiryNotifyContentData;
import cn.xinzhili.api.doctor.bean.MedicineAdjustmentStatus;
import cn.xinzhili.api.doctor.bean.NoticeContentType;
import cn.xinzhili.api.doctor.bean.StatisticAdviceDateType;
import cn.xinzhili.api.doctor.bean.UserRole;
import cn.xinzhili.api.doctor.bean.request.AddConsultationApiRequest;
import cn.xinzhili.api.doctor.bean.request.MedicationPlan.Status;
import cn.xinzhili.api.doctor.bean.request.SendDoctorAdviceRequest;
import cn.xinzhili.api.doctor.bean.request.UpdateConsultationApiRequest;
import cn.xinzhili.api.doctor.bean.request.UpdateDoctorAdviceClientRequest;
import cn.xinzhili.api.doctor.bean.request.UpdateMessageRequest;
import cn.xinzhili.api.doctor.bean.request.UpdateReadMessageRequest;
import cn.xinzhili.api.doctor.bean.response.ConsultationListResponse;
import cn.xinzhili.api.doctor.bean.response.DoctorAdviceDataResponse;
import cn.xinzhili.api.doctor.bean.response.DoctorAdviceListResponse;
import cn.xinzhili.api.doctor.bean.response.DoctorAdviceResponse;
import cn.xinzhili.api.doctor.bean.response.DoctorAdviceWrapperDataResponse;
import cn.xinzhili.api.doctor.bean.response.DoctorAdviceWrapperListResponse;
import cn.xinzhili.api.doctor.bean.response.DoctorAdviceWrapperResponse;
import cn.xinzhili.api.doctor.bean.response.DoctorMessageResponse;
import cn.xinzhili.api.doctor.bean.response.MedicineAdjustmentsClient;
import cn.xinzhili.api.doctor.bean.response.MedicineAdjustmentsClient.MedicineAdjustment;
import cn.xinzhili.api.doctor.bean.response.MessageResponse;
import cn.xinzhili.api.doctor.bean.response.OperatorNotificationResponse;
import cn.xinzhili.api.doctor.bean.response.StatisticsAdvicesApiResponse;
import cn.xinzhili.api.doctor.client.DpcServiceClient;
import cn.xinzhili.api.doctor.client.MedicalServiceClient;
import cn.xinzhili.api.doctor.client.UserServiceClient;
import cn.xinzhili.api.doctor.config.ApplicationConfiguration;
import cn.xinzhili.api.doctor.util.AdvicesFactory;
import cn.xinzhili.api.doctor.util.ConsultationFactory;
import cn.xinzhili.api.doctor.util.MessageFactory;
import cn.xinzhili.api.doctor.util.OperatorNotificationFactory;
import cn.xinzhili.dpc.api.AdvicesListParam;
import cn.xinzhili.dpc.api.ConsultationStatus;
import cn.xinzhili.dpc.api.DpcMessageType;
import cn.xinzhili.dpc.api.request.UpdateNotificationReadAtRequest;
import cn.xinzhili.dpc.api.request.UpdateNotificationStatusRequest;
import cn.xinzhili.dpc.api.response.StaffNotificationListResponse;
import cn.xinzhili.medical.api.request.TabooRequest;
import cn.xinzhili.medical.api.request.TabooRequest.Type;
import cn.xinzhili.medical.api.response.MedicinesTaboosResponse;
import cn.xinzhili.medical.api.response.MedicinesTaboosResponse.MedicineTaboos;
import cn.xinzhili.user.api.AdviceLevel;
import cn.xinzhili.user.api.StaffRole;
import cn.xinzhili.xutils.core.ErrorCode;
import cn.xinzhili.xutils.core.FailureException;
import cn.xinzhili.xutils.core.SystemException;
import cn.xinzhili.xutils.core.http.Response;
import cn.xinzhili.xutils.core.util.HashUtils;
import com.google.common.collect.ImmutableMap;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author loki, xin
 */
@Service
public class DpcService {

  private static final Logger logger = LoggerFactory.getLogger(DpcService.class);

  private final DpcServiceClient dpcServiceClient;

  private final UserServiceClient userServiceClient;

  private final ApplicationConfiguration acf;

  @Autowired
  private MedicalServiceClient medicalServiceClient;

  @Autowired
  public DpcService(DpcServiceClient dpcServiceClient,
      UserServiceClient userServiceClient, ApplicationConfiguration applicationConfiguration) {
    this.dpcServiceClient = dpcServiceClient;
    this.userServiceClient = userServiceClient;
    this.acf = applicationConfiguration;
  }

  public DoctorMessageResponse searchDoctorMessages(
      long senderId, long patientId, long page, StaffRole role) {

    String pid = DoctorMessagePrefix.PATIENT.join(patientId);

    //sender 传null 查询的是所有该患者发送和接受到的消息
    Response response;
    if (role.equals(StaffRole.OPERATOR)) {
      String oid = DoctorMessagePrefix.DOCTOR.join(senderId);
      response = dpcServiceClient.searchDoctorMessages(oid, pid, page, acf.getMessagePerPage());
    } else {
      response = dpcServiceClient.messageByDoctor(null, pid, page, acf.getMessagePerPage());
    }

    if (response.isFailed()) {
      logger.warn("get patient chat msg fail ! pid -> {},{}", patientId, response);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    } else if (response.isError()) {
      logger.error("get patient chat msg fail ! pid -> {},{}", patientId, response);
      throw new FailureException(ErrorCode.SERVER_ERROR);
    }

    @SuppressWarnings("unchecked")
    List<Map<String, Object>> messages = (List<Map<String, Object>>) response.getData()
        .get("messages");
    DoctorMessageResponse messageResponse = new DoctorMessageResponse();
    List<DoctorApiMessage> result = new ArrayList<>();
    for (Map<String, Object> message : messages) {
      result.add(MessageFactory.Map2ApiMassage(message));
    }
    messageResponse.setMessages(result);
    return messageResponse;
  }

  public MessageResponse commitChatToDoctor(long operatorId, long patientId) {
    String pid = DoctorMessagePrefix.PATIENT.join(patientId);
    //目前一个患者 当前只有一个医生 doctorId 可以不传
    UpdateMessageRequest request = new UpdateMessageRequest(null, pid);

    Response response = dpcServiceClient.commitChatToDoctor(request);
    if (response.isFailed()) {
      logger.warn("commit chat to doctor fail:{}", response);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    } else if (response.isError()) {
      logger.error("commit chat to doctor error:{}", response);
      throw new FailureException(ErrorCode.SERVER_ERROR);
    }
    return new MessageResponse((int) response.getData().get("number"));
  }

  public MessageResponse changeChatToDoctorRead(long doctorId, long patientId, long commitTimes,
      boolean isDoctor) {

    String pid = DoctorMessagePrefix.PATIENT.join(patientId);
    //目前一个患者 当前只有一个医生 doctorId 可以不传
    UpdateReadMessageRequest request = new UpdateReadMessageRequest(null, pid, commitTimes,
        isDoctor);
    Response response = dpcServiceClient.changeChatToDoctorRead(request);
    if (response.isFailed()) {
      logger.warn("change chat to doctor fail:{}", response);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    } else if (response.isError()) {
      logger.error("change chat to doctor error:{}", response);
      throw new FailureException(ErrorCode.SERVER_ERROR);
    }
    return new MessageResponse((int) response.getData().get("number"));
  }

  public void sendImageReviewSystemMessage(long operatorId, long patientId) {

    DoctorMessage message = new DoctorMessage();
    message.setReceiver(DoctorMessagePrefix.PATIENT.join(patientId));
    message.setSender(DoctorMessagePrefix.DOCTOR.join(operatorId));
    message.setType(DoctorMessageContentType.TEXT.name());
    message.setContent(acf.getImageReviewSystemMessageContent());
    sendDoctorMessages(message, message.getContent(), DpcMessageType.IMAGE_REVIEW_BLURRING);
  }

  public void sendInquiriesSystemMessage(long sender, List<Long> receivers) {

    receivers.forEach(t -> {
      DoctorMessage message = new DoctorMessage();
      message.setReceiver(DoctorMessagePrefix.PATIENT.join(t));
      message.setSender(DoctorMessagePrefix.DOCTOR.join(sender));
      message.setType(DoctorMessageContentType.TEXT.name());

      InquiryNotifyContentData content = new InquiryNotifyContentData();
      content.setType(NoticeContentType.LINK.getCode());
      content.setContent(acf.getInquiryPageUrl());
      content.setCreatedAt(new Date().getTime());
      content.setDescription(acf.getInquiryDescription());
      content.setTitle(acf.getInquiryTitle());
      content.setBannerUrl(acf.getInquiryBannerUrl());
      sendDoctorMessages(message, content, DpcMessageType.INQUIRIES);
    });
  }

  public void sendChatMessage(DoctorMessage body) {
    sendDoctorMessages(body, body.getContent(), DpcMessageType.CHAT);
  }

  private void sendDoctorMessages(DoctorMessage body, Object dataContent, DpcMessageType type) {

    ImmutableMap<String, Object> data = ImmutableMap
        .of("type", DoctorMessageContentType.valueOf(body.getType()).ordinal(), "content",
            dataContent);

    ImmutableMap<String, Serializable> content = ImmutableMap
        .of("data", data, "type", type.ordinal());

    Map<String, Object> request = ImmutableMap
        .of("sender", body.getSender(), "receiver", body.getReceiver(), "content", content);
    Response response = dpcServiceClient.addDoctorMessages(request);

    if (!response.isSuccessful()) {
      logger.warn("添加用户聊天信息失败: {}", response.getData());
      throw new SystemException(ErrorCode.SERVER_ERROR);
    }
  }

  private AdviceLevel levelByIntValue(int value) {
    return value == 0 ? AdviceLevel.NONE : (value == 1 ? AdviceLevel.NORMAL : AdviceLevel.SEVERE);
  }

  public Response sendDoctorAdvice(SendDoctorAdviceRequest request) {

    Response response = dpcServiceClient
        .adviceLevel(request.getDoctorId(), request.getAssistantId(), request.getPatientId());
    if (!response.isSuccessful()) {
      logger.warn("get advice level failed -> {}", response);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    }

    Response<DoctorAdviceDataResponse> adviceResponse = dpcServiceClient.sendDoctorAdvice(request);
    if (!adviceResponse.isSuccessful()) {
      logger.warn("send doctor advice failed ->  {}", adviceResponse);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    }

    DoctorAdviceWrapperDataResponse doctorAdviceWrapperDataResponse = adviceResponse
        .getDataAs(DoctorAdviceDataResponse.class)
        .getAdvice().toDoctorAdviceWrapperDataResponse();
    return Response.instanceSuccess(doctorAdviceWrapperDataResponse);
  }

  public Response getConfirmedAdviceList(long patientId, long doctorId, Long assistantId,
      boolean isDoctor, int page, int perPage) {

    if (isDoctor && Objects.nonNull(assistantId)) {
      return getAdviceListBy(patientId, doctorId, assistantId, true,
          AdvicesListParam.DOCTOR_ASSISTANT_CONFIRMED_STATUS,
          AdvicesListParam.DOCTOR_ASSISTANT_CONFIRMED_CATEGORY, page, perPage);
    } else if (isDoctor) {
      return getAdviceListBy(patientId, doctorId, null,
          true, AdvicesListParam.DOCTOR_CONFIRMED_STATUS,
          AdvicesListParam.DOCTOR_CONFIRMED_CATEGORY, page, perPage);
    } else {
      return getAdviceByCategoryStatusKey(patientId, doctorId, assistantId, false,
          AdvicesListParam.ASSISTANT_CONFIRMED_CATEGORY_STATUS_KEYS, page, perPage);
    }
  }

  public Response getUnconfirmedAdviceList(long patientId, long doctorId, Long assistantId,
      boolean isDoctor, int page, int perPage) {

    if (isDoctor && Objects.nonNull(assistantId)) {
      return getAdviceByCategoryStatusKey(patientId, doctorId, assistantId, true,
          AdvicesListParam.DOCTOR_ASSISTANT_UNCONFIRMED_CATEGORY_STATUS_KEYS, page, perPage);
    } else if (isDoctor) {
      return getAdviceListBy(patientId, doctorId, null, true,
          AdvicesListParam.DOCTOR_UNCONFIRMED_STATUS, AdvicesListParam.DOCTOR_UNCONFIRMED_CATEGORY,
          page, perPage);
    } else {
      return getAdviceListBy(patientId, doctorId, assistantId, false,
          AdvicesListParam.ASSISTANT_UNCONFIRMED_STATUS,
          AdvicesListParam.ASSISTANT_UNCONFIRMED_CATEGORY, page, perPage);
    }
  }

  public Response doctorAdvices(long patientId, long doctorId, Long assistantId,
      List<Integer> status, boolean isDoctor, boolean orderByUpdateTime, int page, int perPage) {
    List<Integer> category = null;
    if (isDoctor && assistantId != null) {
      category = Arrays.stream(AdviceCategory.values())
//          .filter(ad -> !Objects.equals(ad, AdviceCategory.RE_EXAMINATION))
          .map(AdviceCategory::getCode).collect(Collectors.toList());
    }
    return getAdviceListBy(patientId, doctorId, assistantId, isDoctor, status, category, page,
        perPage);
  }

  private Response getAdviceByCategoryStatusKey(long patientId, long doctorId, Long assistantId,
      boolean isDoctor, List<String> categoryStatus, int page, int perPage) {

    Response<DoctorAdviceListResponse> response = dpcServiceClient
        .advicesByCategoryAndStatus(patientId, doctorId, isDoctor ? null : assistantId,
            categoryStatus,
            true, page, perPage);
    if (!response.isSuccessful()) {
      logger.warn("get doctor advices failed -> {}", response);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    }
    return Response.instanceSuccess(doctorAdviceWrapperListResponse(response
        .getDataAs(DoctorAdviceListResponse.class)));
  }

  private Response getAdviceListBy(long patientId, long doctorId, Long assistantId,
      boolean isDoctor, List<Integer> status, List<Integer> category, int page, int perPage) {

    Response<DoctorAdviceListResponse> response = dpcServiceClient
        .advices(patientId, doctorId, isDoctor ? null : assistantId, status, category,
            true, page, perPage);
    if (!response.isSuccessful()) {
      logger.warn("get doctor advices failed -> {}", response);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    }
    return Response.instanceSuccess(doctorAdviceWrapperListResponse(response
        .getDataAs(DoctorAdviceListResponse.class)));
  }

  private DoctorAdviceWrapperListResponse doctorAdviceWrapperListResponse(
      DoctorAdviceListResponse source) {

    List<DoctorAdviceWrapperResponse> doctorAdviceWrapperResponses = source.getAdvices().stream()
        .map(DoctorAdviceResponse::toDoctorAdviceWrapperResponse).collect(Collectors.toList());
    return DoctorAdviceWrapperListResponse.newBuilder().advices(doctorAdviceWrapperResponses)
        .total(source.getTotal()).build();
  }

  public Response updateDoctorAdvice(long id, UpdateDoctorAdviceClientRequest request,
      long assistantId,
      long patientId) {
    Response updateResponse = dpcServiceClient
        .updateDoctorAdvice(id, request.toUpdateDoctorAdviceServiceRequest());

    if (!updateResponse.isSuccessful()) {
      logger.warn("update advice fail ! request -> {}", request);
      throw new FailureException(ErrorCode.REQUEST_FAILED, "update advice fail !");
    }

    Response response = dpcServiceClient.adviceLevel(request.getDoctorId(), assistantId, patientId);
    if (!response.isSuccessful()) {
      logger.warn("get advice level failed -> {}", response);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    }

    return updateResponse;
  }

  public void genTabooAdviceReason(UpdateDoctorAdviceClientRequest body,
      long patientId, int adviceCategory, UserRole userRole) {
    if (adviceCategory == AdviceCategory.CONTRAINDICATION_MEDICINE_ADJUSTMENT.getCode()) {
      MedicineAdjustmentsClient medicineSupplement = body.getMedicineSupplement();
      Optional<List<Long>> ids = medicineSupplement.getMedicineSupplement().stream().filter(
          medicineAdjustment -> Objects.equals(medicineAdjustment.getRole(), userRole))
          .map(this::getMedicineIds).findFirst();
      ids.ifPresent(longs -> {
        if (!longs.isEmpty()) {
          MedicinesTaboosResponse medicineTaboos = medicalServiceClient
              .checkMedicineTaboo(new TabooRequest(patientId, longs, Type.ADJUST_ME))
              .getDataAs(MedicinesTaboosResponse.class);
          body.setReason(MessageFactory.jsonToString(medicineTaboos));
          genMedicineTaboosText(medicineTaboos.getMedicineTaboos()).ifPresent(body::setQuestion);
        }
      });
    }
  }

  public void sendConsultation(AddConsultationApiRequest request) {
    Response response = dpcServiceClient.sendConsultation(ConsultationFactory.of(request));
    if (response.isFailed()) {
      logger.warn("send consultation fail request:{}", request);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    } else if (response.isError()) {
      logger.error("send consultation error request:{}", request);
      throw new FailureException(ErrorCode.SERVER_ERROR);
    }
  }

  public void updateConsultation(UpdateConsultationApiRequest request) {
    Response response = dpcServiceClient.updateConsultation(ConsultationFactory.of(request));
    if (response.isFailed()) {
      logger.warn("update consultation fail request:{}", request);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    } else if (response.isError()) {
      logger.error("update consultation error request:{}", request);
      throw new FailureException(ErrorCode.SERVER_ERROR);
    }
  }

  public Boolean getBindByConsultationAndPatient(Long consultationDoctorId, Long patientId) {

    Response dpcResponse = dpcServiceClient
        .getBindingByConsultationAndPatient(consultationDoctorId, patientId);
    if (dpcResponse.isFailed()) {
      logger.warn("get consultation bind fail response:{}", consultationDoctorId);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    } else if (dpcResponse.isError()) {
      logger.error("get consultation bind error response:{}", consultationDoctorId);
      throw new FailureException(ErrorCode.SERVER_ERROR);
    }
    @SuppressWarnings("unchecked")
    Integer number = (Integer) dpcResponse.getData().get("number");
    return number > 0;
  }

  public ConsultationListResponse getConsultation(Long uid, Long patientId,
      Boolean isConsultationDoctor, Integer page, Integer perPage) {
    Response response = null;
    if (isConsultationDoctor) {
      response = dpcServiceClient
          .getConsultation(patientId, null, uid, ConsultationStatus.NEW.getCode(), page, perPage);
    } else {
      response = dpcServiceClient
          .getConsultation(patientId, uid, null, ConsultationStatus.RESPONSE.getCode(), page,
              perPage);
    }

    if (response.isFailed()) {
      logger.warn("update consultation fail response:{}", response);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    } else if (response.isError()) {
      logger.error("update consultation error response:{}", response);
      throw new FailureException(ErrorCode.SERVER_ERROR);
    }
    @SuppressWarnings("unchecked")
    List<Map<String, Object>> consultations = (List<Map<String, Object>>) response.getData()
        .get("consultation");

    @SuppressWarnings("unchecked")
    Integer total = (Integer) response.getData().get("total");
    if (total <= 0) {
      return new ConsultationListResponse(List.of(), total);
    }
    List<ConsultationInfo> result = new ArrayList<>();
    for (Map<String, Object> consultation : consultations) {
      result.add(ConsultationFactory.Map2Consultation(consultation));
    }
    return new ConsultationListResponse(result, total);
  }

  private static Optional<String> genMedicineTaboosText(List<MedicineTaboos> medicineTaboos) {
    String result = null;
    if (Objects.nonNull(medicineTaboos) && !medicineTaboos.isEmpty()) {
      result = medicineTaboos.stream().map(taboo -> new StringJoiner("").add("由于")
          .add(taboo.getReasons().stream().collect(Collectors.joining("，")))
          .add(taboo.getCategory().getDescription()).add(taboo.getMedicineName())
          .toString())
          .collect(Collectors.joining("，", "患者", "，系统检测到患者正在使用该药，是否调药。"));
    }
    return Optional.ofNullable(result);
  }

  private List<Long> getMedicineIds(MedicineAdjustment ma) {
    return ma.getMedicineAdjustment().stream()
        .filter(mac -> !Objects.equals(mac.getStatus(), MedicineAdjustmentStatus.ADD))
        .map(mas -> Objects.equals(mas.getStatus(), MedicineAdjustmentStatus.NORMAL) ? (
            mas.getPlan().stream()
                .anyMatch(plan -> !Objects.equals(plan.getStatus(), Status.NONE))
                ? HashUtils.decode(mas.getId()) : 0) : HashUtils.decode(mas.getId()))
        .filter(aLong -> aLong != 0).collect(Collectors.toList());
  }

  public enum DoctorMessagePrefix {
    DOCTOR("d_"),
    PATIENT("p_");

    private String prefix;

    DoctorMessagePrefix(String prefix) {
      this.prefix = prefix;
    }

    public static String removeFrom(String prefixId) {
      for (DoctorMessagePrefix messagePrefix : values()) {
        if (prefixId.startsWith(messagePrefix.prefix)) {
          return prefixId.substring(messagePrefix.prefix.length());
        }
      }
      throw new IllegalArgumentException();
    }

    public String join(long id) {
      return prefix + id;
    }
  }

  public enum DoctorMessageContentType {
    TEXT
  }

  public enum AdviceStatus {
    WAITING(0),
    OK(1),
    IGNORE(2),
    MODIFY(3),
    DOCTOR_DIRECT(4),
    ASSISTANT_WAITING(5),
    ASSISTANT_IGNORE(6);

    AdviceStatus(int code) {
      this.code = code;
    }

    private int code;

    public int getCode() {
      return code;
    }

    public void setCode(int code) {
      this.code = code;
    }
  }

  public OperatorNotificationResponse getOperatorNotificationList(Long operatorId, Integer pageAt,
      Integer pageSize) {
    Response<StaffNotificationListResponse> response = dpcServiceClient
        .getNotificationList(operatorId, pageAt, pageSize);

    if (response.isSuccessful()) {

      StaffNotificationListResponse staffNotification = response
          .getDataAs(StaffNotificationListResponse.class);
      return new OperatorNotificationResponse(staffNotification.getNotifications().stream()
          .map(OperatorNotificationFactory::api).collect(Collectors.toList()),
          staffNotification.getTotal(), staffNotification.isHasUnRead());

    } else if (response.isFailed()) {

      logger.warn("get operator notification fail ! {}", response);
      throw new FailureException(ErrorCode.REQUEST_FAILED);

    } else {
      logger.error("get operator notification fail ! {}", response);
      throw new FailureException(ErrorCode.SERVER_ERROR);
    }
  }

  public void updateOperatorNotificationReadAt(Long operatorId) {
    UpdateNotificationReadAtRequest request = new UpdateNotificationReadAtRequest();
    request.setReceiver(operatorId);
    Response response = dpcServiceClient.updateNotificationReadAt(request);
    if (response.isFailed()) {
      logger.warn("update operator notification fail ! {},{}", operatorId, response);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    } else if (response.isError()) {
      logger.error("update operator notification error ! {},{}", operatorId, response);
      throw new FailureException(ErrorCode.SERVER_ERROR);
    }
  }

  public void updateOperatorNotification(UpdateNotificationStatusRequest request) {

    Response response = dpcServiceClient.updateNotificationStatus(request);
    if (response.isFailed()) {
      logger.warn("update operator notification fail ! {}, {}", request, response);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    } else if (response.isError()) {
      logger.error("update operator notification error ! {} ,{}", request, response);
      throw new FailureException(ErrorCode.SERVER_ERROR);
    }

  }

  public StatisticsAdvicesApiResponse getDepartmentAdvices(String organizationId,
      StatisticAdviceDateType dateType) {

    Response response = dpcServiceClient.getDepartmentAdvices(HashUtils.decode(organizationId),
        dateType.getCode());
    if (response.isFailed()) {
      logger.warn("get department messages fail ! {}", organizationId);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    } else if (response.isError()) {
      logger.error("get department messages error ! {}", organizationId);
      throw new FailureException(ErrorCode.SERVER_ERROR);
    }
    @SuppressWarnings("unchecked")
    List<Map<String, Object>> statisticsWorkload = (List<Map<String, Object>>) response.getData()
        .get("statisticsWorkload");
    return AdvicesFactory.list2Response(statisticsWorkload);
  }

}

