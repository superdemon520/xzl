package cn.xinzhili.api.doctor.controller;

import static cn.xinzhili.api.doctor.config.ConfigConsts.prefixWithContext;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cn.xinzhili.api.doctor.bean.ConsultationInfo;
import cn.xinzhili.api.doctor.bean.DoctorApiMessage;
import cn.xinzhili.api.doctor.bean.DoctorMessage;
import cn.xinzhili.api.doctor.bean.InspectionApiStatus;
import cn.xinzhili.api.doctor.bean.InspectionStandard;
import cn.xinzhili.api.doctor.bean.InspectionStandardData;
import cn.xinzhili.api.doctor.bean.InspectionStandards;
import cn.xinzhili.api.doctor.bean.MedicineAdjustmentStatus;
import cn.xinzhili.api.doctor.bean.OperatorNotification;
import cn.xinzhili.api.doctor.bean.OperatorNotificationCategory;
import cn.xinzhili.api.doctor.bean.OperatorNotificationStatus;
import cn.xinzhili.api.doctor.bean.PatientApiInfo;
import cn.xinzhili.api.doctor.bean.UserRole;
import cn.xinzhili.api.doctor.bean.request.AddConsultationApiRequest;
import cn.xinzhili.api.doctor.bean.request.MedicationPlan;
import cn.xinzhili.api.doctor.bean.request.MedicationPlan.Status;
import cn.xinzhili.api.doctor.bean.request.SendDoctorAdviceWrapperRequest;
import cn.xinzhili.api.doctor.bean.request.UpdateChatApiRequest;
import cn.xinzhili.api.doctor.bean.request.UpdateConsultationApiRequest;
import cn.xinzhili.api.doctor.bean.request.UpdateDoctorAdviceClientRequest;
import cn.xinzhili.api.doctor.bean.request.UpdateOperatorNotificationRequest;
import cn.xinzhili.api.doctor.bean.response.AssayReport;
import cn.xinzhili.api.doctor.bean.response.AssaySupplement;
import cn.xinzhili.api.doctor.bean.response.ConsultationListResponse;
import cn.xinzhili.api.doctor.bean.response.DoctorAdviceWrapperResponse;
import cn.xinzhili.api.doctor.bean.response.DoctorMessageResponse;
import cn.xinzhili.api.doctor.bean.response.MedicineAdjustmentClient;
import cn.xinzhili.api.doctor.bean.response.MedicineAdjustmentsClient;
import cn.xinzhili.api.doctor.bean.response.MedicineAdjustmentsClient.MedicineAdjustment;
import cn.xinzhili.api.doctor.bean.response.MessageResponse;
import cn.xinzhili.api.doctor.bean.response.OperatorNotificationResponse;
import cn.xinzhili.api.doctor.bean.response.PatientDetailApiResponse;
import cn.xinzhili.api.doctor.bean.response.SideEffectSupplement;
import cn.xinzhili.api.doctor.common.ContextMock;
import cn.xinzhili.api.doctor.config.ConfigConsts;
import cn.xinzhili.api.doctor.service.DpcService;
import cn.xinzhili.api.doctor.service.DpcService.DoctorMessageContentType;
import cn.xinzhili.api.doctor.service.NotifyService;
import cn.xinzhili.api.doctor.service.UserService;
import cn.xinzhili.api.doctor.util.MessageFactory;
import cn.xinzhili.dpc.api.ConsultationLevel;
import cn.xinzhili.dpc.api.ConsultationStatus;
import cn.xinzhili.dpc.api.request.UpdateNotificationStatusRequest;
import cn.xinzhili.user.api.StaffRole;
import cn.xinzhili.xutils.core.http.Response;
import cn.xinzhili.xutils.spring.interceptor.ExceptionResolver;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * @author by Loki on 16/9/26.
 */
@RunWith(MockitoJUnitRunner.class)
public class DpcControllerTest {

  @Rule
  public JUnitRestDocumentation restDocumentation =
      new JUnitRestDocumentation("build/generated-snippets");

  private MockMvc mockMvc;

  @InjectMocks
  private DpcController dpcController;

  private ObjectMapper mapper;


  @InjectMocks
  private ExceptionResolver exceptionResolver;

  @Mock
  private DpcService dpcService;

  @Mock
  private UserService userService;

  @Mock
  private NotifyService notifyService;

  private List<Map<String, Object>> testArray;

  private ContextMock contextMock = new ContextMock();


  @Before
  public void setUp() {

    mapper = new ObjectMapper();

    int index = 0;
    testArray = new ArrayList<>();
    while (index < 5) {
      Map<String, Object> entry = new LinkedHashMap<>();
      entry.put("id", 123L);
      entry.put("receiver", "p_123");
      entry.put("sender", "d_123");
      entry.put("createdAt", "1474898377518");

      Map<String, Object> contentMap = new HashMap<>();
      Map<String, Object> dataMap = new HashMap<>();
      dataMap.put("type", 0);
      dataMap.put("content", "kds");
      contentMap.put("data", dataMap);
      contentMap.put("type", 0);

      entry.put("content", contentMap);

      testArray.add(entry);
      index++;
    }

    MockitoAnnotations.initMocks(this);
    this.mockMvc = MockMvcBuilders
        .standaloneSetup(this.dpcController)
        .setControllerAdvice(exceptionResolver)
        .setCustomArgumentResolvers(
            new AuthenticationPrincipalArgumentResolver(),
            contextMock.getMockUidResolver())
        .apply(MockMvcRestDocumentation
            .documentationConfiguration(restDocumentation)
            .uris()
            .withHost(ConfigConsts.API_HOST)
            .withPort(ConfigConsts.API_PORT))
        .build();
    this.contextMock.withUid(111L);

  }

