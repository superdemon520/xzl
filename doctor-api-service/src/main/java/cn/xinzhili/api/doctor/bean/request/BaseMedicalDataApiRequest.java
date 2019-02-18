package cn.xinzhili.api.doctor.bean.request;

import cn.xinzhili.api.doctor.bean.BasicHospitalApiInfo;
import cn.xinzhili.api.doctor.bean.ReportApiStatus;
import java.util.Date;
import org.apache.commons.lang.StringUtils;

/**
 * @author by Loki on 17/3/16.
 */
class BaseMedicalDataApiRequest {

  private String imageId;
  private String patientId;
  private ReportApiStatus status;
  private Date reportAt;
  private BasicHospitalApiInfo hospital;

  public boolean invalid() {
    return StringUtils.isEmpty(patientId) ||
        (!StringUtils.isEmpty(imageId) && (status == null || reportAt == null));
  }

  public String getImageId() {
    return imageId;
  }

  public void setImageId(String imageId) {
    this.imageId = imageId;
  }

  public String getPatientId() {
    return patientId;
  }

  public void setPatientId(String patientId) {
    this.patientId = patientId;
  }

  public ReportApiStatus getStatus() {
    return status;
  }

  public void setStatus(ReportApiStatus status) {
    this.status = status;
  }

  public Date getReportAt() {
    return reportAt;
  }

  public void setReportAt(Date reportAt) {
    this.reportAt = reportAt;
  }

  public BasicHospitalApiInfo getHospital() {
    return hospital;
  }

  public void setHospital(BasicHospitalApiInfo hospital) {
    this.hospital = hospital;
  }
}
