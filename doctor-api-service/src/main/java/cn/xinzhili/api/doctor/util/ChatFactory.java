package cn.xinzhili.api.doctor.util;

import cn.xinzhili.api.doctor.bean.ChatSessionInfo;
import cn.xinzhili.api.doctor.bean.GroupMember;
import cn.xinzhili.api.doctor.bean.MessageApiInfo;
import cn.xinzhili.api.doctor.bean.MessageDetailInfo;
import cn.xinzhili.api.doctor.bean.UnreadCountInfo;
import cn.xinzhili.api.doctor.bean.request.CleanUnreadApiRequest;
import cn.xinzhili.api.doctor.bean.request.SendMessageRequest;
import cn.xinzhili.api.doctor.bean.request.UpdateAssistantChatStatusRequest;
import cn.xinzhili.api.doctor.bean.request.UpdateUnreadStatusApiRequest;
import cn.xinzhili.api.doctor.bean.response.ChatSessionApiResponse;
import cn.xinzhili.api.doctor.bean.response.MessageListResponse;
import cn.xinzhili.api.doctor.bean.response.UnreadApiResponse;
import cn.xinzhili.api.doctor.bean.response.UnreadCountApiResponse;
import cn.xinzhili.chat.api.MessageInfo;
import cn.xinzhili.chat.api.MessageSessionInfo;
import cn.xinzhili.chat.api.RoleType;
import cn.xinzhili.chat.api.SessionInfo;
import cn.xinzhili.chat.api.UserStatus;
import cn.xinzhili.chat.api.request.AddMessageRequest;
import cn.xinzhili.chat.api.request.CleanUnreadRequest;
import cn.xinzhili.chat.api.request.UpdateAssistantStatusRequest;
import cn.xinzhili.chat.api.request.UpdateUnreadStatusRequest;
import cn.xinzhili.chat.api.response.MessageResponse;
import cn.xinzhili.chat.api.response.SessionResponse;
import cn.xinzhili.chat.api.response.UnreadResponse;
import cn.xinzhili.chat.api.response.UnreadResponse.MedicalUnreadCount;
import cn.xinzhili.xutils.core.util.HashUtils;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ChatFactory {

  public static ChatSessionApiResponse sessionToChatSession(SessionResponse sessionResponse) {
    ChatSessionApiResponse response = new ChatSessionApiResponse();
    List<ChatSessionInfo> chatSessionInfos = sessionResponse.getSession().stream()
        .map(ChatFactory::sessionInfoToChatSessionInfo)
        .collect(Collectors.toList());
    response.setSessions(chatSessionInfos);
    return response;
  }

  private static ChatSessionInfo sessionInfoToChatSessionInfo(SessionInfo sessionInfo) {
    ChatSessionInfo chatSessionInfo = new ChatSessionInfo();
    chatSessionInfo.setSessionId(HashUtils.encode(sessionInfo.getId()));
    chatSessionInfo.setGroupType(sessionInfo.getType());
    chatSessionInfo.setInitiator(HashUtils.encode(sessionInfo.getInitiatorId()));
    chatSessionInfo.setOrganizationId(HashUtils.encode(sessionInfo.getOrganizationId()));
    chatSessionInfo.setSessionType(sessionInfo.getSessionType());
    chatSessionInfo
        .setMembers(sessionInfo.getMessageSessionInfos().stream().map(
            ChatFactory::messageSessionToGroupMember).collect(Collectors.toList()));
    return chatSessionInfo;
  }

  private static GroupMember messageSessionToGroupMember(MessageSessionInfo messageSessionInfo) {
    GroupMember groupMember = new GroupMember();
    groupMember.setAvatar(messageSessionInfo.getAvatar());
    groupMember.setCountShow(messageSessionInfo.getUnreadStatus());
    groupMember.setName(messageSessionInfo.getName());
    groupMember.setRole(messageSessionInfo.getRoleType());
    groupMember.setUserId(HashUtils.encode(messageSessionInfo.getUserId()));
    groupMember.setUserStatus(messageSessionInfo.getUserStatus());
    return groupMember;
  }

  public static AddMessageRequest of(SendMessageRequest request) {
    AddMessageRequest addMessageRequest = new AddMessageRequest();
    addMessageRequest.setSessionId(HashUtils.decode(request.getSessionId()));
    addMessageRequest.setAvatar(request.getAvatar());
    addMessageRequest.setName(request.getName());
    addMessageRequest.setSenderId(HashUtils.decode(request.getSender()));
    addMessageRequest.setSenderRoleType(request.getSenderRoleType());
    addMessageRequest.setOrganizationId(HashUtils.decode(request.getOrganizationId()));
    addMessageRequest.setReceiverId(
        Objects.isNull(request.getReceiver()) ? null : HashUtils.decode(request.getReceiver()));
    addMessageRequest.setContent(request.getMessage().getContent());
    addMessageRequest.setMessageType(request.getMessage().getType());
    return addMessageRequest;
  }

  public static MessageListResponse api(MessageResponse response) {
    MessageListResponse messageListResponse = new MessageListResponse();
    messageListResponse.setMessageDetail(response.getMessageInfos().stream().map(ChatFactory
        ::api).collect(Collectors.toList()));
    messageListResponse.setTotal(response.getTotal());
    return messageListResponse;
  }

  public static MessageDetailInfo api(MessageInfo info) {
    MessageDetailInfo messageDetailInfo = new MessageDetailInfo();
    messageDetailInfo.setSessionId(HashUtils.encode(info.getSessionId()));
    messageDetailInfo.setSender(HashUtils.encode(info.getSenderId()));
    messageDetailInfo.setSenderRoleType(info.getSenderRoleType());
    messageDetailInfo.setAvatar(info.getAvatar());
    messageDetailInfo.setName(info.getName());
    messageDetailInfo.setReceiver(
        Objects.isNull(info.getReceiverId()) ? null : HashUtils.encode(info.getReceiverId()));
    messageDetailInfo.setSendTime(info.getCreatedAt());
    MessageApiInfo message = new MessageApiInfo();
    message.setContent(info.getContent());
    message.setType(info.getMessageType());
    messageDetailInfo.setMessage(message);
    return messageDetailInfo;
  }

  public static UpdateUnreadStatusRequest of(UpdateUnreadStatusApiRequest apiRequest, Long userId) {
    UpdateUnreadStatusRequest request = new UpdateUnreadStatusRequest();
    request.setUserId(userId);
    request.setRoleType(apiRequest.getRoleType());
    request.setSessionId(HashUtils.decode(apiRequest.getSessionId()));
    request.setUnreadStatus(apiRequest.getUnreadStatus());
    return request;
  }

  public static UnreadApiResponse api(UnreadResponse response) {
    UnreadApiResponse apiResponse = new UnreadApiResponse();
    apiResponse.setUnreadCount(response.getPatientUnreadCount());
    return apiResponse;
  }

  public static UnreadCountApiResponse apiToUnreadResponse(UnreadResponse response) {
    UnreadCountApiResponse apiResponse = new UnreadCountApiResponse();
    apiResponse.setPatientUnreadCount(response.getPatientUnreadCount());
    apiResponse.setOtherUnreadCount(response.getOtherUnreadCount());
    apiResponse.setMedicalUnreadCounts(
        response.getMedicalUnreadCounts().stream().map(ChatFactory::toUnreadCountInfo).collect(
            Collectors.toList()));
    return apiResponse;
  }

  public static UnreadCountInfo toUnreadCountInfo(MedicalUnreadCount medicalUnreadCount) {
    UnreadCountInfo unreadCountInfo = new UnreadCountInfo();
    unreadCountInfo.setSenderId(HashUtils.encode(medicalUnreadCount.getSenderId()));
    unreadCountInfo.setUnreadCount(medicalUnreadCount.getUnreadCount());
    return unreadCountInfo;
  }

  public static CleanUnreadRequest toClearReuqest(CleanUnreadApiRequest request, RoleType roleType,
      Long userId) {
    CleanUnreadRequest unreadRequest = new CleanUnreadRequest();
    unreadRequest.setRoleType(roleType);
    unreadRequest.setSessionId(HashUtils.decode(request.getSessionId()));
    unreadRequest.setUserId(userId);
    unreadRequest.setReceiverId(
        Objects.nonNull(request.getSenderId()) ? HashUtils.decode(request.getSenderId()) : null);
    return unreadRequest;
  }

  public static UpdateAssistantStatusRequest toAssistantRequest(
      UpdateAssistantChatStatusRequest request, Long userId) {
    UpdateAssistantStatusRequest statusRequest = new UpdateAssistantStatusRequest();
    statusRequest.setAssistantId(HashUtils.decode(request.getAssistantId()));
    statusRequest.setDoctorId(userId);
    statusRequest.setUserStatus(UserStatus.valueOf(request.getStatus().toString()));
    return statusRequest;
  }
}
