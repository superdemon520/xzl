package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.BasicTreatmentApiInfo;
import java.util.List;

public class BasicTreatmentsApiResponse {

  private List<BasicTreatmentApiInfo> treatmentInfos;

  public List<BasicTreatmentApiInfo> getTreatmentInfos() {
    return treatmentInfos;
  }

  public void setTreatmentInfos(List<BasicTreatmentApiInfo> treatmentInfos) {
    this.treatmentInfos = treatmentInfos;
  }
}
