package cn.xinzhili.api.doctor.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cn.xinzhili.api.doctor.bean.CountAdvicesApiInfo;
import cn.xinzhili.api.doctor.bean.StatisticAdviceDateType;
import cn.xinzhili.api.doctor.bean.DepartmentCountPatientApiInfo;
import cn.xinzhili.api.doctor.bean.DepartmentImagesApiInfo;
import cn.xinzhili.api.doctor.bean.DepartmentAdvicesApiInfo;
import cn.xinzhili.api.doctor.bean.DepartmentStandardApiInfo;
import cn.xinzhili.api.doctor.bean.StandardRateApiInfo;
import cn.xinzhili.api.doctor.bean.response.StatisticsImagesApiResponse;
import cn.xinzhili.api.doctor.bean.response.StatisticsAdvicesApiResponse;
import cn.xinzhili.api.doctor.bean.response.StatisticsPatientsApiResponse;
import cn.xinzhili.api.doctor.bean.response.StatisticsStandardApiResponse;
import cn.xinzhili.api.doctor.common.ContextMock;
import cn.xinzhili.api.doctor.config.ConfigConsts;
import cn.xinzhili.api.doctor.service.DpcService;
import cn.xinzhili.api.doctor.service.MedicalService;
import cn.xinzhili.api.doctor.service.UserService;
import cn.xinzhili.xutils.spring.interceptor.ExceptionResolver;
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
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/4/11 上午10:19
 */
@RunWith(MockitoJUnitRunner.class)
public class StatisticsControllerTest {

  @Rule
  public JUnitRestDocumentation restDocumentation =
      new JUnitRestDocumentation("build/generated-snippets");

  @InjectMocks
  private StatisticsController statisticsController;

  @InjectMocks
  private ExceptionResolver exceptionResolver;

  private MockMvc mockMvc;

  private Long mockUserId = 66666L;

  private ContextMock contextMock = new ContextMock().withUid(mockUserId);

  private ObjectMapper mapper;

  @Mock
  private UserService userService;

  @Mock
  private MedicalService medicalService;
  @Mock
  private DpcService dpcService;

  @Before
  public void setUp() {
    this.mockMvc = MockMvcBuilders
        .standaloneSetup(this.statisticsController)
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
  public void testGetStatisticsPatients() throws Exception {

    StatisticsPatientsApiResponse response = new StatisticsPatientsApiResponse();
    DepartmentCountPatientApiInfo info = new DepartmentCountPatientApiInfo();
    info.setName("心之力全科");
    info.setPatientCount(20);
    response.setDepartmentCountPatientInfos(List.of(info));
    given(userService.getStatisticsPatients(anyString(), anyLong()
    )).willReturn(response);

    this.mockMvc
        .perform(MockMvcRequestBuilders.get("/statistics/department/patients")
            .param("organizationId", "8XBZKr")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("get_statistics_patients",
            requestParameters(
                parameterWithName("organizationId").description("机构id")),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data.departmentCountPatientInfos[].name").description("部门名称"),
                fieldWithPath("data.departmentCountPatientInfos[].patientCount").description("患者人数")
            )))
        .andDo(print())
        .andReturn();
  }

  @Test
  public void testGetDepartmentImages() throws Exception {

    StatisticsImagesApiResponse response = new StatisticsImagesApiResponse();
    DepartmentImagesApiInfo info = new DepartmentImagesApiInfo();
    info.setDepartmentName("心之力全科");
    info.setCountImages(20);
    response.setCountImagesInfos(List.of(info));
    given(medicalService.getDepartmentImages(anyString())).willReturn(response);

    this.mockMvc
        .perform(MockMvcRequestBuilders.get("/statistics/department/images")
            .param("organizationId", "8XBZKr")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("get_statistics_images",
            requestParameters(
                parameterWithName("organizationId").description("机构id")),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data.countImagesInfos[].departmentName").description("部门名称"),
                fieldWithPath("data.countImagesInfos[].countImages").description("图片数量")
            )))
        .andDo(print())
        .andReturn();
  }

  @Test
  public void testGetDepartmentStandardRates() throws Exception {

    StatisticsStandardApiResponse response = new StatisticsStandardApiResponse();
    DepartmentStandardApiInfo info = new DepartmentStandardApiInfo();
    info.setDepartmentName("心之力全科");
    StandardRateApiInfo standardRateInfo = new StandardRateApiInfo();
    standardRateInfo.setInspectionName("血压");
    standardRateInfo.setStandardRate("88.00");
    info.setStandardRateInfos(List.of(standardRateInfo));
    response.setDepartmentStandardInfos(List.of(info));
    given(medicalService.getDepartmentStandardRates(anyString()
    )).willReturn(response);

    this.mockMvc
        .perform(MockMvcRequestBuilders.get("/statistics/department/standard/rate")
            .param("organizationId", "10010")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("get_statistics_standard_rate",
            requestParameters(
                parameterWithName("organizationId").description("机构id")),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data.departmentStandardInfos[].departmentName")
                    .description("部门名称"),
                fieldWithPath("data.departmentStandardInfos[].standardRateInfos[].inspectionName")
                    .description("统计维度"),
                fieldWithPath("data.departmentStandardInfos[].standardRateInfos[].standardRate")
                    .description("各个维度下达标率")
            )))
        .andDo(print())
        .andReturn();
  }

  @Test
  public void getDoctorWorkload() throws Exception {

    StatisticsAdvicesApiResponse response = new StatisticsAdvicesApiResponse();
    DepartmentAdvicesApiInfo info = new DepartmentAdvicesApiInfo();
    info.setDepartmentName("心之力全科");
    CountAdvicesApiInfo countAdvicesApiInfo = new CountAdvicesApiInfo();
    countAdvicesApiInfo.setAdvicesTypeName("系统处理提示");
    countAdvicesApiInfo.setCountAdvices(10086);
    info.setCountAdvicesInfos(List.of(countAdvicesApiInfo));
    response.setDepartmentAdvicesInfos(List.of(info));
    given(dpcService.getDepartmentAdvices(anyString(), any(StatisticAdviceDateType.class)
    )).willReturn(response);

    this.mockMvc
        .perform(MockMvcRequestBuilders.get("/statistics/department/workload")
            .param("organizationId", "10010")
            .param("dateType", "ONE_MONTH")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("get_statistics_workload",
            requestParameters(
                parameterWithName("organizationId").description("机构id"),
                parameterWithName("dateType")
                    .description(
                        "统计日期类型（ONE_MONTH 一个月、THREE_MONTH 三个月、HALF_YEAR 半年、 ONE_YEAR 一年）")),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data.departmentAdvicesInfos[].departmentName")
                    .description("部门名称"),
                fieldWithPath("data.departmentAdvicesInfos[].countAdvicesInfos[].advicesTypeName")
                    .description("消息维度"),
                fieldWithPath("data.departmentAdvicesInfos[].countAdvicesInfos[].countAdvices")
                    .description("消息个数")
            )))
        .andDo(print())
        .andReturn();
  }

}
