package cn.xinzhili.api.doctor.bean;

import cn.xinzhili.chat.api.RoleType;
import lombok.Data;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/5/9 下午2:34
 */
@Data
public class MessageDetailInfo {

  private String avatar;
  private String name;
  private String sender;
  private RoleType senderRoleType;
  private String receiver;
  private String sessionId;
  private MessageApiInfo message;
  private Long sendTime;
}
