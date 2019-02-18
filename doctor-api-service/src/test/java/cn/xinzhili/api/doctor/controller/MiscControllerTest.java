package cn.xinzhili.api.doctor.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyInt;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cn.xinzhili.api.doctor.common.ContextMock;
import cn.xinzhili.api.doctor.config.ConfigConsts;
import cn.xinzhili.api.doctor.service.UserService;
import cn.xinzhili.user.api.RegionInfo;
import cn.xinzhili.xutils.spring.interceptor.ExceptionResolver;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.map.ObjectMapper;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Created by marlinl on 29/03/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class MiscControllerTest {

  @Rule
  public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(
      "build/generated-snippets");

  private MockMvc mockMvc;

  @InjectMocks
  private MiscController miscController;

  @InjectMocks
  private ExceptionResolver exceptionResolver;

  @Mock
  private UserService userService;

  private Long mockUserId = 66666L;

  private ObjectMapper mapper;

  private ContextMock contextMock = new ContextMock().withUid(mockUserId);

  @Before
  public void setup(){
    this.mockMvc = MockMvcBuilders
        .standaloneSetup(this.miscController)
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
  public void testGetRegionInfo() throws Exception {
    List<RegionInfo> list = new ArrayList<>();
    int index = 0;
    while (index < 5) {
      RegionInfo info = new RegionInfo();
      info.setId(1);
      info.setRegionName("北京市");
      list.add(info);
      index++;
    }
    given(userService.getRegionList(anyInt()))
        .willReturn(list);
    this.mockMvc.perform(get(ConfigConsts.prefixWithContext("/region"))
        .contextPath(ConfigConsts.API_CONTEXT)
        .param("pid", "0"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("get_patient_region",
            requestParameters(
                parameterWithName("pid").description("省市县需要查询的id，当pid是0，则查询所有省级单位，当返回为空则没有下一级了")
            ),
            responseFields(
                fieldWithPath("status").description("返回状态"),
                fieldWithPath("data.regions[]").description("省市县的数组"),
                fieldWithPath("data.regions[].id").description("省市县的id"),
                fieldWithPath("data.regions[].regionName").description("省市县名称 ")
            )));
  }

}
