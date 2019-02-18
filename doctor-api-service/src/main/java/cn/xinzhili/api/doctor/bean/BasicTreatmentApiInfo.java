package cn.xinzhili.api.doctor.bean;

import java.util.List;

public class BasicTreatmentApiInfo {

  private String id;
  private String name;
  private List<AttachedApiInfo> attachedInfos;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<AttachedApiInfo> getAttachedInfos() {
    return attachedInfos;
  }

  public void setAttachedInfos(List<AttachedApiInfo> attachedInfos) {
    this.attachedInfos = attachedInfos;
  }
}
