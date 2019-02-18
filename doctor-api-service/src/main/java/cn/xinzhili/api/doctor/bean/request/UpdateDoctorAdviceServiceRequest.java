package cn.xinzhili.api.doctor.bean.request;

import cn.xinzhili.api.doctor.bean.InspectionStandards;
import javax.validation.Valid;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by @xin.
 */

public class UpdateDoctorAdviceServiceRequest {

  private String patientId;
  private long doctorId;
  @NotEmpty
  private String advice;
  private int status;
  private int category;
  private String reason;
  private String question;
  @Valid
  private MedicineAdjustmentsService medicineSupplement;
  private InspectionStandards inspectionStandards;

  public String getPatientId() {
    return patientId;
  }

  public void setPatientId(String patientId) {
    this.patientId = patientId;
  }

  public long getDoctorId() {
    return doctorId;
  }

  public void setDoctorId(long doctorId) {
    this.doctorId = doctorId;
  }

  public String getAdvice() {
    return advice;
  }

  public void setAdvice(String advice) {
    this.advice = advice;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public int getCategory() {
    return category;
  }

  public void setCategory(int category) {
    this.category = category;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public MedicineAdjustmentsService getMedicineSupplement() {
    return medicineSupplement;
  }

  public void setMedicineSupplement(
      MedicineAdjustmentsService medicineSupplement) {
    this.medicineSupplement = medicineSupplement;
  }

  public InspectionStandards getInspectionStandards() {
    return inspectionStandards;
  }

  public void setInspectionStandards(InspectionStandards inspectionStandards) {
    this.inspectionStandards = inspectionStandards;
  }
}
