package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.response.MedicineExternalDataResponse.Builder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;

/**
 * Created by @xin.
 */
@JsonDeserialize(builder = Builder.class)
public class MedicineExternalDataResponse {

  private List<MedicineUnionDataResponse> medicines;

  private MedicineExternalDataResponse(Builder builder) {
    setMedicines(builder.medicines);
  }

  public static Builder builder() {
    return new Builder();
  }

  public List<MedicineUnionDataResponse> getMedicines() {
    return medicines;
  }

  public void setMedicines(
      List<MedicineUnionDataResponse> medicines) {
    this.medicines = medicines;
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static final class Builder {

    private List<MedicineUnionDataResponse> medicines;

    private Builder() {
    }

    public Builder medicines(List<MedicineUnionDataResponse> val) {
      medicines = val;
      return this;
    }

    public MedicineExternalDataResponse build() {
      return new MedicineExternalDataResponse(this);
    }
  }
}
