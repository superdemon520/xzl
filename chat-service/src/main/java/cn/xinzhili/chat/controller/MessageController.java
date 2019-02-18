package cn.xinzhili.chat.controller;


import cn.xinzhili.chat.api.RoleType;
import cn.xinzhili.chat.api.UnreadStatus;
import cn.xinzhili.chat.api.UserStatus;
import cn.xinzhili.chat.api.Type;
import cn.xinzhili.chat.api.request.AddMessageRequest;
import cn.xinzhili.chat.api.request.CleanUnreadRequest;
import cn.xinzhili.chat.api.request.UpdateUnreadStatusRequest;
import cn.xinzhili.chat.api.request.UpdateAssistantStatusRequest;
import cn.xinzhili.chat.api.response.MemberStatusResponse;
import cn.xinzhili.chat.api.response.SessionResponse;
import cn.xinzhili.chat.api.response.MessageResponse;
import cn.xinzhili.chat.api.response.UnreadResponse;
import cn.xinzhili.chat.config.AppConfiguration;
import cn.xinzhili.chat.config.MqttConfiguration;
import cn.xinzhili.chat.model.MemberStatus;
import cn.xinzhili.chat.model.Session;
import cn.xinzhili.chat.service.GroupMemberDetailService;
import cn.xinzhili.chat.service.InitService;
import cn.xinzhili.chat.service.MessageService;
import cn.xinzhili.chat.service.MessageSessionService;
import cn.xinzhili.chat.service.RedisService;
import cn.xinzhili.chat.service.SessionService;
import cn.xinzhili.chat.service.UserService;
import cn.xinzhili.chat.service.mqtt.MessageHandlerService;
import cn.xinzhili.chat.service.notify.NotifyService;
import cn.xinzhili.chat.util.MqttMessageFactory;
import cn.xinzhili.chat.util.RedisKeyFactory;
import cn.xinzhili.chat.util.TopicFactory;
import cn.xinzhili.xutils.core.http.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/message")
@Api(value = "消息接口", description = "消息聊天接口")
@Slf4j
public class MessageController {

  @Autowired
  private InitService initService;

  @Autowired
  private MessageHandlerService messageHandlerService;

  @Autowired
  private MessageService messageService;

  @Autowired
  private MessageSessionService messageSessionService;

  @Autowired
  private UserService userService;

  @Autowired
  private SessionService sessionService;

  @Autowired
  private AppConfiguration appConfiguration;

  @Autowired
  private GroupMemberDetailService groupMemberDetailService;
  @Autowired
  private RedisService redisService;
  @Autowired
  private NotifyService notifyService;

  @PostMapping
  @ApiOperation(value = "添加一条聊天消息")
  public Response sendMessage(@RequestBody @Valid AddMessageRequest request) {
    messageService.addMessage(request);
    messageHandlerService
        .publishMessage(TopicFactory.generateMqttTopic(request, appConfiguration.getEnv()),
            MqttMessageFactory.generateMqttMessage(request));
    if (!request.getSenderRoleType().equals(RoleType.PATIENT) && !redisService.getGroupType(
        RedisKeyFactory.getImKey(request.getSessionId())).equals(Type.GROUP_ORG)) {
      notifyService.sendNotifyMessage(request);
    }
    return Response.instanceSuccess();
  }

  @GetMapping
  @ApiOperation(value = "查询当前群组下的所有消息")
  public Response getMessage(@RequestParam("sessionId") Long sessionId,
      @RequestParam(value = "senderId", required = false) Long senderId,
      @RequestParam(value = "pageAt", defaultValue = "1") int pageAt,
      @RequestParam(value = "pageSize", defaultValue = "15") int pageSize,
      @RequestParam(value = "toUserId", required = false) Long toUserId) {
    MessageResponse response = messageService
        .getMessageBySessionId(sessionId, pageAt, pageSize, senderId, toUserId);
    return Response.instanceSuccess(response);
  }

