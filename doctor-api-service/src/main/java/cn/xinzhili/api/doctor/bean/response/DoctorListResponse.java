package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.Doctor;
import cn.xinzhili.api.doctor.bean.User;

import java.util.List;

/**
 * @author by Loki on 17/2/24.
 */
public class DoctorListResponse {

  private List<Doctor> doctors;
  private Integer total;

  public List<Doctor> getDoctors() {
    return doctors;
  }

  public void setDoctors(List<Doctor> doctors) {
    this.doctors = doctors;
  }

  public Integer getTotal() {
    return total;
  }

  public void setTotal(Integer total) {
    this.total = total;
  }
}
