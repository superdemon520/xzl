package cn.xinzhili.api.doctor.bean.response;

import java.util.List;

/**
 * Created by @xin.
 */
public class DoctorAdviceListResponse {

  private List<DoctorAdviceResponse> advices;
  private int total;

  public List<DoctorAdviceResponse> getAdvices() {
    return advices;
  }

  public void setAdvices(List<DoctorAdviceResponse> advices) {
    this.advices = advices;
  }

  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
  }
}
