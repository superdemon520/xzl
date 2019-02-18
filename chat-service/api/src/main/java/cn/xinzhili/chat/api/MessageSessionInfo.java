package cn.xinzhili.chat.api;

import lombok.Data;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/5/7 上午10:56
 */
@Data
public class MessageSessionInfo {
  private Long id;
  private Long sessionId;
  private Long userId;
  private RoleType roleType;
  private int unreadCount;
  private UserStatus userStatus;
  private UnreadStatus unreadStatus;
  private String name;
  private String avatar;
}
