package cn.xinzhili.api.doctor.bean.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * Created by @xin.
 */
@JsonDeserialize(builder = DoctorAdviceWrapperDataResponse.Builder.class)
public class DoctorAdviceWrapperDataResponse {

  private DoctorAdviceWrapperResponse advice;

  private DoctorAdviceWrapperDataResponse(Builder builder) {
    setAdvice(builder.advice);
  }

  public static Builder builder() {
    return new Builder();
  }

  public DoctorAdviceWrapperResponse getAdvice() {
    return advice;
  }

  public void setAdvice(DoctorAdviceWrapperResponse advice) {
    this.advice = advice;
  }


  /**
   * {@code DoctorAdviceWrapperDataResponse} builder static inner class.
   */
  @JsonPOJOBuilder(withPrefix = "")
  public static final class Builder {

    private DoctorAdviceWrapperResponse advice;

    private Builder() {
    }

    /**
     * Sets the {@code advice} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code advice} to set
     * @return a reference to this Builder
     */
    public Builder advice(DoctorAdviceWrapperResponse val) {
      advice = val;
      return this;
    }

    /**
     * Returns a {@code DoctorAdviceWrapperDataResponse} built from the parameters previously set.
     *
     * @return a {@code DoctorAdviceWrapperDataResponse} built with parameters of this {@code
     * DoctorAdviceWrapperDataResponse.Builder}
     */
    public DoctorAdviceWrapperDataResponse build() {
      return new DoctorAdviceWrapperDataResponse(this);
    }
  }
}
