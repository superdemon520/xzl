package cn.xinzhili.chat.util;

import cn.xinzhili.chat.api.RoleType;

public class RedisKeyFactory {

  public static String getImKey(Long sessionId) {
    return "IM/" + sessionId;
  }

  public static String getGroupOrgUnreadKey(Long currentId, Long opositId) {
    return "groupOrgUnread-" + currentId + "-" + opositId;
  }

  public static String getGroupAllUnreadKey(Long senderId, RoleType type) {
    String role;
    switch (type) {
      case PATIENT:
        role = "p_";
        break;
      default:
        role = "d_";
    }
    return "groupAllUnread-" + role + senderId;
  }

  public static String getTypeKey() {
    return "groupType";
  }

  public static String getInitiatorKey(Long sessionId) {
    return "initiator-" + sessionId;
  }

}
