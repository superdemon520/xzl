package cn.xinzhili.api.doctor.bean;

/**
 * @author by Loki on 17/3/31.
 */
public class DiagnosisStage3Info {

  private String fatherId;
  private String diseaseId;
  private String name;

  public String getFatherId() {
    return fatherId;
  }

  public void setFatherId(String fatherId) {
    this.fatherId = fatherId;
  }

  public String getDiseaseId() {
    return diseaseId;
  }

  public void setDiseaseId(String diseaseId) {
    this.diseaseId = diseaseId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
