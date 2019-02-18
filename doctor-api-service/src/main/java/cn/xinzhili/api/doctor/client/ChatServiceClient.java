package cn.xinzhili.api.doctor.client;

import cn.xinzhili.chat.api.RoleType;
import cn.xinzhili.chat.api.Type;
import cn.xinzhili.chat.api.request.AddMessageRequest;
import cn.xinzhili.chat.api.request.CleanUnreadRequest;
import cn.xinzhili.chat.api.request.UpdateAssistantStatusRequest;
import cn.xinzhili.chat.api.request.UpdateUnreadStatusRequest;
import cn.xinzhili.chat.api.response.MemberStatusResponse;
import cn.xinzhili.chat.api.response.MessageResponse;
import cn.xinzhili.chat.api.response.SessionResponse;
import cn.xinzhili.chat.api.response.UnreadResponse;
import cn.xinzhili.chat.api.response.UnreadStatusResponse;
import cn.xinzhili.xutils.core.http.Response;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("chat-service")
public interface ChatServiceClient {

  @RequestMapping(method = RequestMethod.GET, value = "/message/session")
  Response<SessionResponse> getSessions(@RequestParam("currentUserId") Long currentUserId,
      @RequestParam("currentType") RoleType currentType,
      @RequestParam("initiatorId") Long initiatorId,
      @RequestParam("initiatorRoleType") RoleType initiatorRoleType,
      @RequestParam(value = "type", required = false) Type type);

  @RequestMapping(method = RequestMethod.POST, value = "/message",
      consumes = MediaType.APPLICATION_JSON_VALUE)
  Response sendMessage(@RequestBody AddMessageRequest request);

  @RequestMapping(method = RequestMethod.GET, value = "/message")
  Response<MessageResponse> getMessages(@RequestParam("sessionId") Long sessionId,
      @RequestParam(value = "senderId", required = false) Long senderId,
      @RequestParam(value = "pageAt", defaultValue = "1") int pageAt,
      @RequestParam(value = "pageSize", defaultValue = "15") int pageSize,
      @RequestParam(value = "toUserId", required = false) Long toUserId);

  @RequestMapping(method = RequestMethod.PATCH, value = "/message/unread/status",
      consumes = MediaType.APPLICATION_JSON_VALUE)
  Response changeCountStatus(@RequestBody UpdateUnreadStatusRequest request);

  @RequestMapping(method = RequestMethod.GET, value = "/message/unread")
  Response<UnreadResponse> getUserUnread(
      @RequestParam("initiatorId") Long initiatorId,
      @RequestParam(value = "initiatorRoleType", defaultValue = "DOCTOR") RoleType initiatorRoleType,
      @RequestParam("userId") Long userId,
      @RequestParam("roleType") RoleType roleType);

  @RequestMapping(method = RequestMethod.PATCH, value = "/message/clean",
      consumes = MediaType.APPLICATION_JSON_VALUE)
  Response cleanUnreadByUser(@RequestBody CleanUnreadRequest request);

  @RequestMapping(method = RequestMethod.PATCH, value = "/message/assistant/status",
      consumes = MediaType.APPLICATION_JSON_VALUE)
  Response updateAssistantStatus(UpdateAssistantStatusRequest updateAssistantStatusRequest);

  @GetMapping("/message/assistant/status")
  Response<MemberStatusResponse> getAssistantStatus(@RequestParam("doctorId") Long doctorId,
      @RequestParam("assistantId") Long assistantId);

  @GetMapping("/message/unread/status")
  Response<UnreadStatusResponse> getUnreadStatus(@RequestParam("initiatorId") Long initiatorId,
      @RequestParam("initiatorRoleType") RoleType initiatorRoleType,
      @RequestParam("type") Type type,
      @RequestParam("userId") Long userId, @RequestParam("roleType") RoleType roleType);
}
