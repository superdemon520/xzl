package cn.xinzhili.api.doctor.bean;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author by Loki on 16/9/28.
 */
public class DoctorMessage {

  private String sender;
  @NotEmpty
  private String receiver;
  @NotEmpty
  private String type;
  @NotEmpty
  private String content;

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }

  public String getReceiver() {
    return receiver;
  }

  public void setReceiver(String receiver) {
    this.receiver = receiver;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}

