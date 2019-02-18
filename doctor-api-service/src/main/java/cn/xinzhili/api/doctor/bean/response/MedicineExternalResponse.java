package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.response.MedicineExternalResponse.Builder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;

/**
 * Created by @xin.
 */

@JsonDeserialize(builder = Builder.class)
public class MedicineExternalResponse {

  private List<MedicineUnionResponse> medicines;

  private MedicineExternalResponse(Builder builder) {
    setMedicines(builder.medicines);
  }

  public static Builder builder() {
    return new Builder();
  }

  public List<MedicineUnionResponse> getMedicines() {
    return medicines;
  }

  public void setMedicines(
      List<MedicineUnionResponse> medicines) {
    this.medicines = medicines;
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static final class Builder {

    private List<MedicineUnionResponse> medicines;

    private Builder() {
    }

    public Builder medicines(List<MedicineUnionResponse> val) {
      medicines = val;
      return this;
    }

    public MedicineExternalResponse build() {
      return new MedicineExternalResponse(this);
    }
  }
}
