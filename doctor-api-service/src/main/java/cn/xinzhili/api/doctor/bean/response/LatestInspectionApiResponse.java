package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.CustomizedInspectionInfo;
import java.util.List;

/**
 * @author by Loki on 17/3/28.
 */
public class LatestInspectionApiResponse {

  private List<CustomizedInspectionInfo> inspections;

  public List<CustomizedInspectionInfo> getInspections() {
    return inspections;
  }

  public void setInspections(
      List<CustomizedInspectionInfo> inspections) {
    this.inspections = inspections;
  }
}
