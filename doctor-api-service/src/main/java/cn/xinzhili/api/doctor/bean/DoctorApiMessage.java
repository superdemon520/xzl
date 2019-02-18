package cn.xinzhili.api.doctor.bean;

/**
 * @author by Loki on 17/4/10.
 */
public class DoctorApiMessage {

  private String id;
  private Long createdAt;
  private String sender;
  private String receiver;
  private Long readAt;
  private PatientContent content;
  private MessageDirection direction;
  private Integer doctorRead;
  private Integer assistantRead;
  private Long commitTimes;

  public Integer getAssistantRead() {
    return assistantRead;
  }

  public void setAssistantRead(Integer assistantRead) {
    this.assistantRead = assistantRead;
  }

  public Integer getDoctorRead() {
    return doctorRead;
  }

  public void setDoctorRead(Integer doctorRead) {
    this.doctorRead = doctorRead;
  }

  public Long getCommitTimes() {
    return commitTimes;
  }

  public void setCommitTimes(Long commitTimes) {
    this.commitTimes = commitTimes;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Long getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Long createdAt) {
    this.createdAt = createdAt;
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

  public Long getReadAt() {
    return readAt;
  }

  public void setReadAt(Long readAt) {
    this.readAt = readAt;
  }

  public PatientContent getContent() {
    return content;
  }

  public void setContent(PatientContent content) {
    this.content = content;
  }

  public MessageDirection getDirection() {
    return direction;
  }

  public void setDirection(MessageDirection direction) {
    this.direction = direction;
  }

  public static class PatientContent {

    private Integer type;
    private ContentData data;

    public Integer getType() {
      return type;
    }

    public void setType(Integer type) {
      this.type = type;
    }

    public ContentData getData() {
      return data;
    }

    public void setData(ContentData data) {
      this.data = data;
    }
  }

  public static class ContentData {

    private Integer type;
    private String content;

    public Integer getType() {
      return type;
    }

    public void setType(Integer type) {
      this.type = type;
    }

    public String getContent() {
      return content;
    }

    public void setContent(String content) {
      this.content = content;
    }
  }


}
