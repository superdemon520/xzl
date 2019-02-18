package cn.xinzhili.api.doctor.controller;

import static cn.xinzhili.api.doctor.config.ConfigConsts.prefixWithContext;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cn.xinzhili.api.doctor.bean.ChatSessionInfo;
import cn.xinzhili.api.doctor.bean.GroupMember;
import cn.xinzhili.api.doctor.bean.MessageApiInfo;
import cn.xinzhili.api.doctor.bean.MessageDetailInfo;
import cn.xinzhili.api.doctor.bean.UnreadCountInfo;
import cn.xinzhili.api.doctor.bean.UserChatStatus;
import cn.xinzhili.api.doctor.bean.request.CleanUnreadApiRequest;
import cn.xinzhili.api.doctor.bean.request.SendMessageRequest;
import cn.xinzhili.api.doctor.bean.request.UpdateAssistantChatStatusRequest;
import cn.xinzhili.api.doctor.bean.request.UpdateUnreadStatusApiRequest;
import cn.xinzhili.api.doctor.bean.response.ChatSessionApiResponse;
import cn.xinzhili.api.doctor.bean.response.MessageListResponse;
import cn.xinzhili.api.doctor.bean.response.UnreadCountApiResponse;
import cn.xinzhili.api.doctor.common.ContextMock;
import cn.xinzhili.api.doctor.config.ConfigConsts;
import cn.xinzhili.api.doctor.service.ChatService;
import cn.xinzhili.chat.api.MessageType;
import cn.xinzhili.chat.api.RoleType;
import cn.xinzhili.chat.api.SessionType;
import cn.xinzhili.chat.api.Type;
import cn.xinzhili.chat.api.UnreadStatus;
import cn.xinzhili.chat.api.UserStatus;
import cn.xinzhili.xutils.core.http.Response;
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
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * @author by Loki on 17/3/8.
 */
@RunWith(MockitoJUnitRunner.class)
public class ChatControllerTest {


  @Rule
  public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(
      "build/generated-snippets");

  private MockMvc mockMvc;

  @InjectMocks
  private ChatController chatController;

  @InjectMocks
  private ExceptionResolver exceptionResolver;

  @Mock
  private ChatService chatService;

  private ObjectMapper mapper;

  private Long mockUserId = 66666L;

  private ContextMock contextMock = new ContextMock().withUid(mockUserId);

