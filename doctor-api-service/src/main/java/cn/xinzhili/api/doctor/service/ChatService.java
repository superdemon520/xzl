package cn.xinzhili.api.doctor.service;

import cn.xinzhili.api.doctor.bean.UserRole;
import cn.xinzhili.api.doctor.bean.request.CleanUnreadApiRequest;
import cn.xinzhili.api.doctor.bean.request.SendMessageRequest;
import cn.xinzhili.api.doctor.bean.request.UpdateAssistantChatStatusRequest;
import cn.xinzhili.api.doctor.bean.request.UpdateUnreadStatusApiRequest;
import cn.xinzhili.api.doctor.bean.response.ChatSessionApiResponse;
import cn.xinzhili.api.doctor.bean.response.MessageListResponse;
import cn.xinzhili.api.doctor.bean.response.UnreadCountApiResponse;
import cn.xinzhili.api.doctor.client.ChatServiceClient;
import cn.xinzhili.api.doctor.util.AuthUtils;
import cn.xinzhili.api.doctor.util.ChatFactory;
import cn.xinzhili.chat.api.RoleType;
import cn.xinzhili.chat.api.Type;
import cn.xinzhili.chat.api.UnreadStatus;
import cn.xinzhili.chat.api.UserStatus;
import cn.xinzhili.chat.api.response.MemberStatusResponse;
import cn.xinzhili.chat.api.response.MessageResponse;
import cn.xinzhili.chat.api.response.SessionResponse;
import cn.xinzhili.chat.api.response.UnreadResponse;
import cn.xinzhili.chat.api.response.UnreadStatusResponse;
import cn.xinzhili.user.api.StaffRole;
import cn.xinzhili.xutils.core.ErrorCode;
import cn.xinzhili.xutils.core.FailureException;
import cn.xinzhili.xutils.core.http.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ChatService {

  @Autowired
  private ChatServiceClient chatServiceClient;

  public ChatSessionApiResponse getChatSession(Long userId, Long patientId) {
    Response<SessionResponse> sessions = chatServiceClient
        .getSessions(userId, getRoleType(), patientId, RoleType.PATIENT, null);
    if (sessions.isSuccessful()) {
      SessionResponse sessionResponse = sessions.getDataAs(SessionResponse.class);
      return ChatFactory.sessionToChatSession(sessionResponse);
    } else {
      log.warn("get sessions failed, patientId:{}, doctorId:{}, role:{}", patientId, userId,
          getRoleType());
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    }
  }


  public Response sendMessage(SendMessageRequest request) {
    Response response = chatServiceClient.sendMessage(ChatFactory.of(request));
    if (response.isSuccessful()) {
      return Response.instanceSuccess();
    } else {
      return Response.instanceFail();
    }
  }

  public MessageListResponse getMessages(Long userId, Long senderId, long sessionId, int pageAt,
      int pageSize) {
    Response<MessageResponse> messages = chatServiceClient
        .getMessages(sessionId, senderId, pageAt, pageSize, userId);
    if (messages.isSuccessful()) {
      return ChatFactory.api(messages.getDataAs(MessageResponse.class));
    } else {
      log.warn("get messages failed, senderId:{}, userId:{}, sessionId:{}", senderId, userId,
          sessionId);
      throw new FailureException(ErrorCode.INVALID_PARAMS);
    }
  }

  public Response changCount(Long userId, UpdateUnreadStatusApiRequest request) {
    Response response = chatServiceClient.changeCountStatus(ChatFactory.of(request, userId));
    if (response.isSuccessful()) {
      return Response.instanceSuccess();
    } else {
      return Response.instanceFail();
    }
  }

  public UnreadCountApiResponse getUnreadCount(Long userId, long patientId) {
    RoleType roleType = getRoleType();
    Response<UnreadResponse> userUnread = chatServiceClient
        .getUserUnread(patientId, RoleType.PATIENT, userId, roleType);
    if (userUnread.isSuccessful()) {
      return ChatFactory.apiToUnreadResponse(userUnread.getDataAs(UnreadResponse.class));
    } else {
      log.warn("get unread count failed, userId:{}, patientId:{}", userId, patientId);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    }
  }

  private RoleType getRoleType() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    RoleType currentUserType;
    if (AuthUtils.isUserOfRole(UserRole.DOCTOR, authentication)) {
      currentUserType = RoleType.DOCTOR;
    } else if (AuthUtils.isUserOfRole(UserRole.ASSISTANT, authentication)) {
      currentUserType = RoleType.ASSISTANT;
    } else {
      currentUserType = RoleType.OPERATOR;
    }
    return currentUserType;
  }

  public Response clearUnread(CleanUnreadApiRequest request, Long userId) {
    RoleType roleType = getRoleType();
    Response response = chatServiceClient
        .cleanUnreadByUser(ChatFactory.toClearReuqest(request, roleType, userId));
    if (response.isSuccessful()) {
      return Response.instanceSuccess();
    } else {
      log.warn("清理未读数异常，userId: {}", userId);
      return Response.instanceFail(ErrorCode.REQUEST_FAILED);
    }
  }

  public Response updateAssistantStatus(UpdateAssistantChatStatusRequest request, Long userId) {
    Response response = chatServiceClient
        .updateAssistantStatus(ChatFactory.toAssistantRequest(request, userId));
    if (response.isSuccessful()) {
      return Response.instanceSuccess();
    } else {
      log.warn("修改医助状态失败 doctorId:{}, assistantId:{}", userId, request.getAssistantId());
      return Response.instanceFail(ErrorCode.REQUEST_FAILED);
    }
  }

  public UserStatus getAssistantChatStatus(long doctorId, long assistantId) {
    Response<MemberStatusResponse> response = chatServiceClient
        .getAssistantStatus(doctorId, assistantId);
    if (response.isSuccessful()) {
      MemberStatusResponse statusResponse = response.getDataAs(MemberStatusResponse.class);
      return statusResponse.getAssistantStatus();
    } else {
      log.warn("获取assistant状态失败, doctorId:{}, assistantId:{}", doctorId, assistantId);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    }
  }

  public UnreadStatus getUnreadStatusByPatient(long initiatorId, Long userId, StaffRole role) {
    Response<UnreadStatusResponse> unreadStatus = chatServiceClient
        .getUnreadStatus(initiatorId, RoleType.PATIENT, Type.GROUP_ALL, userId,
            RoleType.valueOf(role.toString()));
    if (unreadStatus.isSuccessful()) {
      return unreadStatus.getDataAs(UnreadStatusResponse.class).getUnreadStatus();
    } else {
      log.warn("获取显示未读数显示状态数据失败，initiatorId：{}，currentUserId:{},role:{}", initiatorId, userId,
          role);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    }

  }
}
