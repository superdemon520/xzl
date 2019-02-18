package cn.xinzhili.api.doctor.bean.request;

import cn.xinzhili.api.doctor.bean.PatientApiProgress;
import java.util.Objects;

/**
 * @author by Loki on 17/3/6.
 */
public class UpdatePatientRemarkApiRequest {

  private String remark;
  private PatientApiProgress progress;

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public PatientApiProgress getProgress() {
    return progress;
  }

  public void setProgress(PatientApiProgress progress) {
    this.progress = progress;
  }

  public boolean invalid() {
    return Objects.isNull(progress);
  }
}
