package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.BasicHospitalApiInfo;
import cn.xinzhili.medical.api.BasicHospitalInfo;
import java.util.List;

/**
 * Created by dkw on 8/12/2017.
 */
public class BasicHospitalsApiResponse {

  private List<BasicHospitalApiInfo> basicHospitalInfos;

  public BasicHospitalsApiResponse() {
  }

  public BasicHospitalsApiResponse(
      List<BasicHospitalApiInfo> basicHospitalInfos) {
    this.basicHospitalInfos = basicHospitalInfos;
  }

  public List<BasicHospitalApiInfo> getBasicHospitalApiInfos() {
    return basicHospitalInfos;
  }

  public void seBasicHospitalApiInfos(
      List<BasicHospitalApiInfo> basicHospitalInfos) {
    this.basicHospitalInfos = basicHospitalInfos;
  }

}
