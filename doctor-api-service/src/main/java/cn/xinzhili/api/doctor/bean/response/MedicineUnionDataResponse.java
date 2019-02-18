package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.response.MedicineUnionDataResponse.Builder;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * Created by @xin.
 */

@JsonDeserialize(builder = Builder.class)
public class MedicineUnionDataResponse {

  private String id;
  private String name;

  private MedicineUnionDataResponse(Builder builder) {
    id = builder.id;
    name = builder.name;
  }

  public static Builder builder() {
    return new Builder();
  }

  public MedicineUnionDataResponse() {
  }

  public MedicineUnionDataResponse(String id, String name) {
    this.id = id;
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }


  /**
   * {@code MedicineUnionDataResponse} builder static inner class.
   */
  @JsonPOJOBuilder(withPrefix = "")
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static final class Builder {

    private String id;
    private String name;

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
     * Sets the {@code name} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code name} to set
     * @return a reference to this Builder
     */
    public Builder name(String val) {
      name = val;
      return this;
    }

    /**
     * Returns a {@code MedicineUnionDataResponse} built from the parameters previously set.
     *
     * @return a {@code MedicineUnionDataResponse} built with parameters of this {@code
     * MedicineUnionDataResponse.Builder}
     */
    public MedicineUnionDataResponse build() {
      return new MedicineUnionDataResponse(this);
    }
  }
}
