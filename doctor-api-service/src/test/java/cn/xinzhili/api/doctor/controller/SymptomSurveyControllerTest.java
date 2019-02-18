package cn.xinzhili.api.doctor.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cn.xinzhili.api.doctor.config.ConfigConsts;
import cn.xinzhili.api.doctor.service.SymptomSurveyService;
import cn.xinzhili.medicine.api.response.SymptomSurveyListResponse;
import cn.xinzhili.medicine.api.response.SymptomSurveyResponse;
import cn.xinzhili.xutils.spring.interceptor.ExceptionResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Created by ywb on 13/9/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class SymptomSurveyControllerTest {

  @Rule
  public JUnitRestDocumentation restDocumentation =
      new JUnitRestDocumentation("build/generated-snippets");


  private MockMvc mockMvc;

  private ObjectMapper mapper;


  @InjectMocks
  private ExceptionResolver exceptionResolver;


  @InjectMocks
  private SymptomSurveyController symptomSurveyController;

  @Mock
  private SymptomSurveyService symptomSurveyService;


  @Before
  public void setUp() {

    mapper = new ObjectMapper();

    MockitoAnnotations.initMocks(this);
    this.mockMvc = MockMvcBuilders
        .standaloneSetup(this.symptomSurveyController)
        .setControllerAdvice(exceptionResolver)
        .apply(MockMvcRestDocumentation
            .documentationConfiguration(restDocumentation))
        .build();
  }


  @Test
  public void testGetSymptomSurvey() throws Exception {
    List<SymptomSurveyResponse> surveyResponses = Collections
        .singletonList(new SymptomSurveyResponse("阿莫西林", Collections.singletonList("背痛")));
    SymptomSurveyListResponse response = new SymptomSurveyListResponse(surveyResponses, new Date());
    given(symptomSurveyService.find(any(Long.class), anyListOf(Long.class)))
        .willReturn(response);

    mockMvc.perform(
        RestDocumentationRequestBuilders.get("/api/v0/doctor//patient/{patientId}/symptom/survey", "5bey28")
            .param("ids","1000,1100")
            .contextPath(ConfigConsts.API_CONTEXT))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("get_symptom_survey",
            pathParameters(
                parameterWithName("patientId").description("患者id")
            ),

            responseFields(
                fieldWithPath("status").description("返回结果"),
                fieldWithPath("data.symptomSurveyResponses[]").description("药品症状列表"),
                fieldWithPath("data.symptomSurveyResponses[].medicineName").description("药品名"),
                fieldWithPath("data.symptomSurveyResponses[].content[]").description("症状列表"),
                fieldWithPath("data.createdAt").description("创建时间")
            )));

  }


}
