package cn.xinzhili.api.doctor.bean.request;

import cn.xinzhili.api.doctor.bean.ApiCustomizableItem;
import cn.xinzhili.user.api.util.StringUtils;

/**
 * @author by Loki on 17/7/4.
 */
public class AddInspectionReferenceRequest {

  private String patientId;
  private ApiCustomizableItem item;
  private Double referenceMax;
  private Double referenceMin;

  public boolean invalid() {
    return StringUtils.isEmpty(patientId) || item == null || referenceMax == null
        || referenceMin == null;
  }

  public String getPatientId() {
    return patientId;
  }

  public void setPatientId(String patientId) {
    this.patientId = patientId;
  }

  public ApiCustomizableItem getItem() {
    return item;
  }

  public void setItem(ApiCustomizableItem item) {
    this.item = item;
  }

  public Double getReferenceMax() {
    return referenceMax;
  }

  public void setReferenceMax(Double referenceMax) {
    this.referenceMax = referenceMax;
  }

  public Double getReferenceMin() {
    return referenceMin;
  }

  public void setReferenceMin(Double referenceMin) {
    this.referenceMin = referenceMin;
  }
}
