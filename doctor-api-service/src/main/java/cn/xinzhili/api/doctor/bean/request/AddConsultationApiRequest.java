package cn.xinzhili.api.doctor.bean.request;

import cn.xinzhili.dpc.api.ConsultationLevel;
import javax.validation.constraints.NotNull;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/3/27 上午11:31
 */
public class AddConsultationApiRequest {

  @NotNull(message = "患者id不能为空")
  private String patientId;
  @NotNull(message = "医生id不能为空")
  private String doctorId;
  @NotNull(message = "会诊医生id不能为空")
  private String consultationDoctorId;
  @NotNull(message = "会诊医生名称不能为空")
  private String consultationDoctorName;
  @NotNull(message = "会诊问题不能为空")
  private String question;
  @NotNull(message = "问题等级不能为空")
  private ConsultationLevel level;

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

  public ConsultationLevel getLevel() {
    return level;
  }

  public void setLevel(ConsultationLevel level) {
    this.level = level;
  }
}
