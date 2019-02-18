package cn.xinzhili.chat.bean;

import cn.xinzhili.chat.api.Type;
import lombok.Data;

@Data
public class TopicGenerateBean {

  private Long sessionId;
  private Type type;
  private Long orgId;

}
