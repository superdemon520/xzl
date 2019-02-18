package cn.xinzhili.api.doctor.bean.response;

import java.util.List;

/**
 * @author by Loki on 17/8/8.
 */
public class PatientRegionListResponse {

  private List<String> regions;

  public PatientRegionListResponse() {
  }

  public PatientRegionListResponse(List<String> regions) {
    this.regions = regions;
  }

  public List<String> getRegions() {
    return regions;
  }

  public void setRegions(List<String> regions) {
    this.regions = regions;
  }
}
