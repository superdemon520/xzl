package cn.xinzhili.api.doctor.bean;

import cn.xinzhili.medical.api.DiagnosisTreatmentAttachedInfo;
import java.util.List;

/**
 * @author by Loki on 17/3/21.
 */
public class TreatmentApiInfo {

  private String id;
  private String situation;
  private List<DiagnosisTreatmentAttachedInfo> attachedInfos;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getSituation() {
    return situation;
  }

  public void setSituation(String situation) {
    this.situation = situation;
  }

  public List<DiagnosisTreatmentAttachedInfo> getAttachedInfos() {
    return attachedInfos;
  }

  public void setAttachedInfos(
      List<DiagnosisTreatmentAttachedInfo> attachedInfos) {
    this.attachedInfos = attachedInfos;
  }
}
