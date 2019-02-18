package cn.xinzhili.api.doctor.controller;


import static cn.xinzhili.api.doctor.config.ConfigConsts.prefixWithContext;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cn.xinzhili.api.doctor.bean.PatientApiInfo;
import cn.xinzhili.api.doctor.bean.PatientApiProgress;
import cn.xinzhili.api.doctor.bean.UnreadApiStatus;
import cn.xinzhili.api.doctor.bean.UserGender;
import cn.xinzhili.api.doctor.bean.UserRole;
import cn.xinzhili.api.doctor.bean.UserStatus;
import cn.xinzhili.api.doctor.bean.request.AddPatientApiRequest;
import cn.xinzhili.api.doctor.bean.request.DelAssistantPatientRelationRequest;
import cn.xinzhili.api.doctor.bean.request.UpdatePatientApiRequest;
import cn.xinzhili.api.doctor.bean.request.UpdatePatientBindingRequest;
import cn.xinzhili.api.doctor.bean.request.UpdatePatientRemarkApiRequest;
import cn.xinzhili.api.doctor.bean.response.PatientDetailApiResponse;
import cn.xinzhili.api.doctor.bean.response.PatientListApiResponse;
import cn.xinzhili.api.doctor.common.ContextMock;
import cn.xinzhili.api.doctor.config.ConfigConsts;
import cn.xinzhili.api.doctor.service.UserService;
import cn.xinzhili.user.api.AdviceLevel;
import cn.xinzhili.user.api.ConsultationLevel;
import cn.xinzhili.user.api.MedicationStatus;
import cn.xinzhili.user.api.MetricsStatus;
import cn.xinzhili.user.api.PatientMarriage;
import cn.xinzhili.user.api.PatientProgress;
import cn.xinzhili.user.api.RiskFactor;
import cn.xinzhili.user.api.ServiceLevel;
import cn.xinzhili.user.api.StaffRole;
import cn.xinzhili.user.api.request.AddPatientRequest;
import cn.xinzhili.user.api.request.BatchUpdatePatientRelationRequest;
import cn.xinzhili.user.api.request.DelAssistantPatientRelationApiRequest;
import cn.xinzhili.user.api.request.GetPatientByCriteriaRequest;
import cn.xinzhili.user.api.request.UpdatePatientAndRelationRequest;
import cn.xinzhili.user.api.response.PatientVisualInfoResponse;
import cn.xinzhili.user.api.response.PatientVisualInfoResponse.AgeNode;
import cn.xinzhili.user.api.response.PatientVisualInfoResponse.DiseaseNode;
import cn.xinzhili.user.api.response.PatientVisualInfoResponse.HeadNode;
import cn.xinzhili.user.api.response.PatientVisualInfoResponse.StatusNode;
import cn.xinzhili.xutils.core.util.HashUtils;
import cn.xinzhili.xutils.spring.interceptor.ExceptionResolver;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.codehaus.jackson.map.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Date: 16/02/2017 Time: 1:18 PM
 *
 * @author Loki
 */
@RunWith(MockitoJUnitRunner.class)
public class PatientControllerTest {

  @Rule
  public JUnitRestDocumentation restDocumentation
      = new JUnitRestDocumentation("build/generated-snippets");

  private MockMvc mockMvc;

  @InjectMocks
  private PatientController patientController;

  @InjectMocks
  private ExceptionResolver exceptionResolver;

  @Mock
  private UserService userService;

  private PatientApiInfo patientApiInfo;

  private ObjectMapper mapper;

  private Long mockUserId = 66666L;

  private ContextMock contextMock = new ContextMock().withUid(mockUserId);