  @PatchMapping("/clean")
  @ApiOperation(value = "清零当前用户未读消息数")
  public Response cleanUnreadByUser(@RequestBody CleanUnreadRequest request) {
    try {
      redisService.cleanUnreadCount(request);
    } catch (Exception e) {
      log.warn("clean unread count failed, error :{}", e.getMessage());
      groupMemberDetailService.cleanUnreadCount(request);
    }
    return Response.instanceSuccess();
  }

  @GetMapping("/session")
  @ApiOperation(value = "根据发起者查询某一组的详细信息")
  @ApiResponses({@ApiResponse(code = 200, message = "OK", response = SessionResponse.class)})
  public Response getSession(
      @RequestParam("currentUserId") Long currentUserId,
      @RequestParam("currentType") RoleType currentType,
      @ApiParam(value = "聊天群发起人", defaultValue = "10037")
      @RequestParam("initiatorId") Long initiatorId,
      @ApiParam(value = "发起人角色 默认为患者")
      @RequestParam(value = "initiatorRoleType", defaultValue = "PATIENT") RoleType initiatorRoleType,
      @ApiParam(value = "聊天群组类型")
      @RequestParam(value = "type", required = false) Type type) {
    SessionResponse response = sessionService
        .getSessionByInitiator(initiatorId, initiatorRoleType, type);
    return Response.instanceSuccess(response);
  }

  @GetMapping("/unread")
  @ApiOperation(value = "获取当前用户的所有未读数")
  @ApiResponses({@ApiResponse(code = 200, message = "OK", response = UnreadResponse.class)})
  public Response getUserUnread(
      @ApiParam(value = "聊天群发起人", defaultValue = "10037")
      @RequestParam("initiatorId") Long initiatorId,
      @ApiParam(value = "发起人角色 默认为患者")
      @RequestParam(value = "initiatorRoleType", defaultValue = "PATIENT") RoleType initiatorRoleType,
      @ApiParam(value = "当前用户id")
      @RequestParam("userId") Long userId,
      @ApiParam(value = "当前用户角色")
      @RequestParam("roleType") RoleType roleType) {
    UnreadResponse response = groupMemberDetailService
        .getUnreadCount(initiatorId, initiatorRoleType, userId, roleType);
    return Response.instanceSuccess(response);
  }

  @PatchMapping("/unread/status")
  public Response changeUnreadStatus(@RequestBody UpdateUnreadStatusRequest request) {
    messageSessionService.updateUnreadStatus(request);
    return Response.instanceSuccess();
  }


  @GetMapping("/unread/status")
  public Response getUnreadStatus(@RequestParam("initiatorId") Long initiatorId,
      @RequestParam(value = "initiatorRoleType", defaultValue = "PATIENT") RoleType initiatorRoleType,
      @RequestParam("type") Type type,
      @RequestParam("userId") Long userId, @RequestParam("roleType") RoleType roleType) {
    return Response.instanceSuccess(messageSessionService
        .getUnreadStatus(initiatorId, initiatorRoleType, type, userId, roleType));
  }

  @GetMapping("/init")
  public String init() {
    initService.initData();
    return "success";
  }

  @PatchMapping("/assistant/status")
  @ApiOperation(value = "大医生修改医助状态")
  public Response insertOrUpdateAssistantStatus(@RequestBody UpdateAssistantStatusRequest request) {
    messageSessionService.insertOrUpdate(request);
    return Response.instanceSuccess();

  }

  @GetMapping("/assistant/status")
  @ApiOperation(value = "大医生查询一个医助状态")
  public Response getAssistantStatus(@RequestParam("doctorId") Long doctorId,
      @RequestParam("assistantId") Long assistantId) {
    MemberStatus memberStatus = messageSessionService.getMemberStatus(doctorId, assistantId);
    return Response.instanceSuccess(new MemberStatusResponse(
        memberStatus == null ? UserStatus.NORMAL : memberStatus.getAssistantStatus()));

  }

}
