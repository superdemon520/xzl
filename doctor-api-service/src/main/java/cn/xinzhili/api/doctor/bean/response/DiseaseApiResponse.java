package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.DiseaseApiInfo;
import java.util.List;

/**
 * @author by Loki on 17/3/21.
 */
public class DiseaseApiResponse {

  private List<DiseaseApiInfo> disease;

  public List<DiseaseApiInfo> getDisease() {
    return disease;
  }

  public void setDisease(List<DiseaseApiInfo> disease) {
    this.disease = disease;
  }
}
