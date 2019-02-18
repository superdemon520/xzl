package cn.xinzhili.api.doctor.controller;

import cn.xinzhili.api.doctor.bean.DoctorMessage;
import cn.xinzhili.api.doctor.bean.InspectionStandards;
import cn.xinzhili.api.doctor.bean.UserRole;
import cn.xinzhili.api.doctor.bean.request.AddConsultationApiRequest;
import cn.xinzhili.api.doctor.bean.request.MedicationPlan.Status;
import cn.xinzhili.api.doctor.bean.request.SendDoctorAdviceRequest;
import cn.xinzhili.api.doctor.bean.request.SendDoctorAdviceRequest.Builder;
import cn.xinzhili.api.doctor.bean.request.SendDoctorAdviceWrapperRequest;
import cn.xinzhili.api.doctor.bean.request.UpdateChatApiRequest;
import cn.xinzhili.api.doctor.bean.request.UpdateConsultationApiRequest;
import cn.xinzhili.api.doctor.bean.request.UpdateDoctorAdviceClientRequest;
import cn.xinzhili.api.doctor.bean.request.UpdateOperatorNotificationRequest;
import cn.xinzhili.api.doctor.bean.response.ConsultationListResponse;
import cn.xinzhili.api.doctor.bean.response.DoctorMessageResponse;
import cn.xinzhili.api.doctor.bean.response.MessageResponse;
import cn.xinzhili.api.doctor.bean.response.PatientDetailApiResponse;
import cn.xinzhili.api.doctor.config.ApplicationConfiguration;
import cn.xinzhili.api.doctor.error.AuthErrorCode;
import cn.xinzhili.api.doctor.error.UserErrorCode;
import cn.xinzhili.api.doctor.service.DpcService;
import cn.xinzhili.api.doctor.service.DpcService.DoctorMessagePrefix;
import cn.xinzhili.api.doctor.service.MedicalService;
import cn.xinzhili.api.doctor.service.NotifyService;
import cn.xinzhili.api.doctor.service.UserService;
import cn.xinzhili.api.doctor.util.AuthUtils;
import cn.xinzhili.api.doctor.util.InspectionFactory;
import cn.xinzhili.api.doctor.util.OperatorNotificationFactory;
import cn.xinzhili.api.doctor.util.ValidationUtils;
import cn.xinzhili.dpc.api.DoctorAdviceCategory;
import cn.xinzhili.dpc.api.DoctorAdviceStatus;
import cn.xinzhili.medical.api.request.HandleCustomizableReferenceRequest;
import cn.xinzhili.user.api.PatientRelativeNotifyCategory;
import cn.xinzhili.user.api.StaffRole;
import cn.xinzhili.xutils.auth.CurrentUserId;
import cn.xinzhili.xutils.core.ErrorCode;
import cn.xinzhili.xutils.core.FailureException;
import cn.xinzhili.xutils.core.http.Response;
import cn.xinzhili.xutils.core.util.HashUtils;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by Loki on 16/9/28.
 */
@RestController
@Validated
public class DpcController {

  private static final Logger logger = LoggerFactory.getLogger(DpcController.class);

  @Autowired
  private DpcService dpcService;

  @Autowired
  private UserService userService;

  @Autowired
  private NotifyService notifyService;

  @Autowired
  private MedicalService medicalService;

  @Autowired
  private ApplicationConfiguration applicationConfiguration;

  @PreAuthorize("hasAnyRole('DOCTOR','ASSISTANT','OPERATOR')")
  @RequestMapping(value = "/message", method = RequestMethod.GET)
  public Response getDoctorMessages(@CurrentUserId Long uid,
      @RequestParam("patientId") String patientId,
      @RequestParam(value = "page", defaultValue = "1") Long page) {

    long pId = HashUtils.decode(patientId);
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    boolean isBinding = userService.checkBindRelation(uid, pId);
    if (!isBinding) {
      throw new FailureException(UserErrorCode.BINDING_RELATION_INVALID, "不合法的绑定关系");
    }

    StaffRole role = null;

    if (AuthUtils.isUserOfRole(UserRole.DOCTOR, authentication)) {
      role = StaffRole.DOCTOR;
    } else if (AuthUtils.isUserOfRole(UserRole.ASSISTANT, authentication)) {
      role = StaffRole.ASSISTANT;
    } else if (AuthUtils.isUserOfRole(UserRole.OPERATOR, authentication)) {
      role = StaffRole.OPERATOR;
    }

    DoctorMessageResponse response =
        dpcService.searchDoctorMessages(uid, HashUtils.decode(patientId), page, role);

    userService.resetPatientNotification(pId, role);
    return Response.instanceSuccess(response);
  }

