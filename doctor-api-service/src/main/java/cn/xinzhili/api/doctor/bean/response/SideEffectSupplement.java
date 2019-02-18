package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.response.SideEffectSupplement.Builder;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * Created by @xin.
 */
@JsonDeserialize(builder = Builder.class)
public class SideEffectSupplement {

  private String detailLink;

  private SideEffectSupplement(Builder builder) {
    detailLink = builder.detailLink;
  }

  public static Builder builder() {
    return new Builder();
  }

  public SideEffectSupplement() {
  }

  public SideEffectSupplement(String detailLink) {
    this.detailLink = detailLink;
  }


  public String getDetailLink() {
    return detailLink;
  }

  /**
   * {@code SideEffectSupplement} builder static inner class.
   */
  @JsonPOJOBuilder(withPrefix = "")
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static final class Builder {

    private String detailLink;

    private Builder() {
    }

    /**
     * Sets the {@code detailLink} and returns a reference to this Builder so that the methods can
     * be chained together.
     *
     * @param val the {@code detailLink} to set
     * @return a reference to this Builder
     */
    public Builder detailLink(String val) {
      detailLink = val;
      return this;
    }

    /**
     * Returns a {@code SideEffectSupplement} built from the parameters previously set.
     *
     * @return a {@code SideEffectSupplement} built with parameters of this {@code
     * SideEffectSupplement.Builder}
     */
    public SideEffectSupplement build() {
      return new SideEffectSupplement(this);
    }
  }
}