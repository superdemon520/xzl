package cn.xinzhili.chat.util;

import cn.xinzhili.chat.api.Type;
import cn.xinzhili.chat.api.MessageSessionInfo;
import cn.xinzhili.chat.api.RoleType;
import cn.xinzhili.chat.api.UnreadStatus;
import cn.xinzhili.chat.api.UserStatus;
import cn.xinzhili.chat.api.request.UpdateAssistantStatusRequest;
import cn.xinzhili.chat.bean.PatientDetailBean;
import cn.xinzhili.chat.bean.UpdateMemberBean;
import cn.xinzhili.chat.bean.UserBean;
import cn.xinzhili.chat.model.MemberStatus;
import cn.xinzhili.chat.model.MessageSession;
import cn.xinzhili.user.api.response.PatientDetailResponse;
import cn.xinzhili.user.api.response.PatientRelationResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/5/4 下午6:30
 */
public class MessageSessionFactory {

  public static MessageSession of(Long sessionId, Long userId, RoleType roleType,
      UserStatus userStatus) {
    MessageSession messageSession = new MessageSession();
    messageSession.setSessionId(sessionId);
    messageSession.setUserId(userId);
    messageSession.setRoleType(roleType);
    messageSession.setUserStatus(userStatus);
    messageSession.setUnreadStatus(UnreadStatus.BLOCK);
    return messageSession;
  }


  public static List<MessageSession> of(Long sessionId, PatientRelationResponse response,
      Type type) {
    List<PatientDetailBean> detailBeans = UserFactory.getDetail(response, type);
    return detailBeans.stream()
        .map(t -> of(sessionId, t.getId(), t.getRoleType(), UserStatus.NORMAL)).collect(
            Collectors.toList());
  }

  public static UpdateMemberBean of(PatientRelationResponse response,
      List<MessageSession> messageSessions, Type type) {
    UpdateMemberBean updateMemberBean = new UpdateMemberBean();
    List<PatientDetailBean> pdbs = UserFactory.getDetail(response, type);
    List<PatientDetailBean> mess = mess(messageSessions);
    List<PatientDetailBean> retainList = new ArrayList<>(pdbs);
    retainList.retainAll(mess);
    pdbs.removeAll(retainList);
    updateMemberBean.setAddList(pdbs);
    mess.removeAll(retainList);
    updateMemberBean.setRemoveList(mess);
    return updateMemberBean;

  }

  private static List<PatientDetailBean> mess(List<MessageSession> messageSessions) {
    return messageSessions.stream().map(t -> {
      PatientDetailBean patientDetailBean = new PatientDetailBean();
      patientDetailBean.setId(t.getUserId());
      patientDetailBean.setRoleType(t.getRoleType());
      return patientDetailBean;
    }).collect(Collectors.toList());
  }


  public static MessageSessionInfo api(MessageSession messageSession,
      PatientDetailResponse response, UserStatus assistantStatus, Type type) {
    MessageSessionInfo info = new MessageSessionInfo();
    BeanUtils.copyProperties(messageSession, info);
    UserBean user = UserFactory.getNameAndAvatar(info.getRoleType(), response);
    if (type.equals(Type.GROUP_ALL) && messageSession.getRoleType().equals(RoleType.ASSISTANT)
        && assistantStatus != null) {
      info.setUserStatus(assistantStatus);
    }
    info.setAvatar(user.getAvatar());
    info.setName(user.getName());
    return info;
  }


  public static MemberStatus of(UpdateAssistantStatusRequest request) {
    MemberStatus memberStatus = new MemberStatus();
    memberStatus.setDoctorId(request.getDoctorId());
    memberStatus.setAssistantId(request.getAssistantId());
    memberStatus.setAssistantStatus(request.getUserStatus());
    return memberStatus;
  }
}
