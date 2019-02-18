package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.BasicTreatmentApiInfo;

public class BasicTreatmentApiResponse {

  private BasicTreatmentApiInfo treatmentInfo;

  public BasicTreatmentApiResponse(BasicTreatmentApiInfo treatmentInfo) {
    this.treatmentInfo = treatmentInfo;
  }

  public BasicTreatmentApiInfo getTreatmentInfo() {

    return treatmentInfo;
  }

  public void setTreatmentInfo(BasicTreatmentApiInfo treatmentInfo) {
    this.treatmentInfo = treatmentInfo;
  }
}
