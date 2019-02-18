package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.InspectionStandards;
import cn.xinzhili.api.doctor.bean.request.MedicineAdjustmentsService;
import cn.xinzhili.xutils.core.util.HashUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by @xin.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DoctorAdviceResponse {

  private long id;
  private long patientId;
  private long doctorId;
  private Long assistantId;
  private String question;
  private String advice;
  private String reason;
  private int status;
  private int level;
  private long createdAt;
  private int category;
  private MedicineAdjustmentsService medicineSupplement;
  private AssaySupplement assaySupplement;
  private SideEffectSupplement sideEffectSupplement;
  private InspectionStandards inspectionStandards;

  public DoctorAdviceWrapperResponse toDoctorAdviceWrapperResponse() {
    return DoctorAdviceWrapperResponse.builder().id(HashUtils.encode(id))
        .patientId(HashUtils.encode(patientId)).doctorId(HashUtils.encode(doctorId))
        .assistantId(assistantId == null ? null : HashUtils.encode(assistantId))
        .question(question).advice(advice).reason(reason).status(status).level(level)
        .createdAt(createdAt).category(category)
        .medicineSupplement(
            medicineSupplement == null ? null : medicineSupplement.toMedicineAdjustmentsClient())
        .assaySupplement(assaySupplement).sideEffectSupplement(sideEffectSupplement)
        .inspectionStandards(inspectionStandards).build();
  }

  public DoctorAdviceWrapperDataResponse toDoctorAdviceWrapperDataResponse() {
    return DoctorAdviceWrapperDataResponse.builder().advice(toDoctorAdviceWrapperResponse())
        .build();
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getPatientId() {
    return patientId;
  }

  public void setPatientId(long patientId) {
    this.patientId = patientId;
  }

  public long getDoctorId() {
    return doctorId;
  }

  public void setDoctorId(long doctorId) {
    this.doctorId = doctorId;
  }

  public Long getAssistantId() {
    return assistantId;
  }

  public void setAssistantId(Long assistantId) {
    this.assistantId = assistantId;
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

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public long getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(long createdAt) {
    this.createdAt = createdAt;
  }

  public int getCategory() {
    return category;
  }

  public void setCategory(int category) {
    this.category = category;
  }

  public MedicineAdjustmentsService getMedicineSupplement() {
    return medicineSupplement;
  }

  public void setMedicineSupplement(
      MedicineAdjustmentsService medicineSupplement) {
    this.medicineSupplement = medicineSupplement;
  }

  public AssaySupplement getAssaySupplement() {
    return assaySupplement;
  }

  public void setAssaySupplement(AssaySupplement assaySupplement) {
    this.assaySupplement = assaySupplement;
  }

  public SideEffectSupplement getSideEffectSupplement() {
    return sideEffectSupplement;
  }

  public void setSideEffectSupplement(
      SideEffectSupplement sideEffectSupplement) {
    this.sideEffectSupplement = sideEffectSupplement;
  }

  public InspectionStandards getInspectionStandards() {
    return inspectionStandards;
  }

  public void setInspectionStandards(InspectionStandards inspectionStandards) {
    this.inspectionStandards = inspectionStandards;
  }
}
