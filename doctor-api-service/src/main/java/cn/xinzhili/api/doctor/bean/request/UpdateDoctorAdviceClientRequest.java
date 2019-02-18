package cn.xinzhili.api.doctor.bean.request;

import cn.xinzhili.api.doctor.bean.InspectionStandards;
import cn.xinzhili.api.doctor.bean.response.MedicineAdjustmentsClient;
import java.util.Objects;
import javax.validation.Valid;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by @xin.
 */
public class UpdateDoctorAdviceClientRequest {

  private String patientId;
  private long doctorId;
  @NotEmpty
  private String advice;
  private int status;
  private String reason;
  private int category;
  private String question;
  @Valid
  private MedicineAdjustmentsClient medicineSupplement;
  private InspectionStandards inspectionStandards;

  public UpdateDoctorAdviceServiceRequest toUpdateDoctorAdviceServiceRequest() {
    UpdateDoctorAdviceServiceRequest request = new UpdateDoctorAdviceServiceRequest();
    request.setAdvice(advice);
    request.setCategory(category);
    request.setDoctorId(doctorId);
    request.setMedicineSupplement(Objects.isNull(medicineSupplement) ? null
        : medicineSupplement.toMedicineAdjustmentsService());
    request.setStatus(status);
    request.setPatientId(patientId);
    request.setReason(reason);
    request.setQuestion(question);
    request.setInspectionStandards(inspectionStandards);
    return request;
  }

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

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public int getCategory() {
    return category;
  }

  public void setCategory(int category) {
    this.category = category;
  }

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
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
