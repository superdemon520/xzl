package cn.xinzhili.api.doctor.bean;

import cn.xinzhili.chat.api.MessageType;
import lombok.Data;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/5/9 下午2:03
 */
@Data
public class MessageApiInfo {

  private MessageType type;
  private String content;
}