  @PreAuthorize("hasAnyRole('OPERATOR')")
  @RequestMapping(value = "/group/chat/{patientId}", method = RequestMethod.PATCH)
  public Response commitChatToDoctor(@CurrentUserId Long uid,
      @PathVariable String patientId
  ) {
    long pid = HashUtils.decode(patientId);
    MessageResponse response = dpcService.commitChatToDoctor(uid, pid);
    userService.updatePendingMessage(pid);
    return Response.instanceSuccess(response);
  }

  @PreAuthorize("hasAnyRole('DOCTOR','ASSISTANT')")
  @RequestMapping(value = "/read/chat", method = RequestMethod.PATCH)
  public Response changeChatToDoctorRead(@CurrentUserId Long uid, @RequestBody
      UpdateChatApiRequest request) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    boolean isDoctor = AuthUtils.isUserOfRole(UserRole.DOCTOR, authentication);
    long pid = HashUtils.decode(request.getPatientId());
    MessageResponse response = dpcService
        .changeChatToDoctorRead(uid, pid, request.getCommitTimes(), isDoctor);
    return Response.instanceSuccess(response);
  }

  @PreAuthorize("hasRole('OPERATOR')")
  @RequestMapping(value = "/chat", method = RequestMethod.POST)
  public Response sendDoctorMessage(@CurrentUserId Long uid,
      @Valid @RequestBody DoctorMessage body) {
    body.setSender(DoctorMessagePrefix.DOCTOR.join(uid));
    long patientId = HashUtils.decode(body.getReceiver());
    body.setReceiver(DoctorMessagePrefix.PATIENT.join(patientId));

    dpcService.sendChatMessage(body);

    if (!notifyService.pushMessageNotify(body.getContent(), patientId).isSuccessful()) {
      // TODO: just for debug
      logger.warn("update patient notification or push message failed");
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    }
    return Response.instanceSuccess();
  }

  @PreAuthorize("hasAnyRole('DOCTOR','ASSISTANT')")
  @RequestMapping(value = "/advice", method = RequestMethod.POST)
  public Response sendDoctorAdvice(@CurrentUserId Long uid,
      @Valid @RequestBody SendDoctorAdviceWrapperRequest body) {
    long patientId = HashUtils.decode(body.getPatientId());
    PatientDetailApiResponse patient = userService.getPatientById(patientId);
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Builder request = SendDoctorAdviceRequest.builder().patientId(patientId)
        .question(body.getQuestion()).advice(body.getAdvice()).reason(body.getReason())
        .level(body.getLevel());
    if (AuthUtils.isUserOfRole(UserRole.DOCTOR, authentication)) {
      if (!Objects.equals(HashUtils.decode(patient.getDoctorId()), uid)) {
        throw new FailureException(ErrorCode.INVALID_PARAMS);
      }
      request.doctorId(uid);
      request.assistantId(patient.hasAssistant() ? HashUtils.decode(patient.getAssistantId()) : 0);
      request.status(DoctorAdviceStatus.DOCTOR_DIRECT.ordinal());
      notifyService.pushAdviceNotify(body.getAdvice(), patientId);
    } else {
      if (!Objects.equals(HashUtils.decode(patient.getAssistantId()), uid)) {
        throw new FailureException(ErrorCode.INVALID_PARAMS);
      }
      request.doctorId(HashUtils.decode(patient.getDoctorId())).assistantId(uid);
      //主动调药医助调药后修改医嘱状态为5
      if (body.getCategory() == DoctorAdviceCategory.LIVING_STANDARD_MEDICINE_ADJUSTMENT.getCode()
          ||
          body.getCategory() == DoctorAdviceCategory.INSPECTION_STANDARD.getCode()) {
        request.status(DoctorAdviceStatus.ASSISTANT_WAITING.ordinal());
      } else {
        request.status(DoctorAdviceStatus.WAITING.ordinal());
      }
    }
    if (body.getCategory() == DoctorAdviceCategory.LIVING_STANDARD_MEDICINE_ADJUSTMENT.getCode()) {
      request.category(DoctorAdviceCategory.LIVING_STANDARD_MEDICINE_ADJUSTMENT.getCode());
      request.medicineSupplement(body.getMedicineSupplement().toMedicineAdjustmentsService());
    } else if (body.getCategory() == DoctorAdviceCategory.INSPECTION_STANDARD.getCode()) {
      request.category(DoctorAdviceCategory.INSPECTION_STANDARD.getCode())
          .inspectionStandards(body.getInspectionStandards());
      //大医生自己改达标值
      handleInspectionReference(InspectionFactory.of(body), UserRole.DOCTOR);
    }
    return dpcService.sendDoctorAdvice(request.build());
  }

  @PreAuthorize("hasAnyRole('DOCTOR','ASSISTANT')")
  @RequestMapping(value = "/advice", method = RequestMethod.GET)
  public Response doctorAdvices(@CurrentUserId Long uid,
      @RequestParam String patientId,
      @RequestParam List<Integer> status,
      @RequestParam(defaultValue = "false") boolean orderByUpdateTime,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "25") int perPage
  ) {
    Long pid = HashUtils.decode(patientId);
    PatientDetailApiResponse patient = userService.getPatientById(pid);
    Long assistantId = patient.hasAssistant() ? HashUtils.decode(patient.getAssistantId()) : null;
    Long doctorId = HashUtils.decode(patient.getDoctorId());

    boolean isRoleValid = assistantId == null ? Objects.equals(uid, doctorId)
        : (Objects.equals(uid, assistantId) || Objects.equals(uid, doctorId));
    if (!isRoleValid) {
      throw new FailureException(ErrorCode.INVALID_PARAMS);
    }

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    boolean isDoctor = AuthUtils.isUserOfRole(UserRole.DOCTOR, authentication);
    return dpcService
        .doctorAdvices(pid, doctorId, assistantId, status, isDoctor, orderByUpdateTime, page,
            perPage);
  }


  @PreAuthorize("hasAnyRole('DOCTOR','ASSISTANT')")
  @RequestMapping(value = "/advice/unconfirmed", method = RequestMethod.GET)
  public Response getAdvicesUnconfirmed(@CurrentUserId Long uid,
      @RequestParam String patientId,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "25") int pageSize
  ) {
    Long pid = HashUtils.decode(patientId);
    PatientDetailApiResponse patient = userService.getPatientById(pid);
    Long assistantId = patient.hasAssistant() ? HashUtils.decode(patient.getAssistantId()) : null;
    Long doctorId = HashUtils.decode(patient.getDoctorId());

    boolean isRoleValid = assistantId == null ? Objects.equals(uid, doctorId)
        : (Objects.equals(uid, assistantId) || Objects.equals(uid, doctorId));
    if (!isRoleValid) {
      throw new FailureException(ErrorCode.INVALID_PARAMS);
    }

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    boolean isDoctor = AuthUtils.isUserOfRole(UserRole.DOCTOR, authentication);
    return dpcService
        .getUnconfirmedAdviceList(pid, doctorId, assistantId, isDoctor, page, pageSize);
  }

  @PreAuthorize("hasAnyRole('DOCTOR','ASSISTANT')")
  @RequestMapping(value = "/advice/confirmed", method = RequestMethod.GET)
  public Response getAdvicesConfirmed(@CurrentUserId Long uid,
      @RequestParam String patientId,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "25") int pageSize
  ) {
    Long pid = HashUtils.decode(patientId);
    PatientDetailApiResponse patient = userService.getPatientById(pid);
    Long assistantId = patient.hasAssistant() ? HashUtils.decode(patient.getAssistantId()) : null;
    Long doctorId = HashUtils.decode(patient.getDoctorId());

    boolean isRoleValid = assistantId == null ? Objects.equals(uid, doctorId)
        : (Objects.equals(uid, assistantId) || Objects.equals(uid, doctorId));
    if (!isRoleValid) {
      throw new FailureException(ErrorCode.INVALID_PARAMS);
    }

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    boolean isDoctor = AuthUtils.isUserOfRole(UserRole.DOCTOR, authentication);
    return dpcService.getConfirmedAdviceList(pid, doctorId, assistantId, isDoctor, page, pageSize);
  }


  @PreAuthorize("hasAnyRole('DOCTOR','ASSISTANT')")
  @RequestMapping(value = "/advice/{id}", method = RequestMethod.PATCH)
  public Response updateDoctorAdvices(@CurrentUserId Long uid,
      @PathVariable String id, @Valid @RequestBody UpdateDoctorAdviceClientRequest body) {
    long patientId = HashUtils.decode(body.getPatientId());
    int status = body.getStatus();
    PatientDetailApiResponse patient = userService.getPatientById(patientId);
    long assistantId = patient.hasAssistant() ? HashUtils.decode(patient.getAssistantId()) : 0L;
    body.setDoctorId(HashUtils.decode(patient.getDoctorId()));

    int adviceCategory = body.getCategory();
    if (adviceCategory == DoctorAdviceCategory.MEDICINE_ADJUSTMENT.getCode() ||
        adviceCategory == DoctorAdviceCategory.SIDE_EFFECT_MEDICINE_ADJUSTMENT.getCode() ||
        adviceCategory == DoctorAdviceCategory.CONTRAINDICATION_MEDICINE_ADJUSTMENT.getCode() ||
        adviceCategory == DoctorAdviceCategory.LIVING_STANDARD_MEDICINE_ADJUSTMENT.getCode() ||
        adviceCategory == DoctorAdviceCategory.INSPECTION_STANDARD.getCode()) {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (AuthUtils.isUserOfRole(UserRole.DOCTOR, authentication)) { //医生角色登录
        if (assistantId == 0L) { //无医助情况
          if (status == DoctorAdviceStatus.WAITING.ordinal()) { //待审核状态，将医嘱状态置为医生直接通过
            body.setStatus(DoctorAdviceStatus.DOCTOR_DIRECT.ordinal());
            dpcService.genTabooAdviceReason(body, patientId, adviceCategory, UserRole.DOCTOR);
            userService.patientRelativeNotify(Collections.singletonList(patientId),
                PatientRelativeNotifyCategory.ADJUSTMENT_NOTIFY);
          }
        } else { //有医助情况
          if (status == DoctorAdviceStatus.WAITING.ordinal()) { //待审核状态，医生无权限操作调药单
            return Response.instanceFail(AuthErrorCode.DOCTOR_UNAUTH_MODIFY);
          }
        }
        if (status == DoctorAdviceStatus.IGNORE.ordinal()) { //医生忽略，将医嘱状态置为忽略
          body.setStatus(DoctorAdviceStatus.IGNORE.ordinal());
          if (adviceCategory == DoctorAdviceCategory.INSPECTION_STANDARD.getCode()) {
            handleInspectionReference(body, UserRole.ASSISTANT);
          }
        }
        if (status == DoctorAdviceStatus.ASSISTANT_WAITING.ordinal()
            || status == DoctorAdviceStatus.ASSISTANT_IGNORE.ordinal()) { //医助修改完成，将医嘱状态置为审核通过状态
          body.setStatus(DoctorAdviceStatus.OK.ordinal());
          dpcService.genTabooAdviceReason(body, patientId, adviceCategory, UserRole.DOCTOR);
          if (adviceCategory != DoctorAdviceCategory.INSPECTION_STANDARD.getCode()) {
            userService.patientRelativeNotify(Collections.singletonList(patientId),
                PatientRelativeNotifyCategory.ADJUSTMENT_NOTIFY);
          } else {
            handleInspectionReference(body, UserRole.DOCTOR);//医生修改患者生化达标值
          }
        }
      } else { //医助角色登录
        if (status == DoctorAdviceStatus.ASSISTANT_IGNORE.ordinal()) { //待审核状态，医助点击忽略，将医嘱状态置为医助忽略
          dealAssistantIgnoreData(body, adviceCategory);
          body.setStatus(DoctorAdviceStatus.ASSISTANT_IGNORE.ordinal());
        } else if (status == DoctorAdviceStatus.WAITING.ordinal()) { //待审核状态，医助确认调药，将医嘱状态置为医助修改完成待审核
          dpcService.genTabooAdviceReason(body, patientId, adviceCategory, UserRole.ASSISTANT);
          body.setStatus(DoctorAdviceStatus.ASSISTANT_WAITING.ordinal());
        }
      }
    } else if (body.getCategory() == DoctorAdviceCategory.RE_EXAMINATION.getCode()) { //复查医嘱医生无权限修改
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (AuthUtils.isUserOfRole(UserRole.DOCTOR, authentication) && assistantId != 0) {
        return Response.instanceFail(AuthErrorCode.DOCTOR_UNAUTH_MODIFY);
      }
      userService.patientRelativeNotify(Collections.singletonList(patientId),
          PatientRelativeNotifyCategory.REVIEW_NOTIFY);
    } else if (body.getCategory() == DoctorAdviceCategory.INCOMPLETE_MEDICINE_NOTIFY.getCode()) {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (AuthUtils.isUserOfRole(UserRole.DOCTOR, authentication) && assistantId != 0) {
        return Response.instanceFail(AuthErrorCode.DOCTOR_NO_NEED_MODIFY);
      }
      body.setStatus(DoctorAdviceStatus.ASSISTANT_CONFIRM.getCode());
    }
    Response response = dpcService
        .updateDoctorAdvice(HashUtils.decode(id), body, assistantId, patientId);

    if (status != DoctorAdviceStatus.WAITING.ordinal()
        && status != DoctorAdviceStatus.IGNORE.ordinal()
        && status != DoctorAdviceStatus.ASSISTANT_WAITING.ordinal()//新增医助修改过状态
        && status != DoctorAdviceStatus.ASSISTANT_IGNORE.ordinal()) {//新增医助忽略状态
      notifyService.pushAdviceNotify(body.getAdvice(), patientId);
    }
    return response;
  }

  @PreAuthorize("hasAnyRole('DOCTOR')")
  @GetMapping(value = "/consultation")
  public Response getConsultation(@CurrentUserId Long userId,
      @RequestParam("patientId") String patientId,
      @RequestParam("isConsultationDoctor") Boolean isConsultationDoctor,
      @RequestParam(value = "pageAt", defaultValue = "1") Integer pageAt,
      @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
    ConsultationListResponse response = dpcService
        .getConsultation(userId, HashUtils.decode(patientId), isConsultationDoctor, pageAt,
            pageSize);
    if(!isConsultationDoctor){
      userService.resetPatientNotification(HashUtils.decode(patientId), StaffRole.DOCTOR);
    }
    return Response.instanceSuccess(response);
  }

  @PreAuthorize("hasAnyRole('DOCTOR')")
  @RequestMapping(value = "/consultation", method = RequestMethod.POST)
  public Response sendConsultation(@Valid @RequestBody AddConsultationApiRequest request) {
    dpcService.sendConsultation(request);
    return Response.instanceSuccess();
  }

  @PreAuthorize("hasAnyRole('DOCTOR')")
  @RequestMapping(value = "/consultation", method = RequestMethod.PATCH)
  public Response updateConsultation(@Valid @RequestBody UpdateConsultationApiRequest request) {
    dpcService.updateConsultation(request);
    return Response.instanceSuccess();
  }

  private void handleInspectionReference(UpdateDoctorAdviceClientRequest body, UserRole userRole) {
    String patientId = body.getPatientId();
    body.getInspectionStandards()
        .getInspectionStandardList().stream().filter(t -> t.getUserRole() == userRole)
        .findFirst().ifPresent(source -> {
      HandleCustomizableReferenceRequest request = new HandleCustomizableReferenceRequest();
      request.setPatientId(HashUtils.decode(patientId));
      request
          .setReferences(source.getEditIns().stream().filter(t -> t.getAction() == Status.EDIT).map(
              InspectionFactory::newCustomizableReference).collect(
              Collectors.toList()));
      medicalService.handleInspectionReference(request);
    });
  }

  private void dealAssistantIgnoreData(
      @Valid @RequestBody UpdateDoctorAdviceClientRequest body, int adviceCategory) {
    if (adviceCategory == DoctorAdviceCategory.CONTRAINDICATION_MEDICINE_ADJUSTMENT.getCode()) {
      String question = body.getQuestion();
      if (Objects.nonNull(question) && question.contains("，系统检测到患者正在使用该药，是否调药。")) {
        body.setQuestion(
            question.replace("，系统检测到患者正在使用该药，是否调药。"
                , "，您的助手未对患者用药做出调整，是否调药。"));
      }
    } else if (adviceCategory == DoctorAdviceCategory.INSPECTION_STANDARD.getCode()) {
      InspectionStandards inspectionStandards = InspectionStandards.builder()
          .inspectionStandardList(List.of()).build();
      body.setInspectionStandards(inspectionStandards);
    }
  }

  @PreAuthorize("hasRole('OPERATOR')")
  @GetMapping(value = "/notification/operator")
  public Response getOperatorNotificationList(@CurrentUserId Long uid,
      @RequestParam(value = "pageAt", defaultValue = "0") Integer pageAt,
      @RequestParam(value = "pageSize", defaultValue = "15") Integer pageSize) {

    ValidationUtils.validatePaginationParams(pageAt, pageSize);
    return Response.instanceSuccess(dpcService.getOperatorNotificationList(uid, pageAt, pageSize));
  }


  @PreAuthorize("hasRole('OPERATOR')")
  @PatchMapping(value = "/notification/operator")
  public Response updateOperatorNotification(
      @RequestBody UpdateOperatorNotificationRequest request) {

    if (request.invalid()) {
      logger.warn("update operator param invalid ! {}", request);
      throw new FailureException(ErrorCode.INVALID_PARAMS);
    }

    dpcService.updateOperatorNotification(OperatorNotificationFactory.of(request));
    return Response.instanceSuccess();
  }

  @PreAuthorize("hasRole('OPERATOR')")
  @PatchMapping(value = "/notification/operator/read")
  public Response updateNotificationReadAt(@CurrentUserId Long uid) {
    dpcService.updateOperatorNotificationReadAt(uid);
    return Response.instanceSuccess();
  }
}