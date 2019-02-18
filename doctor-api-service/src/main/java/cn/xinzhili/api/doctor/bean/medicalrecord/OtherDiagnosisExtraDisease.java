package cn.xinzhili.api.doctor.bean.medicalrecord;

/**
 * @author by Loki on 17/6/8.
 */
public class OtherDiagnosisExtraDisease {

  private String diseaseId;
  private String diseaseName;
  private Integer since;
  private DiseaseApiCure diseaseCure;

  public String getDiseaseId() {
    return diseaseId;
  }

  public void setDiseaseId(String diseaseId) {
    this.diseaseId = diseaseId;
  }

  public String getDiseaseName() {
    return diseaseName;
  }

  public void setDiseaseName(String diseaseName) {
    this.diseaseName = diseaseName;
  }

  public Integer getSince() {
    return since;
  }

  public void setSince(Integer since) {
    this.since = since;
  }

  public DiseaseApiCure getDiseaseCure() {
    return diseaseCure;
  }

  public void setDiseaseCure(DiseaseApiCure diseaseCure) {
    this.diseaseCure = diseaseCure;
  }
}
