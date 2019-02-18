package cn.xinzhili.api.doctor.bean.request;

import cn.xinzhili.api.doctor.bean.request.PatientNotificationRequest.Builder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * Created by @xin.
 */
@JsonDeserialize(builder = Builder.class)
public class PatientNotificationRequest {

  private long patientId;
  private int count;

  private PatientNotificationRequest(Builder builder) {
    setPatientId(builder.patientId);
    setCount(builder.count);
  }

  public static Builder builder() {
    return new Builder();
  }

  public long getPatientId() {
    return patientId;
  }

  public void setPatientId(long patientId) {
    this.patientId = patientId;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }


  /**
   * {@code PatientNotificationRequest} builder static inner class.
   */
  @JsonPOJOBuilder(withPrefix = "")
  public static final class Builder {

    private long patientId;
    private int count;

    private Builder() {
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
     * Sets the {@code count} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code count} to set
     * @return a reference to this Builder
     */
    public Builder count(int val) {
      count = val;
      return this;
    }

    /**
     * Returns a {@code PatientNotificationRequest} built from the parameters previously set.
     *
     * @return a {@code PatientNotificationRequest} built with parameters of this {@code
     * PatientNotificationRequest.Builder}
     */
    public PatientNotificationRequest build() {
      return new PatientNotificationRequest(this);
    }
  }
}