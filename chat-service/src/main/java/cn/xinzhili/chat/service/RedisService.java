package cn.xinzhili.chat.service;

import cn.xinzhili.chat.api.RoleType;
import cn.xinzhili.chat.api.Type;
import cn.xinzhili.chat.api.request.AddMessageRequest;
import cn.xinzhili.chat.api.request.CleanUnreadRequest;
import cn.xinzhili.chat.api.response.UnreadResponse;
import cn.xinzhili.chat.api.response.UnreadResponse.MedicalUnreadCount;
import cn.xinzhili.chat.model.GroupMemberDetail;
import cn.xinzhili.chat.model.MessageSession;
import cn.xinzhili.chat.model.Session;
import cn.xinzhili.chat.util.RedisKeyFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

  @Autowired
  private RedisBasicService redisBasicService;
  @Autowired
  private GroupMemberDetailService groupMemberDetailService;

  public void generateRedisSessionHash(Long sessionId, List<MessageSession> messageSessions,
      Type type, Long initiatorId) {
    String imKey = RedisKeyFactory.getImKey(sessionId);
    redisBasicService.deleteKey(imKey);
    //todo need promote
    Map<String, String> resultMap = generateKeysByType(messageSessions, imKey, type);
    redisBasicService.setHashValue(imKey, resultMap);
    redisBasicService.setHashValue(imKey, RedisKeyFactory.getTypeKey(), type.toString());
    redisBasicService.setString(RedisKeyFactory.getInitiatorKey(sessionId), String.valueOf(initiatorId));
//    redisBasicService.setHashStringValue(imKey, RedisKeyFactory.getInitiatorKey(initiatorId), String.valueOf(initiatorId));
    redisBasicService.setKeyExpire(imKey, 7L, TimeUnit.DAYS);
    redisBasicService.setKeyExpire(RedisKeyFactory.getInitiatorKey(sessionId), 7L, TimeUnit.DAYS);
  }

  private Map<String, String> generateKeysByType(List<MessageSession> messageSessions,
      String imKey, Type type) {
    Map<String, String> map = new HashMap<>();
    switch (type) {
      case GROUP_ALL:
        generateAllResult(messageSessions, imKey, map);
        break;
      case GROUP_ORG:
        generateOrgResult(messageSessions, imKey, map);
        break;
    }
    return map;
  }

  private void generateOrgResult(List<MessageSession> messageSessions, String imKey,
      Map<String, String> map) {
    messageSessions.forEach(sender -> messageSessions.forEach(receiver -> {
      if (!sender.getUserId().equals(receiver.getUserId())) {
        String groupOrgUnreadKey = RedisKeyFactory
            .getGroupOrgUnreadKey(sender.getUserId(), receiver.getUserId());
        if (Objects.isNull(redisBasicService.getHashIntegerValue(imKey, groupOrgUnreadKey))) {
          GroupMemberDetail groupMemberDetail = groupMemberDetailService
              .getGroupMemberDetail(receiver.getUserId(), receiver.getRoleType(),
                  receiver.getSessionId(), sender.getUserId());
          map.put(groupOrgUnreadKey, Objects.isNull(groupMemberDetail) ? "0"
              : groupMemberDetail.getReceiverUnreadCount().toString());
        }
      }
    }));
  }


  private void generateAllResult(List<MessageSession> messageSessions, String imKey,
      Map<String, String> map) {
    messageSessions.forEach(ms -> {
      String groupAllUnreadKey = RedisKeyFactory
          .getGroupAllUnreadKey(ms.getUserId(), ms.getRoleType());
      if (Objects.isNull(redisBasicService.getHashIntegerValue(imKey, groupAllUnreadKey))) {
        GroupMemberDetail groupMemberDetail = groupMemberDetailService
            .getGroupMemberDetail(ms.getUserId(), ms.getRoleType(), ms.getSessionId(), null);
        map.put(groupAllUnreadKey, Objects.isNull(groupMemberDetail) ? "0"
            : groupMemberDetail.getReceiverUnreadCount().toString());

      }
    });
  }

  public void addMessageCountToRedis(AddMessageRequest request) {
    String imKey = RedisKeyFactory.getImKey(request.getSessionId());
    updateRedisUnreadCountByType(imKey, getGroupType(imKey), request);
  }

  public Type getGroupType(String imKey) {
    Type type = Type
        .valueOf(redisBasicService.getHashStringValue(imKey, RedisKeyFactory.getTypeKey()));
    return type;
  }