  @Before
  public void setUp() {

    mapper = new ObjectMapper();

    this.mockMvc = MockMvcBuilders
        .standaloneSetup(this.patientController)
        .setControllerAdvice(exceptionResolver)
        .setCustomArgumentResolvers(
            new AuthenticationPrincipalArgumentResolver(),
            contextMock.getMockUidResolver())
        .apply(MockMvcRestDocumentation
            .documentationConfiguration(restDocumentation)
            .uris()
            .withHost(ConfigConsts.API_HOST)
            .withPort(ConfigConsts.API_PORT)
        )
        .build();

    patientApiInfo = new PatientApiInfo();
    patientApiInfo.setId("5xzAWl");
    patientApiInfo.setTel("13900000000");
    patientApiInfo.setAvatar("https://s3.cn-north-1.amazonaws.com.cn/patient-avatars-dev/" +
        "f4d5a666-7a65-40d0-8de2-3f853b7e8881");
    patientApiInfo.setName("张三");
    patientApiInfo.setAge(40);
    patientApiInfo.setSex(UserGender.FEMALE);
    patientApiInfo.setDoctorName("李时珍");
    patientApiInfo.setAssistantName("张助理");
    patientApiInfo.setOperatorName("瓜运营");
    patientApiInfo.setServiceLevel(ServiceLevel.VIP);
    patientApiInfo.setMedicationStatus(MedicationStatus.ABNORMAL);
    patientApiInfo.setMetricsStatus(MetricsStatus.ABNORMAL);
    patientApiInfo.setPendingDoctorMessage(5);
    patientApiInfo.setPendingAssistantMessage(22);
    patientApiInfo.setPendingOperatorMessage(33);
    patientApiInfo.setPendingDoctorAdviceLevel(AdviceLevel.SEVERE);
    patientApiInfo.setPendingDoctorAdviceCount(20);
    patientApiInfo.setPendingAssistantAdviceLevel(AdviceLevel.NONE);
    patientApiInfo.setPendingAssistantAdviceCount(22);
    patientApiInfo.setPendingConsultationLevel(ConsultationLevel.COMMONLY);
    patientApiInfo.setPendingDoctorConsultation(99);
    patientApiInfo.setEthnicity("汉族");
    patientApiInfo.setRemark("备注");
    patientApiInfo.setArea("北京市 北京市 朝阳区");
    patientApiInfo
        .setRiskFactor(Arrays.asList(RiskFactor.HIGH_BLOOD_LIPIDS, RiskFactor.HIGH_BLOOD_PRESSURE));
    patientApiInfo.setHasAssistant(true);
    patientApiInfo.setStatus(UserStatus.NORMAL);

    patientApiInfo.setProvince(10);
    patientApiInfo.setCity(22);
    patientApiInfo.setTown(11);
    patientApiInfo.setUnauditedImageCount(2);
    patientApiInfo.setUnreadStatus(UnreadApiStatus.BLOCK);
    patientApiInfo.setAddress("详细地址");
    patientApiInfo.setBirthday(1232433231232l);

  }

