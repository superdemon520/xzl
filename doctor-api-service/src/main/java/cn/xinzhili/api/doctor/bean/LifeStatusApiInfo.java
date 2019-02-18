package cn.xinzhili.api.doctor.bean;

import cn.xinzhili.medical.api.LivingHabitType;
import java.util.Date;

/**
 * Created by ywb on 18/4/2017.
 */
public class LifeStatusApiInfo {

  private String patientId;
  private LivingHabitType type;
  private Object content;
  private Date createdAt;

  public String getPatientId() {
    return patientId;
  }

  public void setPatientId(String patientId) {
    this.patientId = patientId;
  }

  public LivingHabitType getType() {
    return type;
  }

  public void setType(LivingHabitType type) {
    this.type = type;
  }

  public Object getContent() {
    return content;
  }

  public void setContent(Object content) {
    this.content = content;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }
}
