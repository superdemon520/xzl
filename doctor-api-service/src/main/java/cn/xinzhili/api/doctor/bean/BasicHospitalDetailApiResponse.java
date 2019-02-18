package cn.xinzhili.api.doctor.bean;

import cn.xinzhili.medical.api.BasicHospitalInfo;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2017/12/18 下午5:34
 */
public class BasicHospitalDetailApiResponse {

  private BasicHospitalApiInfo hospital;

  public BasicHospitalApiInfo getHospital() {
    return hospital;
  }

  public void setHospital(BasicHospitalApiInfo hospital) {
    this.hospital = hospital;
  }

  public BasicHospitalDetailApiResponse() {
  }

  public BasicHospitalDetailApiResponse(BasicHospitalApiInfo hospital) {

    this.hospital = hospital;
  }
}