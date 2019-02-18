package cn.xinzhili.api.doctor.controller;

import static cn.xinzhili.api.doctor.config.ConfigConsts.prefixWithContext;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cn.xinzhili.api.doctor.bean.Department;
import cn.xinzhili.api.doctor.bean.Institution;
import cn.xinzhili.api.doctor.bean.User;
import cn.xinzhili.api.doctor.bean.UserRole;
import cn.xinzhili.api.doctor.bean.request.SendInquiryRequest;
import cn.xinzhili.api.doctor.bean.response.UserDetailResponse;
import cn.xinzhili.api.doctor.common.ContextMock;
import cn.xinzhili.api.doctor.config.ConfigConsts;
import cn.xinzhili.api.doctor.service.DpcService;
import cn.xinzhili.api.doctor.service.NotifyService;
import cn.xinzhili.api.doctor.service.UserService;
import cn.xinzhili.xutils.core.util.HashUtils;
import cn.xinzhili.xutils.spring.interceptor.ExceptionResolver;
import java.util.Collections;
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

@RunWith(MockitoJUnitRunner.class)
public class NotificationControllerTest {

  @Rule
  public JUnitRestDocumentation restDocumentation
      = new JUnitRestDocumentation("build/generated-snippets");

  private MockMvc mockMvc;

  @InjectMocks
  private NotificationController notificationController;

  @InjectMocks
  private ExceptionResolver exceptionResolver;
  @Mock
  private UserService userService;
  @Mock
  private DpcService dpcService;
  @Mock
  private NotifyService notifyService;

  private ObjectMapper mapper;

  private Long mockUserId = 66666L;

  private ContextMock contextMock = new ContextMock().withUid(mockUserId)
      .withUsername("hahahah").withRole("ROLE_" + UserRole.ASSISTANT);

  @Before
  public void setUp() {

    mapper = new ObjectMapper();

    this.mockMvc = MockMvcBuilders
        .standaloneSetup(this.notificationController)
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

  }

  @Test
  public void testSendInquiries() throws Exception {

    SendInquiryRequest request = new SendInquiryRequest();
    request.setOrganizationId(HashUtils.encode(1000L));

    UserDetailResponse response = new UserDetailResponse();
    response.setInstitution(new Institution());
    response.setUser(new User());
    response.setDepartment(new Department());
    given(userService.getUserDetails(anyLong(), anyLong(), anyBoolean())).willReturn(response);
    given(userService.getManagedPatientIds(any(UserRole.class), anyLong()))
        .willReturn(Collections.singletonList(1000L));

    doNothing().when(dpcService).sendInquiriesSystemMessage(anyLong(), anyListOf(Long.class));
    doNothing().when(notifyService).pushInquiries(anyListOf(Long.class));

    this.mockMvc
        .perform(post(prefixWithContext("/inquiry"))
            .contextPath(ConfigConsts.API_CONTEXT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(request))
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("send_inquiry",
            requestFields(
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
}
