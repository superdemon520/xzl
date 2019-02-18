package cn.xinzhili.chat.api;

import lombok.Data;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/5/3 下午3:34
 */
@Data
public class MessageInfo {

  private Long id;
  private String avatar;
  private String name;
  private Long senderId;
  private RoleType senderRoleType;
  private Long sessionId;
  private String content;
  private MessageType messageType;
  private Long receiverId;
  private Long createdAt;


}
