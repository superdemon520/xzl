package cn.xinzhili.chat.service;

import cn.xinzhili.chat.api.SessionInfo;
import cn.xinzhili.chat.api.Type;
import cn.xinzhili.chat.api.RoleType;
import cn.xinzhili.chat.api.UnreadStatus;
import cn.xinzhili.chat.api.UserStatus;
import cn.xinzhili.chat.api.response.SessionResponse;
import cn.xinzhili.chat.bean.UpdateMemberBean;
import cn.xinzhili.chat.dao.SessionMapper;
import cn.xinzhili.chat.model.GroupMemberDetail;
import cn.xinzhili.chat.model.Session;
import cn.xinzhili.chat.model.SessionExample;
import cn.xinzhili.chat.model.SessionExample.Criteria;
import cn.xinzhili.chat.model.MessageSession;
import cn.xinzhili.chat.util.GroupMemberDetailFactory;
import cn.xinzhili.chat.util.SessionFactory;
import cn.xinzhili.chat.util.MessageSessionFactory;
import cn.xinzhili.user.api.response.PatientDetailResponse;
import cn.xinzhili.user.api.response.PatientRelationResponse;
import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/5/4 下午4:21
 */
@Service
@Slf4j
@Transactional
public class SessionService {

  @Autowired
  private UserService userService;
  @Autowired
  private SessionMapper sessionMapper;
  @Autowired
  private MessageSessionService messageSessionService;
  @Autowired
  private RedisService redisService;
  @Autowired
  private GroupMemberDetailService groupMemberDetailService;

  public void addSessionByPatient(Long initiatorId, Type type) {
    if (type == null) {
      Arrays.stream(Type.values()).forEach(t -> addSessionByPatientBySessionType(initiatorId, t));
    } else {
      addSessionByPatientBySessionType(initiatorId, type);
    }

  }

  public void addSessionByPatientBySessionType(Long initiatorId, Type type) {
    log.info("根据患者添加群组，initiatorId:{}", initiatorId);
    PatientRelationResponse relation = userService
        .getRelationByUserId(initiatorId, RoleType.PATIENT, null);
    Long sessionId = addSession(SessionFactory.of(initiatorId, type, relation.getOrganizationId()));
    List<MessageSession> messageSessions = MessageSessionFactory.of(sessionId, relation, type);
    messageSessions.forEach(messageSessionService::addMessageSession);
    List<GroupMemberDetail> groupMemberDetails = GroupMemberDetailFactory
        .of(sessionId, relation, type);
    groupMemberDetails.forEach(groupMemberDetailService::add);
  }


  public long addSession(Session session) {
    return sessionMapper.insertSelectiveReturningId(session);
  }

  public SessionResponse getSessionByInitiator(Long initiatorId, RoleType initiatorRoleType,
      Type type) {
    //todo 当多机构时引入机构id
    List<Session> sessions = getSession(initiatorId, initiatorRoleType, type);
    if (sessions.isEmpty()) {
      addSessionByPatient(initiatorId, type);
      sessions = getSession(initiatorId, initiatorRoleType, type);
    } else {
      final List<Session> tempSession = Lists.newArrayList(sessions);
      Arrays.stream(Type.values())
          .filter(t -> tempSession.stream().noneMatch(s -> t.equals(s.getType())))
          .forEach(t -> addSessionByPatient(initiatorId, t));
      sessions = getSession(initiatorId, initiatorRoleType, type);
      sessions.forEach(this::updateMessageSession);
    }
    PatientDetailResponse patientBindInfos = userService.getPatientBindInfos(initiatorId);
    List<SessionInfo> sessionInfos = sessions.stream().map(t -> {
      List<MessageSession> messageSessions = messageSessionService
          .getMessageSessionBySessionId(t.getId());
      redisService.generateRedisSessionHash(t.getId(), messageSessions, t.getType(), initiatorId);
      return SessionFactory
          .api(t, messageSessionService.getMessageSessionBySessionId(t.getId()), patientBindInfos,
              messageSessionService.getUserStatus(patientBindInfos));
    }).collect(Collectors.toList());
    return new SessionResponse(sessionInfos);
  }

  private void updateMessageSession(Session session) {

    List<MessageSession> messageSessionBySessions = messageSessionService
        .getMessageSessionBySessionId(session.getId());
    PatientRelationResponse relation = userService
        .getRelationByUserId(session.getInitiatorId(), RoleType.PATIENT, null);
    if (!session.getOrganizationId().equals(relation.getOrganizationId())) {
      session.setOrganizationId(relation.getOrganizationId());
      sessionMapper.updateByPrimaryKey(session);
    }
    UpdateMemberBean updateMemberBean = MessageSessionFactory
        .of(relation, messageSessionBySessions, session.getType());
    if (!Objects.isNull(updateMemberBean.getRemoveList())) {
      updateMemberBean.getRemoveList().forEach(t -> {
        messageSessionService
            .updateUserStatus(session.getId(), t.getId(), t.getRoleType(), UserStatus.INVALID);
        groupMemberDetailService.deleteBy(session.getId(), t.getId(), t.getRoleType());
      });
    }
    if (!Objects.isNull(updateMemberBean.getAddList())) {
      updateMemberBean.getAddList().forEach(t -> {
        messageSessionService.addMessageSession(MessageSessionFactory
            .of(session.getId(), t.getId(), t.getRoleType(), UserStatus.NORMAL));
        GroupMemberDetailFactory.of(session, t.getId(), t.getRoleType(), relation)
            .forEach(g -> groupMemberDetailService.add(g));
      });

    }

  }

  public List<Session> getSession(Long initiatorId,
      RoleType initiatorRoleType, Type type) {
    SessionExample example = new SessionExample();
    Criteria criteria = example.or();
    criteria.andDeletedAtIsNull()
        .andInitiatorIdEqualTo(initiatorId)
        .andInitiatorRoleTypeEqualTo(initiatorRoleType);
    if (type != null) {
      criteria.andTypeEqualTo(type);
    }
    return sessionMapper.selectByExample(example);
  }

  public List<Session> getSessionByOrgId(Long userId, RoleType initiatorRoleType, Long orgId) {
    SessionExample example = new SessionExample();
    example.or().andInitiatorIdEqualTo(userId).andOrganizationIdEqualTo(orgId)
        .andInitiatorRoleTypeEqualTo(initiatorRoleType);
    return sessionMapper.selectByExample(example);
  }

  public Session getById(Long id) {
    return sessionMapper.selectByPrimaryKey(id);
  }

}