//  public String getInitiatorId(String imKey) {
//    return (String) redisBasicService.getHashValue(imKey, RedisKeyFactory.getInitiatorKey());
//  }

  private void updateRedisUnreadCountByType(String imKey, Type type, AddMessageRequest request) {
    switch (type) {
      case GROUP_ORG:
        increaseOrgGroupCount(imKey, request);
        break;
      case GROUP_ALL:
        increaseAllGroupCount(imKey, request);
        break;
    }
  }

  private void increaseOrgGroupCount(String imKey, AddMessageRequest request) {
    String name = RedisKeyFactory
        .getGroupOrgUnreadKey(request.getSenderId(), request.getReceiverId());
    redisBasicService.unreadCountPlus(imKey, name, 1);
  }

  private void increaseAllGroupCount(String imKey, AddMessageRequest request) {
    Set<Object> hashKeys = redisBasicService.getHashKeys(imKey);
    hashKeys.forEach(o -> {
      if (!o.toString().contains(request.getSenderId().toString()) && !o.toString()
          .equals(RedisKeyFactory.getTypeKey())) {
        redisBasicService.unreadCountPlus(imKey, o.toString(), 1);
      }
    });
  }


  public void cleanUnreadCount(CleanUnreadRequest request) {
    String imKey = RedisKeyFactory.getImKey(request.getSessionId());
    String hname = null;

    switch (getGroupType(imKey)) {
      case GROUP_ALL:
        hname = RedisKeyFactory.getGroupAllUnreadKey(request.getUserId(), request.getRoleType());
        break;
      case GROUP_ORG:
        if (Objects.nonNull(request.getReceiverId())) {
          hname = RedisKeyFactory
              .getGroupOrgUnreadKey(request.getReceiverId(), request.getUserId());
          break;
        }
    }
    if (Objects.nonNull(hname)) {
      redisBasicService.setHashValue(imKey, hname, "0");
    }
  }

  public UnreadResponse getUnreadCount(Long userId, RoleType roleType, List<Session> sessions) {
    UnreadResponse response = new UnreadResponse();
    sessions.forEach(session -> {
      String imKey = RedisKeyFactory.getImKey(session.getId());
      switch (getGroupType(imKey)) {
        case GROUP_ALL:
          Integer allUnread = redisBasicService
              .getHashIntegerValue(imKey, RedisKeyFactory.getGroupAllUnreadKey(userId, roleType));
          response.setPatientUnreadCount(allUnread);
          break;
        case GROUP_ORG:
          List<MedicalUnreadCount> medicalUnreadCounts = new ArrayList<>();
          Set<Object> unreadCounts = redisBasicService.getHashKeys(imKey);
          unreadCounts.forEach(c -> {
            if (c.toString().endsWith(userId.toString())) {
              MedicalUnreadCount unreadCount = new MedicalUnreadCount();
              unreadCount.setReceiverId(Long.valueOf(c.toString().split("-")[2]));
              unreadCount.setUnreadCount(
                  redisBasicService.getHashIntegerValue(imKey, c.toString()));
              unreadCount.setSenderId(Long.valueOf(c.toString().split("-")[1]));
              medicalUnreadCounts.add(unreadCount);
            }
          });
          response.setMedicalUnreadCounts(medicalUnreadCounts);
          break;
      }
    });
    return response;
  }
}
