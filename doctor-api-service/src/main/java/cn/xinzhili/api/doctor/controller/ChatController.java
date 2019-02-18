package cn.xinzhili.api.doctor.controller;

import cn.xinzhili.api.doctor.bean.request.CleanUnreadApiRequest;
import cn.xinzhili.api.doctor.bean.request.SendMessageRequest;
import cn.xinzhili.api.doctor.bean.request.UpdateAssistantChatStatusRequest;
import cn.xinzhili.api.doctor.bean.request.UpdateUnreadStatusApiRequest;
import cn.xinzhili.api.doctor.service.ChatService;
import cn.xinzhili.xutils.auth.CurrentUserId;
import cn.xinzhili.xutils.core.http.Response;
import cn.xinzhili.xutils.core.util.HashUtils;
import java.util.Objects;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat/session")
@Slf4j
public class ChatController {

  @Autowired
  private ChatService chatService;

  @PreAuthorize("hasAnyRole('DOCTOR','ASSISTANT','OPERATOR')")
  @GetMapping
  public Response getChatSession(@CurrentUserId Long userId,
      @RequestParam("patientId") String patientId) {
    return Response
        .instanceSuccess(chatService.getChatSession(userId, HashUtils.decode(patientId)));
  }


  @PreAuthorize("hasAnyRole('DOCTOR','ASSISTANT','OPERATOR')")
  @GetMapping("/message")
  public Response getChatList(@CurrentUserId Long userId,
      @RequestParam(value = "senderId", required = false) String senderId,
      @RequestParam("sessionId") String sessionId,
      @RequestParam(value = "pageAt", defaultValue = "1") int pageAt,
      @RequestParam(value = "pageSize", defaultValue = "15") int pageSize) {
    return Response.instanceSuccess(
        chatService
            .getMessages(userId, Objects.isNull(senderId) ? null : HashUtils.decode(senderId),
                HashUtils.decode(sessionId), pageAt, pageSize));
  }

  @PreAuthorize("hasAnyRole('DOCTOR','ASSISTANT','OPERATOR')")
  @PostMapping("/message")
  public Response sendMessage(@RequestBody @Valid SendMessageRequest request) {
    return chatService.sendMessage(request);
  }

  @PreAuthorize("hasAnyRole('DOCTOR','ASSISTANT','OPERATOR')")
  @PatchMapping("/count/change")
  public Response changeCountShow(@CurrentUserId Long userId,
      @RequestBody @Valid UpdateUnreadStatusApiRequest request) {
    return chatService.changCount(userId, request);
  }

  @PreAuthorize("hasAnyRole('DOCTOR','ASSISTANT','OPERATOR')")
  @GetMapping("/unread")
  public Response getUnreadCount(@CurrentUserId Long userId,
      @RequestParam("patientId") String patientId) {
    return Response
        .instanceSuccess(chatService.getUnreadCount(userId, HashUtils.decode(patientId)));
  }

  @PreAuthorize("hasAnyRole('DOCTOR','ASSISTANT','OPERATOR')")
  @PatchMapping("/clean")
  public Response clearUnread(@CurrentUserId Long userId,
      @RequestBody CleanUnreadApiRequest request) {
    return chatService.clearUnread(request, userId);
  }

  @PreAuthorize("hasAnyRole('DOCTOR')")
  @PatchMapping("/assistant/status")
  public Response updateAssistantStatus(@CurrentUserId Long userId,
      @RequestBody UpdateAssistantChatStatusRequest request) {
    return chatService.updateAssistantStatus(request, userId);
  }


}
