package cn.xinzhili.api.doctor.controller;

import static cn.xinzhili.api.doctor.config.ConfigConsts.API_CONTEXT;
import static cn.xinzhili.api.doctor.config.ConfigConsts.prefixWithContext;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cn.xinzhili.api.doctor.UserFutureDoctorType;
import cn.xinzhili.api.doctor.bean.Assistant;
import cn.xinzhili.api.doctor.bean.ConsultationPatientApiInfo;
import cn.xinzhili.api.doctor.bean.DepartmentDetail;
import cn.xinzhili.api.doctor.bean.Doctor;
import cn.xinzhili.api.doctor.bean.DoctorTitle;
import cn.xinzhili.api.doctor.bean.FutureDoctorApiInfo;
import cn.xinzhili.api.doctor.bean.ImageUploadInfo;
import cn.xinzhili.api.doctor.bean.Institution;
import cn.xinzhili.api.doctor.bean.Operator;
import cn.xinzhili.api.doctor.bean.User;
import cn.xinzhili.api.doctor.bean.UserChatStatus;
import cn.xinzhili.api.doctor.bean.UserGender;
import cn.xinzhili.api.doctor.bean.UserInviteStatus;
import cn.xinzhili.api.doctor.bean.UserReview;
import cn.xinzhili.api.doctor.bean.UserRole;
import cn.xinzhili.api.doctor.bean.UserStatus;
import cn.xinzhili.api.doctor.bean.request.AddAssistantApiRequest;
import cn.xinzhili.api.doctor.bean.request.AddDoctorApiRequest;
import cn.xinzhili.api.doctor.bean.request.AddFutureDoctorApiRequest;
import cn.xinzhili.api.doctor.bean.request.AddOperatorApiRequest;
import cn.xinzhili.api.doctor.bean.request.DeleteOperatorApiRequest;
import cn.xinzhili.api.doctor.bean.request.GetStaffListRequest;
import cn.xinzhili.api.doctor.bean.request.InitUserRequest;
import cn.xinzhili.api.doctor.bean.request.PasswordForgotRequest;
import cn.xinzhili.api.doctor.bean.request.PasswordUpdateRequest;
import cn.xinzhili.api.doctor.bean.request.ResetStaffPwdByAdminRequest;
import cn.xinzhili.api.doctor.bean.request.StaffDepartmentRequest;
import cn.xinzhili.api.doctor.bean.request.UpdateAssistantRequest;
import cn.xinzhili.api.doctor.bean.request.UpdateDoctorRequest;
import cn.xinzhili.api.doctor.bean.request.UpdateFutureDoctorApiRequest;
import cn.xinzhili.api.doctor.bean.request.UpdateOperatorRequest;
import cn.xinzhili.api.doctor.bean.request.UpdateUserCertificateApiRequest;
import cn.xinzhili.api.doctor.bean.response.AssistantListResponse;
import cn.xinzhili.api.doctor.bean.response.AvatarUploadInfoResponse;
import cn.xinzhili.api.doctor.bean.response.CertificateUploadInfoResponse;
import cn.xinzhili.api.doctor.bean.response.ConsultationPatientListApiResponse;
import cn.xinzhili.api.doctor.bean.response.DoctorListResponse;
import cn.xinzhili.api.doctor.bean.response.InstitutionResponse;
import cn.xinzhili.api.doctor.bean.response.OperatorListResponse;
import cn.xinzhili.api.doctor.bean.response.PatientRegionListResponse;
import cn.xinzhili.api.doctor.bean.response.UserDetailResponse;
import cn.xinzhili.api.doctor.common.ContextMock;
import cn.xinzhili.api.doctor.config.ConfigConsts;
import cn.xinzhili.api.doctor.service.NotifyService;
import cn.xinzhili.api.doctor.service.UserService;
import cn.xinzhili.user.api.CertificateApi;
import cn.xinzhili.user.api.CertificateType;
import cn.xinzhili.user.api.ConsultationLevel;
import cn.xinzhili.user.api.FutureDoctorType;
import cn.xinzhili.user.api.Gender;
import cn.xinzhili.user.api.RiskFactor;
import cn.xinzhili.user.api.request.AddStaffRequest;
import cn.xinzhili.user.api.request.UpdateStaffRequest;
import cn.xinzhili.xutils.core.http.Response;
import cn.xinzhili.xutils.core.util.HashUtils;
import cn.xinzhili.xutils.spring.interceptor.ExceptionResolver;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Date: 16/02/2017 Time: 1:18 PM
 *
 * @author Gan Dong
 */
