package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.medicine.api.ManualInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * @author by Loki on 17/3/17.
 */
@JsonDeserialize(builder = ManualInfoResponse.Builder.class)
public class ManualInfoResponse {

  private ManualInfo manualInfo;

  public ManualInfo getManualInfo() {
    return manualInfo;
  }

  private ManualInfoResponse(Builder builder) {
    manualInfo = builder.manualInfo;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * {@code ManualInfoResponse} builder static inner class.
   */
  public static final class Builder {

    private ManualInfo manualInfo;

    private Builder() {
    }

    /**
     * Sets the {@code manualInfo} and returns a reference to this Builder so that the methods can
     * be chained together.
     *
     * @param val the {@code manualInfo} to set
     * @return a reference to this Builder
     */
    public Builder withManualInfo(ManualInfo val) {
      manualInfo = val;
      return this;
    }

    /**
     * Returns a {@code ManualInfoResponse} built from the parameters previously set.
     *
     * @return a {@code ManualInfoResponse} built with parameters of this {@code
     * ManualInfoResponse.Builder}
     */
    public ManualInfoResponse build() {
      return new ManualInfoResponse(this);
    }
  }
}
