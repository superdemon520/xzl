package cn.xinzhili.chat.service;

import cn.xinzhili.chat.api.RoleType;
import cn.xinzhili.chat.api.Type;
import cn.xinzhili.chat.api.request.CleanUnreadRequest;
import cn.xinzhili.chat.api.response.UnreadResponse;
import cn.xinzhili.chat.api.response.UnreadResponse.MedicalUnreadCount;
import cn.xinzhili.chat.dao.GroupMemberDetailMapper;
import cn.xinzhili.chat.dao.custom.CustomGroupMemberDetailMapper;
import cn.xinzhili.chat.model.GroupMemberDetail;
import cn.xinzhili.chat.model.GroupMemberDetailExample;
import cn.xinzhili.chat.model.GroupMemberDetailExample.Criteria;
import cn.xinzhili.chat.model.Session;
import cn.xinzhili.chat.util.RedisKeyFactory;
import cn.xinzhili.xutils.core.ErrorCode;
import cn.xinzhili.xutils.core.FailureException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/5/11 下午4:14
 */
@Slf4j
@Service
public class GroupMemberDetailService {


  @Autowired
  private SessionService sessionService;
  @Autowired
  private GroupMemberDetailMapper groupMemberDetailMapper;
  @Autowired
  private CustomGroupMemberDetailMapper customGroupMemberDetailMapper;
  @Autowired
  private RedisService redisService;
  @Autowired
  private RedisBasicService redisBasicService;


  public void add(GroupMemberDetail groupMemberDetail) {
    groupMemberDetailMapper.insertSelective(groupMemberDetail);
  }

  @Transactional
  public void deleteBy(Long sessionId, Long senderId, RoleType roleType) {
    GroupMemberDetailExample example = new GroupMemberDetailExample();
    example.or().andSessionIdEqualTo(sessionId)
        .andSenderIdEqualTo(senderId)
        .andSenderRoleTypeEqualTo(roleType);
    example.or().andSessionIdEqualTo(sessionId)
        .andReceiverIdEqualTo(senderId)
        .andDeletedAtIsNull();
    int num = groupMemberDetailMapper.deleteByExample(example);
    if (num <= 0) {
      log.error("delete group by session error !sessionId: {}", sessionId);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    }
  }

  public void updateUnreadCountBySessionId(Long sessionId, Long senderId, RoleType senderRoleType,
      Long receiverId) {
    int num = customGroupMemberDetailMapper
        .updateUnread(sessionId, senderId, senderRoleType, receiverId, 1);
    if (num <= 0) {
      log.error("update unread count error !sessionId: {}", sessionId);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    }
  }


  public void cleanUnreadCount(CleanUnreadRequest request) {
    redisService.cleanUnreadCount(request);
    int num = customGroupMemberDetailMapper
        .cleanUnread(request.getUserId(), request.getRoleType(), request.getSessionId(),
            request.getReceiverId());
    if (num <= 0) {
      log.error("clean unread count error !sessionId: {}", request.getSessionId());
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    }
  }

  private UnreadResponse getUnreadCountMessageBySessions(Long userId, RoleType roleType,
      List<Session> sessions) {
    UnreadResponse response = new UnreadResponse();
    sessions.forEach(g -> {
      List<GroupMemberDetail> groupMemberDetails = getGroupsByUser(userId, roleType, g.getId());
      if (groupMemberDetails == null || groupMemberDetails.size() <= 0) {
        return;
      }
      if (g.getType().equals(Type.GROUP_ALL)) {
        response.setPatientUnreadCount(groupMemberDetails.get(0).getReceiverUnreadCount());
      } else if (g.getType().equals(Type.GROUP_ORG)) {
        List<MedicalUnreadCount> medicalUnreadCounts = groupMemberDetails.stream().map(t -> {
          MedicalUnreadCount medicalUnreadCount = new MedicalUnreadCount();
          medicalUnreadCount.setReceiverId(t.getReceiverId());
          medicalUnreadCount.setUnreadCount(t.getReceiverUnreadCount());
          return medicalUnreadCount;
        }).collect(Collectors.toList());
        response.setMedicalUnreadCounts(medicalUnreadCounts);
      }
    });
    return response;
  }

  public UnreadResponse getUnreadCount(Long initiatorId, RoleType initiatorRoleType, Long userId,
      RoleType roleType) {
    List<Session> sessions = sessionService.getSession(initiatorId, initiatorRoleType, null);
    if (sessions.isEmpty()) {
      return new UnreadResponse();
    } else {
      return redisService.getUnreadCount(userId, roleType, sessions);
//      return getUnreadCountMessageBySessions(userId, roleType, sessions);
    }
  }

  private List<GroupMemberDetail> getGroupsByUser(Long userId, RoleType roleType, Long sessionId) {
    GroupMemberDetailExample example = new GroupMemberDetailExample();
    example.or().andSessionIdEqualTo(sessionId)
        .andSenderIdEqualTo(userId)
        .andSenderRoleTypeEqualTo(roleType)
        .andDeletedAtIsNull();
    return groupMemberDetailMapper.selectByExample(example);
  }

  public GroupMemberDetail getGroupMemberDetail(Long userId, RoleType roleType, Long sessionId,
      Long receiverId) {
    GroupMemberDetailExample example = new GroupMemberDetailExample();
    Criteria criteria = example.or().andDeletedAtIsNull().andSessionIdEqualTo(sessionId)
        .andSenderIdEqualTo(userId)
        .andSenderRoleTypeEqualTo(roleType);
    if (Objects.nonNull(receiverId)) {
      criteria.andReceiverIdEqualTo(receiverId);
    }
    List<GroupMemberDetail> groupMemberDetails = groupMemberDetailMapper.selectByExample(example);
    return groupMemberDetails.isEmpty() ? null : groupMemberDetails.get(0);
  }

}