@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

  @Rule
  public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(
      "build/generated-snippets");

  private MockMvc mockMvc;

  @InjectMocks
  private UserController userController;

  @InjectMocks
  private ExceptionResolver exceptionResolver;

  @Mock
  private UserService userService;

  @Mock
  private NotifyService notifyService;

  private ObjectMapper mapper;

  private Long mockUserId = 66666L;

  private ContextMock contextMock = new ContextMock().withUid(mockUserId);

  private List<CertificateApi> certs = Arrays
      .asList(
          new CertificateApi(Collections.singletonList("test"), CertificateType.PROFESSION_CERT),
          new CertificateApi(Collections.singletonList("xxx"), CertificateType.BADGE));

  @Before
  public void setUp() {
    this.mockMvc = MockMvcBuilders
        .standaloneSetup(this.userController)
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

    mapper = new ObjectMapper();
  }

  @Test
  public void testGetUserDetail() throws Exception {

    User user = new User();
    user.setName("鹳狸猿");
    user.setUsername("admin1");
    user.setSex(UserGender.FEMALE);
    user.setAvatar("avatar url");
    user.setTel("15800000000");
    user.setBiography("个人简介");
    user.setExperience("个人经历");
    user.setCertificates(certs);
    user.setExpertise("个人擅长");
    user.setDepartmentName("心内科");
    user.setRoles(Collections.singletonList(UserRole.DOCTOR));
    user.setStatus(UserStatus.PENDING);
    user.setInviteStatus(UserInviteStatus.CONFIRMED);
    user.setMeetingLecture("会议与讲课");
    user.setHospitalIntroduction("所属医院");
    user.setFirstProfessionCompany("第一职业单位");

    final String instId = "8XBZKr";
    Institution institution = new Institution();
    institution.setId(instId);
    institution.setName("动物园医院");

    DepartmentDetail department = new DepartmentDetail();
    department.setId("20sslflw");
    department.setName("禽流感科");
    department.setAssistantCount(10);

    UserDetailResponse response = new UserDetailResponse();
    response.setInstitution(institution);
    response.setUser(user);
    response.setDepartment(department);
    response.setReviewOrgId(HashUtils.encode(10000L));

    given(userService.getUserDetails(anyLong(), anyLong(), anyBoolean()))
        .willReturn(response);

    contextMock.withUsername("admin1").withRole("ROLE_DOCTOR");
    mockMvc.perform(get("/api/v0/doctor/user")
        .param("userId", "8XBZKr")
        .param("organizationId", "8XBZKr")
        .contextPath(ConfigConsts.API_CONTEXT))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andExpect(jsonPath("$.data.user.tel", equalTo("15800000000")))
        .andExpect(jsonPath("$.data.user.avatar", equalTo("avatar url")))
        .andExpect(jsonPath("$.data.user.expertise", equalTo("个人擅长")))
        .andExpect(jsonPath("$.data.user.biography", equalTo("个人简介")))
        .andExpect(jsonPath("$.data.user.experience", equalTo("个人经历")))
        .andExpect(jsonPath("$.data.user.meetingLecture", equalTo("会议与讲课")))
        .andExpect(jsonPath("$.data.user.hospitalIntroduction", equalTo("所属医院")))
        .andExpect(jsonPath("$.data.user.firstProfessionCompany", equalTo("第一职业单位")))
        .andExpect(jsonPath("$.data.user.departmentName", equalTo("心内科")))
        .andExpect(jsonPath("$.data.user.username", equalTo("admin1")))
        .andExpect(jsonPath("$.data.user.sex", equalTo("FEMALE")))
        .andExpect(jsonPath("$.data.user.name", equalTo("鹳狸猿")))
        .andExpect(jsonPath("$.data.user.roles", hasSize(1)))
        .andExpect(jsonPath("$.data.user.status", equalTo("PENDING")))
        .andExpect(jsonPath("$.data.user.inviteStatus", equalTo("CONFIRMED")))
        .andExpect(
            jsonPath("$.data.user.roles", contains(UserRole.DOCTOR.name())))
        .andExpect(jsonPath("$.data.institution.id", equalTo(instId)))
        .andExpect(jsonPath("$.data.institution.name", equalTo("动物园医院")))
        .andDo(document("get_user_details",
            requestParameters(
                parameterWithName("userId").description("用户id（该参数为选填，不填则返回当前登录者信息）"),
                parameterWithName("organizationId").description("机构id:可选参数，当需要职工邀请状态时必填")
            ),
            responseFields(
                fieldWithPath("status").description("返回结果"),
                fieldWithPath("data").description("数据payload"),
                fieldWithPath("data.user.tel").description("电话号码"),
                fieldWithPath("data.user.name").description("姓名"),
                fieldWithPath("data.user.sex").description("性别"),
                fieldWithPath("data.user.avatar").description("头像"),
                fieldWithPath("data.user.expertise").description("擅长"),
                fieldWithPath("data.user.biography").description("简介"),
                fieldWithPath("data.user.experience").description("经历"),
                fieldWithPath("data.user.meetingLecture").description("会议与讲课"),
                fieldWithPath("data.user.hospitalIntroduction").description("所属医院"),
                fieldWithPath("data.user.firstProfessionCompany").description("第一职业单位"),

                fieldWithPath("data.user.certificates[].imageIds[]").description("证书图片ID"),
                fieldWithPath("data.user.certificates[].type").description("证书类型"),
                fieldWithPath("data.user.departmentName").description("科室名"),
                fieldWithPath("data.user.status").description("用户状态: NORMAL/PENDING/TO_REVIEW"),
                fieldWithPath("data.user.inviteStatus")
                    .description("用户受邀请状态:WAITING/CONFIRMED/REFUSED"),
                fieldWithPath("data.user.roles[]").description("用户的角色"),
                fieldWithPath("data.institution").description("用户的机构"),
                fieldWithPath("data.institution.name").description("用户所在机构的名称"),
                fieldWithPath("data.department").description(
                    "用户的科室。此字段当前只有在请求者为医生/医生助理 用户的时候才会返回, 即如果传了userId参数，或者登录用户不是医生／医生助理 用户，则此字段可能为 `null` "),
                fieldWithPath("data.department.id").description("用户所在的科室id"),
                fieldWithPath("data.department.name")
                    .description("用户所在的科室name"),
                fieldWithPath("data.department.assistantCount")
                    .description("用户所在的科室拥有的医助数量(可能为空)"),
                fieldWithPath("data.reviewOrgId").description("有权审核职员机构id")
            )));
  }

  @Test
  public void testAddDoctor() throws Exception {

    AddDoctorApiRequest request = new AddDoctorApiRequest();
    request.setSex(UserGender.MALE);
    request.setName("李时珍");
    request.setTitle(DoctorTitle.ATTENDING_PHYSICIAN);
    request.setDepartmentId("8POLbl");
    request.setOrganizationId("lQvRX8");
    request.setTel("15800000000");

    doNothing().when(userService).addStaff(any(AddStaffRequest.class));
    this.contextMock.withUid(mockUserId);

    this.mockMvc
        .perform(post(prefixWithContext("/user/doctor"))
            .contextPath(ConfigConsts.API_CONTEXT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("add_doctor",
            requestFields(
                fieldWithPath("name").description("医生姓名"),
                fieldWithPath("title").description(
                    "医生职称: CHIEF_PHYSICIAN-主任医师，ASSOCIATE_CHIEF_PHYSICIAN-副主任医师，ATTENDING_PHYSICIAN-主治医师, RESIDENT_PHYSICIAN - 住院医师"),
                fieldWithPath("sex").description("医生性别:(男／MALE 女／FEMALE)"),
                fieldWithPath("tel").description("医生电话"),
                fieldWithPath("departmentId").description("医生所属科室id"),
                fieldWithPath("organizationId").description("医生所属医院id")
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
  public void testAddAssistant() throws Exception {

    AddAssistantApiRequest request = new AddAssistantApiRequest();
    request.setSex(UserGender.MALE);
    request.setName("张助理");
    request.setDepartmentId("8POLbl");
    request.setOrganizationId("lQvRX8");
    request.setTel("15800000001");

    doNothing().when(userService).addStaff(any(AddStaffRequest.class));
    this.contextMock.withUid(mockUserId);

    this.mockMvc
        .perform(post(prefixWithContext("/user/assistant"))
            .contextPath(ConfigConsts.API_CONTEXT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("add_assistant",
            requestFields(
                fieldWithPath("name").description("医生助理姓名"),
                fieldWithPath("sex").description("性别:(男／MALE 女／FEMALE)"),
                fieldWithPath("tel").description("电话"),
                fieldWithPath("departmentId").description("医生助理所属科室id"),
                fieldWithPath("organizationId").description("医生助理所属医院id")
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
  public void testAddOperator() throws Exception {

    AddOperatorApiRequest request = new AddOperatorApiRequest();
    request.setSex(UserGender.MALE);
    request.setDepartmentId("8POLbl");
    request.setOrganizationId("lQvRX8");
    request.setTel("15800000001");

    doNothing().when(userService).addStaff(any(AddStaffRequest.class));
    this.contextMock.withUid(mockUserId);

    this.mockMvc
        .perform(post(prefixWithContext("/user/operator"))
            .contextPath(ConfigConsts.API_CONTEXT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("add_operator",
            requestFields(
                fieldWithPath("sex").description("性别:(男／MALE 女／FEMALE)"),
                fieldWithPath("tel").description("电话"),
                fieldWithPath("departmentId").description("运营所属科室id"),
                fieldWithPath("organizationId").description("运营所属医院id")
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
  public void testGetDoctorsPagination() throws Exception {

    DoctorListResponse response = new DoctorListResponse();
    List<Doctor> doctors = new ArrayList<>();
    Doctor doctor = new Doctor();
    doctor.setId("54OzK8");
    doctor.setStatus(UserStatus.NORMAL);
    doctor.setAvatar("url");
    doctor.setName("张三");
    doctor.setTitle(DoctorTitle.ATTENDING_PHYSICIAN);
    doctor.setTel("100000000");
    doctor.setSex(UserGender.FEMALE);
    doctor.setBiography("这是简介");
    doctor.setExperience("经历丰富");
    doctor.setDepartmentName("心内科");
    doctor.setExpertise("治病救人");
    doctor.setCertificates(certs);
    doctor.setMeetingLecture("医学课");
    doctor.setHospitalIntroduction("医院简介");
    doctor.setFirstProfessionCompany("第一职业单位");
    doctors.add(doctor);
    response.setDoctors(doctors);
    response.setTotal(10000);

    given(userService
        .getDoctorPagination(any(Long.class), any(GetStaffListRequest.class))).willReturn(response);
    this.contextMock.withUid(mockUserId);

    this.mockMvc
        .perform(MockMvcRequestBuilders.get("/user/doctor")
            .param("title", "ATTENDING_PHYSICIAN")
            .param("organizationId", "8POLbl")
            .param("departmentId", "lQvRX8")
            .param("keyword", "100000000")
            .param("status", "NORMAL")
            .param("pageAt", "0")
            .param("pageSize", "15")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(print())
        .andDo(document("get_doctor_list",
            requestParameters(
                parameterWithName("title").description(
                    "医生职称（可选）: CHIEF_PHYSICIAN-主任医师，ASSOCIATE_CHIEF_PHYSICIAN-副主任医师，ATTENDING_PHYSICIAN-主治医师, RESIDENT_PHYSICIAN - 住院医师"),
                parameterWithName("organizationId").description("机构id"),
                parameterWithName("departmentId").description("部门id"),
                parameterWithName("keyword").description("搜索关键字"),
                parameterWithName("status")
                    .description("职工状态：NORMAL/正常,PENDING/未完成注册,TO_REVIEW/待审核,FAILURE/审核失败"),
                parameterWithName("pageAt").description("页码"),
                parameterWithName("pageSize").description("页数")),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("结果数据"),
                fieldWithPath("data.doctors[].id").description("数据id"),
                fieldWithPath("data.doctors[].tel").description("电话号码"),
                fieldWithPath("data.doctors[].name").description("姓名"),
                fieldWithPath("data.doctors[].title").description(
                    "职称: CHIEF_PHYSICIAN-主任医师，ASSOCIATE_CHIEF_PHYSICIAN-副主任医师，ATTENDING_PHYSICIAN-主治医师, RESIDENT_PHYSICIAN - 住院医师"),
                fieldWithPath("data.doctors[].sex").description("性别: MALE/FEMALE"),
                fieldWithPath("data.doctors[].status")
                    .description("状态：NORMAL - 正常，PENDING - 未修改密码,TO_REVIEW-待审核,FAILURE-审核失败"),
                fieldWithPath("data.doctors[].avatar").description("头像"),
                fieldWithPath("data.doctors[].expertise").description("擅长"),
                fieldWithPath("data.doctors[].biography").description("简介"),
                fieldWithPath("data.doctors[].experience").description("经历"),
                fieldWithPath("data.doctors[].meetingLecture").description("会议与讲课"),
                fieldWithPath("data.doctors[].hospitalIntroduction").description("所属医院"),
                fieldWithPath("data.doctors[].firstProfessionCompany").description("第一职业单位"),
                fieldWithPath("data.doctors[].certificates[].imageIds[]").description("证书图片ID"),
                fieldWithPath("data.doctors[].certificates[].type").description("证书类型"),
                fieldWithPath("data.doctors[].departmentName")
                    .description("科室名"),
                fieldWithPath("data.total").description("数据总量")

            )))
        .andDo(print())
        .andReturn();
  }

  @Test
  public void testGetAssistantsPagination() throws Exception {

    AssistantListResponse response = new AssistantListResponse();
    List<Assistant> assistants = new ArrayList<>();
    Assistant assistant = new Assistant();
    assistant.setId("54OzK8");
    assistant.setStatus(UserStatus.NORMAL);
    assistant.setAvatar("url");
    assistant.setName("李四");
    assistant.setTel("100000000");
    assistant.setSex(UserGender.FEMALE);
    assistant.setBiography("这是简介");
    assistant.setExperience("经历丰富");
    assistant.setDepartmentName("心外科");
    assistant.setExpertise("帮助医生治病救人");
    assistant.setMeetingLecture("医学课");
    assistant.setHospitalIntroduction("医院简介");
    assistant.setFirstProfessionCompany("第一职业单位");
    assistant.setCertificates(certs);
    assistants.add(assistant);
    response.setAssistants(assistants);
    response.setTotal(10000);

    given(userService
        .getAssistantPagination(any(Long.class), any(GetStaffListRequest.class)))
        .willReturn(response);

    this.mockMvc
        .perform(MockMvcRequestBuilders.get("/user/assistant")
            .param("organizationId", "8POLbl")
            .param("departmentId", "lQvRX8")
            .param("doctorId", "54OzK8")
            .param("keyword", "100000000")
            .param("status", "NORMAL")
            .param("isBindingPatient", "true")
            .param("pageAt", "0")
            .param("pageSize", "15")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("get_assistant_list",
            requestParameters(
                parameterWithName("organizationId").description("机构id"),
                parameterWithName("departmentId").description("部门id"),
                parameterWithName("doctorId").description("医生助理关联患者所绑定的医生id"),
                parameterWithName("keyword").description("搜索关键字"),
                parameterWithName("isBindingPatient").description("助理是否存在绑定患者:true/存在，false/不存在"),
                parameterWithName("status")
                    .description("职工状态：NORMAL/正常,PENDING/未完成注册,TO_REVIEW/待审核,FAILURE/审核失败"),
                parameterWithName("pageAt").description("页码"),
                parameterWithName("pageSize").description("页数")),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("结果数据"),
                fieldWithPath("data.assistants[].id").description("数据id"),
                fieldWithPath("data.assistants[].tel").description("电话号码"),
                fieldWithPath("data.assistants[].name").description("姓名"),
                fieldWithPath("data.assistants[].sex").description("性别 MALE/FEMALE"),
                fieldWithPath("data.assistants[].status").description("状态 NORMAL/PENDING"),
                fieldWithPath("data.assistants[].avatar").description("头像"),
                fieldWithPath("data.assistants[].expertise").description("擅长"),
                fieldWithPath("data.assistants[].biography").description("简介"),
                fieldWithPath("data.assistants[].experience").description("经历"),
                fieldWithPath("data.assistants[].meetingLecture").description("会议与讲课"),
                fieldWithPath("data.assistants[].hospitalIntroduction").description("所属医院"),
                fieldWithPath("data.assistants[].firstProfessionCompany").description("第一职业单位"),

                fieldWithPath("data.assistants[].certificates[].imageIds[]").description("证书图片ID"),
                fieldWithPath("data.assistants[].certificates[].type").description("证书类型"),
                fieldWithPath("data.assistants[].departmentName")
                    .description("科室名"),
                fieldWithPath("data.total").description("数据总量")
            )))
        .andDo(print())
        .andReturn();
  }

  @Test
  public void testGetOperatorPagination() throws Exception {

    OperatorListResponse response = new OperatorListResponse();
    List<Operator> operators = new ArrayList<>();
    Operator operator = new Operator();
    operator.setId("54OzK8");
    operator.setStatus(UserStatus.NORMAL);
    operator.setAvatar("url");
    operator.setName("李四");
    operator.setTel("100000000");
    operator.setSex(UserGender.FEMALE);
    operator.setBiography("这是简介");
    operator.setExperience("经历丰富");
    operator.setDepartmentName("心外科");
    operator.setExpertise("帮助医生治病救人");
    operator.setMeetingLecture("医学课");
    operator.setHospitalIntroduction("医院简介");
    operator.setCertificates(certs);
    operators.add(operator);
    response.setOperators(operators);
    response.setTotal(10000);
    operator.setInviteStatus(UserInviteStatus.CONFIRMED);

    given(userService
        .getOperatorPagination(any(Long.class), any(GetStaffListRequest.class)))
        .willReturn(response);

    this.mockMvc
        .perform(MockMvcRequestBuilders.get("/user/operator")
            .param("organizationId", "8POLbl")
            .param("departmentId", "lQvRX8")
            .param("inviteStatuses", "WAITING")
            .param("status", "NORMAL")
            .param("keyword", "100000000")
            .param("isBindingPatient", "true")
            .param("pageAt", "0")
            .param("pageSize", "15")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("get_operator_list",
            requestParameters(
                parameterWithName("organizationId").description("机构id"),
                parameterWithName("departmentId").description("部门id"),
                parameterWithName("inviteStatuses")
                    .description("邀请状态:  WAITING/待确认,CONFIRMED/已确认,"
                        + "  REFUSED/已拒绝"),
                parameterWithName("status")
                    .description("职工状态：NORMAL/正常,PENDING/未完成注册,TO_REVIEW/待审核,FAILURE/审核失败"),
                parameterWithName("keyword").description("搜索关键字"),
                parameterWithName("isBindingPatient").description("运营是否存在绑定患者:true/存在，false/不存在"),
                parameterWithName("pageAt").description("页码"),
                parameterWithName("pageSize").description("页数")),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("结果数据"),
                fieldWithPath("data.operators[].id").description("数据id"),
                fieldWithPath("data.operators[].tel").description("电话号码"),
                fieldWithPath("data.operators[].name").description("姓名"),
                fieldWithPath("data.operators[].sex").description("性别 MALE/FEMALE"),
                fieldWithPath("data.operators[].status").description("状态 NORMAL/PENDING"),
                fieldWithPath("data.operators[].avatar").description("头像"),
                fieldWithPath("data.operators[].expertise").description("擅长"),
                fieldWithPath("data.operators[].biography").description("简介"),
                fieldWithPath("data.operators[].experience").description("经历"),
                fieldWithPath("data.operators[].meetingLecture").description("会议与讲课"),
                fieldWithPath("data.operators[].hospitalIntroduction").description("所属医院"),
                fieldWithPath("data.operators[].firstProfessionCompany")
                    .description("第一职业单位（运营没有）"),

                fieldWithPath("data.operators[].certificates[].imageIds[]").description("证书图片ID"),
                fieldWithPath("data.operators[].certificates[].type").description("证书类型"),
                fieldWithPath("data.operators[].departmentName").description("科室名"),
                fieldWithPath("data.operators[].inviteStatus")
                    .description("邀请状态:  WAITING/待确认,CONFIRMED/已确认,REFUSED/已拒绝"),
                fieldWithPath("data.total").description("数据总量")
            )))
        .andDo(print())
        .andReturn();
  }

  @Test
  public void testDeleteDoctor() throws Exception {

    List<String> ids = new ArrayList<>();
    ids.add("54OzK8");
    ids.add("5RWgvr");

    doNothing().when(userService).deleteDoctor(anyListOf(Long.class));
    this.contextMock.withUid(mockUserId);

    this.mockMvc
        .perform(delete(prefixWithContext("/user/doctor"))
            .contextPath(ConfigConsts.API_CONTEXT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(ids)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("delete_doctor",
            requestFields(
                fieldWithPath("[]").description("待删除医生id")
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
  public void testDeleteAssistant() throws Exception {

    List<String> ids = new ArrayList<>();
    ids.add("5amxL5");

    doNothing().when(userService).deleteAssistant(anyListOf(Long.class));
    this.contextMock.withUid(mockUserId);

    this.mockMvc
        .perform(delete(prefixWithContext("/user/assistant"))
            .contextPath(ConfigConsts.API_CONTEXT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(ids)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("delete_assistant",
            requestFields(
                fieldWithPath("[]").description("待删除医生助手id")
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
  public void testDeleteOperator() throws Exception {

    List<String> ids = new ArrayList<>();
    ids.add("5amxL5");

    DeleteOperatorApiRequest request = new DeleteOperatorApiRequest();
    request.setOperatorId(ids);
    request.setOrganizationId("5amxL5");

    doNothing().when(userService).deleteAssistant(anyListOf(Long.class));
    this.contextMock.withUid(mockUserId);

    this.mockMvc
        .perform(delete(prefixWithContext("/user/operator"))
            .contextPath(ConfigConsts.API_CONTEXT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("delete_operator",
            requestFields(
                fieldWithPath("operatorId[]").description("待删除运营id"),
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
  public void testUpdateDoctor() throws Exception {

    UpdateDoctorRequest request = new UpdateDoctorRequest();
    request.setId("54OzK8");
    request.setName("李时珍");
    request.setTitle(DoctorTitle.ATTENDING_PHYSICIAN);
    request.setAvatar("new avatar url");
    request.setExpertise("治病救人");
    request.setSex(UserGender.FEMALE);
    request.setTel("15900000000");
    request.setExperience("经历");
    request.setBiography("个人简介");
    request.setDepartmentId("5amxL5");
    request.setOrganizationId("5RWgvr");
    request.setMeetingLecture("医学课");
    request.setHospitalIntroduction("北京医院");
    request.setFirstProfessionCompany("第一职业单位");

    Doctor doctor = new Doctor();
    doctor.setId("54OzK8");
    doctor.setName("李时珍");
    doctor.setTitle(DoctorTitle.ATTENDING_PHYSICIAN);
    doctor.setAvatar("new avatar url");
    doctor.setExpertise("治病救人");
    doctor.setSex(UserGender.FEMALE);
    doctor.setTel("15900000000");
    doctor.setExperience("经历");
    doctor.setCertificates(certs);
    doctor.setBiography("个人简介");
    doctor.setRoles(Arrays.asList(UserRole.ASSISTANT, UserRole.DOCTOR));
    doctor.setDepartmentName("心内科");
    doctor.setUsername("15900000000");
    doctor.setStatus(UserStatus.NORMAL);
    doctor.setInviteStatus(UserInviteStatus.CONFIRMED);
    doctor.setMeetingLecture("医学课");
    doctor.setHospitalIntroduction("北京医院");
    doctor.setFirstProfessionCompany("第一职业单位");

    given(userService.updateDoctor(any(UpdateStaffRequest.class)))
        .willReturn(doctor);
    this.contextMock.withUid(mockUserId);

    this.mockMvc
        .perform(patch(prefixWithContext("/user/doctor"))
            .contextPath(ConfigConsts.API_CONTEXT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("update_doctor",
            requestFields(
                fieldWithPath("id").description("医生id(必填)"),
                fieldWithPath("organizationId").description("所属医院id(必填)"),
                fieldWithPath("name").description("姓名"),
                fieldWithPath("title").description(
                    "职称: CHIEF_PHYSICIAN-主任医师，ASSOCIATE_CHIEF_PHYSICIAN-副主任医师，ATTENDING_PHYSICIAN-主治医师, RESIDENT_PHYSICIAN - 住院医师"),
                fieldWithPath("avatar").description("头像"),
                fieldWithPath("expertise").description("擅长"),
                fieldWithPath("sex").description("性别"),
                fieldWithPath("tel").description("电话"),
                fieldWithPath("experience").description("经验"),
                fieldWithPath("biography").description("个人简介"),
                fieldWithPath("departmentId").description("新的科室id"),
                fieldWithPath("meetingLecture").description("会议与研究"),
                fieldWithPath("hospitalIntroduction").description("所属医院"),
                fieldWithPath("firstProfessionCompany").description("第一职业单位")

            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data.doctor.id").description("医生id"),
                fieldWithPath("data.doctor.name").description("姓名"),
                fieldWithPath("data.doctor.title").description(
                    "职称: CHIEF_PHYSICIAN-主任医师，ASSOCIATE_CHIEF_PHYSICIAN-副主任医师，ATTENDING_PHYSICIAN-主治医师, RESIDENT_PHYSICIAN - 住院医师"),
                fieldWithPath("data.doctor.avatar").description("头像"),
                fieldWithPath("data.doctor.expertise").description("擅长"),
                fieldWithPath("data.doctor.sex").description("性别 MALE/FEMALE"),
                fieldWithPath("data.doctor.status")
                    .description("状态 NORMAL/PENDING/TO_REVIEW-待审核/FAILURE/审核失败"),
                fieldWithPath("data.doctor.inviteStatus")
                    .description("用户受邀请状态:WAITING/CONFIRMED/REFUSED"),
                fieldWithPath("data.doctor.tel").description("电话"),
                fieldWithPath("data.doctor.username").description("帐号"),
                fieldWithPath("data.doctor.experience").description("经验"),
                fieldWithPath("data.doctor.meetingLecture").description("会议与研究"),
                fieldWithPath("data.doctor.hospitalIntroduction").description("所属医院"),
                fieldWithPath("data.doctor.firstProfessionCompany").description("第一职业单位"),
                fieldWithPath("data.doctor.certificates[].imageIds[]").description("证书图片ID"),
                fieldWithPath("data.doctor.certificates[].type").description("证书类型"),
                fieldWithPath("data.doctor.biography").description("个人简介"),
                fieldWithPath("data.doctor.qrCodeUrl").description("医生二维码 其他角色为null"),
                fieldWithPath("data.doctor.departmentName").description("科室名"),
                fieldWithPath("data.doctor.roles[]").description("角色")
            )
        ))
        .andDo(print())
        .andReturn();
  }

  @Test
  public void testUpdateUserCertificate() throws Exception {

    UpdateUserCertificateApiRequest request = new UpdateUserCertificateApiRequest();
    request.setCertificates(certs);

    doNothing().when(userService)
        .updateUserCertificate(anyLong(), any(UpdateUserCertificateApiRequest.class));
    this.contextMock.withUid(mockUserId);

    this.mockMvc
        .perform(patch(prefixWithContext("/user/certificate"))
            .contextPath(ConfigConsts.API_CONTEXT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("update_user_certificate",
            requestFields(
                fieldWithPath("certificates[].imageIds[]").description("证书图片ID"),
                fieldWithPath("certificates[].type")
                    .description("证书类型 : PROFESSION_CERT/执业证书,BADGE/胸牌,NURSE_CERT/护士资格证")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("null")
            )
        ))
        .andDo(print())
        .andReturn();
  }

  @Test
  public void testUpdateAssistant() throws Exception {

    UpdateAssistantRequest request = new UpdateAssistantRequest();
    request.setId("54OzK8");
    request.setName("李时珍助理");
    request.setAvatar("new avatar url");
    request.setExpertise("治病救人");
    request.setSex(UserGender.FEMALE);
    request.setTel("15900000000");
    request.setExperience("经历");
    request.setBiography("个人简介");
    request.setDepartmentId("5amxL5");
    request.setOrganizationId("5RWgvr");
    request.setMeetingLecture("医学课");
    request.setHospitalIntroduction("北京医院");
    request.setFirstProfessionCompany("第一职业单位");

    Assistant assistant = new Assistant();
    assistant.setId("54OzK8");
    assistant.setName("李时珍助理");
    assistant.setAvatar("new avatar url");
    assistant.setExpertise("治病救人");
    assistant.setSex(UserGender.FEMALE);
    assistant.setStatus(UserStatus.NORMAL);
    assistant.setTel("15900000000");
    assistant.setExperience("经历");
    assistant.setCertificates(certs);
    assistant.setBiography("个人简介");
    assistant.setRoles(Arrays.asList(UserRole.ASSISTANT, UserRole.DOCTOR));
    assistant.setDepartmentName("心内科");
    assistant.setUsername("15900000000");
    assistant.setInviteStatus(UserInviteStatus.CONFIRMED);
    assistant.setChatStatus(UserChatStatus.NORMAL);
    assistant.setMeetingLecture("医学课");
    assistant.setHospitalIntroduction("北京医院");
    assistant.setFirstProfessionCompany("第一职业单位");

    given(userService.updateAssistant(any(UpdateStaffRequest.class)))
        .willReturn(assistant);
    this.contextMock.withUid(mockUserId);

    this.mockMvc
        .perform(patch(prefixWithContext("/user/assistant"))
            .contextPath(ConfigConsts.API_CONTEXT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("update_assistant",
            requestFields(
                fieldWithPath("id").description("医生助理id（必填）"),
                fieldWithPath("organizationId").description("所属医院id（必填）"),
                fieldWithPath("name").description("姓名"),
                fieldWithPath("avatar").description("头像"),
                fieldWithPath("expertise").description("擅长"),
                fieldWithPath("sex").description("性别"),
                fieldWithPath("tel").description("电话"),
                fieldWithPath("experience").description("经验"),
                fieldWithPath("biography").description("个人简介"),
                fieldWithPath("meetingLecture").description("会议与研究"),
                fieldWithPath("hospitalIntroduction").description("所属医院"),
                fieldWithPath("firstProfessionCompany").description("第一职业单位"),
                fieldWithPath("departmentId").description("新的科室id")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data.assistant.id").description("医生助理id"),
                fieldWithPath("data.assistant.name").description("姓名"),
                fieldWithPath("data.assistant.avatar").description("头像"),
                fieldWithPath("data.assistant.expertise").description("擅长"),
                fieldWithPath("data.assistant.sex").description("性别: FEMALE/MALE/OTHER"),
                fieldWithPath("data.assistant.status").description("状态: NORMAL/PENDING/TO_REVIEW"),
                fieldWithPath("data.assistant.inviteStatus")
                    .description("用户受邀请状态:WAITING/CONFIRMED/REFUSED"),
                fieldWithPath("data.assistant.tel").description("电话"),
                fieldWithPath("data.assistant.username").description("帐号"),
                fieldWithPath("data.assistant.experience").description("经验"),
                fieldWithPath("data.assistant.meetingLecture").description("会议与研究"),
                fieldWithPath("data.assistant.hospitalIntroduction").description("所属医院"),
                fieldWithPath("data.assistant.firstProfessionCompany").description("所属医院"),

                fieldWithPath("data.assistant.certificates[].imageIds[]").description("证书图片ID"),
                fieldWithPath("data.assistant.certificates[].type").description("证书类型"),
                fieldWithPath("data.assistant.biography").description("个人简介"),
                fieldWithPath("data.assistant.qrCodeUrl").description("医生二维码 其他角色为null"),
                fieldWithPath("data.assistant.chatStatus")
                    .description("聊天状态 NORMAL 正常  PROHIBITION_OF_SPEECH 禁止发言"),
                fieldWithPath("data.assistant.departmentName")
                    .description("科室名"),
                fieldWithPath("data.assistant.roles[]").description("角色")
            )
        ))
        .andDo(print())
        .andReturn();
  }

  @Test
  public void testUpdateOperator() throws Exception {

    UpdateOperatorRequest request = new UpdateOperatorRequest();
    request.setId("54OzK8");
    request.setName("运营");
    request.setAvatar("new avatar url");
    request.setExpertise("治病救人");
    request.setSex(UserGender.FEMALE);
    request.setTel("15900000000");
    request.setExperience("经历");
    request.setBiography("个人简介");
    request.setDepartmentId("5amxL5");
    request.setOrganizationId("5RWgvr");
    request.setMeetingLecture("医学课");
    request.setHospitalIntroduction("北京医院");

    Operator operator = new Operator();
    operator.setId("54OzK8");
    operator.setName("运营");
    operator.setAvatar("new avatar url");
    operator.setExpertise("治病救人");
    operator.setSex(UserGender.FEMALE);
    operator.setStatus(UserStatus.NORMAL);
    operator.setTel("15900000000");
    operator.setExperience("经历");
    operator.setCertificates(certs);
    operator.setBiography("个人简介");
    operator.setMeetingLecture("医学课");
    operator.setHospitalIntroduction("北京医院");
    operator.setRoles(Arrays.asList(UserRole.ASSISTANT, UserRole.DOCTOR));
    operator.setDepartmentName("心内科");
    operator.setUsername("15900000000");
    operator.setInviteStatus(UserInviteStatus.CONFIRMED);

    given(userService.updateOperator(any(UpdateStaffRequest.class)))
        .willReturn(operator);
    this.contextMock.withUid(mockUserId);

    this.mockMvc
        .perform(patch(prefixWithContext("/user/operator"))
            .contextPath(ConfigConsts.API_CONTEXT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("update_operator",
            requestFields(
                fieldWithPath("id").description("运营id（必填）"),
                fieldWithPath("organizationId").description("所属医院id（必填）"),
                fieldWithPath("name").description("姓名"),
                fieldWithPath("avatar").description("头像"),
                fieldWithPath("expertise").description("擅长"),
                fieldWithPath("sex").description("性别"),
                fieldWithPath("tel").description("电话"),
                fieldWithPath("experience").description("经验"),
                fieldWithPath("biography").description("个人简介"),
                fieldWithPath("meetingLecture").description("会议与研究"),
                fieldWithPath("hospitalIntroduction").description("所属医院"),
                fieldWithPath("firstProfessionCompany").description("第一职业单位(运营不填)"),
                fieldWithPath("departmentId").description("新的科室id")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data.operator.id").description("医生助理id"),
                fieldWithPath("data.operator.name").description("姓名"),
                fieldWithPath("data.operator.avatar").description("头像"),
                fieldWithPath("data.operator.expertise").description("擅长"),
                fieldWithPath("data.operator.sex").description("性别: FEMALE/MALE/OTHER"),
                fieldWithPath("data.operator.status").description("状态: NORMAL/PENDING/TO_REVIEW"),
                fieldWithPath("data.operator.inviteStatus")
                    .description("用户受邀请状态:WAITING/CONFIRMED/REFUSED"),
                fieldWithPath("data.operator.tel").description("电话"),
                fieldWithPath("data.operator.username").description("帐号"),
                fieldWithPath("data.operator.experience").description("经验"),
                fieldWithPath("data.operator.meetingLecture").description("会议与研究"),
                fieldWithPath("data.operator.hospitalIntroduction").description("所属医院"),
                fieldWithPath("data.operator.firstProfessionCompany").description("第一职业单位(运营没有)"),
                fieldWithPath("data.operator.certificates[].imageIds[]").description("证书图片ID"),
                fieldWithPath("data.operator.certificates[].type").description("证书类型"),
                fieldWithPath("data.operator.biography").description("个人简介"),
                fieldWithPath("data.operator.departmentName").description("科室名"),
                fieldWithPath("data.operator.qrCodeUrl").description("医生二维码 其他角色为null"),
                fieldWithPath("data.operator.roles[]").description("角色"))
        ))
        .andDo(print())
        .andReturn();
  }

  @Test
  public void testMoveStaffDepartment() throws Exception {
    List<String> ids = Arrays.asList("1", "2", "3");
    StaffDepartmentRequest request = new StaffDepartmentRequest();
    request.setDepartmentId("1");
    request.setStaffIds(ids);
    request.setUserRole(UserRole.OPERATOR);

    given(userService.moveStaffDepartment(anyLong(), any()))
        .willReturn(Response.instanceSuccess());

    contextMock.withUid(mockUserId);

    mockMvc.perform(post(ConfigConsts.prefixWithContext("/user/department"))
        .contextPath(API_CONTEXT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsBytes(request)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("move_staff_department",
            requestFields(
                fieldWithPath("departmentId").description("部门 id"),
                fieldWithPath("staffIds").description("用户 id 列表"),
                fieldWithPath("userRole").description("用户角色：ASSISTANT,DOCTOR")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("结果数据")
            )
        ))
        .andDo(print())
        .andReturn();
  }

  @Test
  public void testCompleteStaffInfo() throws Exception {
    final String newPwd = "mynewpwd";
    final String name = "杨大全";
    final String biography = "医学世家";
    final String expertise = "把脉";
    final String experience = "曾任太医";
    final long userId = 10032L;

    InitUserRequest request = new InitUserRequest();
    request.setName(name);
    request.setAvatar("XXX-XXX");
    request.setNewPassword(newPwd);
    request.setBiography(biography);
    request.setSex(UserGender.MALE);
    request.setExperience(experience);
    request.setExpertise(expertise);
    request.setCertificates(certs);
    request.setMeetingLecture("医学课");
    request.setHospitalIntroduction("医科大学");
    request.setFirstProfessionCompany("第一职业单位");
    given(userService.completeStaffInfo(userId, request))
        .willReturn(true);
    contextMock.withUid(mockUserId);

    mockMvc.perform(post(ConfigConsts.prefixWithContext("/user/init"))
        .contextPath(API_CONTEXT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsBytes(request)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("init_staff",
            requestFields(
                fieldWithPath("newPassword").description("新密码"),
                fieldWithPath("name").description("用户姓名"),
                fieldWithPath("avatar").description("用户头像"),
                fieldWithPath("sex").description("用户性别：MALE/FEMALE/OTHER"),
                fieldWithPath("biography").description("个人简介"),
                fieldWithPath("expertise").description("擅长领域"),
                fieldWithPath("experience").description("个人经历"),
                fieldWithPath("meetingLecture").description("会议与研究"),
                fieldWithPath("hospitalIntroduction").description("所属医院"),
                fieldWithPath("firstProfessionCompany").description("第一职业单位"),
                fieldWithPath("certificates[].imageIds[]").description("证书图片ID"),
                fieldWithPath("certificates[].type")
                    .description("证书类型:PROFESSION_CERT->执业证书，BADGE->胸牌")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("结果数据")
            )
        ))
        .andDo(print())
        .andReturn();

  }

  @Test
  public void testAdminReviewUser() throws Exception {
    UserReview review = new UserReview();
    review.setUserId("54OzK8");
    review.setPass(true);
    review.setRole(UserRole.OPERATOR);
    review.setOrganizationId(HashUtils.encode(1111L));
    doNothing().when(userService).reviewUser(review);
    mockMvc.perform(post(ConfigConsts.prefixWithContext("/admin/review/user"))
        .contextPath(ConfigConsts.API_CONTEXT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsBytes(review)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("admin_review_user",
            requestFields(
                fieldWithPath("userId").description("被审核人id"),
                fieldWithPath("role").description("审核角色"),
                fieldWithPath("organizationId").description("角色结构id"),
                fieldWithPath("pass").description("是否通过")),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("结果数据")
            )));
  }

  @Test
  public void testAdminResetPassword() throws Exception {

    ResetStaffPwdByAdminRequest request = new ResetStaffPwdByAdminRequest();
    request.setOrganizationId(HashUtils.encode(1000L));

    given(userService.resetUserPasswordByAdmin(anyLong(), anyLong(), anyLong())).willReturn(true);
    this.contextMock.withUid(mockUserId);

    mockMvc.perform(RestDocumentationRequestBuilders
        .post(ConfigConsts.prefixWithContext("/user/password/{id}"), HashUtils.encode(1000L))
        .contextPath(ConfigConsts.API_CONTEXT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("admin_reset_pwd",
            pathParameters(
                parameterWithName("id").description("被重置密码的员工的id")
            ),
            requestFields(
                fieldWithPath("organizationId").description("机构id")
            ),
            responseFields(
                fieldWithPath("status").description("返回结果"),
                fieldWithPath("data").description("数据payload")
            )
        ))
        .andDo(print())
        .andReturn();
  }

  @Test
  public void testResetUserPassword() throws Exception {

    final String username = "13800138000";
    final String newPassword = "my new passcode";
    final String vcode = "1230";

    final PasswordForgotRequest request = new PasswordForgotRequest();
    request.setUsername(username);
    request.setNewPassword(newPassword);
    request.setVcode(vcode);

    given(userService.resetUserPassword(username, newPassword, vcode))
        .willReturn(true);

    mockMvc.perform(post(ConfigConsts.prefixWithContext("/user/password"))
        .contextPath(API_CONTEXT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsBytes(request)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("reset_pwd",
            requestFields(
                fieldWithPath("username").description("用户名／用户手机号"),
                fieldWithPath("newPassword").description("新密码"),
                fieldWithPath("vcode").description("验证码")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("结果数据")
            )
        ))
        .andDo(print())
        .andReturn();
  }

  @Test
  public void testUpdateUserPassword() throws Exception {

    final String originalPassword = "my old password";
    final String newPassword = "my new password";
    final Long userId = 10032L;

    final PasswordUpdateRequest request = new PasswordUpdateRequest();
    request.setOriginalPassword(originalPassword);
    request.setNewPassword(newPassword);

    given(userService.updateUserPassword(userId, newPassword, originalPassword))
        .willReturn(true);
    contextMock.withUid(userId);

    mockMvc.perform(patch(ConfigConsts.prefixWithContext("/user/password"))
        .contextPath(API_CONTEXT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsBytes(request)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("update_pwd",
            requestFields(
                fieldWithPath("originalPassword").description("原密码"),
                fieldWithPath("newPassword").description("新密码")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("结果数据")
            )
        ))
        .andDo(print())
        .andReturn();
  }

  @Test
  public void testSendVcode() throws Exception {
    doNothing().when(notifyService).sendVcode(anyString());
    Map<String, String> request = new HashMap<>();
    request.put("phone", "13800010001");

    mockMvc.perform(post(ConfigConsts.prefixWithContext("/user/verification"))
        .contextPath(ConfigConsts.API_CONTEXT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsBytes(request)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("send_v_code",
            requestFields(
                fieldWithPath("phone").description("手机号")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("结果数据")
            )));
  }

  @Test
  public void testHandleStaffFileUpload() throws Exception {
    given(userService
        .handleStaffFileUpload(any(), anyLong(), anyLong(), anyLong(), any(UserRole.class)))
        .willReturn(Response.instanceSuccess());
    mockMvc.perform(
        MockMvcRequestBuilders.fileUpload(ConfigConsts.prefixWithContext("/admin/staff/upload"))
            .file("file", new byte[]{1, 2, 3, 4})
            .contextPath(ConfigConsts.API_CONTEXT).contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
            .param("departmentId", "54OzK8")
            .param("organizationId", "54OzK8")
            .param("role", UserRole.DOCTOR.name()))
        .andDo(print()).andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("handle_staff_file_upload",
            requestParts(
                partWithName("file").description("上传文件")
            ),
            requestParameters(
                parameterWithName("departmentId").description("科室id"),
                parameterWithName("organizationId").description("机构id"),
                parameterWithName("role").description("角色: PATIENT, DOCTOR, ASSISTANT")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("结果数据")
            )));
  }

  @Test
  public void testHandlePatientFileUpload() throws Exception {
    given(userService.handlePatientFileUpload(anyLong(), any()))
        .willReturn(Response.instanceSuccess());
    mockMvc.perform(
        MockMvcRequestBuilders.fileUpload(ConfigConsts.prefixWithContext("/admin/patient/upload"))
            .file("file", new byte[]{1, 2, 3, 4})
            .contextPath(ConfigConsts.API_CONTEXT).contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
            .param("organizationId", "54OzK8"))
        .andDo(print()).andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("handle_patient_file_upload",
            requestParts(
                partWithName("file").description("上传文件:患者手机,医生手机,医助手机,运营手机,患者级别}")
            ),
            requestParameters(
                parameterWithName("organizationId").description("机构id")),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("结果数据")
            )));
  }


  @Test
  public void testGetAvatarUploadInfo() throws Exception {

    String originSignUrl =
        "https://xzl.s3.cn-north-1.amazonaws.com.cn/ops/cf82bd5e-ee5e-481b-bc12-92b958f00490?" +
            "x-amz-acl=public-read&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20161009T022434Z&X-Amz-SignedHeaders="
            +
            "content-type%3Bhost&X-Amz-Expires=299&X-Amz-Credential=AKIAOAEHEJT7DJCWQILQ%2F20161009%2Fcn-north-1%2Fs3%2Faws4_request&"
            +
            "X-Amz-Signature=2f80eea725f8fe7ecb8b8be46894d61ac3aa0fd7c9925e9b26dca96d939396b3";

    AvatarUploadInfoResponse response = new AvatarUploadInfoResponse();
    ImageUploadInfo uploadInfo = new ImageUploadInfo();
    uploadInfo.setContentType("image/jpeg");
    uploadInfo.setImageId("d030a9f5-5303-4551-9248-705c8cadf0b4");
    uploadInfo.setSignUrl(originSignUrl);
    uploadInfo.setSignUrl(
        "https://s3.cn-north-1.amazonaws.com.cn/medical-images/d030a9f5-5303-4551-9248-705c8cadf0b4");
    response.setUploadInfo(uploadInfo);
    given(userService.getAvatarUploadInfo(any(String.class))).willReturn(response);
    this.contextMock.withUid(mockUserId);

    this.mockMvc
        .perform(MockMvcRequestBuilders.get("/user/avatar")
            .param("filename", "a.jpg")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(print())
        .andDo(document("avatar_upload_info",
            requestParameters(
                parameterWithName("filename").description("文件名")),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data.uploadInfo.signUrl").description("图片上传url,使用PUT方式请求"),
                fieldWithPath("data.uploadInfo.contentType").description("图片上传contentType"),
                fieldWithPath("data.uploadInfo.rawUrl").description("上传成功后图片的访问链接"),
                fieldWithPath("data.uploadInfo.imageId").description("更新医生/医助资料时的avatar参数"),
                fieldWithPath("data.uploadInfo.fileName").description("文件名")
            )));
  }

  @Test
  public void testGetCertificateUploadInfo() throws Exception {

    String originSignUrl =
        "https://xzl.s3.cn-north-1.amazonaws.com.cn/ops/cf82bd5e-ee5e-481b-bc12-92b958f00490?" +
            "x-amz-acl=public-read&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20161009T022434Z&X-Amz-SignedHeaders="
            +
            "content-type%3Bhost&X-Amz-Expires=299&X-Amz-Credential=AKIAOAEHEJT7DJCWQILQ%2F20161009%2Fcn-north-1%2Fs3%2Faws4_request&"
            +
            "X-Amz-Signature=2f80eea725f8fe7ecb8b8be46894d61ac3aa0fd7c9925e9b26dca96d939396b3";

    CertificateUploadInfoResponse response = new CertificateUploadInfoResponse();
    ImageUploadInfo uploadInfo = new ImageUploadInfo();
    uploadInfo.setContentType("image/jpeg");
    uploadInfo.setImageId("d030a9f5-5303-4551-9248-705c8cadf0b4");
    uploadInfo.setSignUrl(originSignUrl);
    uploadInfo.setSignUrl(
        "https://s3.cn-north-1.amazonaws.com.cn/medical-images/d030a9f5-5303-4551-9248-705c8cadf0b4");
    response.setUploadInfo(uploadInfo);
    List<String> fileNames = Collections.singletonList("a.jpg");
    given(userService.getCertificateUploadInfo(fileNames)).willReturn(response);
    this.contextMock.withUid(mockUserId);
    this.mockMvc
        .perform(MockMvcRequestBuilders.get("/user/certificate")
            .param("filename", "a.jpg")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(fileNames)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(print())
        .andDo(document("certificate_upload_info",
            requestParameters(
                parameterWithName("filename").description("文件名")),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data.uploadInfo.signUrl").description("图片上传url,使用PUT方式请求"),
                fieldWithPath("data.uploadInfo.contentType").description("图片上传contentType"),
                fieldWithPath("data.uploadInfo.rawUrl").description("上传成功后图片的访问链接"),
                fieldWithPath("data.uploadInfo.imageId").description("更新医生/医助资料时的certificate参数"),
                fieldWithPath("data.uploadInfo.fileName").description("文件名")
            )));
  }


  @Test
  public void testGetStaffPatientRegion() throws Exception {

    PatientRegionListResponse response = new PatientRegionListResponse();
    response.setRegions(Collections.singletonList("帝都"));
    given(userService.getStaffPatientRegion(any(UserRole.class), anyLong())).willReturn(response);
    this.contextMock.withUid(mockUserId);

    contextMock.withUsername("admin1").withRole("ROLE_DOCTOR");

    this.mockMvc
        .perform(MockMvcRequestBuilders.get("/user/patient/region"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(print())
        .andDo(document("user_patient_region",
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data.regions[]").description("省份名称")
            )));
  }

  @Test
  public void testGetPatientByConsultationDoctor() throws Exception {
    ConsultationPatientApiInfo apiInfo = new ConsultationPatientApiInfo();
    apiInfo.setId("54OzK8");
    apiInfo.setArea("北京");
    apiInfo.setAge(122);
    apiInfo.setName("张三");
    apiInfo.setPendingConsultationLevel(ConsultationLevel.COMMONLY);
    apiInfo.setRiskFactor(List.of(RiskFactor.CORONARY_HEART_DISEASE));
    apiInfo.setSex(Gender.FEMALE);
    ConsultationPatientListApiResponse apiResponse = new ConsultationPatientListApiResponse();
    apiResponse.setTotal(10);
    apiResponse.setPatients(List.of(apiInfo));

    contextMock.withUid(10037l);

    given(userService.getPatientByConsultationDoctor(anyLong(), anyInt(), anyInt()))
        .willReturn(apiResponse);
    this.mockMvc
        .perform(MockMvcRequestBuilders.get("/consultation/patients")
            .param("pageAt", "1")
            .param("pageSize", "10")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(print())
        .andDo(document("get_consultation_patients",
            requestParameters(
                parameterWithName("pageAt").description("页码"),
                parameterWithName("pageSize").description("分页条数")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data.total").description("总条数"),
                fieldWithPath("data.patients[].id").description("患者id"),
                fieldWithPath("data.patients[].name").description("患者名称"),
                fieldWithPath("data.patients[].area").description("地区"),
                fieldWithPath("data.patients[].age").description("年龄"),
                fieldWithPath("data.patients[].sex").description("性别"),
                fieldWithPath("data.patients[].pendingConsultationLevel")
                    .description("会诊问题疾病: - ORDINARY: 普通问题，COMMONLY: 一般问题，URGENT: 紧急问题"),
                fieldWithPath("data.patients[].riskFactor").description("风险因素")
            )));
  }

  @Test
  public void testGetConsultationDoctors() throws Exception {

    DoctorListResponse response = new DoctorListResponse();
    List<Doctor> doctors = new ArrayList<>();
    Doctor doctor = new Doctor();
    doctor.setId("54OzK8");
    doctor.setStatus(UserStatus.NORMAL);
    doctor.setAvatar("url");
    doctor.setName("张三");
    doctor.setTitle(DoctorTitle.ATTENDING_PHYSICIAN);
    doctor.setTel("100000000");
    doctor.setSex(UserGender.FEMALE);
    doctor.setBiography("这是简介");
    doctor.setExperience("经历丰富");
    doctor.setDepartmentName("心内科");
    doctor.setExpertise("治病救人");
    doctor.setMeetingLecture("医学课程");
    doctor.setHospitalIntroduction("医院");
    doctor.setFirstProfessionCompany("第一职业单位");
    doctor.setCertificates(certs);
    doctors.add(doctor);
    response.setDoctors(doctors);
    response.setTotal(10000);

    given(userService
        .getConsultationDoctor(any(String.class), any(Long.class), any(Long.class), any(Long.class),
            any(Integer.class), any(Integer.class))).willReturn(response);
    this.contextMock.withUid(mockUserId);

    this.mockMvc
        .perform(MockMvcRequestBuilders.get("/consultation/doctor")
            .param("doctorName", "杨医生")
            .param("organizationId", "8POLbl")
            .param("departmentId", "lQvRX8")
            .param("pageAt", "0")
            .param("pageSize", "15")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(print())
        .andDo(document("get_consultation_doctor_list",
            requestParameters(
                parameterWithName("doctorName").description("医生名称"),
                parameterWithName("organizationId").description("机构id"),
                parameterWithName("departmentId").description("部门id"),
                parameterWithName("pageAt").description("页码"),
                parameterWithName("pageSize").description("页数")),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("结果数据"),
                fieldWithPath("data.doctors[].id").description("数据id"),
                fieldWithPath("data.doctors[].tel").description("电话号码"),
                fieldWithPath("data.doctors[].name").description("姓名"),
                fieldWithPath("data.doctors[].title").description(
                    "职称: CHIEF_PHYSICIAN-主任医师，ASSOCIATE_CHIEF_PHYSICIAN-副主任医师，ATTENDING_PHYSICIAN-主治医师, RESIDENT_PHYSICIAN - 住院医师"),
                fieldWithPath("data.doctors[].sex").description("性别: MALE/FEMALE"),
                fieldWithPath("data.doctors[].status")
                    .description("状态：NORMAL - 正常，PENDING - 未修改密码,TO_REVIEW-待审核,FAILURE-审核失败"),
                fieldWithPath("data.doctors[].avatar").description("头像"),
                fieldWithPath("data.doctors[].expertise").description("擅长"),
                fieldWithPath("data.doctors[].biography").description("简介"),
                fieldWithPath("data.doctors[].experience").description("经历"),
                fieldWithPath("data.doctors[].meetingLecture").description("会议与研究"),
                fieldWithPath("data.doctors[].hospitalIntroduction").description("所属医院"),
                fieldWithPath("data.doctors[].firstProfessionCompany").description("第一职业单位"),
                fieldWithPath("data.doctors[].certificates[].imageIds[]").description("证书图片ID"),
                fieldWithPath("data.doctors[].certificates[].type").description("证书类型"),
                fieldWithPath("data.doctors[].departmentName")
                    .description("科室名"),
                fieldWithPath("data.total").description("数据总量")

            )))
        .andDo(print())
        .andReturn();
  }

  @Test
  public void testGetOperatorOrganization() throws Exception {

    Institution institution = new Institution();
    institution.setId(HashUtils.encode(1000L));
    institution.setName("第一医院");
    contextMock.withUid(10037L);

    given(userService.getAllOperatorOrganization(anyLong()))
        .willReturn(new InstitutionResponse(List.of(institution)));
    this.mockMvc
        .perform(MockMvcRequestBuilders.get("/user/operator/institution"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(print())
        .andDo(document("get_operator_institution",
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data.institutions[].id").description("机构id"),
                fieldWithPath("data.institutions[].name").description("机构名称")
            )));
  }

  @Test
  public void testAddFutureDoctor() throws Exception {

    doNothing().when(userService).addFutureDoctor(anyLong(), any(AddFutureDoctorApiRequest.class));

    AddFutureDoctorApiRequest futureDoctorRequest = new AddFutureDoctorApiRequest();
    futureDoctorRequest.setExperience("一等奖");
    futureDoctorRequest.setAvatar("头像路径");
    futureDoctorRequest.setBiography("主治医师");
    futureDoctorRequest.setDepartment("内科");
    futureDoctorRequest.setExpertise("个人经历");
    futureDoctorRequest.setHospitalIntroduction("协和医院");
    futureDoctorRequest.setMeetingLecture("会议与讲课");
    futureDoctorRequest.setName("张三");
    futureDoctorRequest.setSex(UserGender.FEMALE);
    futureDoctorRequest.setTitle(DoctorTitle.ATTENDING_PHYSICIAN);
    futureDoctorRequest.setType(UserFutureDoctorType.INTERNAL);
    futureDoctorRequest.setHospitalName("协和医院");
    this.mockMvc.perform(post("/future/doctor")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(futureDoctorRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("add_future_doctor",
            requestFields(
                fieldWithPath("avatar").description("头像路径"),
                fieldWithPath("name").description("姓名"),
                fieldWithPath("sex").description("性别  "
                    + "FEMALE(0, \"女\"),\n"
                    + "\n"
                    + "  MALE(1, \"男\"),\n"
                    + "\n"
                    + "  OTHER(2, \"其他\");"),
                fieldWithPath("title").description("职称"),
                fieldWithPath("department").description("科室"),
                fieldWithPath("biography").description("个人简介"),
                fieldWithPath("expertise").description("擅长领域"),
                fieldWithPath("experience").description("科研成果"),
                fieldWithPath("meetingLecture").description("会议与讲课"),
                fieldWithPath("hospitalIntroduction").description("所在医院"),
                fieldWithPath("type").description("类型   "
                    + "INTERNAL(1, \"内科\"),\n"
                    + "\n"
                    + "SURGERY(2, \"外科\");"),
                fieldWithPath("hospitalName").description("医院名称")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("无")
            )
        ))
        .andDo(print())
        .andReturn();
  }

  @Test
  public void testUpdateFutureDoctor() throws Exception {

    UpdateFutureDoctorApiRequest request = new UpdateFutureDoctorApiRequest();

    request.setId("rkZyL5");
    request.setExperience("一等奖");
    request.setAvatar("头像路径");
    request.setBiography("主治医师");
    request.setDepartment("内科");
    request.setExpertise("个人经历");
    request.setHospitalIntroduction("协和医院");
    request.setMeetingLecture("会议与讲课");
    request.setName("张三");
    request.setSex(UserGender.FEMALE);
    request.setTitle(DoctorTitle.ATTENDING_PHYSICIAN);
    request.setType(UserFutureDoctorType.INTERNAL);
    request.setHospitalName("阜外医院");
    FutureDoctorApiInfo futureDoctorInfo = new FutureDoctorApiInfo();
    given(userService.updateFutureDoctor(anyLong(), any(UpdateFutureDoctorApiRequest.class)))
        .willReturn(futureDoctorInfo);

    this.mockMvc.perform(patch("/future/doctor")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("update_future_doctor",
            requestFields(
                fieldWithPath("id").description("未来医生Id"),
                fieldWithPath("avatar").description("头像路径"),
                fieldWithPath("name").description("姓名"),
                fieldWithPath("sex").description("性别  "
                    + "FEMALE(0, \"女\"),\n"
                    + "\n"
                    + "  MALE(1, \"男\"),\n"
                    + "\n"
                    + "  OTHER(2, \"其他\");"),
                fieldWithPath("title").description("职称"),
                fieldWithPath("department").description("科室"),
                fieldWithPath("biography").description("个人简介"),
                fieldWithPath("expertise").description("擅长领域"),
                fieldWithPath("experience").description("科研成果"),
                fieldWithPath("meetingLecture").description("会议与讲课"),
                fieldWithPath("hospitalIntroduction").description("所在医院"),
                fieldWithPath("type").description("类型   "
                    + "INTERNAL(1, \"内科\"),\n"
                    + "\n"
                    + "SURGERY(2, \"外科\");"),
                fieldWithPath("hospitalName").description("医院名称")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data.id").description("未来医生Id"),
                fieldWithPath("data.doctorId").description("医生Id"),
                fieldWithPath("data.avatar").description("头像路径"),
                fieldWithPath("data.name").description("姓名"),
                fieldWithPath("data.sex").description("性别  "
                    + "FEMALE(0, \"女\"),\n"
                    + "\n"
                    + "  MALE(1, \"男\"),\n"
                    + "\n"
                    + "  OTHER(2, \"其他\");"),
                fieldWithPath("data.title").description("职称"),
                fieldWithPath("data.department").description("科室"),
                fieldWithPath("data.biography").description("个人简介"),
                fieldWithPath("data.expertise").description("擅长领域"),
                fieldWithPath("data.experience").description("科研成果"),
                fieldWithPath("data.meetingLecture").description("会议与讲课"),
                fieldWithPath("data.hospitalIntroduction").description("所在医院"),
                fieldWithPath("data.type").description("类型   "
                    + "INTERNAL(1, \"内科\"),\n"
                    + "\n"
                    + "SURGERY(2, \"外科\");"),
                fieldWithPath("data.hospitalName").description("医院名称")
            )
        ))
        .andDo(print())
        .andReturn();
  }

  @Test
  public void testGetFutureDoctorByDoctorId() throws Exception {

    FutureDoctorApiInfo futureDoctorInfo = new FutureDoctorApiInfo();

    futureDoctorInfo.setId("rkZyL5");
    futureDoctorInfo.setExperience("一等奖");
    futureDoctorInfo.setAvatar("头像路径");
    futureDoctorInfo.setBiography("主治医师");
    futureDoctorInfo.setDepartment("内科");
    futureDoctorInfo.setDoctorId("ldVDV5");
    futureDoctorInfo.setExpertise("个人经历");
    futureDoctorInfo.setHospitalIntroduction("协和医院");
    futureDoctorInfo.setMeetingLecture("会议与讲课");
    futureDoctorInfo.setName("张三");
    futureDoctorInfo.setSex(UserGender.FEMALE);
    futureDoctorInfo.setTitle(DoctorTitle.ATTENDING_PHYSICIAN);
    futureDoctorInfo.setType(UserFutureDoctorType.INTERNAL);
    futureDoctorInfo.setHospitalName("妇幼保健院");

    given(userService.getFutureDoctorByDoctorId(anyLong(), any(FutureDoctorType.class)))
        .willReturn(futureDoctorInfo);

    mockMvc.perform(RestDocumentationRequestBuilders.get("/future/doctor")
        .param("doctorId", "10521").param("type", "SURGERY"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("get_future_doctor",
            requestParameters(
                parameterWithName("doctorId").description("医生Id"),
                parameterWithName("type").description("类别 "
                    + "INTERNAL(1, \"内科\"),\n"
                    + "\n"
                    + "  SURGERY(2, \"外科\");")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data.id").description("未来医生Id"),
                fieldWithPath("data.doctorId").description("医生Id"),
                fieldWithPath("data.avatar").description("头像路径"),
                fieldWithPath("data.name").description("姓名"),
                fieldWithPath("data.sex").description("性别  "
                    + "FEMALE(0, \"女\"),\n"
                    + "\n"
                    + "  MALE(1, \"男\"),\n"
                    + "\n"
                    + "  OTHER(2, \"其他\");"),
                fieldWithPath("data.title").description("职称"),
                fieldWithPath("data.department").description("科室"),
                fieldWithPath("data.biography").description("个人简介"),
                fieldWithPath("data.expertise").description("擅长领域"),
                fieldWithPath("data.experience").description("科研成果"),
                fieldWithPath("data.meetingLecture").description("会议与讲课"),
                fieldWithPath("data.hospitalIntroduction").description("所在医院"),
                fieldWithPath("data.type").description("类型   "
                    + "INTERNAL(1, \"内科\"),\n"
                    + "\n"
                    + "SURGERY(2, \"外科\");"),
                fieldWithPath("data.hospitalName").description("医院名称")
            )));
  }
}
