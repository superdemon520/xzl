package cn.xinzhili.api.doctor.bean.request;

import cn.xinzhili.api.doctor.bean.DoctorTitle;

/**
 * @author by Loki on 17/2/24.
 */
public class UpdateDoctorRequest extends UpdateUserRequest {

  private DoctorTitle title;

  public DoctorTitle getTitle() {
    return title;
  }

  public void setTitle(DoctorTitle title) {
    this.title = title;
  }
}
