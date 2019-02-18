package cn.xinzhili.chat.service;

import cn.xinzhili.chat.api.RoleType;
import cn.xinzhili.chat.api.Type;
import cn.xinzhili.chat.api.UnreadStatus;
import cn.xinzhili.chat.api.UserStatus;
import cn.xinzhili.chat.api.request.UpdateAssistantStatusRequest;
import cn.xinzhili.chat.api.request.UpdateUnreadStatusRequest;
import cn.xinzhili.chat.api.response.SessionResponse;
import cn.xinzhili.chat.api.response.UnreadStatusResponse;
import cn.xinzhili.chat.dao.MemberStatusMapper;
import cn.xinzhili.chat.dao.MessageSessionMapper;
import cn.xinzhili.chat.model.MemberStatus;
import cn.xinzhili.chat.model.MemberStatusExample;
import cn.xinzhili.chat.model.MessageSession;
import cn.xinzhili.chat.model.MessageSessionExample;
import cn.xinzhili.chat.model.Session;
import cn.xinzhili.chat.util.MessageSessionFactory;
import cn.xinzhili.user.api.response.PatientDetailResponse;
import cn.xinzhili.xutils.core.ErrorCode;
import cn.xinzhili.xutils.core.FailureException;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/5/4 下午4:22
 */
@Slf4j
@Service
@Transactional
public class MessageSessionService {

  @Autowired
  private MessageSessionMapper messageSessionMapper;

  @Autowired
  private MemberStatusMapper memberStatusMapper;
  @Autowired
  private SessionService sessionService;
  @Autowired
  private UserService userService;

  public List<MessageSession> getMessageSessionBySessionId(Long sessionId) {
    MessageSessionExample example = new MessageSessionExample();
    example.or().andDeletedAtIsNull().andSessionIdEqualTo(sessionId).andUserStatusNotEqualTo(
        UserStatus.INVALID);

    return messageSessionMapper.selectByExample(example);
  }

  public UserStatus getUserStatus(PatientDetailResponse response) {
    if (Objects.nonNull(response.getBoundAssistant())) {
      MemberStatus memberStatus = getMemberStatus(response.getBoundDoctor().getId(),
          response.getBoundAssistant().getId());
      if (Objects.nonNull(memberStatus)) {
        return memberStatus.getAssistantStatus();
      }
    }
    return null;

  }

  public void addMessageSession(MessageSession messageSession) {
    messageSessionMapper.insertSelective(messageSession);
  }


  public void updateUnreadStatus(UpdateUnreadStatusRequest request) {
    MessageSessionExample example = new MessageSessionExample();
    example.or()
        .andSessionIdEqualTo(request.getSessionId())
        .andUserIdEqualTo(request.getUserId())
        .andRoleTypeEqualTo(request.getRoleType())
        .andDeletedAtIsNull();
    MessageSession messageSession = new MessageSession();
    messageSession.setUnreadStatus(request.getUnreadStatus());
    int num = messageSessionMapper.updateByExampleSelective(messageSession, example);
    if (num <= 0) {
      log.error("update unreadStatus error !,request:{}", request);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    }
  }


  @Transactional
  public void updateUserStatus(Long sessionId, Long userId, RoleType roleType,
      UserStatus userStatus) {
    MessageSessionExample example = new MessageSessionExample();
    example.or()
        .andSessionIdEqualTo(sessionId)
        .andUserIdEqualTo(userId)
        .andRoleTypeEqualTo(roleType)
        .andDeletedAtIsNull();
    MessageSession messageSession = new MessageSession();
    messageSession.setUserStatus(userStatus);
    int num = messageSessionMapper.updateByExampleSelective(messageSession, example);
    if (num <= 0) {
      log.error("update userStatus error !");
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    }
  }

  public void insertOrUpdate(UpdateAssistantStatusRequest request) {
    MemberStatus memberStatus = MessageSessionFactory.of(request);
    if (getMemberStatus(memberStatus.getDoctorId(), memberStatus.getAssistantId()) == null) {
      addMemberStatus(memberStatus);
    } else {
      updateMemberStatus(memberStatus);
    }
  }

  public void addMemberStatus(MemberStatus memberStatus) {
    memberStatusMapper.insertSelective(memberStatus);
  }

  public void updateMemberStatus(MemberStatus memberStatus) {

    MemberStatusExample example = new MemberStatusExample();
    example.or().andAssistantIdEqualTo(memberStatus.getAssistantId())
        .andDoctorIdEqualTo(memberStatus.getDoctorId()).andDeletedAtIsNull();
    memberStatusMapper.updateByExampleSelective(memberStatus, example);
  }

  public MemberStatus getMemberStatus(Long doctorId, Long assistantId) {
    MemberStatusExample example = new MemberStatusExample();
    example.or().andAssistantIdEqualTo(assistantId)
        .andDoctorIdEqualTo(doctorId).andDeletedAtIsNull();
    List<MemberStatus> memberStatuses = memberStatusMapper.selectByExample(example);
    return memberStatuses.isEmpty() ? null : memberStatuses.get(0);
  }


  public UnreadStatusResponse getUnreadStatus(Long initiatorId, RoleType initiatorRoleType,
      Type type,
      Long userId, RoleType roleType) {
    List<Session> session = sessionService
        .getSession(initiatorId, initiatorRoleType, type);
    if (!session.isEmpty()) {
      MessageSessionExample example = new MessageSessionExample();
      example.or().andDeletedAtIsNull().andSessionIdEqualTo(session.get(0).getId())
          .andUserIdEqualTo(userId).andRoleTypeEqualTo(roleType);
      List<MessageSession> messageSessions = messageSessionMapper.selectByExample(example);
      if (!messageSessions.isEmpty()) {
        UnreadStatusResponse response = new UnreadStatusResponse();
        response.setUnreadStatus(messageSessions.get(0).getUnreadStatus());
        return response;
      } else {
        log.warn("get message session failed sessionId:{}, userId:{}, userType:{}",
            session.get(0).getId(), userId, roleType);
        throw new FailureException(ErrorCode.REQUEST_FAILED);
      }
    } else {
      log.warn("get session failed initiatorId:{}, initiatorRoleType:{}", initiatorId,
          initiatorRoleType);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    }

  }
}
