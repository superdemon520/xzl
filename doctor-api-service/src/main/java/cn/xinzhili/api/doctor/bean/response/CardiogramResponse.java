package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.BasicHospitalApiInfo;
import cn.xinzhili.api.doctor.bean.InspectionApiInfo;
import java.util.List;

/**
 * @author by Loki on 17/6/6.
 */
public class CardiogramResponse {

  private List<InspectionApiInfo> cardiogramInfo;
  private BasicHospitalApiInfo hospital;

  public List<InspectionApiInfo> getCardiogramInfo() {
    return cardiogramInfo;
  }

  public void setCardiogramInfo(
      List<InspectionApiInfo> cardiogramInfo) {
    this.cardiogramInfo = cardiogramInfo;
  }

  public BasicHospitalApiInfo getHospital() {
    return hospital;
  }

  public void setHospital(BasicHospitalApiInfo hospital) {
    this.hospital = hospital;
  }
}
