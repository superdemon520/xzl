package cn.xinzhili.api.doctor.controller;

import static cn.xinzhili.api.doctor.config.ConfigConsts.prefixWithContext;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cn.xinzhili.api.doctor.bean.response.MedicineExternalDataResponse;
import cn.xinzhili.api.doctor.bean.response.MedicineUnionDataResponse;
import cn.xinzhili.api.doctor.common.ContextMock;
import cn.xinzhili.api.doctor.config.ConfigConsts;
import cn.xinzhili.api.doctor.service.MedicineService;
import cn.xinzhili.medicine.api.ManualInfo;
import cn.xinzhili.xutils.core.http.Response;
import cn.xinzhili.xutils.spring.interceptor.ExceptionResolver;
import java.util.List;
import java.util.Optional;
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
 * Created by @xin.
 */
@RunWith(MockitoJUnitRunner.class)
public class MedicineControllerTests {

  @Rule
  public JUnitRestDocumentation restDocumentation =
      new JUnitRestDocumentation("build/generated-snippets");

  private MockMvc mockMvc;

  @InjectMocks
  private MedicineController medicineController;

  @InjectMocks
  private ExceptionResolver exceptionResolver;

  @Mock
  private MedicineService medicineService;

  private ContextMock contextMock = new ContextMock();


  @Before
  public void setUp() {

    MockitoAnnotations.initMocks(this);
    this.mockMvc = MockMvcBuilders
        .standaloneSetup(this.medicineController)
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
  public void testGetMedicineByKey() throws Exception {
    contextMock.withUid(111L);

    given(medicineService.getMedicineByKey(anyString())).willReturn(Response.instanceSuccess(
        MedicineExternalDataResponse.builder()
            .medicines(List.of(MedicineUnionDataResponse.builder().name("阿司匹林").id("O8K3y8").build()))
            .build()));

    mockMvc
        .perform(get(prefixWithContext("/medicine/{name}"), "阿司")
            .contentType(MediaType.APPLICATION_JSON)
            .contextPath(ConfigConsts.API_CONTEXT))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("get_medicine_by_key",
            pathParameters(
                parameterWithName("name").description("药品名")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data.medicines[]").description("药品列表"),
                fieldWithPath("data.medicines[].id").description("药品 id"),
                fieldWithPath("data.medicines[].name").description("药品名")
            )
        ))
        .andDo(print())
        .andReturn();
  }

  @Test
  public void testGetManualByMedicineIds() throws Exception {
    contextMock.withUid(111L);

    ManualInfo manualInfo = ManualInfo.builder().adverseReaction("不良反应").consideration("注意事项")
        .contraindication("禁忌").dosage("用法用量").drugInteraction("药物相互作用").indication("适应症")
        .medicineId(222L).pharmaco("药理作用").specialCrowd("特殊人群").build();
    given(medicineService.getManualByMedicineId(anyString()))
        .willReturn(Optional.ofNullable(manualInfo));
    mockMvc
        .perform(get(prefixWithContext("/medicine/manual/{medicineId}"), "8XRGor")
            .contentType(MediaType.APPLICATION_JSON)
            .contextPath(ConfigConsts.API_CONTEXT))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("get_manual_by_medicine_ids",
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data.manualInfo").description("药品说明书"),
                fieldWithPath("data.manualInfo.medicineId").description("药品id"),
                fieldWithPath("data.manualInfo.indication").description("适应症"),
                fieldWithPath("data.manualInfo.dosage").description("用法用量"),
                fieldWithPath("data.manualInfo.adverseReaction").description("不良反应"),
                fieldWithPath("data.manualInfo.contraindication").description("禁忌"),
                fieldWithPath("data.manualInfo.consideration").description("注意事项"),
                fieldWithPath("data.manualInfo.specialCrowd").description("特殊人群"),
                fieldWithPath("data.manualInfo.drugInteraction").description("药物相互作用"),
                fieldWithPath("data.manualInfo.pharmaco").description("药理作用")
            )
        ))
        .andDo(print())
        .andReturn();
  }
}
