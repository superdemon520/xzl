package cn.xinzhili.api.doctor.bean;

/**
 * @author by Loki on 17/3/28.
 */
public class InspectionApiInfo {

  private String name;
  private Long measuredAt;
  private String value;
  private String unit;
  private InspectionApiStatus status;
  private String abbreviation;

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

  public String getAbbreviation() {
    return abbreviation;
  }

  public void setAbbreviation(String abbreviation) {
    this.abbreviation = abbreviation;
  }
}