  @Test
  public void testGetDoctorMessages() throws Exception {

    DoctorMessageResponse response = new DoctorMessageResponse();
    List<DoctorApiMessage> messages = new ArrayList<>();
    for (Map message : testArray) {
      messages.add(MessageFactory.Map2ApiMassage(message));
    }
    response.setMessages(messages);

    given(userService.checkBindRelation(any(Long.class), any(Long.class))).willReturn(true);
    given(dpcService.searchDoctorMessages(any(Long.class), any(Long.class), any(Long.class),
        any(StaffRole.class)))
        .willReturn(response);
    doNothing().when(userService).resetPatientNotification(anyLong(), any(StaffRole.class));
    this.contextMock.withUid(111L);

    this.mockMvc
        .perform(get(prefixWithContext("/message"))
            .param("patientId", "O8K3y8")
            .param("page", "1")
            .contentType(MediaType.APPLICATION_JSON)
            .contextPath(ConfigConsts.API_CONTEXT))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("get_doctor_messages",
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data.messages").description("返回聊天消息列表"),
                fieldWithPath("data.messages[].id").description("聊天消息id"),
                fieldWithPath("data.messages[].receiver").description("聊天消息接受者"),
                fieldWithPath("data.messages[].sender").description("聊天消息发送者"),
                fieldWithPath("data.messages[].doctorRead").description("大医生是否已读（0未读，1已读）"),
                fieldWithPath("data.messages[].assistantRead").description("小医生是否已读（0未读，1已读）"),
                fieldWithPath("data.messages[].commitTimes").description("运营第几次提交给医生"),
                fieldWithPath("data.messages[].direction").
                    description("消息指向：  INBOUND/该医生接受到的消息, OUTBOUND/该医生发送的消息"),
                fieldWithPath("data.messages[].createdAt").description("聊天消息创建时间"),
                fieldWithPath("data.messages[].readAt").description("消息读取时间"),
                fieldWithPath("data.messages[].content").description("聊天消息体"),
                fieldWithPath("data.messages[].content.data").description("聊天消息内容"),
                fieldWithPath("data.messages[].content.type").description("聊天消息类型")
            )
        ))
        .andDo(print())
        .andReturn();
  }

  @Test
  public void testCommitChatToDoctor() throws Exception {
    MessageResponse response = new MessageResponse(1);
    given(dpcService.commitChatToDoctor(any(Long.class), anyLong()))
        .willReturn(response);

    this.mockMvc
        .perform(patch(prefixWithContext("/group/chat/{id}"), "O8K3y8")
            .contentType(MediaType.APPLICATION_JSON)
            .contextPath(ConfigConsts.API_CONTEXT))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("commit_chat_to_doctor",
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data.number").description("修改记录数")
            )
        ))
        .andDo(print())
        .andReturn();
  }

  @Test
  public void testChangeChatToDoctorRead() throws Exception {
    MessageResponse response = new MessageResponse(1);
    given(dpcService.changeChatToDoctorRead(any(Long.class), anyLong(), anyLong(), anyBoolean()))
        .willReturn(response);
    UpdateChatApiRequest request = new UpdateChatApiRequest();
    request.setPatientId("O8K3y8");
    request.setCommitTimes(1l);
    this.mockMvc
        .perform(patch(prefixWithContext("/read/chat"))
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request))
            .contextPath(ConfigConsts.API_CONTEXT))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("doctor_read_chat",
            requestFields(
                fieldWithPath("patientId").description("患者id"),
                fieldWithPath("commitTimes").description("医助提交次数")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data.number").description("修改记录数")
            )
        ))
        .andDo(print())
        .andReturn();
  }

  @Test
  public void testAddDoctorMessages() throws Exception {

    doNothing().when(dpcService).sendChatMessage(any());
    given(notifyService.pushMessageNotify(any(), any())).willReturn(Response.instanceSuccess());
    this.contextMock.withUid(111L);

    DoctorMessage message = new DoctorMessage();
    message.setReceiver("O8K3y8");
    message.setContent("...");
    message.setType(DoctorMessageContentType.TEXT.name());

    this.mockMvc
        .perform(post(prefixWithContext("/chat"))
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(message))
            .contextPath(ConfigConsts.API_CONTEXT))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("add_doctor_messages",
            requestFields(
                fieldWithPath("sender").ignored(),
                fieldWithPath("receiver").description("消息接受者"),
                fieldWithPath("type").description("消息类型"),
                fieldWithPath("content").description("消息内容")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("")
            )
        ))
        .andDo(print())
        .andReturn();
  }

  @Test
  public void testSendDoctorAdvice() throws Exception {
    PatientDetailApiResponse response = new PatientDetailApiResponse();
    response.setDoctorId("O8K3y8");
    response.setAssistantId("O8K3y8");
    response.setPatientInfo(new PatientApiInfo());
    given(userService.getPatientById(anyLong())).willReturn(response);
    contextMock.withUid(111L);

    SendDoctorAdviceWrapperRequest request = new SendDoctorAdviceWrapperRequest();
    request.setPatientId("O8K3y8");
    request.setQuestion("000");
    request.setAdvice("000");
    request.setReason("000");
    request.setLevel(0);
    request.setCategory(3);

    MedicineAdjustmentClient adjustment = MedicineAdjustmentClient.builder().name("name")
        .id("m5Rq38")
        .markId("1")
        .status(MedicineAdjustmentStatus.DELETE)
        .unit("mg").plan(Collections
            .singletonList(
                MedicationPlan.builder().originDosage("2").dosage("1").id("1")
                    .status(MedicationPlan.Status.DELETE)
                    .originTakeTime(Collections.singletonList(2))
                    .takeTime(Collections.singletonList(1)).build())).build();

    MedicineAdjustment medicineAdjustment = MedicineAdjustment.builder().role(UserRole.DOCTOR)
        .note("note").medicineAdjustment(Collections.singletonList(adjustment)).build();
    MedicineAdjustmentsClient supplement = MedicineAdjustmentsClient.builder()
        .medicineSupplement(Collections.singletonList(medicineAdjustment)).build();

    InspectionStandardData inspectionStandardData = InspectionStandardData.builder()
        .action(Status.EDIT).customizedReferenceMax(200.00).customizedReferenceMin(20.2).item("mg")
        .measuredAt(1506963661000L).name("血压").originCustomizedReferenceMax(300.1)
        .originCustomizedReferenceMin(33.3).spliceSymbol("/").status(InspectionApiStatus.NORMAL)
        .unit("uv").unifiedReference("test").value("123").build();

    InspectionStandard inspectionStandard = InspectionStandard.builder()
        .userRole(UserRole.ASSISTANT).advice("note")
        .editIns(List.of(inspectionStandardData, inspectionStandardData)).build();
    InspectionStandards inspectionStandards = InspectionStandards.builder()
        .inspectionStandardList(List.of(inspectionStandard)).build();

    request.setMedicineSupplement(supplement);
    request.setInspectionStandards(inspectionStandards);

    given(notifyService.pushAdviceNotify(any(), any())).willReturn(Response.instanceSuccess());
    given(dpcService.sendDoctorAdvice(any())).willReturn(Response.instanceSuccess());

    this.mockMvc
        .perform(post(prefixWithContext("/advice"))
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request))
            .contextPath(ConfigConsts.API_CONTEXT))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("send_doctor_advice",
            requestFields(
                fieldWithPath("patientId").description("患者 id"),
                fieldWithPath("question").description("问题"),
                fieldWithPath("advice").description("医嘱"),
                fieldWithPath("reason").description("原因"),
                fieldWithPath("level").description("级别: 0 -> 普通; 1 -> 严重"),
                fieldWithPath("category")
                    .description("医嘱类型:0/普通医嘱，1/调药医嘱，2/复查，3/副作用调药，4/禁忌症调药，5/生活达标调药，6/达标建议值调整"),
                fieldWithPath("medicineSupplement").description("调药补遗（可选参数仅在 category 为调药时有效）")
                    .optional(),
                fieldWithPath("medicineSupplement.medicineSupplement[]").description("调药单"),
                fieldWithPath("medicineSupplement.medicineSupplement[].role")
                    .description("调药角色 DOCTOR/大医生 ASSISTANT/小医生"),
                fieldWithPath("medicineSupplement.medicineSupplement[].note").description("调药备注"),
                fieldWithPath("medicineSupplement.medicineSupplement[].medicineAdjustment[].id")
                    .description("药品 id"),
                fieldWithPath("medicineSupplement.medicineSupplement[].medicineAdjustment[].name")
                    .description("药名"),
                fieldWithPath("medicineSupplement.medicineSupplement[].medicineAdjustment[].markId")
                    .description("编号"),
                fieldWithPath("medicineSupplement.medicineSupplement[].medicineAdjustment[].unit")
                    .description("单位"),
                fieldWithPath("medicineSupplement.medicineSupplement[].medicineAdjustment[].status")
                    .description("状态: NORMAL, ADD, DELETE"),
                fieldWithPath("medicineSupplement.medicineSupplement[].medicineAdjustment[].plan")
                    .description("用药计划"),
                fieldWithPath(
                    "medicineSupplement.medicineSupplement[].medicineAdjustment[].plan[].id")
                    .description("编号"),
                fieldWithPath(
                    "medicineSupplement.medicineSupplement[].medicineAdjustment[].plan[].originDosage")
                    .description("原剂量（修改时用）"),
                fieldWithPath(
                    "medicineSupplement.medicineSupplement[].medicineAdjustment[].plan[].dosage")
                    .description("剂量"),
                fieldWithPath(
                    "medicineSupplement.medicineSupplement[].medicineAdjustment[].plan[].status")
                    .description("状态: ADD, EDIT, DELETE, NONE"),
                fieldWithPath(
                    "medicineSupplement.medicineSupplement[].medicineAdjustment[].plan[].originTakeTime")
                    .description("原用药时间（修改时用）"),
                fieldWithPath(
                    "medicineSupplement.medicineSupplement[].medicineAdjustment[].plan[].takeTime")
                    .description("用药时间"),
                fieldWithPath(
                    "inspectionStandards").description("达标值diff"),
                fieldWithPath(
                    "inspectionStandards.inspectionStandardList[].advice")
                    .description("注释"),
                fieldWithPath(
                    "inspectionStandards.inspectionStandardList[].userRole")
                    .description("操作角色"),
                fieldWithPath(
                    "inspectionStandards.inspectionStandardList[].editIns[].name")
                    .description("达标名"),
                fieldWithPath(
                    "inspectionStandards.inspectionStandardList[].editIns[].measuredAt")
                    .description("测量时间"),
                fieldWithPath(
                    "inspectionStandards.inspectionStandardList[].editIns[].value")
                    .description("修改值"),
                fieldWithPath(
                    "inspectionStandards.inspectionStandardList[].editIns[].unit")
                    .description("单位"),
                fieldWithPath(
                    "inspectionStandards.inspectionStandardList[].editIns[].status")
                    .description("状态 NORMAL、HIGH、LOW"),
                fieldWithPath(
                    "inspectionStandards.inspectionStandardList[].editIns[].unifiedReference")
                    .description("参考值"),
                fieldWithPath(
                    "inspectionStandards.inspectionStandardList[].editIns[].customizedReferenceMax")
                    .description("修改后最大值"),
                fieldWithPath(
                    "inspectionStandards.inspectionStandardList[].editIns[].spliceSymbol")
                    .description("分隔符"),
                fieldWithPath(
                    "inspectionStandards.inspectionStandardList[].editIns[].action")
                    .description("操作动作 EDIT、NONE"),
                fieldWithPath(
                    "inspectionStandards.inspectionStandardList[].editIns[].originCustomizedReferenceMax")
                    .description("原始最大值"),
                fieldWithPath(
                    "inspectionStandards.inspectionStandardList[].editIns[].originCustomizedReferenceMin")
                    .description("原始最小值"),
                fieldWithPath(
                    "inspectionStandards.inspectionStandardList[].editIns[].item")
                    .description("缩写")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("")
            )
        ))
        .andDo(print())
        .andReturn();
  }


  @Test
  public void testDoctorAdvices() throws Exception {
    PatientDetailApiResponse response = new PatientDetailApiResponse();
    response.setDoctorId("O8K3y8");
    response.setAssistantId("O8K3y8");
    response.setPatientInfo(new PatientApiInfo());
    given(userService.getPatientById(anyLong())).willReturn(response);
    contextMock.withUid(111L);

    MedicineAdjustmentClient adjustment = MedicineAdjustmentClient.builder().name("name")
        .id("O8K3y8")
        .markId("1")
        .status(MedicineAdjustmentStatus.NORMAL)
        .unit("mg").plan(Collections.singletonList(
            MedicationPlan.builder().originDosage("1").dosage("3").id("1")
                .status(MedicationPlan.Status.EDIT).originTakeTime(Collections.singletonList(2))
                .takeTime(Collections.singletonList(1)).build())).build();

    MedicineAdjustment medicineAdjustment = MedicineAdjustment.builder().role(UserRole.DOCTOR)
        .note("note").medicineAdjustment(Collections.singletonList(adjustment)).build();

    MedicineAdjustmentsClient supplement = MedicineAdjustmentsClient.builder()
        .medicineSupplement(Collections.singletonList(medicineAdjustment)).build();

    AssaySupplement assay = AssaySupplement.builder().report(Collections.singletonList(
        AssayReport.builder().name("生化信息").link(Collections.singletonList("http://example.com"))
            .build())).build();

    SideEffectSupplement sideEffectSupplement = SideEffectSupplement.builder()
        .detailLink("http://example.com").build();

    InspectionStandardData inspectionStandardData = InspectionStandardData.builder()
        .action(Status.EDIT).customizedReferenceMax(200.00).customizedReferenceMin(20.2).item("mg")
        .measuredAt(1506963661000L).name("血压").originCustomizedReferenceMax(300.1)
        .originCustomizedReferenceMin(33.3).spliceSymbol("/").status(InspectionApiStatus.NORMAL)
        .unit("uv").unifiedReference("test").value("123").build();

    InspectionStandard inspectionStandard = InspectionStandard.builder()
        .userRole(UserRole.ASSISTANT).advice("note")
        .editIns(List.of(inspectionStandardData, inspectionStandardData)).build();
    InspectionStandards inspectionStandards = InspectionStandards.builder()
        .inspectionStandardList(List.of(inspectionStandard)).build();

    DoctorAdviceWrapperResponse data = DoctorAdviceWrapperResponse.builder().id("O8K3y8")
        .patientId("O8K3y8").doctorId("O8K3y8")
        .assistantId("O8K3y8").question("").advice("").reason("").status(0).category(1)
        .medicineSupplement(supplement).assaySupplement(assay)
        .sideEffectSupplement(sideEffectSupplement).inspectionStandards(inspectionStandards)
        .build();

    given(dpcService
        .doctorAdvices(anyLong(), anyLong(), anyLong(), anyListOf(Integer.class), anyBoolean(),
            anyBoolean(), anyInt(),
            anyInt())).willReturn(
        Response.instanceSuccess().withDataEntry("advices", Collections.singletonList(data)));

    mockMvc
        .perform(get(prefixWithContext("/advice"))
            .param("patientId", "O8K3y8")
            .param("status", "0")
            .param("status", "1")
            .param("orderByUpdateTime", "true")
            .contentType(MediaType.APPLICATION_JSON)
            .contextPath(ConfigConsts.API_CONTEXT))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("doctor_advices",
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data.advices").description("返回医嘱列表"),
                fieldWithPath("data.advices[].id").description("医嘱id"),
                fieldWithPath("data.advices[].patientId").description("患者 id"),
                fieldWithPath("data.advices[].doctorId").description("医生 id"),
                fieldWithPath("data.advices[].assistantId").description("助手 id"),
                fieldWithPath("data.advices[].question").description("问题"),
                fieldWithPath("data.advices[].advice").description("医嘱"),
                fieldWithPath("data.advices[].reason").description("原因"),
                fieldWithPath("data.advices[].status")
                    .description(
                        "状态: 0 -> 待审核; 1 -> 同意; 2 -> 忽略; 3 -> 修改; 4 -> 医生直发; 5 -> 助手修改待审核; 6 -> 助手忽略"),
                fieldWithPath("data.advices[].category")
                    .description(
                        "0 -> 普通; 1 -> 调药; 2 -> 复查反馈; 3 -> 副作用调药; 4 -> 禁忌症调药; 5 -> 生活达标调药; 6 -> 达标建议值调整. 默认 0"),
                fieldWithPath("data.advices[].medicineSupplement")
                    .description("调药补遗（可选参数仅在 category 为调药时有效）")
                    .optional(),
                fieldWithPath("data.advices[].medicineSupplement.medicineSupplement[].role")
                    .description("修改调药单角色"),
                fieldWithPath("data.advices[].medicineSupplement.medicineSupplement[].note")
                    .description("修改调药单备注"),
                fieldWithPath(
                    "data.advices[].medicineSupplement.medicineSupplement[].medicineAdjustment[].id")
                    .description("药品id"),
                fieldWithPath(
                    "data.advices[].medicineSupplement.medicineSupplement[].medicineAdjustment[].name")
                    .description("药名"),
                fieldWithPath(
                    "data.advices[].medicineSupplement.medicineSupplement[].medicineAdjustment[].markId")
                    .description("编号"),
                fieldWithPath(
                    "data.advices[].medicineSupplement.medicineSupplement[].medicineAdjustment[].unit")
                    .description("单位"),
                fieldWithPath(
                    "data.advices[].medicineSupplement.medicineSupplement[].medicineAdjustment[].status")
                    .description("状态: NORMAL, ADD, DELETE"),
                fieldWithPath(
                    "data.advices[].medicineSupplement.medicineSupplement[].medicineAdjustment[].plan")
                    .description("用药计划"),
                fieldWithPath(
                    "data.advices[].medicineSupplement.medicineSupplement[].medicineAdjustment[].plan[].id")
                    .description("编号"),
                fieldWithPath(
                    "data.advices[].medicineSupplement.medicineSupplement[].medicineAdjustment[].plan[].originDosage")
                    .description("原剂量（修改时用）"),
                fieldWithPath(
                    "data.advices[].medicineSupplement.medicineSupplement[].medicineAdjustment[].plan[].dosage")
                    .description("剂量"),
                fieldWithPath(
                    "data.advices[].medicineSupplement.medicineSupplement[].medicineAdjustment[].plan[].status")
                    .description("状态: ADD, EDIT, DELETE, NONE"),
                fieldWithPath(
                    "data.advices[].medicineSupplement.medicineSupplement[].medicineAdjustment[].plan[].originTakeTime")
                    .description("原用药时间（修改时用）"),
                fieldWithPath(
                    "data.advices[].medicineSupplement.medicineSupplement[].medicineAdjustment[].plan[].takeTime")
                    .description("用药时间"),
                fieldWithPath("data.advices[].assaySupplement").description("化验补遗"),
                fieldWithPath("data.advices[].assaySupplement.report").description("化验单"),
                fieldWithPath("data.advices[].assaySupplement.report.[].name").description("化验名"),
                fieldWithPath("data.advices[].assaySupplement.report.[].link").description("链接(s)"),
                fieldWithPath("data.advices[].sideEffectSupplement").description("副作用补遗"),
                fieldWithPath("data.advices[].sideEffectSupplement.detailLink")
                    .description("查看详情链接"),
                fieldWithPath(
                    "data.advices[].inspectionStandards").description("达标值diff"),
                fieldWithPath(
                    "data.advices[].inspectionStandards.inspectionStandardList[].advice")
                    .description("注释"),
                fieldWithPath(
                    "data.advices[].inspectionStandards.inspectionStandardList[].userRole")
                    .description("操作角色"),
                fieldWithPath(
                    "data.advices[].inspectionStandards.inspectionStandardList[].editIns[].name")
                    .description("达标名"),
                fieldWithPath(
                    "data.advices[].inspectionStandards.inspectionStandardList[].editIns[].measuredAt")
                    .description("测量时间"),
                fieldWithPath(
                    "data.advices[].inspectionStandards.inspectionStandardList[].editIns[].value")
                    .description("修改值"),
                fieldWithPath(
                    "data.advices[].inspectionStandards.inspectionStandardList[].editIns[].unit")
                    .description("单位"),
                fieldWithPath(
                    "data.advices[].inspectionStandards.inspectionStandardList[].editIns[].status")
                    .description("状态 NORMAL、HIGH、LOW"),
                fieldWithPath(
                    "data.advices[].inspectionStandards.inspectionStandardList[].editIns[].unifiedReference")
                    .description("参考值"),
                fieldWithPath(
                    "data.advices[].inspectionStandards.inspectionStandardList[].editIns[].customizedReferenceMax")
                    .description("修改后最大值"),
                fieldWithPath(
                    "data.advices[].inspectionStandards.inspectionStandardList[].editIns[].spliceSymbol")
                    .description("分隔符"),
                fieldWithPath(
                    "data.advices[].inspectionStandards.inspectionStandardList[].editIns[].action")
                    .description("操作动作 EDIT、NONE"),
                fieldWithPath(
                    "data.advices[].inspectionStandards.inspectionStandardList[].editIns[].originCustomizedReferenceMax")
                    .description("原始最大值"),
                fieldWithPath(
                    "data.advices[].inspectionStandards.inspectionStandardList[].editIns[].originCustomizedReferenceMin")
                    .description("原始最小值"),
                fieldWithPath(
                    "data.advices[].inspectionStandards.inspectionStandardList[].editIns[].item")
                    .description("缩写")
            )
        ))
        .andDo(print())
        .andReturn();
  }


  @Test
  public void testAdvicesConfirmed() throws Exception {
    PatientDetailApiResponse response = new PatientDetailApiResponse();
    response.setDoctorId("O8K3y8");
    response.setAssistantId("O8K3y8");
    response.setPatientInfo(new PatientApiInfo());
    given(userService.getPatientById(anyLong())).willReturn(response);
    contextMock.withUid(111L);

    MedicineAdjustmentClient adjustment = MedicineAdjustmentClient.builder().name("name")
        .id("O8K3y8")
        .markId("1")
        .status(MedicineAdjustmentStatus.NORMAL)
        .unit("mg").plan(Collections.singletonList(
            MedicationPlan.builder().originDosage("1").dosage("3").id("1")
                .status(MedicationPlan.Status.EDIT).originTakeTime(Collections.singletonList(2))
                .takeTime(Collections.singletonList(1)).build())).build();

    MedicineAdjustment medicineAdjustment = MedicineAdjustment.builder().role(UserRole.DOCTOR)
        .note("note").medicineAdjustment(Collections.singletonList(adjustment)).build();

    MedicineAdjustmentsClient supplement = MedicineAdjustmentsClient.builder()
        .medicineSupplement(Collections.singletonList(medicineAdjustment)).build();

    AssaySupplement assay = AssaySupplement.builder().report(Collections.singletonList(
        AssayReport.builder().name("生化信息").link(Collections.singletonList("http://example.com"))
            .build())).build();

    SideEffectSupplement sideEffectSupplement = SideEffectSupplement.builder()
        .detailLink("http://example.com").build();

    InspectionStandardData inspectionStandardData = InspectionStandardData.builder()
        .action(Status.EDIT).customizedReferenceMax(200.00).customizedReferenceMin(20.2).item("mg")
        .measuredAt(1506963661000L).name("血压").originCustomizedReferenceMax(300.1)
        .originCustomizedReferenceMin(33.3).spliceSymbol("/").status(InspectionApiStatus.NORMAL)
        .unit("uv").unifiedReference("test").value("123").build();

    InspectionStandard inspectionStandard = InspectionStandard.builder()
        .userRole(UserRole.ASSISTANT).advice("note")
        .editIns(List.of(inspectionStandardData, inspectionStandardData)).build();
    InspectionStandards inspectionStandards = InspectionStandards.builder()
        .inspectionStandardList(List.of(inspectionStandard)).build();

    DoctorAdviceWrapperResponse data = DoctorAdviceWrapperResponse.builder().id("O8K3y8")
        .patientId("O8K3y8").doctorId("O8K3y8")
        .assistantId("O8K3y8").question("").advice("").reason("").status(0).category(1)
        .medicineSupplement(supplement).assaySupplement(assay)
        .sideEffectSupplement(sideEffectSupplement).inspectionStandards(inspectionStandards)
        .build();

    given(dpcService
        .getConfirmedAdviceList(anyLong(), anyLong(), anyLong(), anyBoolean(), anyInt(),
            anyInt())).willReturn(
        Response.instanceSuccess().withDataEntry("advices", Collections.singletonList(data))
            .withDataEntry("total", 22));

    mockMvc
        .perform(get(prefixWithContext("/advice/confirmed"))
            .param("patientId", "O8K3y8")
            .param("page", "1")
            .param("pageSize", "25")
            .contentType(MediaType.APPLICATION_JSON)
            .contextPath(ConfigConsts.API_CONTEXT))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("doctor_advice_confirmed",
            requestParameters(
                parameterWithName("patientId").description("患者id"),
                parameterWithName("page").description("页码：默认1"),
                parameterWithName("pageSize").description("每页数：默认25")

            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data.advices").description("返回医嘱列表"),
                fieldWithPath("data.advices[].id").description("医嘱id"),
                fieldWithPath("data.advices[].patientId").description("患者 id"),
                fieldWithPath("data.advices[].doctorId").description("医生 id"),
                fieldWithPath("data.advices[].assistantId").description("助手 id"),
                fieldWithPath("data.advices[].question").description("问题"),
                fieldWithPath("data.advices[].advice").description("医嘱"),
                fieldWithPath("data.advices[].reason").description("原因"),
                fieldWithPath("data.advices[].status")
                    .description(
                        "状态: 0 -> 待审核; 1 -> 同意; 2 -> 忽略; 3 -> 修改; 4 -> 医生直发; 5 -> 助手修改待审核; 6 -> 助手忽略; 7 -> 助手确认"),
                fieldWithPath("data.advices[].category")
                    .description(
                        "0 -> 普通; 1 -> 调药; 2 -> 复查反馈; 3 -> 副作用调药; 4 -> 禁忌症调药; 5 -> 生活达标调药; 6 -> 达标建议值调整; 7 -> 忘服漏服提醒. 默认 0"),
                fieldWithPath("data.advices[].medicineSupplement")
                    .description("调药补遗（可选参数仅在 category 为调药时有效）")
                    .optional(),
                fieldWithPath("data.advices[].medicineSupplement.medicineSupplement[].role")
                    .description("修改调药单角色"),
                fieldWithPath("data.advices[].medicineSupplement.medicineSupplement[].note")
                    .description("修改调药单备注"),
                fieldWithPath(
                    "data.advices[].medicineSupplement.medicineSupplement[].medicineAdjustment[].id")
                    .description("药品id"),
                fieldWithPath(
                    "data.advices[].medicineSupplement.medicineSupplement[].medicineAdjustment[].name")
                    .description("药名"),
                fieldWithPath(
                    "data.advices[].medicineSupplement.medicineSupplement[].medicineAdjustment[].markId")
                    .description("编号"),
                fieldWithPath(
                    "data.advices[].medicineSupplement.medicineSupplement[].medicineAdjustment[].unit")
                    .description("单位"),
                fieldWithPath(
                    "data.advices[].medicineSupplement.medicineSupplement[].medicineAdjustment[].status")
                    .description("状态: NORMAL, ADD, DELETE"),
                fieldWithPath(
                    "data.advices[].medicineSupplement.medicineSupplement[].medicineAdjustment[].plan")
                    .description("用药计划"),
                fieldWithPath(
                    "data.advices[].medicineSupplement.medicineSupplement[].medicineAdjustment[].plan[].id")
                    .description("编号"),
                fieldWithPath(
                    "data.advices[].medicineSupplement.medicineSupplement[].medicineAdjustment[].plan[].originDosage")
                    .description("原剂量（修改时用）"),
                fieldWithPath(
                    "data.advices[].medicineSupplement.medicineSupplement[].medicineAdjustment[].plan[].dosage")
                    .description("剂量"),
                fieldWithPath(
                    "data.advices[].medicineSupplement.medicineSupplement[].medicineAdjustment[].plan[].status")
                    .description("状态: ADD, EDIT, DELETE, NONE"),
                fieldWithPath(
                    "data.advices[].medicineSupplement.medicineSupplement[].medicineAdjustment[].plan[].originTakeTime")
                    .description("原用药时间（修改时用）"),
                fieldWithPath(
                    "data.advices[].medicineSupplement.medicineSupplement[].medicineAdjustment[].plan[].takeTime")
                    .description("用药时间"),
                fieldWithPath("data.advices[].assaySupplement").description("化验补遗"),
                fieldWithPath("data.advices[].assaySupplement.report").description("化验单"),
                fieldWithPath("data.advices[].assaySupplement.report.[].name").description("化验名"),
                fieldWithPath("data.advices[].assaySupplement.report.[].link").description("链接(s)"),
                fieldWithPath("data.advices[].sideEffectSupplement").description("副作用补遗"),
                fieldWithPath("data.advices[].sideEffectSupplement.detailLink")
                    .description("查看详情链接"),
                fieldWithPath(
                    "data.advices[].inspectionStandards").description("达标值diff"),
                fieldWithPath(
                    "data.advices[].inspectionStandards.inspectionStandardList[].advice")
                    .description("注释"),
                fieldWithPath(
                    "data.advices[].inspectionStandards.inspectionStandardList[].userRole")
                    .description("操作角色"),
                fieldWithPath(
                    "data.advices[].inspectionStandards.inspectionStandardList[].editIns[].name")
                    .description("达标名"),
                fieldWithPath(
                    "data.advices[].inspectionStandards.inspectionStandardList[].editIns[].measuredAt")
                    .description("测量时间"),
                fieldWithPath(
                    "data.advices[].inspectionStandards.inspectionStandardList[].editIns[].value")
                    .description("修改值"),
                fieldWithPath(
                    "data.advices[].inspectionStandards.inspectionStandardList[].editIns[].unit")
                    .description("单位"),
                fieldWithPath(
                    "data.advices[].inspectionStandards.inspectionStandardList[].editIns[].status")
                    .description("状态 NORMAL、HIGH、LOW"),
                fieldWithPath(
                    "data.advices[].inspectionStandards.inspectionStandardList[].editIns[].unifiedReference")
                    .description("参考值"),
                fieldWithPath(
                    "data.advices[].inspectionStandards.inspectionStandardList[].editIns[].customizedReferenceMax")
                    .description("修改后最大值"),
                fieldWithPath(
                    "data.advices[].inspectionStandards.inspectionStandardList[].editIns[].spliceSymbol")
                    .description("分隔符"),
                fieldWithPath(
                    "data.advices[].inspectionStandards.inspectionStandardList[].editIns[].action")
                    .description("操作动作 EDIT、NONE"),
                fieldWithPath(
                    "data.advices[].inspectionStandards.inspectionStandardList[].editIns[].originCustomizedReferenceMax")
                    .description("原始最大值"),
                fieldWithPath(
                    "data.advices[].inspectionStandards.inspectionStandardList[].editIns[].originCustomizedReferenceMin")
                    .description("原始最小值"),
                fieldWithPath(
                    "data.advices[].inspectionStandards.inspectionStandardList[].editIns[].item")
                    .description("缩写"),
                fieldWithPath("data.total").description("总数")
            )
        ))
        .andDo(print())
        .andReturn();
  }


  @Test
  public void testAdvicesUnconfirmed() throws Exception {
    PatientDetailApiResponse response = new PatientDetailApiResponse();
    response.setDoctorId("O8K3y8");
    response.setAssistantId("O8K3y8");
    response.setPatientInfo(new PatientApiInfo());
    given(userService.getPatientById(anyLong())).willReturn(response);
    contextMock.withUid(111L);

    MedicineAdjustmentClient adjustment = MedicineAdjustmentClient.builder().name("name")
        .id("O8K3y8")
        .markId("1")
        .status(MedicineAdjustmentStatus.NORMAL)
        .unit("mg").plan(Collections.singletonList(
            MedicationPlan.builder().originDosage("1").dosage("3").id("1")
                .status(MedicationPlan.Status.EDIT).originTakeTime(Collections.singletonList(2))
                .takeTime(Collections.singletonList(1)).build())).build();

    MedicineAdjustment medicineAdjustment = MedicineAdjustment.builder().role(UserRole.DOCTOR)
        .note("note").medicineAdjustment(Collections.singletonList(adjustment)).build();

    MedicineAdjustmentsClient supplement = MedicineAdjustmentsClient.builder()
        .medicineSupplement(Collections.singletonList(medicineAdjustment)).build();

    AssaySupplement assay = AssaySupplement.builder().report(Collections.singletonList(
        AssayReport.builder().name("生化信息").link(Collections.singletonList("http://example.com"))
            .build())).build();

    SideEffectSupplement sideEffectSupplement = SideEffectSupplement.builder()
        .detailLink("http://example.com").build();

    InspectionStandardData inspectionStandardData = InspectionStandardData.builder()
        .action(Status.EDIT).customizedReferenceMax(200.00).customizedReferenceMin(20.2).item("mg")
        .measuredAt(1506963661000L).name("血压").originCustomizedReferenceMax(300.1)
        .originCustomizedReferenceMin(33.3).spliceSymbol("/").status(InspectionApiStatus.NORMAL)
        .unit("uv").unifiedReference("test").value("123").build();

    InspectionStandard inspectionStandard = InspectionStandard.builder()
        .userRole(UserRole.ASSISTANT).advice("note")
        .editIns(List.of(inspectionStandardData, inspectionStandardData)).build();
    InspectionStandards inspectionStandards = InspectionStandards.builder()
        .inspectionStandardList(List.of(inspectionStandard)).build();

    DoctorAdviceWrapperResponse data = DoctorAdviceWrapperResponse.builder().id("O8K3y8")
        .patientId("O8K3y8").doctorId("O8K3y8")
        .assistantId("O8K3y8").question("").advice("").reason("").status(0).category(1)
        .medicineSupplement(supplement).assaySupplement(assay)
        .sideEffectSupplement(sideEffectSupplement).inspectionStandards(inspectionStandards)
        .build();

    given(dpcService
        .getUnconfirmedAdviceList(anyLong(), anyLong(), anyLong(), anyBoolean(), anyInt(),
            anyInt())).willReturn(
        Response.instanceSuccess().withDataEntry("advices", Collections.singletonList(data))
            .withDataEntry("total", 22));

    mockMvc
        .perform(get(prefixWithContext("/advice/unconfirmed"))
            .param("patientId", "O8K3y8")
            .param("page", "1")
            .param("pageSize", "25")
            .contentType(MediaType.APPLICATION_JSON)
            .contextPath(ConfigConsts.API_CONTEXT))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("doctor_advice_unconfirmed",
            requestParameters(
                parameterWithName("patientId").description("患者id"),
                parameterWithName("page").description("页码：默认1"),
                parameterWithName("pageSize").description("每页数：默认25")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data.advices").description("返回医嘱列表"),
                fieldWithPath("data.advices[].id").description("医嘱id"),
                fieldWithPath("data.advices[].patientId").description("患者 id"),
                fieldWithPath("data.advices[].doctorId").description("医生 id"),
                fieldWithPath("data.advices[].assistantId").description("助手 id"),
                fieldWithPath("data.advices[].question").description("问题"),
                fieldWithPath("data.advices[].advice").description("医嘱"),
                fieldWithPath("data.advices[].reason").description("原因"),
                fieldWithPath("data.advices[].status")
                    .description(
                        "状态: 0 -> 待审核; 1 -> 同意; 2 -> 忽略; 3 -> 修改; 4 -> 医生直发; 5 -> 助手修改待审核; 6 -> 助手忽略; 7 -> 助手确认"),
                fieldWithPath("data.advices[].category")
                    .description(
                        "0 -> 普通; 1 -> 调药; 2 -> 复查反馈; 3 -> 副作用调药; 4 -> 禁忌症调药; 5 -> 生活达标调药; 6 -> 达标建议值调整; 7 -> 忘服漏服提醒. 默认 0"),
                fieldWithPath("data.advices[].medicineSupplement")
                    .description("调药补遗（可选参数仅在 category 为调药时有效）")
                    .optional(),
                fieldWithPath("data.advices[].medicineSupplement.medicineSupplement[].role")
                    .description("修改调药单角色"),
                fieldWithPath("data.advices[].medicineSupplement.medicineSupplement[].note")
                    .description("修改调药单备注"),
                fieldWithPath(
                    "data.advices[].medicineSupplement.medicineSupplement[].medicineAdjustment[].id")
                    .description("药品id"),
                fieldWithPath(
                    "data.advices[].medicineSupplement.medicineSupplement[].medicineAdjustment[].name")
                    .description("药名"),
                fieldWithPath(
                    "data.advices[].medicineSupplement.medicineSupplement[].medicineAdjustment[].markId")
                    .description("编号"),
                fieldWithPath(
                    "data.advices[].medicineSupplement.medicineSupplement[].medicineAdjustment[].unit")
                    .description("单位"),
                fieldWithPath(
                    "data.advices[].medicineSupplement.medicineSupplement[].medicineAdjustment[].status")
                    .description("状态: NORMAL, ADD, DELETE"),
                fieldWithPath(
                    "data.advices[].medicineSupplement.medicineSupplement[].medicineAdjustment[].plan")
                    .description("用药计划"),
                fieldWithPath(
                    "data.advices[].medicineSupplement.medicineSupplement[].medicineAdjustment[].plan[].id")
                    .description("编号"),
                fieldWithPath(
                    "data.advices[].medicineSupplement.medicineSupplement[].medicineAdjustment[].plan[].originDosage")
                    .description("原剂量（修改时用）"),
                fieldWithPath(
                    "data.advices[].medicineSupplement.medicineSupplement[].medicineAdjustment[].plan[].dosage")
                    .description("剂量"),
                fieldWithPath(
                    "data.advices[].medicineSupplement.medicineSupplement[].medicineAdjustment[].plan[].status")
                    .description("状态: ADD, EDIT, DELETE, NONE"),
                fieldWithPath(
                    "data.advices[].medicineSupplement.medicineSupplement[].medicineAdjustment[].plan[].originTakeTime")
                    .description("原用药时间（修改时用）"),
                fieldWithPath(
                    "data.advices[].medicineSupplement.medicineSupplement[].medicineAdjustment[].plan[].takeTime")
                    .description("用药时间"),
                fieldWithPath("data.advices[].assaySupplement").description("化验补遗"),
                fieldWithPath("data.advices[].assaySupplement.report").description("化验单"),
                fieldWithPath("data.advices[].assaySupplement.report.[].name").description("化验名"),
                fieldWithPath("data.advices[].assaySupplement.report.[].link").description("链接(s)"),
                fieldWithPath("data.advices[].sideEffectSupplement").description("副作用补遗"),
                fieldWithPath("data.advices[].sideEffectSupplement.detailLink")
                    .description("查看详情链接"),
                fieldWithPath(
                    "data.advices[].inspectionStandards").description("达标值diff"),
                fieldWithPath(
                    "data.advices[].inspectionStandards.inspectionStandardList[].advice")
                    .description("注释"),
                fieldWithPath(
                    "data.advices[].inspectionStandards.inspectionStandardList[].userRole")
                    .description("操作角色"),
                fieldWithPath(
                    "data.advices[].inspectionStandards.inspectionStandardList[].editIns[].name")
                    .description("达标名"),
                fieldWithPath(
                    "data.advices[].inspectionStandards.inspectionStandardList[].editIns[].measuredAt")
                    .description("测量时间"),
                fieldWithPath(
                    "data.advices[].inspectionStandards.inspectionStandardList[].editIns[].value")
                    .description("修改值"),
                fieldWithPath(
                    "data.advices[].inspectionStandards.inspectionStandardList[].editIns[].unit")
                    .description("单位"),
                fieldWithPath(
                    "data.advices[].inspectionStandards.inspectionStandardList[].editIns[].status")
                    .description("状态 NORMAL、HIGH、LOW"),
                fieldWithPath(
                    "data.advices[].inspectionStandards.inspectionStandardList[].editIns[].unifiedReference")
                    .description("参考值"),
                fieldWithPath(
                    "data.advices[].inspectionStandards.inspectionStandardList[].editIns[].customizedReferenceMax")
                    .description("修改后最大值"),
                fieldWithPath(
                    "data.advices[].inspectionStandards.inspectionStandardList[].editIns[].spliceSymbol")
                    .description("分隔符"),
                fieldWithPath(
                    "data.advices[].inspectionStandards.inspectionStandardList[].editIns[].action")
                    .description("操作动作 EDIT、NONE"),
                fieldWithPath(
                    "data.advices[].inspectionStandards.inspectionStandardList[].editIns[].originCustomizedReferenceMax")
                    .description("原始最大值"),
                fieldWithPath(
                    "data.advices[].inspectionStandards.inspectionStandardList[].editIns[].originCustomizedReferenceMin")
                    .description("原始最小值"),
                fieldWithPath(
                    "data.advices[].inspectionStandards.inspectionStandardList[].editIns[].item")
                    .description("缩写"),
                fieldWithPath("data.total").description("总数")
            )
        ))
        .andDo(print())
        .andReturn();
  }

  @Test
  public void testUpdateDoctorAdvice() throws Exception {
    contextMock.withUid(111L);
    UpdateDoctorAdviceClientRequest request = new UpdateDoctorAdviceClientRequest();
    request.setAdvice("x");
    request.setStatus(1);
    request.setCategory(4);
    request.setPatientId("54O2j8");
    request.setDoctorId(1);
    request.setReason("reason");
    request.setQuestion("question");
    MedicineAdjustmentClient adjustment = MedicineAdjustmentClient.builder().name("name")
        .id("m5Rq38")
        .markId("1")
        .status(MedicineAdjustmentStatus.DELETE)
        .unit("mg").plan(Collections
            .singletonList(
                MedicationPlan.builder().originDosage("2").dosage("1").id("1")
                    .status(MedicationPlan.Status.DELETE)
                    .originTakeTime(Collections.singletonList(2))
                    .takeTime(Collections.singletonList(1)).build())).build();

    MedicineAdjustment medicineAdjustment = MedicineAdjustment.builder().role(UserRole.DOCTOR)
        .note("note").medicineAdjustment(Collections.singletonList(adjustment)).build();
    MedicineAdjustmentsClient supplement = MedicineAdjustmentsClient.builder()
        .medicineSupplement(Collections.singletonList(medicineAdjustment)).build();

    InspectionStandardData inspectionStandardData = InspectionStandardData.builder()
        .action(Status.EDIT).customizedReferenceMax(200.00).customizedReferenceMin(20.2).item("mg")
        .measuredAt(1506963661000L).name("血压").originCustomizedReferenceMax(300.1)
        .originCustomizedReferenceMin(33.3).spliceSymbol("/").status(InspectionApiStatus.NORMAL)
        .unit("uv").unifiedReference("test").value("123").build();

    InspectionStandard inspectionStandard = InspectionStandard.builder()
        .userRole(UserRole.ASSISTANT).advice("note")
        .editIns(List.of(inspectionStandardData, inspectionStandardData)).build();
    InspectionStandards inspectionStandards = InspectionStandards.builder()
        .inspectionStandardList(List.of(inspectionStandard)).build();

    request.setInspectionStandards(inspectionStandards);
    request.setMedicineSupplement(supplement);

    given(notifyService.pushAdviceNotify(any(), any())).willReturn(Response.instanceSuccess());
    PatientDetailApiResponse response = new PatientDetailApiResponse();
    response.setAssistantId("O8K3y8");
    response.setDoctorId("O8K3y8");
    given(userService.getPatientById(anyLong())).willReturn(response);
    given(dpcService.updateDoctorAdvice(anyLong(), any(), anyLong(), anyLong()))
        .willReturn(Response.instanceSuccess());
    mockMvc.perform(patch(prefixWithContext("/advice/{id}"), "O8K3y8")
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
        .content(mapper.writeValueAsBytes(request))
        .contextPath(ConfigConsts.API_CONTEXT))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("update_doctor_advice",
            pathParameters(
                parameterWithName("id").description("医嘱 id")
            ),
            requestFields(
                fieldWithPath("patientId").description("患者 id"),
                fieldWithPath("doctorId").ignored(),
                fieldWithPath("advice").description("医嘱"),
                fieldWithPath("status").description("状态"),
                fieldWithPath("reason").description("调药原因"),
                fieldWithPath("question").description("医嘱问题"),
                fieldWithPath("category")
                    .description("医嘱类型:0/普通医嘱，1/调药医嘱，2/复查，3/副作用调药，4/禁忌症调药，5/生活达标调药，6/达标建议值调整"),
                fieldWithPath("medicineSupplement").description("调药补遗（可选参数仅在 category 为调药时有效）")
                    .optional(),
                fieldWithPath("medicineSupplement.medicineSupplement[]").description("调药单"),
                fieldWithPath("medicineSupplement.medicineSupplement[].role")
                    .description("调药角色 DOCTOR/大医生 ASSISTANT/小医生"),
                fieldWithPath("medicineSupplement.medicineSupplement[].note").description("调药备注"),
                fieldWithPath("medicineSupplement.medicineSupplement[].medicineAdjustment[].id")
                    .description("药品 id"),
                fieldWithPath("medicineSupplement.medicineSupplement[].medicineAdjustment[].name")
                    .description("药名"),
                fieldWithPath("medicineSupplement.medicineSupplement[].medicineAdjustment[].markId")
                    .description("编号"),
                fieldWithPath("medicineSupplement.medicineSupplement[].medicineAdjustment[].unit")
                    .description("单位"),
                fieldWithPath("medicineSupplement.medicineSupplement[].medicineAdjustment[].status")
                    .description("状态: NORMAL, ADD, DELETE"),
                fieldWithPath("medicineSupplement.medicineSupplement[].medicineAdjustment[].plan")
                    .description("用药计划"),
                fieldWithPath(
                    "medicineSupplement.medicineSupplement[].medicineAdjustment[].plan[].id")
                    .description("编号"),
                fieldWithPath(
                    "medicineSupplement.medicineSupplement[].medicineAdjustment[].plan[].originDosage")
                    .description("原剂量（修改时用）"),
                fieldWithPath(
                    "medicineSupplement.medicineSupplement[].medicineAdjustment[].plan[].dosage")
                    .description("剂量"),
                fieldWithPath(
                    "medicineSupplement.medicineSupplement[].medicineAdjustment[].plan[].status")
                    .description("状态: ADD, EDIT, DELETE, NONE"),
                fieldWithPath(
                    "medicineSupplement.medicineSupplement[].medicineAdjustment[].plan[].originTakeTime")
                    .description("原用药时间（修改时用）"),
                fieldWithPath(
                    "medicineSupplement.medicineSupplement[].medicineAdjustment[].plan[].takeTime")
                    .description("用药时间"),
                fieldWithPath(
                    "inspectionStandards").description("达标值diff"),
                fieldWithPath(
                    "inspectionStandards.inspectionStandardList[].advice")
                    .description("注释"),
                fieldWithPath(
                    "inspectionStandards.inspectionStandardList[].userRole")
                    .description("操作角色"),
                fieldWithPath(
                    "inspectionStandards.inspectionStandardList[].editIns[].name")
                    .description("达标名"),
                fieldWithPath(
                    "inspectionStandards.inspectionStandardList[].editIns[].measuredAt")
                    .description("测量时间"),
                fieldWithPath(
                    "inspectionStandards.inspectionStandardList[].editIns[].value")
                    .description("修改值"),
                fieldWithPath(
                    "inspectionStandards.inspectionStandardList[].editIns[].unit")
                    .description("单位"),
                fieldWithPath(
                    "inspectionStandards.inspectionStandardList[].editIns[].status")
                    .description("状态 NORMAL、HIGH、LOW"),
                fieldWithPath(
                    "inspectionStandards.inspectionStandardList[].editIns[].unifiedReference")
                    .description("参考值"),
                fieldWithPath(
                    "inspectionStandards.inspectionStandardList[].editIns[].customizedReferenceMax")
                    .description("修改后最大值"),
                fieldWithPath(
                    "inspectionStandards.inspectionStandardList[].editIns[].spliceSymbol")
                    .description("分隔符"),
                fieldWithPath(
                    "inspectionStandards.inspectionStandardList[].editIns[].action")
                    .description("操作动作 EDIT、NONE"),
                fieldWithPath(
                    "inspectionStandards.inspectionStandardList[].editIns[].originCustomizedReferenceMax")
                    .description("原始最大值"),
                fieldWithPath(
                    "inspectionStandards.inspectionStandardList[].editIns[].originCustomizedReferenceMin")
                    .description("原始最小值"),
                fieldWithPath(
                    "inspectionStandards.inspectionStandardList[].editIns[].item")
                    .description("缩写")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("")
            )));
  }

  @Test
  public void testGetPatientByConsultationDoctor() throws Exception {
    ConsultationInfo info = new ConsultationInfo();
    info.setId("54OzK8");
    info.setDoctorId("54OzK8");
    info.setConsultationDoctorId("54OzK8");
    info.setConsultationDoctorName("张三");
    info.setQuestion("他有病");
    info.setAnswer("没救了");
    info.setUpdatedAt(123123412341l);
    info.setLevel(ConsultationLevel.URGENT);
    info.setStatus(ConsultationStatus.NEW);
    ConsultationListResponse response = new ConsultationListResponse();
    response.setConsultationInfos(List.of(info));
    response.setTotal(1111111);
    contextMock.withUid(10037l);
    given(dpcService.getConsultation(anyLong(), anyLong(), anyBoolean(), anyInt(), anyInt()))
        .willReturn(response);
    doNothing().when(userService).resetPatientNotification(anyLong(), any(StaffRole.class));
    this.mockMvc
        .perform(get("/consultation")
            .param("patientId", "54OzK8")
            .param("isConsultationDoctor", "true")
            .param("pageAt", "1")
            .param("pageSize", "10")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(print())
        .andDo(document("get_consultations",
            requestParameters(
                parameterWithName("patientId").description("患者id"),
                parameterWithName("isConsultationDoctor").description("会诊医生（true）,患者本来的医生（false）"),
                parameterWithName("pageAt").description("页码"),
                parameterWithName("pageSize").description("分页条数")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data.consultationInfos[].id").description("会诊详情id"),
                fieldWithPath("data.consultationInfos[].patientId").description("患者id"),
                fieldWithPath("data.consultationInfos[].doctorId").description("医生id"),
                fieldWithPath("data.consultationInfos[].consultationDoctorId")
                    .description("会诊医生id"),
                fieldWithPath("data.consultationInfos[].consultationDoctorName")
                    .description("会诊医生名称"),
                fieldWithPath("data.consultationInfos[].question").description("会诊问题"),
                fieldWithPath("data.consultationInfos[].answer").description("会诊回答"),
                fieldWithPath("data.consultationInfos[].status")
                    .description("会诊状态（NEW 新建 ，RESPONSE 已回答）"),
                fieldWithPath("data.consultationInfos[].level")
                    .description("会诊问题基本（ORDINARY 普通 ，COMMONLY 一般，URGENT 紧急）"),
                fieldWithPath("data.consultationInfos[].updatedAt").description("修改时间"),
                fieldWithPath("data.total").description("总数")

            )));
  }

  @Test
  public void testendConsultation() throws Exception {

    AddConsultationApiRequest request = new AddConsultationApiRequest();
    request.setPatientId("5Z7g7r");
    request.setDoctorId("8KKVP8");
    request.setConsultationDoctorId("8KKVP8");
    request.setConsultationDoctorName("医生");
    request.setQuestion("他有病");
    request.setLevel(ConsultationLevel.COMMONLY);

    doNothing().when(dpcService).sendConsultation(any(AddConsultationApiRequest.class));
    this.contextMock.withUid(10086l);

    this.mockMvc
        .perform(post(prefixWithContext("/consultation"))
            .contextPath(ConfigConsts.API_CONTEXT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("add_consultation",
            requestFields(
                fieldWithPath("patientId").description("患者id"),
                fieldWithPath("doctorId").description(
                    "医生id"),
                fieldWithPath("consultationDoctorId").description("会诊医生id"),
                fieldWithPath("consultationDoctorName").description("会诊医生姓名"),
                fieldWithPath("question").description("会诊问题"),
                fieldWithPath("level")
                    .description("诊问题疾病: - ORDINARY: 普通问题，COMMONLY: 一般问题，URGENT: 紧急问题")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("")
            )
        ))
        .andDo(print())
        .andReturn();
  }

  @Test
  public void testUpdateConsultation() throws Exception {

    UpdateConsultationApiRequest request = new UpdateConsultationApiRequest();
    request.setId("57M0yl");
    request.setPatientId("5Z7g7r");
    request.setAnswer("没救了");

    doNothing().when(dpcService).updateConsultation(any(UpdateConsultationApiRequest.class));
    this.contextMock.withUid(10082L);

    this.mockMvc
        .perform(patch(prefixWithContext("/consultation"))
            .contextPath(ConfigConsts.API_CONTEXT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("update_consultation",
            requestFields(
                fieldWithPath("id").description("会诊信息id"),
                fieldWithPath("patientId").description("患者id"),
                fieldWithPath("answer").description("会诊回答")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("")
            )))
        .andDo(print())
        .andReturn();
  }


  @Test
  public void testGetOperatorNotificationList() throws Exception {

    OperatorNotification notification = new OperatorNotification();
    notification.setId("Xr2XM8");
    notification.setUpdatedAt(123123412341L);
    notification.setReadAt(123123412341L);
    notification.setCategory(OperatorNotificationCategory.INVITE_FROM_ORGANIZATION);
    notification.setStatus(OperatorNotificationStatus.ACCEPT);
    notification.setContent("你好你很帅");

    contextMock.withUid(10037L);

    OperatorNotificationResponse response = new OperatorNotificationResponse(List.of(notification),
        1, true);
    given(dpcService.getOperatorNotificationList(anyLong(), anyInt(), anyInt()))
        .willReturn(response);
    this.mockMvc
        .perform(get("/notification/operator")
            .param("pageAt", "0")
            .param("pageSize", "15")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(print())
        .andDo(document("get_operator_notification",
            requestParameters(
                parameterWithName("pageAt").description("页码:默认0"),
                parameterWithName("pageSize").description("分页条数:默认15")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data.notifications[].id").description("提醒id"),
                fieldWithPath("data.notifications[].content").description("提醒内容"),
                fieldWithPath("data.notifications[].status")
                    .description("消息状态:WAITING／未处理,ACCEPT/接受,REFUSED/拒绝,PROCESSED/已处理,INVALID/已失效"),
                fieldWithPath("data.notifications[].category").description(
                    "消息类型：INVITE_FROM_ORGANIZATION／机构邀请，NOTIFICATION_OF_NEW_BIND_PATIENT／新绑定患者提醒，REMOVE_STAFF_FROM_ORGANIZATION／被机构移除提醒"),
                fieldWithPath("data.notifications[].updatedAt").description("更新时间"),
                fieldWithPath("data.notifications[].readAt").description("读取时间"),
                fieldWithPath("data.total").description("总数"),
                fieldWithPath("data.hasUnRead").description("是否含有未读消息")
            )));
  }

  @Test
  public void testUpdateOperatorNotification() throws Exception {

    UpdateOperatorNotificationRequest request = new UpdateOperatorNotificationRequest();
    request.setCategory(OperatorNotificationCategory.INVITE_FROM_ORGANIZATION);
    request.setStatus(OperatorNotificationStatus.ACCEPT);
    request.setId("57M0yl");

    doNothing().when(dpcService)
        .updateOperatorNotification(any(UpdateNotificationStatusRequest.class));
    this.contextMock.withUid(10082L);

    this.mockMvc
        .perform(patch(prefixWithContext("/notification/operator"))
            .contextPath(ConfigConsts.API_CONTEXT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("update_operator_notification",
            requestFields(
                fieldWithPath("id").description("提醒消息id"),
                fieldWithPath("category").description(
                    "消息类型：INVITE_FROM_ORGANIZATION／机构邀请"),
                fieldWithPath("status").description("消息状态:ACCEPT/接受,REFUSED/拒绝")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("")
            )))
        .andDo(print())
        .andReturn();
  }


  @Test
  public void testUpdateOperatorNotificationReadAt() throws Exception {

    doNothing().when(dpcService).updateOperatorNotificationReadAt(anyLong());
    this.contextMock.withUid(10082L);

    this.mockMvc
        .perform(patch(prefixWithContext("/notification/operator/read"))
            .contextPath(ConfigConsts.API_CONTEXT)
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("update_operator_notification_read_time",
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("")
            )))
        .andDo(print())
        .andReturn();
  }

}
