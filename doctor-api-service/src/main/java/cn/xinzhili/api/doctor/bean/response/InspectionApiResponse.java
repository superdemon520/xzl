package cn.xinzhili.api.doctor.bean.response;

import java.util.List;

/**
 * @author by Loki on 17/4/17.
 */
public class InspectionApiResponse extends BaseMedicalDataApiResponse {

  private List<ItemInfo> inspections;

  public static class ItemInfo {

    private String id;
    private String referenceId;
    private Double value;

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getReferenceId() {
      return referenceId;
    }

    public void setReferenceId(String referenceId) {
      this.referenceId = referenceId;
    }

    public Double getValue() {
      return value;
    }

    public void setValue(Double value) {
      this.value = value;
    }
  }

  public List<ItemInfo> getInspections() {
    return inspections;
  }

  public void setInspections(
      List<ItemInfo> inspections) {
    this.inspections = inspections;
  }
}
