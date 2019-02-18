package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.ConsultationPatientApiInfo;
import java.util.List;

/**
 * @author by Loki on 17/2/27.
 */

public class ConsultationPatientListApiResponse {

  private List<ConsultationPatientApiInfo> patients;
  private Integer total;

  public List<ConsultationPatientApiInfo> getPatients() {
    return patients;
  }

  public void setPatients(List<ConsultationPatientApiInfo> patients) {
    this.patients = patients;
  }

  public Integer getTotal() {
    return total;
  }

  public void setTotal(Integer total) {
    this.total = total;
  }

  @Override
  public String toString() {
    return "PatientListResponse{" +
        "patients=" + patients +
        ", total=" + total +
        '}';
  }
}
