package cn.xinzhili.api.doctor.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;

import cn.xinzhili.api.doctor.config.ConfigConsts;
import cn.xinzhili.api.doctor.service.LifeStatusService;
import cn.xinzhili.medical.api.LifeStatusInfo;
import cn.xinzhili.medical.api.LivingHabitType;
import cn.xinzhili.medical.api.lifestatus.diet.DietHabit;
import cn.xinzhili.xutils.spring.interceptor.ExceptionResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Created by ywb on 18/4/2017.
 */
@RunWith(MockitoJUnitRunner.class)

public class LifeStatusControllerTest {

  @Rule
  public JUnitRestDocumentation restDocumentation =
      new JUnitRestDocumentation("build/generated-snippets");

  private MockMvc mockMvc;

  private ObjectMapper mapper;

  @InjectMocks
  private LifeStatusController lifeStatusController;

  @InjectMocks
  private ExceptionResolver exceptionResolver;

  @Mock
  private LifeStatusService lifeStatusService;

  @Before
  public void setUp() {

    mapper = new ObjectMapper();

    MockitoAnnotations.initMocks(this);
    this.mockMvc = MockMvcBuilders
        .standaloneSetup(this.lifeStatusController)
        .setControllerAdvice(exceptionResolver)
        .apply(MockMvcRestDocumentation
            .documentationConfiguration(restDocumentation))
        .build();
  }

  @Test
  public void testGetLifeStatus() throws Exception {
    List<LifeStatusInfo> records = new ArrayList<>();
    LifeStatusInfo lifeStatusInfo = new LifeStatusInfo();
    lifeStatusInfo.setPatientId(1L);
    lifeStatusInfo.setType(LivingHabitType.SPORT);
    lifeStatusInfo.setCreatedAt(new Date());
    lifeStatusInfo.setContent(new DietHabit());
    records.add(lifeStatusInfo);
    given(lifeStatusService.getLatestLifeStatusByPatientid(any(Long.class)))
        .willReturn(records);
    this.mockMvc
        .perform(
            get(("/api/v0/doctor/life/status/{patientId}"), "olLDlK")
                .contextPath(ConfigConsts.API_CONTEXT))
        .andDo(print())


        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("get_life_status",
            pathParameters(
                parameterWithName("patientId").description("患者id")
            ),
            responseFields(
                fieldWithPath("status").description("返回结果"),
                fieldWithPath("data.records[].patientId").description("患者id"),
                fieldWithPath("data.records[].type").description("生活习惯类型：SPORT，DIET,SMOKE,DRINk"),
                fieldWithPath("data.records[].content")
                    .description("具体内容，参见患者端api：post life/status/diet,post life/status/sport" ),
                fieldWithPath("data.records[].createdAt").description("创建时间")
                )))
        .andDo(print())
        .andReturn();


  }


}
