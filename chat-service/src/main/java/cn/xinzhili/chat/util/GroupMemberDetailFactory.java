package cn.xinzhili.chat.util;

import cn.xinzhili.chat.api.RoleType;
import cn.xinzhili.chat.api.Type;
import cn.xinzhili.chat.api.UserStatus;
import cn.xinzhili.chat.bean.PatientDetailBean;
import cn.xinzhili.chat.model.GroupMemberDetail;
import cn.xinzhili.chat.model.Session;
import cn.xinzhili.user.api.response.PatientRelationResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/5/11 下午4:35
 */
public class GroupMemberDetailFactory {

  public static GroupMemberDetail of(Long sessionId, Long senderId, RoleType sendRoleType,
      Long receviceId) {
    GroupMemberDetail groupMemberDetail = new GroupMemberDetail();
    groupMemberDetail.setSessionId(sessionId);
    groupMemberDetail.setSenderId(senderId);
    groupMemberDetail.setSenderRoleType(sendRoleType);
    groupMemberDetail.setReceiverId(receviceId);
    return groupMemberDetail;
  }

  public static List<GroupMemberDetail> of(Long sessionId, PatientRelationResponse response,
      Type type) {
    List<PatientDetailBean> detailBeans = UserFactory.getDetail(response, type);
    if (type.equals(Type.GROUP_ALL)) {
      return ofAll(sessionId, detailBeans);
    } else if (type.equals(Type.GROUP_ORG)) {
      return ofOrg(sessionId, detailBeans);
    } else {
      return null;
    }
  }

  private static List<GroupMemberDetail> ofAll(Long sessionId,
      List<PatientDetailBean> detailBeans) {
    return detailBeans.stream().map(t -> of(sessionId, t.getId(), t.getRoleType(), null)).collect(
        Collectors.toList());
  }

  private static List<GroupMemberDetail> ofOrg(Long sessionId,
      List<PatientDetailBean> detailBeans) {
    List<GroupMemberDetail> groupMemberDetails = new ArrayList<>();
    detailBeans.forEach(t -> detailBeans.forEach(d -> {
      if (!(d.getId().equals(t.getId()) && d.getRoleType().equals(t.getRoleType()))) {
        groupMemberDetails.add(of(sessionId, d.getId(), d.getRoleType(), t.getId()));
      }
    }));
    return groupMemberDetails;
  }

  private static List<GroupMemberDetail> ofUpdOrg(Long sessionId,
      List<PatientDetailBean> detailBeans, Long senderId, RoleType senderRoleType) {
    List<GroupMemberDetail> groupMemberDetails = new ArrayList<>();
    detailBeans.forEach(t -> detailBeans.forEach(d -> {
      if (!(d.getId().equals(t.getId()) && d.getRoleType().equals(t.getRoleType()))) {
        if ((d.getId().equals(senderId) && d.getRoleType().equals(senderRoleType)) || t.getId()
            .equals(senderId)) {
          groupMemberDetails.add(of(sessionId, d.getId(), d.getRoleType(), t.getId()));
        }
      }
    }));
    return groupMemberDetails;
  }

  public static List<GroupMemberDetail> of(Session session, Long senderId, RoleType sendRoleType,
      PatientRelationResponse response) {
    List<GroupMemberDetail> groupMemberDetails = new ArrayList<>();
    if (session.getType().equals(Type.GROUP_ALL)) {
      groupMemberDetails.add(of(session.getId(), senderId, sendRoleType, null));
      return groupMemberDetails;
    } else if (session.getType().equals(Type.GROUP_ORG)) {
      List<PatientDetailBean> detailBeans = UserFactory.getDetail(response, Type.GROUP_ORG);
      return ofUpdOrg(session.getId(), detailBeans, senderId, sendRoleType);
    } else {
      return null;
    }
  }
}
