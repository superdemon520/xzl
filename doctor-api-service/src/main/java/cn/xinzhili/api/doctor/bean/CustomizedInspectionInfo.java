package cn.xinzhili.api.doctor.bean;

/**
 * @author by Loki on 17/7/4.
 */
public class CustomizedInspectionInfo extends InspectionApiInfo {

  private String unifiedReference;
  private Double customizedReferenceMax;
  private Double customizedReferenceMin;
  private String spliceSymbol;

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
}
