package cn.xinzhili.chat.bean;

import cn.xinzhili.chat.api.MessageType;
import lombok.Data;

@Data
public class MessageData {

  private MessageType type;
  private String content;

}