  @Test
  public void testGetPatientList() throws Exception {

    PatientListApiResponse response = new PatientListApiResponse();
    response.setTotal(14);
    List<PatientApiInfo> list = new ArrayList<>();

    int index = 0;
    while (index < 5) {
      list.add(patientApiInfo);
      index++;
    }
    response.setPatients(list);
    given(userService.getPatientList(
        any(GetPatientByCriteriaRequest.class), any(StaffRole.class),
        any(Long.class), any(Long.class), any(Integer.class), any(Integer.class), anyLong()))
        .willReturn(response);

    mockMvc.perform(
        get("/api/v0/doctor/user/patient")
            .param("organizationId", "8POLbl")
            .param("departmentId", "lQvRX8")
            .param("doctorId", "lQvRX8")
            .param("assistantId", "lQvRX8")
            .param("operatorId", "lQvRX8")
            .param("keyword", "100000")
            .param("hasAssistant", "true")
            .param("excludeOperator", "false")
            .param("excludeDoctor", "false")
            .param("excludeAssistant", "false")
            .param("province", "1")
            .param("ageMin", "30")
            .param("ageMax", "40")
            .param("risks", "HIGH_BLOOD_PRESSURE")
            .param("risks", "DIABETES")
            .param("excludedRisks", "DIABETES")
            .param("excludedRisks", "CORONARY_HEART_DISEASE")
            .param("integratedStatus", "SEVERE")
            .param("includesNullAge", "true")
            .param("serviceLevel", "VIP")
            .param("progress", "NONE")
            .param("birthday", String.valueOf(1490680044390L))
            .param("address", "gbds9901")
            .param("createdAtStart", String.valueOf(1490680044390L))
            .param("createdAtEnd", String.valueOf(1490680644391L))
            .param("pageAt", "0")
            .param("pageSize", "15")
            .contextPath(ConfigConsts.API_CONTEXT))
        .andDo(print())
        .andExpect(status().isOk())
        .andDo(document("get_patient_list",
            requestParameters(
                parameterWithName("organizationId").description("机构id"),
                parameterWithName("departmentId").description("部门id"),
                parameterWithName("doctorId").description("医生id"),
                parameterWithName("assistantId").description("医生助理id"),
                parameterWithName("operatorId").description("运营id"),
                parameterWithName("keyword").description("搜索关键字"),
                parameterWithName("hasAssistant").description("是否存在绑定医生助理：true/存在，false/不存在"),
                parameterWithName("excludeAssistant")
                    .description("默认false，展示属于当前Assistant的患者"),
                parameterWithName("excludeDoctor")
                    .description("默认false，展示属于当前doctor的患者"),
                parameterWithName("excludeOperator")
                    .description("默认false，展示属于当前Operator的患者"),
                parameterWithName("ageMin").description("年龄最小值"),
                parameterWithName("ageMax").description("年龄最大值"),
                parameterWithName("includesNullAge")
                    .description("是否包含没有年龄患者 : 默认false ，true／包含 ，false／不包含"),
                parameterWithName("province").description("地区（省）"),
                parameterWithName("risks")
                    .description(
                        "患的病：:NONE/无,HIGH_BLOOD_PRESSURE/高血压,DIABETES/糖尿病,"
                            + "HIGH_BLOOD_LIPIDS/高血脂,HIGH_URIC_ACID/高尿酸,CORONARY_HEART_DISEASE/冠心病"),
                parameterWithName("excludedRisks")
                    .description(
                        "没患的病:NONE/无,HIGH_BLOOD_PRESSURE/高血压,DIABETES/糖尿病,"
                            + "HIGH_BLOOD_LIPIDS/高血脂,HIGH_URIC_ACID/高尿酸,CORONARY_HEART_DISEASE/冠心病"),
                parameterWithName("serviceLevel").description("患者服务等级：NORMAL/普通患者,VIP/vip患者"),
                parameterWithName("progress").description("患者的建档完成度：NONE/未建档,PART/部分建档,DONE/已完成建档"),
                parameterWithName("address").description("详细地址"),
                parameterWithName("birthday").description("生日"),
                parameterWithName("integratedStatus")
                    .description("患者的综合状态：  NONE／正常, NORMAL／一般, SEVERE／严重"),

                parameterWithName("createdAtStart").description("注册起始时间"),
                parameterWithName("createdAtEnd").description("注册终止时间"),
                parameterWithName("pageAt").description("页码"),
                parameterWithName("pageSize").description("页数")),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data.patients[].id").description("患者id"),
                fieldWithPath("data.patients[].tel").description("电话号码"),
                fieldWithPath("data.patients[].name").description("姓名"),
                fieldWithPath("data.patients[].sex").description("性别"),
                fieldWithPath("data.patients[].status").description("状态:NORMAL/正常,PENDING/未激活"),
                fieldWithPath("data.patients[].age").description("生日"),
                fieldWithPath("data.patients[].avatar").description("头像"),
                fieldWithPath("data.patients[].doctorName").description("绑定医生姓名"),
                fieldWithPath("data.patients[].remark").description("运维人员给患者添加的记录"),
                fieldWithPath("data.patients[].area").description("患者地区，每一级用空格间隔"),
                fieldWithPath("data.patients[].ethnicity").description("患者的民族"),
                fieldWithPath("data.patients[].assistantName").description("绑定医生助理姓名"),
                fieldWithPath("data.patients[].operatorName").description("绑定运营姓名"),
                fieldWithPath("data.patients[].hasAssistant").description("是否绑定医生助理:true/false"),
                fieldWithPath("data.patients[].height").description("身高，单位是cm"),
                fieldWithPath("data.patients[].weight").description("体重，单位是kg"),
                fieldWithPath("data.patients[].waistline").description("腰围，单位是cm"),
                fieldWithPath("data.patients[].bmi").description("BMI指数"),
                fieldWithPath("data.patients[].address").description("详细地址"),
                fieldWithPath("data.patients[].birthday").description("生日"),
                fieldWithPath("data.patients[].progress")
                    .description("患者的建档完成度：NONE/未建档,PART/部分建档,DONE/已完成建档"),
                fieldWithPath("data.patients[].serviceLevel")
                    .description("患者vip等级: NORMAL, VIP"),
                fieldWithPath("data.patients[].medicationStatus")
                    .description("患者服药异常情况：NORMAL，或者ABNORMAL"),
                fieldWithPath("data.patients[].metricsStatus")
                    .description("患者指标异常情况：NORMAL，或者ABNORMAL"),
                fieldWithPath("data.patients[].pendingDoctorMessage")
                    .description("绑定医生未读聊天消息数量"),
                fieldWithPath("data.patients[].pendingAssistantMessage")
                    .description("绑定助理未读聊天消息数量"),
                fieldWithPath("data.patients[].pendingOperatorMessage")
                    .description("运营未读聊天消息数量"),
                fieldWithPath("data.patients[].pendingDoctorAdviceLevel")
                    .description("绑定医生未处理问题级别: NONE - 无, NORMAL - 普通, SEVERE - 严重"),
                fieldWithPath("data.patients[].pendingDoctorAdviceCount")
                    .description("绑定医生待审核问题数量"),
                fieldWithPath("data.patients[].pendingAssistantAdviceLevel")
                    .description("绑定医生助理未处理问题级别: NONE - 无, NORMAL - 普通, SEVERE - 严重"),
                fieldWithPath("data.patients[].pendingAssistantAdviceCount")
                    .description("绑定医生助理待审核问题数量"),
                fieldWithPath("data.patients[].pendingConsultationLevel")
                    .description("会诊问题疾病: - ORDINARY: 普通问题，COMMONLY: 一般问题，URGENT: 紧急问题"),
                fieldWithPath("data.patients[].pendingDoctorConsultation").description("患者待会诊问题数"),

                fieldWithPath("data.patients[].riskFactor")
                    .description(
                        "风险因素 : - NONE:无,HIGH_BLOOD_PRESSURE:高血压,DIABETES:糖尿病,HIGH_BLOOD_LIPIDS:高血脂,HIGH_URIC_ACID,:高尿酸]"),
                fieldWithPath("data.patients[].province").description("患者省信息"),
                fieldWithPath("data.patients[].city").description("患者市信息"),
                fieldWithPath("data.patients[].town").description("患者县信息"),
                fieldWithPath("data.patients[].unauditedImageCount").description("未审核图片数量"),
                fieldWithPath("data.patients[].unreadStatus")
                    .description("显示未读数为红点或真实数量 BLOCK 显示真实数量  NONE 显示红点"),
                fieldWithPath("data.total").description("数据总数")
            )));
  }

  @Test
  public void testAddPatient() throws Exception {

    AddPatientApiRequest request = new AddPatientApiRequest();

    request.setTel("15800000001");
    request.setDoctorId("8POLbl");
    request.setAssistantId("lQvRX8");
    request.setOperatorId("lQvRX8");
    request.setServiceLevel(ServiceLevel.NORMAL);
    request.setOrganizationId(HashUtils.encode(10000L));

    doNothing().when(userService).addPatient(any(AddPatientRequest.class));
    this.contextMock.withUid(mockUserId);

    this.mockMvc
        .perform(post(prefixWithContext("/user/patient"))
            .contextPath(ConfigConsts.API_CONTEXT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("add_patient",
            requestFields(
                fieldWithPath("tel").description("患者电话"),
                fieldWithPath("doctorId").description("绑定医生id"),
                fieldWithPath("assistantId").description("绑定医生助理id(选填)"),
                fieldWithPath("operatorId").description("绑定运营id"),
                fieldWithPath("serviceLevel").description("患者vip等级(NORMAL/普通患者，VIP/vip 患者)"),
                fieldWithPath("organizationId").description("机构id")),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("")
            )
        ))
        .andDo(print())
        .andReturn();
  }


  @Test
  public void testDeletePatient() throws Exception {

    List<String> ids = new ArrayList<>();
    ids.add("rqO4D8");
    ids.add("8Vbqm8");

    given(userService.deletePatient(anyListOf(Long.class))).willReturn(true);
    this.contextMock.withUid(mockUserId);

    this.mockMvc
        .perform(delete(prefixWithContext("/user/patient"))
            .contextPath(ConfigConsts.API_CONTEXT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(ids)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("delete_patients",
            requestFields(
                fieldWithPath("[]").description("待删除患者id")
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
  public void testPatientById() throws Exception {

    PatientDetailApiResponse response = new PatientDetailApiResponse();
    response.setPatientInfo(patientApiInfo);
    response.setDoctorId("olLDlK");
    response.setAssistantId("olLDlK");
    response.setOperatorId("olLDlK");

    given(userService.getPatientById(any(Long.class)))
        .willReturn(response);

    this.mockMvc.perform(
        get(("/api/v0/doctor/user/patient/{patientId}"), "rqO4D8")
            .contextPath(ConfigConsts.API_CONTEXT))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("get_patient_detail",
            pathParameters(
                parameterWithName("patientId").description("查询患者的ID")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data.patientInfo.id").description("患者id"),
                fieldWithPath("data.patientInfo.tel").description("电话号码"),
                fieldWithPath("data.patientInfo.name").description("姓名"),
                fieldWithPath("data.patientInfo.sex").description("性别"),
                fieldWithPath("data.patientInfo.status").description("状态:NORMAL/正常,PENDING/未激活"),
                fieldWithPath("data.patientInfo.age").description("生日"),
                fieldWithPath("data.patientInfo.avatar").description("头像"),
                fieldWithPath("data.patientInfo.doctorName").description("绑定医生姓名"),
                fieldWithPath("data.patientInfo.remark").description("运维人员给患者添加的记录"),
                fieldWithPath("data.patientInfo.area").description("患者地区，每一级用空格间隔"),
                fieldWithPath("data.patientInfo.ethnicity").description("患者的民族"),
                fieldWithPath("data.patientInfo.height").description("身高，单位是cm"),
                fieldWithPath("data.patientInfo.weight").description("体重，单位是kg"),
                fieldWithPath("data.patientInfo.waistline").description("腰围，单位是cm"),
                fieldWithPath("data.patientInfo.bmi").description("BMI指数"),
                fieldWithPath("data.patientInfo.address").description("详细地址"),
                fieldWithPath("data.patientInfo.birthday").description("生日"),
                fieldWithPath("data.patientInfo.assistantName").description("绑定医生助理姓名"),
                fieldWithPath("data.patientInfo.operatorName").description("绑定运营姓名"),
                fieldWithPath("data.patientInfo.hasAssistant").description("是否绑定医生助理:true/false"),
                fieldWithPath("data.patientInfo.progress")
                    .description("患者的建档完成度：NONE/未建档,PART/部分建档,DONE/已完成建档"),
                fieldWithPath("data.patientInfo.serviceLevel")
                    .description("患者vip等级: NORMAL, VIP"),
                fieldWithPath("data.patientInfo.medicationStatus")
                    .description("患者服药异常情况：NORMAL，或者ABNORMAL"),
                fieldWithPath("data.patientInfo.metricsStatus")
                    .description("患者指标异常情况：NORMAL，或者ABNORMAL"),
                fieldWithPath("data.patientInfo.pendingDoctorMessage")
                    .description("绑定医生未读聊天消息数量"),
                fieldWithPath("data.patientInfo.pendingAssistantMessage")
                    .description("绑定助理未读聊天消息数量"),
                fieldWithPath("data.patientInfo.pendingOperatorMessage")
                    .description("运营未读聊天消息数量"),
                fieldWithPath("data.patientInfo.pendingDoctorAdviceLevel")
                    .description("绑定医生未处理问题级别: NONE - 无, NORMAL - 普通, SEVERE - 严重"),
                fieldWithPath("data.patientInfo.pendingDoctorAdviceCount")
                    .description("绑定医生待审核问题数量"),
                fieldWithPath("data.patientInfo.pendingAssistantAdviceLevel")
                    .description("绑定医生助理未处理问题级别: NONE - 无, NORMAL - 普通, SEVERE - 严重"),
                fieldWithPath("data.patientInfo.pendingAssistantAdviceCount")
                    .description("绑定医生助理未处理问题数量"),
                fieldWithPath("data.patientInfo.pendingConsultationLevel")
                    .description("会诊问题疾病: - ORDINARY: 普通问题，COMMONLY: 一般问题，URGENT: 紧急问题"),
                fieldWithPath("data.patientInfo.pendingDoctorConsultation").description("患者待会诊问题数"),

                fieldWithPath("data.patientInfo.province").description("患者省信息"),
                fieldWithPath("data.patientInfo.city").description("患者市信息"),
                fieldWithPath("data.patientInfo.town").description("患者县信息"),
                fieldWithPath("data.patientInfo.unauditedImageCount").description("未审核图片数量"),
                fieldWithPath("data.patientInfo.riskFactor")
                    .description(
                        "风险因素 : - NONE:无,HIGH_BLOOD_PRESSURE:高血压,DIABETES:糖尿病,HIGH_BLOOD_LIPIDS:高血脂,HIGH_URIC_ACID,:高尿酸]"),
                fieldWithPath("data.doctorId").description("绑定医生id"),
                fieldWithPath("data.assistantId").description("绑定医生助理id"),
                fieldWithPath("data.patientInfo.unreadStatus")
                    .description("显示未读数为红点或真实数量 BLOCK 显示真实数量  NONE 显示红点"),
                fieldWithPath("data.operatorId").description("绑定运营id")
            )))
        .andDo(print())
        .andReturn();
  }

  @Test
  public void testUpdatePatient() throws Exception {

    UpdatePatientApiRequest request = new UpdatePatientApiRequest();
    request.setId("54OzK8");
    request.setName("发财哥");
    request.setSex(UserGender.FEMALE);
    request.setHeight(175);
    request.setWaistline(250);
    request.setWeight(200);
    request.setEthnicity("回族");
    request.setTown(100);
    request.setCity(50);
    request.setProvince(1);
    request.setMarriage(PatientMarriage.MARRIED);
    request.setAddress("朝阳区1号大院");
    request.setDoctorId("olLDlK");
    request.setAssistantId("lQvRX8");
    request.setOperatorId("lQvRX8");
    request.setBirthday(123333424344l);
    request.setProgress(PatientApiProgress.DONE);
    request.setOrganizationId(HashUtils.encode(1000L));

    given(userService.updatePatientRelatedInfo(
        any(UpdatePatientAndRelationRequest.class))).willReturn(true);
    this.contextMock.withUid(mockUserId);

    this.mockMvc
        .perform(patch(prefixWithContext("/user/patient"))
            .contextPath(ConfigConsts.API_CONTEXT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("update_patient",
            requestFields(
                fieldWithPath("id").description("患者id(必填)"),
                fieldWithPath("name").description("姓名"),
                fieldWithPath("sex").description("性别"),
                fieldWithPath("height").description("身高"),
                fieldWithPath("waistline").description("腰围"),
                fieldWithPath("weight").description("体重"),
                fieldWithPath("marriage").description("婚姻状况:1/已婚,0/未婚"),
                fieldWithPath("address").description("详细住址"),
                fieldWithPath("birthday").description("生日"),
                fieldWithPath("doctorId").description("绑定医生id"),
                fieldWithPath("assistantId").description("绑定医生助理id"),
                fieldWithPath("operatorId").description("绑定运营id"),
                fieldWithPath("province").description("省的id"),
                fieldWithPath("city").description("市的id"),
                fieldWithPath("town").description("县的id"),
                fieldWithPath("remark").description("运维人员给患者添加的记录"),
                fieldWithPath("progress").description("患者的建档完成度：NONE/未建档,PART/部分建档,DONE/已完成建档"),
                fieldWithPath("ethnicity").description("患者的民族"),
                fieldWithPath("serviceLevel").description("患者的会员等级"),
                fieldWithPath("organizationId").description("机构id")
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
  public void testUpdatePatientRemark() throws Exception {

    given(userService
        .updatePatientRemark(anyLong(), anyString(), any(PatientProgress.class)))
        .willReturn(patientApiInfo);
    this.contextMock.withUid(mockUserId);

    UpdatePatientRemarkApiRequest request = new UpdatePatientRemarkApiRequest();
    request.setProgress(PatientApiProgress.DONE);
    request.setRemark("XX");

    this.mockMvc
        .perform(patch(prefixWithContext("/user/patient/remark/{patientId}"), "rqO4D8")
            .contextPath(ConfigConsts.API_CONTEXT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("update_patient_remark",
            pathParameters(
                parameterWithName("patientId").description("患者的ID")
            ),
            requestFields(
                fieldWithPath("remark").description("运维人员给患者添加的记录"),
                fieldWithPath("progress").description("患者的建档完成度：NONE/未建档,PART/部分建档,DONE/已完成建档")
            ),
            responseFields(

                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data.id").description("患者id"),
                fieldWithPath("data.tel").description("电话号码"),
                fieldWithPath("data.name").description("姓名"),
                fieldWithPath("data.sex").description("性别"),
                fieldWithPath("data.status").description("状态:NORMAL/正常,PENDING/未激活"),
                fieldWithPath("data.age").description("生日"),
                fieldWithPath("data.avatar").description("头像"),
                fieldWithPath("data.doctorName").description("绑定医生姓名"),
                fieldWithPath("data.remark").description("运维人员给患者添加的记录"),
                fieldWithPath("data.area").description("患者地区，每一级用空格间隔"),
                fieldWithPath("data.ethnicity").description("患者的民族"),
                fieldWithPath("data.height").description("身高，单位是cm"),
                fieldWithPath("data.weight").description("体重，单位是kg"),
                fieldWithPath("data.waistline").description("腰围，单位是cm"),
                fieldWithPath("data.bmi").description("BMI指数"),
                fieldWithPath("data.address").description("详细地址"),
                fieldWithPath("data.birthday").description("生日"),
                fieldWithPath("data.assistantName").description("绑定医生助理姓名"),
                fieldWithPath("data.operatorName").description("绑定运营姓名"),
                fieldWithPath("data.hasAssistant").description("是否绑定医生助理:true/false"),
                fieldWithPath("data.progress")
                    .description("患者的建档完成度：NONE/未建档,PART/部分建档,DONE/已完成建档"),
                fieldWithPath("data.serviceLevel")
                    .description("患者vip等级: NORMAL, VIP"),
                fieldWithPath("data.medicationStatus")
                    .description("患者服药异常情况：NORMAL，或者ABNORMAL"),
                fieldWithPath("data.metricsStatus")
                    .description("患者指标异常情况：NORMAL，或者ABNORMAL"),
                fieldWithPath("data.pendingDoctorMessage")
                    .description("绑定医生未读聊天消息数量"),
                fieldWithPath("data.pendingAssistantMessage")
                    .description("绑定助理未读聊天消息数量"),
                fieldWithPath("data.pendingOperatorMessage")
                    .description("运营未读聊天消息数量"),
                fieldWithPath("data.pendingDoctorAdviceLevel")
                    .description("绑定医生未处理问题级别: NONE - 无, NORMAL - 普通, SEVERE - 严重"),
                fieldWithPath("data.pendingDoctorAdviceCount")
                    .description("绑定医生待审核问题数量"),
                fieldWithPath("data.pendingAssistantAdviceLevel")
                    .description("绑定医生助理未处理问题级别: NONE - 无, NORMAL - 普通, SEVERE - 严重"),
                fieldWithPath("data.pendingAssistantAdviceCount")
                    .description("绑定医生助理未处理问题数量"),
                fieldWithPath("data.pendingConsultationLevel")
                    .description("会诊问题疾病: - ORDINARY: 普通问题，COMMONLY: 一般问题，URGENT: 紧急问题"),
                fieldWithPath("data.pendingDoctorConsultation").description("患者待会诊问题数"),

                fieldWithPath("data.province").description("患者省信息"),
                fieldWithPath("data.city").description("患者市信息"),
                fieldWithPath("data.town").description("患者县信息"),
                fieldWithPath("data.unauditedImageCount").description("未审核图片数量"),
                fieldWithPath("data.unreadStatus")
                    .description("显示未读数为红点或真实数量 BLOCK 显示真实数量  NONE 显示红点"),
                fieldWithPath("data.riskFactor")
                    .description(
                        "风险因素 : - NONE:无,HIGH_BLOOD_PRESSURE:高血压,DIABETES:糖尿病,HIGH_BLOOD_LIPIDS:高血脂,HIGH_URIC_ACID,:高尿酸]")
            )
        ))
        .andDo(print())
        .andReturn();
  }


  @Test
  public void testUpdatePatientsBindingRelation() throws Exception {

    UpdatePatientBindingRequest request =
        new UpdatePatientBindingRequest();
    request.setAssistantId("lQvRX8");
    request.setDoctorId("olLDlK");
    request.setOperatorId("lQvRX8");
    request.setOrganizationId(HashUtils.encode(1000L));

    List<String> patientIds = new ArrayList<String>() {{
      add("54OzK8");
      add("rqO4D8");
      add("8Vbqm8");
    }};
    request.setPatientIds(patientIds);

    doNothing().when(userService)
        .updatePatientsBinding(any(BatchUpdatePatientRelationRequest.class));
    this.mockMvc
        .perform(patch(prefixWithContext("/user/patient/binding"))
            .contextPath(ConfigConsts.API_CONTEXT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("update_patient_binding",
            requestFields(
                fieldWithPath("patientIds").description("待绑定患者id"),
                fieldWithPath("doctorId").description("绑定医生id"),
                fieldWithPath("assistantId").description("绑定医生助理id"),
                fieldWithPath("operatorId").description("绑定运营id"),
                fieldWithPath("organizationId").description("机构id")
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
  public void testDeleteAssistantBindingRelation() throws Exception {

    DelAssistantPatientRelationRequest request =
        new DelAssistantPatientRelationRequest();
    List<String> assistantIds = new ArrayList<String>() {{
      add("54OzK8");
      add("rqO4D8");
      add("8Vbqm8");
    }};
    request.setAssistantIds(assistantIds);

    given(userService.deleteAssistantRelation(
        any(DelAssistantPatientRelationApiRequest.class))).willReturn(true);

    this.mockMvc
        .perform(delete(prefixWithContext("/user/patient/binding/assistant"))
            .contextPath(ConfigConsts.API_CONTEXT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("delete_patient_binding_assistant",
            requestFields(
                fieldWithPath("assistantIds[]").description("绑定医生助理id列表")
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
  public void testPatientVisualCount() throws Exception {

    PatientVisualInfoResponse response = new PatientVisualInfoResponse();
    response.setTotal(3);

    StatusNode statusNode = new StatusNode();
    statusNode.setParam(
        "province=PROVINCE&excludedRisks=CORONARY_HEART_DISEASE&ageMax=200"
            + "&ageMin=65&includesNullAge=false&integratedStatus=SEVERE");
    statusNode.setCount(1);
    AgeNode ageNode = new AgeNode();
    ageNode.setName(">=65");
    ageNode.setParam(
        "province=PROVINCE&excludedRisks=CORONARY_HEART_DISEASE"
            + "&ageMax=200&ageMin=65&includesNullAge=false");
    ageNode.setCount(1);
    ageNode.setChild(Collections.singletonList(statusNode));

    DiseaseNode diseaseNode = new DiseaseNode();
    diseaseNode.setName("冠心病");
    diseaseNode.setParam("province=1&risks=CORONARY_HEART_DISEASE");
    diseaseNode.setCount(1);
    diseaseNode.setChild(Collections.singletonList(ageNode));
    HeadNode headNode = new HeadNode();
    headNode.setName("北京");
    headNode.setParam("province=1");
    headNode.setCount(1);
    headNode.setChild(Collections.singletonList(diseaseNode));
    response.setHeads(Collections.singletonList(headNode));

    given(userService.getIndexVisualInfo(any(UserRole.DOCTOR.getClass()), any(Long.class)))
        .willReturn(response);
    this.contextMock.withUid(10039);
    ;

    this.mockMvc.perform(
        get(("/api/v0/doctor/user/patient/visual/count"))
            .contextPath(ConfigConsts.API_CONTEXT))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("patient_visual_count",
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data.total").description("患者id"),
                fieldWithPath("data.heads[].name").description("头节点名"),
                fieldWithPath("data.heads[].count").description("头节点统计数"),
                fieldWithPath("data.heads[].param").description("头节点跳转url参数"),
                fieldWithPath("data.heads[].child[]").description("疾病节点"),

                fieldWithPath("data.heads[].child[].name").description("疾病节点名"),
                fieldWithPath("data.heads[].child[].count").description("疾病节点统计数"),
                fieldWithPath("data.heads[].child[].param").description("疾病节点跳转url参数"),
                fieldWithPath("data.heads[].child[].child[]").description("年龄节点"),

                fieldWithPath("data.heads[].child[].child[].name").description("年龄节点名"),
                fieldWithPath("data.heads[].child[].child[].count").description("年龄节点统计数"),
                fieldWithPath("data.heads[].child[].child[].param").description("年龄节点跳抓url参数"),
                fieldWithPath("data.heads[].child[].child[].child[]").description("状态节点"),

                fieldWithPath("data.heads[].child[].child[].child[].count").description("状态节点统计数"),
                fieldWithPath("data.heads[].child[].child[].child[].param")
                    .description("状态节点跳转url参数")
            )))
        .andDo(print())
        .andReturn();
  }

}
