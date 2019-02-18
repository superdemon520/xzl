package cn.xinzhili.api.doctor.bean;

import cn.xinzhili.api.doctor.bean.request.MedicationPlan.Status;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = InspectionStandardData.Builder.class)
public class InspectionStandardData {

  private String name;
  private Long measuredAt;
  private String value;
  private String unit;
  private InspectionApiStatus status;
  private String unifiedReference;
  private Double customizedReferenceMax;
  private Double customizedReferenceMin;
  private String spliceSymbol;
  private Status action;
  private Double originCustomizedReferenceMax;
  private Double originCustomizedReferenceMin;
  private String item;

  private InspectionStandardData(Builder builder) {
    setName(builder.name);
    setMeasuredAt(builder.measuredAt);
    setValue(builder.value);
    setUnit(builder.unit);
    setStatus(builder.status);
    setUnifiedReference(builder.unifiedReference);
    setCustomizedReferenceMax(builder.customizedReferenceMax);
    setCustomizedReferenceMin(builder.customizedReferenceMin);
    setSpliceSymbol(builder.spliceSymbol);
    setAction(builder.action);
    setOriginCustomizedReferenceMax(builder.originCustomizedReferenceMax);
    setOriginCustomizedReferenceMin(builder.originCustomizedReferenceMin);
    setItem(builder.item);
  }

  public static Builder builder() {
    return new Builder();
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getMeasuredAt() {
    return measuredAt;
  }

  public void setMeasuredAt(Long measuredAt) {
    this.measuredAt = measuredAt;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public InspectionApiStatus getStatus() {
    return status;
  }

  public void setStatus(InspectionApiStatus status) {
    this.status = status;
  }

  public String getUnifiedReference() {
    return unifiedReference;
  }

  public void setUnifiedReference(String unifiedReference) {
    this.unifiedReference = unifiedReference;
  }

  public Double getCustomizedReferenceMax() {
    return customizedReferenceMax;
  }

  public void setCustomizedReferenceMax(Double customizedReferenceMax) {
    this.customizedReferenceMax = customizedReferenceMax;
  }

  public Double getCustomizedReferenceMin() {
    return customizedReferenceMin;
  }

  public void setCustomizedReferenceMin(Double customizedReferenceMin) {
    this.customizedReferenceMin = customizedReferenceMin;
  }

  public String getSpliceSymbol() {
    return spliceSymbol;
  }

  public void setSpliceSymbol(String spliceSymbol) {
    this.spliceSymbol = spliceSymbol;
  }

  public Status getAction() {
    return action;
  }

  public void setAction(Status action) {
    this.action = action;
  }

  public Double getOriginCustomizedReferenceMax() {
    return originCustomizedReferenceMax;
  }

  public void setOriginCustomizedReferenceMax(Double originCustomizedReferenceMax) {
    this.originCustomizedReferenceMax = originCustomizedReferenceMax;
  }

  public Double getOriginCustomizedReferenceMin() {
    return originCustomizedReferenceMin;
  }

  public void setOriginCustomizedReferenceMin(Double originCustomizedReferenceMin) {
    this.originCustomizedReferenceMin = originCustomizedReferenceMin;
  }

  public String getItem() {
    return item;
  }

  public void setItem(String item) {
    this.item = item;
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static final class Builder {

    private String name;
    private Long measuredAt;
    private String value;
    private String unit;
    private InspectionApiStatus status;
    private String unifiedReference;
    private Double customizedReferenceMax;
    private Double customizedReferenceMin;
    private String spliceSymbol;
    private Status action;
    private Double originCustomizedReferenceMax;
    private Double originCustomizedReferenceMin;
    private String item;

    private Builder() {
    }

    public Builder name(String val) {
      name = val;
      return this;
    }

    public Builder measuredAt(Long val) {
      measuredAt = val;
      return this;
    }

    public Builder value(String val) {
      value = val;
      return this;
    }

    public Builder unit(String val) {
      unit = val;
      return this;
    }

    public Builder status(InspectionApiStatus val) {
      status = val;
      return this;
    }

    public Builder unifiedReference(String val) {
      unifiedReference = val;
      return this;
    }

    public Builder customizedReferenceMax(Double val) {
      customizedReferenceMax = val;
      return this;
    }

    public Builder customizedReferenceMin(Double val) {
      customizedReferenceMin = val;
      return this;
    }

    public Builder spliceSymbol(String val) {
      spliceSymbol = val;
      return this;
    }

    public Builder action(Status val) {
      action = val;
      return this;
    }

    public Builder originCustomizedReferenceMax(Double val) {
      originCustomizedReferenceMax = val;
      return this;
    }

    public Builder originCustomizedReferenceMin(Double val) {
      originCustomizedReferenceMin = val;
      return this;
    }

    public Builder item(String val) {
      item = val;
      return this;
    }

    public InspectionStandardData build() {
      return new InspectionStandardData(this);
    }
  }
}
