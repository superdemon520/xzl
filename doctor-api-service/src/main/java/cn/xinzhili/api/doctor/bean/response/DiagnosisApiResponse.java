package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.DiagnosisApiInfo;
import java.util.List;

/**
 * @author by Loki on 17/3/21.
 */
public class DiagnosisApiResponse
    extends BaseMedicalDataApiResponse {

  private List<DiagnosisApiInfo> diagnosisInfo;

  public List<DiagnosisApiInfo> getDiagnosisInfo() {
    return diagnosisInfo;
  }

  public void setDiagnosisInfo(List<DiagnosisApiInfo> diagnosisInfo) {
    this.diagnosisInfo = diagnosisInfo;
  }
}