  @Before
  public void setUp() {
    this.mockMvc = MockMvcBuilders
        .standaloneSetup(this.chatController)
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
  public void testGetChatSession() throws Exception {

    ChatSessionApiResponse response = new ChatSessionApiResponse();
    ChatSessionInfo chatSessionInfo = new ChatSessionInfo();
    chatSessionInfo.setSessionId("dfefef");
    chatSessionInfo.setGroupType(Type.GROUP_ALL);
    chatSessionInfo.setSessionType(SessionType.GROUP);
    chatSessionInfo.setOrganizationId("fefefe");
    chatSessionInfo.setInitiator("fdkjfke");
    GroupMember groupMember = new GroupMember();
    groupMember.setUserStatus(UserStatus.NORMAL);
    groupMember.setUserId("1dfef");
    groupMember.setRole(RoleType.DOCTOR);
    groupMember.setName("name");
    groupMember.setCountShow(UnreadStatus.NONE);
    groupMember.setAvatar("http://dfjekfje");
    chatSessionInfo.setMembers(List.of(groupMember));
    response.setSessions(List.of(chatSessionInfo));

    given(chatService.getChatSession(anyLong(), anyLong()))
        .willReturn(response);

    this.mockMvc
        .perform(MockMvcRequestBuilders.get("/chat/session")
            .param("patientId", "5Z7g7r")
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("get_chat_session",
            requestParameters(
                parameterWithName("patientId").description("患者id")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("结果数据"),
                fieldWithPath("data.sessions[].sessionId").description("sessionId"),
                fieldWithPath("data.sessions[].organizationId").description("机构id"),
                fieldWithPath("data.sessions[].sessionType")
                    .description("会话类型  GROUP 群聊  SINGLE 单聊 SYSTEM_PUSH 系统推送 "),
                fieldWithPath("data.sessions[].groupType")
                    .description("群聊类型  GROUP_ALL 包含患者群组  GROUP_ORG  不包含患者群组"),
                fieldWithPath("data.sessions[].initiator").description("创建人id"),
                fieldWithPath("data.sessions[].members[]").description("组员信息"),
                fieldWithPath("data.sessions[].members[].name").description("组员姓名"),
                fieldWithPath("data.sessions[].members[].avatar").description("组员头像路径"),
                fieldWithPath("data.sessions[].members[].userId").description("组员id"),
                fieldWithPath("data.sessions[].members[].role")
                    .description("组员角色  DOCTOR 医生 ASSISTANT 小医生 OPERATOR 运营 PATIENT 患者"),
                fieldWithPath("data.sessions[].members[].userStatus")
                    .description("用户状态 NORMAL 正常 PROHIBITION_OF_SPEECH 禁言 INVALID 失效"),
                fieldWithPath("data.sessions[].members[].countShow")
                    .description("显示消息数量  BLOCK 正常显示  NONE 显示小红点")
            )))
        .andDo(print())
        .andReturn();
  }


  @Test
  public void testChangeCountShow() throws Exception {
    UpdateUnreadStatusApiRequest request = new UpdateUnreadStatusApiRequest();
    request.setSessionId("fjefje");
    request.setUnreadStatus(UnreadStatus.BLOCK);
    request.setRoleType(RoleType.DOCTOR);

    given(chatService.changCount(anyLong(), any(UpdateUnreadStatusApiRequest.class)))
        .willReturn(Response.instanceSuccess());

    this.mockMvc
        .perform(patch(prefixWithContext("/chat/session/count/change"))
            .contextPath(ConfigConsts.API_CONTEXT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("update_count_show",
            requestFields(
                fieldWithPath("sessionId").description("sessionId"),
                fieldWithPath("roleType")
                    .description("组员角色  DOCTOR 医生 ASSISTANT 小医生 OPERATOR 运营 PATIENT 患者"),
                fieldWithPath("unreadStatus").description("显示消息数量  BLOCK 正常显示  NONE 显示小红点")
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
  public void TestSendMessage() throws Exception {
    SendMessageRequest request = new SendMessageRequest();
    request.setAvatar(
        "https://s3.cn-north-1.amazonaws.com.cn/staff-avatars-dev/3e590749-ce18-431a-9328-7c8330421120");
    request.setName("愚人节哈哈哈");
    request.setOrganizationId("olLDlK");
    request.setSender("54O2j8");
    request.setReceiver("54O2j8");
    request.setSenderRoleType(RoleType.PATIENT);
    request.setSessionId("l9vXV8");
    MessageApiInfo message = new MessageApiInfo();
    message.setContent("消息内容");
    message.setType(MessageType.TEXT);
    request.setMessage(message);
    given(chatService.sendMessage(any(SendMessageRequest.class)))
        .willReturn(Response.instanceSuccess());
    this.mockMvc
        .perform(post("/chat/session/message")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("send_message",
            requestFields(
                fieldWithPath("organizationId").description("机构id"),
                fieldWithPath("avatar").description("头像地址"),
                fieldWithPath("name").description("姓名"),
                fieldWithPath("sender").description("发送人id"),
                fieldWithPath("senderRoleType")
                    .description("发送人角色 PATIENT|患者 OPERATOR|运营 ASSISTANT|医助 DOCTOR|医生"),
                fieldWithPath("receiver").description("收消息人id 如在患者群组中发消息则不传值"),
                fieldWithPath("sessionId").description("会话id"),
                fieldWithPath("message.type").description("消息类别 TEXT 文本 TIPS 提示消息  IMAGE  图片消息"),
                fieldWithPath("message.content").description("消息内容")
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
  public void testGetMessages() throws Exception {
    MessageListResponse response = new MessageListResponse();
    MessageDetailInfo info = new MessageDetailInfo();
    info.setSessionId("l9vXV8");
    info.setName("愚人节哈哈哈");
    info.setAvatar(
        "https://s3.cn-north-1.amazonaws.com.cn/staff-avatars-dev/3e590749-ce18-431a-9328-7c8330421120");
    info.setSender("54O2j8");
    info.setSenderRoleType(RoleType.DOCTOR);
    info.setReceiver("54O2j8");
    info.setSendTime(1525943772533l);
    MessageApiInfo message = new MessageApiInfo();
    message.setContent("阿斯顿发生");
    message.setType(MessageType.TEXT);
    info.setMessage(message);
    response.setMessageDetail(List.of(info));
    response.setTotal(34);

    given(chatService.getMessages(anyLong(), anyLong(), anyLong(), anyInt(), anyInt()))
        .willReturn(response);
    this.contextMock.withUid(111L);

    this.mockMvc
        .perform(get("/chat/session/message")
            .param("sessionId", "l9vXV8")
            .param("senderId", "l9vXV8")
            .param("pageAt", "1")
            .param("pageSize", "15")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("get_chat_session_message",
            requestParameters(
                parameterWithName("sessionId").description("会话id"),
                parameterWithName("pageAt").description("页码［默认1］"),
                parameterWithName("pageSize").description("每页条数"),
                parameterWithName("senderId").description("发送人id  如果拉取上帝视角消息 此参数不传值")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data.messageDetail").description("消息list"),
                fieldWithPath("data.messageDetail[].avatar").description("头像地址"),
                fieldWithPath("data.messageDetail[].name").description("姓名"),
                fieldWithPath("data.messageDetail[].sender")
                    .description("发送人"),
                fieldWithPath("data.messageDetail[].senderRoleType")
                    .description("发送人角色 PATIENT|患者 OPERATOR|运营 ASSISTANT|医助 DOCTOR|医生"),
                fieldWithPath("data.messageDetail[].receiver").description("消息接收者 有患者的会话值为null"),
                fieldWithPath("data.messageDetail[].sessionId").description("会话id"),
                fieldWithPath("data.messageDetail[].message.type")
                    .description("消息类型 TEXT|文本 IMAGE|图片（预留）"),
                fieldWithPath("data.messageDetail[].message.content").description("消息内容"),
                fieldWithPath("data.messageDetail[].sendTime").description("消息发送时间戳"),
                fieldWithPath("data.total").description("消息总数")

            )
        ))
        .andDo(print())
        .andReturn();
  }


  @Test
  public void testGetUnreadCount() throws Exception {
    UnreadCountApiResponse response = new UnreadCountApiResponse();
    response.setOtherUnreadCount(0);
    response.setPatientUnreadCount(123);
    UnreadCountInfo unreadCountInfo = new UnreadCountInfo();
    unreadCountInfo.setSenderId("sdafdf");
    unreadCountInfo.setUnreadCount(23);
    response.setMedicalUnreadCounts(List.of(unreadCountInfo, unreadCountInfo));

    given(chatService.getUnreadCount(anyLong(), anyLong()))
        .willReturn(response);

    this.mockMvc
        .perform(get("/chat/session/unread")
            .param("patientId", "l9vXV8")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("get_chat_unread_count",
            requestParameters(
                parameterWithName("patientId").description("患者d")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data.patientUnreadCount").description("患者群组未读数"),
                fieldWithPath("data.otherUnreadCount").description("上帝视角未读数"),
                fieldWithPath("data.medicalUnreadCounts[].senderId").description("发送人Id"),
                fieldWithPath("data.medicalUnreadCounts[].unreadCount").description("未读数")

            )
        ))
        .andDo(print())
        .andReturn();
  }

  @Test
  public void testCleanUnread() throws Exception {
    CleanUnreadApiRequest request = new CleanUnreadApiRequest();
    request.setSenderId("l9vXV8");
    request.setSessionId("l9vXV8");

    given(chatService.clearUnread(any(CleanUnreadApiRequest.class), anyLong()))
        .willReturn(Response.instanceSuccess());

    this.mockMvc
        .perform(patch(prefixWithContext("/chat/session/clean"))
            .contextPath(ConfigConsts.API_CONTEXT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("clean_unread_count",
            requestFields(
                fieldWithPath("sessionId").description("sessionId"),
                fieldWithPath("senderId").description("聊天对象id")
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
  public void testUpdateAssistantStatus() throws Exception {
    UpdateAssistantChatStatusRequest request = new UpdateAssistantChatStatusRequest();
    request.setAssistantId("l9vXV8");
    request.setStatus(UserChatStatus.NORMAL);

    given(chatService.updateAssistantStatus(any(UpdateAssistantChatStatusRequest.class), anyLong()))
        .willReturn(Response.instanceSuccess());

    this.mockMvc
        .perform(patch(prefixWithContext("/chat/session/assistant/status"))
            .contextPath(ConfigConsts.API_CONTEXT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("update_assistant_stauts",
            requestFields(
                fieldWithPath("assistantId").description("医助id"),
                fieldWithPath("status")
                    .description("医助要修改为的状态枚举 NORMAL 正常 PROHIBITION_OF_SPEECH 禁言")
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
