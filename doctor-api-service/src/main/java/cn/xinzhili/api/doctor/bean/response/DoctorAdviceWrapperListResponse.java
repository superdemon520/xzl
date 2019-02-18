package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.response.DoctorAdviceWrapperListResponse.Builder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

/**
 * Created by @xin.
 */
@JsonDeserialize(builder = Builder.class)
public class DoctorAdviceWrapperListResponse {

  private List<DoctorAdviceWrapperResponse> advices;

  private int total;

  public List<DoctorAdviceWrapperResponse> getAdvices() {
    return advices;
  }

  public int getTotal() {
    return total;
  }

  private DoctorAdviceWrapperListResponse(Builder builder) {
    advices = builder.advices;
    total = builder.total;
  }

  public static Builder newBuilder() {
    return new Builder();
  }


  /**
   * {@code DoctorAdviceWrapperListResponse} builder static inner class.
   */
  public static final class Builder {

    private List<DoctorAdviceWrapperResponse> advices;
    private int total;

    private Builder() {
    }

    /**
     * Sets the {@code advices} and returns a reference to this Builder so that the methods can be chained together.
     * @param val the {@code advices} to set
     * @return a reference to this Builder
     */
    public Builder advices(List<DoctorAdviceWrapperResponse> val) {
      advices = val;
      return this;
    }

    /**
     * Sets the {@code total} and returns a reference to this Builder so that the methods can be chained together.
     * @param val the {@code total} to set
     * @return a reference to this Builder
     */
    public Builder total(int val) {
      total = val;
      return this;
    }

    /**
     * Returns a {@code DoctorAdviceWrapperListResponse} built from the parameters previously set.
     *
     * @return a {@code DoctorAdviceWrapperListResponse} built with parameters of this {@code DoctorAdviceWrapperListResponse.Builder}
     */
    public DoctorAdviceWrapperListResponse build() {
      return new DoctorAdviceWrapperListResponse(this);
    }
  }
}
