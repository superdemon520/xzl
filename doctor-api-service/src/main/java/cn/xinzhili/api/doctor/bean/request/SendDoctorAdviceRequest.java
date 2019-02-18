package cn.xinzhili.api.doctor.bean.request;

import cn.xinzhili.api.doctor.bean.InspectionStandards;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import javax.validation.Valid;

/**
 * Created by @xin.
 */
public class SendDoctorAdviceRequest {

  private Long id;
  private long patientId;
  private long doctorId;
  private long assistantId;
  private String question;
  private String advice;
  private String reason;
  private int status;
  private int level;
  @Valid
  private MedicineAdjustmentsService medicineSupplement;
  private InspectionStandards inspectionStandards;
  private int category;

  private SendDoctorAdviceRequest(Builder builder) {
    setId(builder.id);
    setPatientId(builder.patientId);
    setDoctorId(builder.doctorId);
    setAssistantId(builder.assistantId);
    setQuestion(builder.question);
    setAdvice(builder.advice);
    setReason(builder.reason);
    setStatus(builder.status);
    setLevel(builder.level);
    setMedicineSupplement(builder.medicineSupplement);
    setInspectionStandards(builder.inspectionStandards);
    setCategory(builder.category);
  }

  public static Builder builder() {
    return new Builder();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
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

  public long getAssistantId() {
    return assistantId;
  }

  public void setAssistantId(long assistantId) {
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

  public MedicineAdjustmentsService getMedicineSupplement() {
    return medicineSupplement;
  }

  public void setMedicineSupplement(
      MedicineAdjustmentsService medicineSupplement) {
    this.medicineSupplement = medicineSupplement;
  }

  public int getCategory() {
    return category;
  }

  public void setCategory(int category) {
    this.category = category;
  }

  public InspectionStandards getInspectionStandards() {
    return inspectionStandards;
  }

  public void setInspectionStandards(InspectionStandards inspectionStandards) {
    this.inspectionStandards = inspectionStandards;
  }

  /**
   * {@code SendDoctorAdviceRequest} builder static inner class.
   */
  @JsonPOJOBuilder(withPrefix = "")
  public static final class Builder {

    private Long id;
    private long patientId;
    private long doctorId;
    private long assistantId;
    private String question;
    private String advice;
    private String reason;
    private int status;
    private int level;
    private MedicineAdjustmentsService medicineSupplement;
    private InspectionStandards inspectionStandards;
    private int category;

    private Builder() {
    }

    /**
     * Sets the {@code id} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code id} to set
     * @return a reference to this Builder
     */
    public Builder id(Long val) {
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
    public Builder patientId(long val) {
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
    public Builder doctorId(long val) {
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
    public Builder assistantId(long val) {
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
     * Sets the {@code medicineSupplement} and returns a reference to this Builder so that the
     * methods can be chained together.
     *
     * @param val the {@code medicineSupplement} to set
     * @return a reference to this Builder
     */
    public Builder medicineSupplement(MedicineAdjustmentsService val) {
      medicineSupplement = val;
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

    public Builder inspectionStandards(InspectionStandards val) {
      inspectionStandards = val;
      return this;
    }

    /**
     * Returns a {@code SendDoctorAdviceRequest} built from the parameters previously set.
     *
     * @return a {@code SendDoctorAdviceRequest} built with parameters of this {@code
     * SendDoctorAdviceRequest.Builder}
     */
    public SendDoctorAdviceRequest build() {
      return new SendDoctorAdviceRequest(this);
    }
  }
}