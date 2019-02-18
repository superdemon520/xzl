package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.PatientApiInfo;
import java.util.List;

/**
 * @author by Loki on 17/2/27.
 */
public class PatientListApiResponse {

  private List<PatientApiInfo> patients;
  private Integer total;

  public List<PatientApiInfo> getPatients() {
    return patients;
  }

  public void setPatients(List<PatientApiInfo> patients) {
    this.patients = patients;
  }

  public Integer getTotal() {
    return total;
  }

  public void setTotal(Integer total) {
    this.total = total;
  }
}
