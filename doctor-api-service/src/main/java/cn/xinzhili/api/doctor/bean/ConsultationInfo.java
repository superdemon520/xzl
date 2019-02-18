package cn.xinzhili.api.doctor.bean;

import cn.xinzhili.dpc.api.ConsultationLevel;
import cn.xinzhili.dpc.api.ConsultationStatus;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/4/2 上午11:13
 */
public class ConsultationInfo {

  private String id;
  private String patientId;
  private String doctorId;
  private String consultationDoctorId;
  private String consultationDoctorName;
  private String question;
  private String answer;
  private Long updatedAt;
  private ConsultationStatus status;
  private ConsultationLevel level;

  public Long getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Long updatedAt) {
    this.updatedAt = updatedAt;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getPatientId() {
    return patientId;
  }

  public void setPatientId(String patientId) {
    this.patientId = patientId;
  }

  public String getDoctorId() {
    return doctorId;
  }

  public void setDoctorId(String doctorId) {
    this.doctorId = doctorId;
  }

  public String getConsultationDoctorId() {
    return consultationDoctorId;
  }

  public void setConsultationDoctorId(String consultationDoctorId) {
    this.consultationDoctorId = consultationDoctorId;
  }

  public String getConsultationDoctorName() {
    return consultationDoctorName;
  }

  public void setConsultationDoctorName(String consultationDoctorName) {
    this.consultationDoctorName = consultationDoctorName;
  }

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }

  public ConsultationStatus getStatus() {
    return status;
  }

  public void setStatus(ConsultationStatus status) {
    this.status = status;
  }

  public ConsultationLevel getLevel() {
    return level;
  }

  public void setLevel(ConsultationLevel level) {
    this.level = level;
  }
}
