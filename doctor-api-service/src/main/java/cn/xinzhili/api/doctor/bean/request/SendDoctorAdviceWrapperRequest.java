package cn.xinzhili.api.doctor.bean.request;

import cn.xinzhili.api.doctor.bean.InspectionStandards;
import cn.xinzhili.api.doctor.bean.response.MedicineAdjustmentsClient;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

public class SendDoctorAdviceWrapperRequest {

  private String patientId;
  private String question = "";
  private String advice;
  @NotNull
  private String reason = "";
  private int level;

  private int category;
  private MedicineAdjustmentsClient medicineSupplement;
  private InspectionStandards inspectionStandards;

  public String getPatientId() {
    return patientId;
  }

  public void setPatientId(String patientId) {
    this.patientId = patientId;
  }

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public String getAdvice() {
    return advice;
  }

  public void setAdvice(String advice) {
    this.advice = advice;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public int getCategory() {
    return category;
  }

  public void setCategory(int category) {
    this.category = category;
  }

  public MedicineAdjustmentsClient getMedicineSupplement() {
    return medicineSupplement;
  }

  public void setMedicineSupplement(
      MedicineAdjustmentsClient medicineSupplement) {
    this.medicineSupplement = medicineSupplement;
  }

  public InspectionStandards getInspectionStandards() {
    return inspectionStandards;
  }

  public void setInspectionStandards(InspectionStandards inspectionStandards) {
    this.inspectionStandards = inspectionStandards;
  }
}
