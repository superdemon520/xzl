package cn.xinzhili.chat.service;

import cn.xinzhili.chat.api.MessageInfo;
import cn.xinzhili.chat.api.request.AddMessageRequest;
import cn.xinzhili.chat.api.response.MessageResponse;
import cn.xinzhili.chat.dao.MessageMapper;
import cn.xinzhili.chat.model.MessageExample;
import cn.xinzhili.chat.model.MessageExample.Criteria;
import cn.xinzhili.chat.model.MessageSession;
import cn.xinzhili.chat.model.Session;
import cn.xinzhili.chat.util.MessageFactory;
import cn.xinzhili.chat.util.RedisKeyFactory;
import cn.xinzhili.user.api.response.PatientDetailResponse;
import cn.xinzhili.xutils.core.ErrorCode;
import cn.xinzhili.xutils.core.FailureException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/5/3 下午3:29
 */
@Slf4j
@Service
public class MessageService {

  @Autowired
  private UserService userService;
  @Autowired
  private MessageMapper messageMapper;

  @Autowired
  private GroupMemberDetailService groupMemberDetailService;
  @Autowired
  private SessionService sessionService;
  @Autowired
  private MessageSessionService messageSessionService;

  @Autowired
  private RedisService redisService;

  public long addMessage(AddMessageRequest request) {

    Long id = messageMapper.insertSelectiveReturningId(MessageFactory.of(request));
    if (id == 0) {
      log.error("insert message error !request: {}", request);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    }
    try {
      redisService.addMessageCountToRedis(request);
    } catch (Exception e) {
      log.warn("insert message to redis fail error:{}", e.getMessage());
      groupMemberDetailService
          .updateUnreadCountBySessionId(request.getSessionId(), request.getSenderId(),
              request.getSenderRoleType(), request.getReceiverId());
    }

    return id;
  }


  public MessageResponse getMessageBySessionId(Long sessionId, int pageAt, int pageSize,
      Long senderId, Long toUserId) {
    MessageExample example = new MessageExample();
    Criteria criteria = example.or().andDeletedAtIsNull().andSessionIdEqualTo(sessionId);
    if (Objects.nonNull(toUserId) && Objects.nonNull(senderId)) {
      criteria.andSenderIdIn(List.of(senderId, toUserId))
          .andReceiverIdIn(List.of(toUserId, senderId));
    } else if (Objects.isNull(senderId) && Objects.nonNull(toUserId)) {
      switch (redisService.getGroupType(RedisKeyFactory.getImKey(sessionId))) {
        case GROUP_ORG:
          List<Long> userIds = messageSessionService
              .getMessageSessionBySessionId(sessionId).stream()
              .filter(messageSession -> !messageSession.getUserId().equals(toUserId))
              .map(MessageSession::getUserId).collect(Collectors.toList());
          if (!userIds.isEmpty()) {
            criteria.andSenderIdIn(userIds).andReceiverIdIn(userIds);
          } else {
            log.warn("获取群组人员数为空，sessionId:{}", sessionId);
            throw new FailureException(ErrorCode.REQUEST_FAILED);
          }
          break;
        case GROUP_ALL:
          criteria.andReceiverIdIsNull();
          break;
      }
    }

    example.setLimit(pageSize);
    example.setOffset((pageAt - 1) * pageSize);
    example.setOrderByClause("created_at desc");
    int total = messageMapper.countByExample(example);
    if (total <= 0) {
      return new MessageResponse(Collections.emptyList(), 0);
    }
    Session session = sessionService.getById(sessionId);
//    PatientDetailResponse response = userService.getPatientBindInfos(session.getInitiatorId());
    List<MessageInfo> messageInfos = messageMapper.selectByExample(example)
        .stream().map(
            t -> MessageFactory
                .api(t, userService.getUserDetail(t.getSenderId(), t.getSenderRoleType())))
        .collect(Collectors.toList());

    return new MessageResponse(messageInfos, total);
  }

}
