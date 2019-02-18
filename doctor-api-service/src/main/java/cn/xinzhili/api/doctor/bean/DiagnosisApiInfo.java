package cn.xinzhili.api.doctor.bean;

import java.util.List;

/**
 * @author by Loki on 17/3/21.
 */
public class DiagnosisApiInfo {

  private String id;
  private DiseaseApiInfo stage1;
  private List<DiseaseApiInfo> stage2;
  private List<DiagnosisStage3Info> stage3;
  private List<TreatmentApiInfo> treatments;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public DiseaseApiInfo getStage1() {
    return stage1;
  }

  public void setStage1(DiseaseApiInfo stage1) {
    this.stage1 = stage1;
  }

  public List<DiseaseApiInfo> getStage2() {
    return stage2;
  }

  public void setStage2(List<DiseaseApiInfo> stage2) {
    this.stage2 = stage2;
  }

  public List<DiagnosisStage3Info> getStage3() {
    return stage3;
  }

  public void setStage3(List<DiagnosisStage3Info> stage3) {
    this.stage3 = stage3;
  }

  public List<TreatmentApiInfo> getTreatments() {
    return treatments;
  }

  public void setTreatments(List<TreatmentApiInfo> treatments) {
    this.treatments = treatments;
  }
}
