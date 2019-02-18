package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.ConsultationInfo;
import java.util.List;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/4/2 上午11:11
 */
public class ConsultationListResponse {

  private List<ConsultationInfo> consultationInfos;
  private Integer total;

  public Integer getTotal() {
    return total;
  }

  public void setTotal(Integer total) {
    this.total = total;
  }

  public List<ConsultationInfo> getConsultationInfos() {
    return consultationInfos;
  }

  public void setConsultationInfos(
      List<ConsultationInfo> consultationInfos) {
    this.consultationInfos = consultationInfos;
  }

  public ConsultationListResponse() {
  }

  public ConsultationListResponse(
      List<ConsultationInfo> consultationInfos, Integer total) {
    this.consultationInfos = consultationInfos;
    this.total = total;
  }
}
