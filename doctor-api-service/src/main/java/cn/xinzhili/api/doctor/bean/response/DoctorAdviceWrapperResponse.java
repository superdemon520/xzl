package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.InspectionStandards;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * Created by @xin.
 */

@JsonDeserialize(builder = DoctorAdviceWrapperResponse.Builder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DoctorAdviceWrapperResponse {

  private String id;
  private String patientId;
  private String doctorId;
  private String assistantId;
  private String question;
  private String advice;
  private String reason;
  private int status;
  private int level;
  private long createdAt;
  private int category;
  private MedicineAdjustmentsClient medicineSupplement;
  private AssaySupplement assaySupplement;
  private SideEffectSupplement sideEffectSupplement;
  private InspectionStandards inspectionStandards;

  private DoctorAdviceWrapperResponse(Builder builder) {
    setId(builder.id);
    setPatientId(builder.patientId);
    setDoctorId(builder.doctorId);
    setAssistantId(builder.assistantId);
    setQuestion(builder.question);
    setAdvice(builder.advice);
    setReason(builder.reason);
    setStatus(builder.status);
    setLevel(builder.level);
    setCreatedAt(builder.createdAt);
    setCategory(builder.category);
    setMedicineSupplement(builder.medicineSupplement);
    setAssaySupplement(builder.assaySupplement);
    setSideEffectSupplement(builder.sideEffectSupplement);
    setInspectionStandards(builder.inspectionStandards);
  }

  public static Builder builder() {
    return new Builder();
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

  public String getAssistantId() {
    return assistantId;
  }

  public void setAssistantId(String assistantId) {
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

  public MedicineAdjustmentsClient getMedicineSupplement() {
    return medicineSupplement;
  }

  public void setMedicineSupplement(
      MedicineAdjustmentsClient medicineSupplement) {
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

  /**
   * {@code DoctorAdviceWrapperResponse} builder static inner class.
   */
  @JsonPOJOBuilder(withPrefix = "")
  public static final class Builder {

    private String id;
    private String patientId;
    private String doctorId;
    private String assistantId;
    private String question;
    private String advice;
    private String reason;
    private int status;
    private int level;
    private long createdAt;
    private int category;
    private MedicineAdjustmentsClient medicineSupplement;
    private AssaySupplement assaySupplement;
    private SideEffectSupplement sideEffectSupplement;
    private InspectionStandards inspectionStandards;

    private Builder() {
    }

    /**
     * Sets the {@code id} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code id} to set
     * @return a reference to this Builder
     */
    public Builder id(String val) {
      id = val;
      return this;
    }

    /**
     * Sets the {@code patientId} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code patientId} to set
     * @return a reference to this Builder
     */
    public Builder patientId(String val) {
      patientId = val;
      return this;
    }

    /**
     * Sets the {@code doctorId} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code doctorId} to set
     * @return a reference to this Builder
     */
    public Builder doctorId(String val) {
      doctorId = val;
      return this;
    }

    /**
     * Sets the {@code assistantId} and returns a reference to this Builder so that the methods can
     * be chained together.
     *
     * @param val the {@code assistantId} to set
     * @return a reference to this Builder
     */
    public Builder assistantId(String val) {
      assistantId = val;
      return this;
    }

    /**
     * Sets the {@code question} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code question} to set
     * @return a reference to this Builder
     */
    public Builder question(String val) {
      question = val;
      return this;
    }

    /**
     * Sets the {@code advice} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code advice} to set
     * @return a reference to this Builder
     */
    public Builder advice(String val) {
      advice = val;
      return this;
    }

    /**
     * Sets the {@code reason} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code reason} to set
     * @return a reference to this Builder
     */
    public Builder reason(String val) {
      reason = val;
      return this;
    }

    /**
     * Sets the {@code status} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code status} to set
     * @return a reference to this Builder
     */
    public Builder status(int val) {
      status = val;
      return this;
    }

    /**
     * Sets the {@code level} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code level} to set
     * @return a reference to this Builder
     */
    public Builder level(int val) {
      level = val;
      return this;
    }

    /**
     * Sets the {@code createdAt} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code createdAt} to set
     * @return a reference to this Builder
     */
    public Builder createdAt(long val) {
      createdAt = val;
      return this;
    }

    /**
     * Sets the {@code category} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code category} to set
     * @return a reference to this Builder
     */
    public Builder category(int val) {
      category = val;
      return this;
    }

    /**
     * Sets the {@code medicineSupplement} and returns a reference to this Builder so that the
     * methods can be chained together.
     *
     * @param val the {@code medicineSupplement} to set
     * @return a reference to this Builder
     */
    public Builder medicineSupplement(MedicineAdjustmentsClient val) {
      medicineSupplement = val;
      return this;
    }

    /**
     * Sets the {@code assaySupplement} and returns a reference to this Builder so that the methods
     * can be chained together.
     *
     * @param val the {@code assaySupplement} to set
     * @return a reference to this Builder
     */
    public Builder assaySupplement(AssaySupplement val) {
      assaySupplement = val;
      return this;
    }

    /**
     * Sets the {@code sideEffectSupplement} and returns a reference to this Builder so that the
     * methods can be chained together.
     *
     * @param val the {@code sideEffectSupplement} to set
     * @return a reference to this Builder
     */
    public Builder sideEffectSupplement(SideEffectSupplement val) {
      sideEffectSupplement = val;
      return this;
    }

    public Builder inspectionStandards(InspectionStandards val) {
      inspectionStandards = val;
      return this;
    }

    /**
     * Returns a {@code DoctorAdviceWrapperResponse} built from the parameters previously set.
     *
     * @return a {@code DoctorAdviceWrapperResponse} built with parameters of this {@code
     * DoctorAdviceWrapperResponse.Builder}
     */
    public DoctorAdviceWrapperResponse build() {
      return new DoctorAdviceWrapperResponse(this);
    }
  }
}
