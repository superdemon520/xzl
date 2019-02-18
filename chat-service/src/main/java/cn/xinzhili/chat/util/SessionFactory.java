package cn.xinzhili.chat.util;

import cn.xinzhili.chat.api.SessionInfo;
import cn.xinzhili.chat.api.SessionType;
import cn.xinzhili.chat.api.Type;
import cn.xinzhili.chat.api.RoleType;
import cn.xinzhili.chat.api.UserStatus;
import cn.xinzhili.chat.model.MessageSession;
import cn.xinzhili.chat.model.Session;
import cn.xinzhili.user.api.response.PatientDetailResponse;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/5/4 下午5:27
 */
public class SessionFactory {

  public static Session of(Long patientId, Type type,
      Long orgId) {
    Session session = new Session();
    session.setInitiatorId(patientId);
    session.setInitiatorRoleType(RoleType.PATIENT);
    session.setSessionType(SessionType.GROUP);
    session.setType(type);
    session.setOrganizationId(orgId);
    return session;
  }

  public static SessionInfo api(Session session, List<MessageSession> messageSessions,
      PatientDetailResponse response, UserStatus assistantStatus) {
    SessionInfo info = new SessionInfo();
    BeanUtils.copyProperties(session, info);
    info.setMessageSessionInfos(messageSessions.stream()
        .map(messageSession -> MessageSessionFactory.api(messageSession, response, assistantStatus,session.getType()))
        .collect(
            Collectors.toList()));
    info.setOrganizationId(response.getPatient().getOrganizationId());
    return info;
  }

}
