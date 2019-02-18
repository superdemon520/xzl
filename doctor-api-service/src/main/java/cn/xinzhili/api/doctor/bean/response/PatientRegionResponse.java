package cn.xinzhili.api.doctor.bean.response;

/**
 * @author by marlinl on 24/03/2017.
 */
public class PatientRegionResponse {

  private String id;
  private String regionName;

  public PatientRegionResponse() {
  }

  public PatientRegionResponse(String id, String regionName) {
    this.id = id;
    this.regionName = regionName;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getRegionName() {
    return regionName;
  }

  public void setRegionName(String regionName) {
    this.regionName = regionName;
  }
}
