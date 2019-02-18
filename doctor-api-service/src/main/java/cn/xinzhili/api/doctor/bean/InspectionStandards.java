package cn.xinzhili.api.doctor.bean;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;

@JsonDeserialize(builder = InspectionStandards.Builder.class)
public class InspectionStandards {

  List<InspectionStandard> inspectionStandardList;

  private InspectionStandards(Builder builder) {
    setInspectionStandardList(builder.inspectionStandardList);
  }

  public static Builder builder() {
    return new Builder();
  }

  public List<InspectionStandard> getInspectionStandardList() {
    return inspectionStandardList;
  }

  public void setInspectionStandardList(
      List<InspectionStandard> inspectionStandardList) {
    this.inspectionStandardList = inspectionStandardList;
  }


  @JsonPOJOBuilder(withPrefix = "")
  public static final class Builder {

    private List<InspectionStandard> inspectionStandardList;

    private Builder() {
    }

    public Builder inspectionStandardList(List<InspectionStandard> val) {
      inspectionStandardList = val;
      return this;
    }

    public InspectionStandards build() {
      return new InspectionStandards(this);
    }
  }
}
