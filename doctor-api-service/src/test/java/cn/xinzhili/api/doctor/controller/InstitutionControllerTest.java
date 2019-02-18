package cn.xinzhili.api.doctor.controller;

import static cn.xinzhili.api.doctor.config.ConfigConsts.prefixWithContext;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cn.xinzhili.api.doctor.bean.Department;
import cn.xinzhili.api.doctor.bean.DepartmentDetail;
import cn.xinzhili.api.doctor.bean.Institution;
import cn.xinzhili.api.doctor.bean.InstitutionDetail;
import cn.xinzhili.api.doctor.bean.request.HospitalDepartmentRequest;
import cn.xinzhili.api.doctor.bean.response.InstitutionDetailResponse;
import cn.xinzhili.api.doctor.bean.response.InstitutionResponse;
import cn.xinzhili.api.doctor.common.ContextMock;
import cn.xinzhili.api.doctor.config.ConfigConsts;
import cn.xinzhili.api.doctor.service.UserService;
import cn.xinzhili.xutils.core.http.Response;
import cn.xinzhili.xutils.spring.interceptor.ExceptionResolver;
import java.util.Arrays;
import java.util.Collections;
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
 * Created by MarlinL on 16/02/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class InstitutionControllerTest {

  @Rule
  public JUnitRestDocumentation restDocumentation =
      new JUnitRestDocumentation("build/generated-snippets");

  private MockMvc mockMvc;

  @InjectMocks
  private InstitutionController institutionController;

  private ObjectMapper mapper;


  @InjectMocks
  private ExceptionResolver exceptionResolver;

  @Mock
  private UserService userService;


  private ContextMock contextMock = new ContextMock();

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    mapper = new ObjectMapper();
    this.mockMvc = MockMvcBuilders
        .standaloneSetup(this.institutionController)
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
  public void testGetHospitalInfo() throws Exception {

    InstitutionDetail institution = new InstitutionDetail();
    institution.setLogoUrl("http://www.baidu.com");
    institution.setIntroduction("不知道");
    institution.setAddress("四环内");
    institution.setName("星星医院");
    institution.setId("asSNGssada");

    DepartmentDetail department = new DepartmentDetail();
    department.setId("ApjWjn");
    department.setName("门诊");
    department.setPatientCount(2);
    department.setDoctorCount(2);
    department.setAssistantCount(3);
    department.setOperatorCount(22);

    InstitutionDetailResponse mock = new InstitutionDetailResponse();
    mock.setInstitution(institution);
    mock.setDepartments(Collections.singletonList(department));

    given(userService.getOrgInfoOfUser(any(String.class), any(Long.class)))
        .willReturn(mock);

    mockMvc.perform(get(prefixWithContext("/institution/{id}"), "kaldjkla")
        .contextPath(ConfigConsts.API_CONTEXT))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("get_institution_detail",
            pathParameters(
                parameterWithName("id").description("医院id")
            ),
            responseFields(
                fieldWithPath("status").description("返回结果"),
                fieldWithPath("data.institution").description("返回结果机构"),
                fieldWithPath("data.institution.id").description("医院id"),
                fieldWithPath("data.institution.name").description("医院名称"),
                fieldWithPath("data.institution.logoUrl").description("医院logo地址"),
                fieldWithPath("data.institution.introduction").description("医院简介"),
                fieldWithPath("data.institution.address").description("医院地址"),
                fieldWithPath("data.departments[]").description("医院部门"),
                fieldWithPath("data.departments[].id").description("部门id"),
                fieldWithPath("data.departments[].name").description("部门名称"),
                fieldWithPath("data.departments[].assistantCount").description("医助数量"),
                fieldWithPath("data.departments[].doctorCount").description("医生数量"),
                fieldWithPath("data.departments[].patientCount").description("患者数量"),
                fieldWithPath("data.departments[].operatorCount").description("运营数量")
            )));
  }


  @Test
  public void testGetHospital() throws Exception {

    Institution institution1 = new Institution();
    institution1.setName("星星医院");
    institution1.setId("asSNGssada");
    Institution institution2 = new Institution();
    institution2.setName("月亮医院");
    institution2.setId("sssssHHddd");

    InstitutionResponse mock = new InstitutionResponse();
    mock.setInstitutions(Arrays.asList(institution1, institution2));

    given(userService.getOrganization(any(Integer.class), any(Integer.class)))
        .willReturn(mock);

    mockMvc.perform(get(prefixWithContext("/institution"))
        .param("pageAt", "10")
        .param("pageSize", "15")
        .contextPath(ConfigConsts.API_CONTEXT))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("get_hospital",
            requestParameters(
                parameterWithName("pageAt").description("页码(可选):分页参数不传 则返回所有"),
                parameterWithName("pageSize").description("每页数(可选)")
            ),
            responseFields(
                fieldWithPath("status").description("返回结果"),
                fieldWithPath("data.institutions[]").description("返回结果机构"),
                fieldWithPath("data.institutions[].id").description("医院id"),
                fieldWithPath("data.institutions[].name").description("医院名称")
            )));
  }

  @Test
  public void testAddHospitalDepartment() throws Exception {
    Department mock = new Department();
    mock.setName("心血管");
    mock.setId("sajk_s");
    HospitalDepartmentRequest request = new HospitalDepartmentRequest();
    request.setName("心血管");
    given(userService.addOrgDepartment(any(String.class), any(String.class),
        anyLong()))
        .willReturn(mock);

    mockMvc.perform(post(prefixWithContext("/institution/{id}/department"), "akdjklaS")
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
        .content(mapper.writeValueAsBytes(request))
        .contextPath(ConfigConsts.API_CONTEXT))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("add_hospital_department",
            pathParameters(
                parameterWithName("id").description("医院id")
            ),
            requestFields(
                fieldWithPath("name").description("添加的部门名称")
            ),
            responseFields(
                fieldWithPath("status").description("返回结果"),
                fieldWithPath("data.id").description("返回部门id"),
                fieldWithPath("data.name").description("返回部门名称")
            )));
  }

  @Test
  public void testRenameHospitalDepartment() throws Exception {
    HospitalDepartmentRequest request = new HospitalDepartmentRequest();
    request.setName("心血管");

    given(userService.renameOrgDepartment(any(String.class), any(String.class), anyLong()))
        .willReturn(true);

    mockMvc.perform(patch(prefixWithContext("/institution/department/{id}"), "Jsnba")
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
        .content(mapper.writeValueAsBytes(request))
        .contextPath(ConfigConsts.API_CONTEXT))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("rename_hospital_department",
            pathParameters(
                parameterWithName("id").description("部门id")
            ),
            requestFields(
                fieldWithPath("name").description("要修改为的部门名称")
            ),
            responseFields(
                fieldWithPath("status").description("返回结果"),
                fieldWithPath("data").description("为空")
            )));

  }

  @Test
  public void testDeleteDepartment() throws Exception {
    given(userService.deleteDepartment(anyLong(), anyString()))
        .willReturn(Response.instanceSuccess());

    mockMvc.perform(delete(ConfigConsts.prefixWithContext("/institution/department/{id}"), "1")
        .contextPath(ConfigConsts.API_CONTEXT))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("delete_department",
            pathParameters(
                parameterWithName("id").description("机构id")
            ),
            responseFields(
                fieldWithPath("status").description("返回结果"),
                fieldWithPath("data").description("数据payload")
            )));
  }
}
