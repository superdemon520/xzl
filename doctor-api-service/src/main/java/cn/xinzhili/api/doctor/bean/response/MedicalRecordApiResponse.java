package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.medicalrecord.MedicalRecordInfo;

/**
 * @author by Loki on 17/3/17.
 */
public class MedicalRecordApiResponse extends BaseMedicalDataApiResponse {

  private MedicalRecordInfo medicalRecordInfo;

  public MedicalRecordInfo getMedicalRecordInfo() {
    return medicalRecordInfo;
  }

  public void setMedicalRecordInfo(
      MedicalRecordInfo medicalRecordInfo) {
    this.medicalRecordInfo = medicalRecordInfo;
  }
}
