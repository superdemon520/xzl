package cn.xinzhili.chat.bean;

import cn.xinzhili.chat.api.RoleType;
import lombok.Data;

@Data
public class MessageBean {

  private String avatar;
  private String name;
  private String sender;
  private String receiver;
  private MessageData message;
  private Long sendTime;
  private RoleType senderRoleType;
  private String sessionId;

}
