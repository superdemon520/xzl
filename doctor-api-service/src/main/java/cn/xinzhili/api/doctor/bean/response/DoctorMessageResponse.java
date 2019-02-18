package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.DoctorApiMessage;
import java.util.List;

/**
 * @author by Loki on 17/4/10.
 */
public class DoctorMessageResponse {

  private List<DoctorApiMessage> messages;

  public List<DoctorApiMessage> getMessages() {
    return messages;
  }

  public void setMessages(List<DoctorApiMessage> messages) {
    this.messages = messages;
  }
}
