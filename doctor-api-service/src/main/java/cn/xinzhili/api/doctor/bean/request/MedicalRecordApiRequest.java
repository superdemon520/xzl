package cn.xinzhili.api.doctor.bean.request;

import cn.xinzhili.api.doctor.bean.medicalrecord.MedicalRecordInfo;
import javax.validation.Valid;
import org.apache.commons.lang.StringUtils;

/**
 * @author by Loki on 17/3/16.
 */
public class MedicalRecordApiRequest extends BaseMedicalDataApiRequest {

  @Valid
  private MedicalRecordInfo medicalRecordInfo;

  public boolean recordInvalid() {

    if (!StringUtils.isEmpty(super.getImageId()) && super.getStatus() == null) {
      return true;
    }

    if (super.getPatientId() == null || medicalRecordInfo == null) {
      return true;
    }
    return false;
  }

  public MedicalRecordInfo getMedicalRecordInfo() {
    return medicalRecordInfo;
  }

  public void setMedicalRecordInfo(
      MedicalRecordInfo medicalRecordInfo) {
    this.medicalRecordInfo = medicalRecordInfo;
  }
}
