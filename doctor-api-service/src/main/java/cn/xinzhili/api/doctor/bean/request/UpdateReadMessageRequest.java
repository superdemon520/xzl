package cn.xinzhili.api.doctor.bean.request;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/3/26 上午10:24
 */
public class UpdateReadMessageRequest {

  private String sender;
  private String receiver;
  private Long commitTimes;
  private Boolean isDoctor;

  public Boolean getIsDoctor() {
    return isDoctor;
  }

  public void setIsDoctor(Boolean isDoctor) {
    isDoctor = isDoctor;
  }

  public Long getCommitTimes() {
    return commitTimes;
  }

  public void setCommitTimes(Long commitTimes) {
    this.commitTimes = commitTimes;
  }

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

  public UpdateReadMessageRequest() {
  }

  public UpdateReadMessageRequest(String sender, String receiver, Long commitTimes,
      boolean isDoctor) {
    this.sender = sender;
    this.receiver = receiver;
    this.commitTimes = commitTimes;
    this.isDoctor = isDoctor;
  }
}
