package cn.xinzhili.api.doctor.bean;

import cn.xinzhili.medical.api.InfoType;
import java.util.List;

public class AttachedApiInfo {

  private InfoType infoType;
  private List<String> idList;

  public InfoType getInfoType() {
    return infoType;
  }

  public void setInfoType(InfoType infoType) {
    this.infoType = infoType;
  }

  public List<String> getIdList() {
    return idList;
  }

  public void setIdList(List<String> idList) {
    this.idList = idList;
  }
}
