package cn.xinzhili.api.doctor.bean.request;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/3/26 上午10:24
 */
public class UpdateMessageRequest {
  private String sender;
  private String receiver;

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

  public UpdateMessageRequest() {
  }

  public UpdateMessageRequest(String sender, String receiver) {
    this.sender = sender;
    this.receiver = receiver;
  }
}
