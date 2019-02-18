package cn.xinzhili.api.doctor.bean.request;

import cn.xinzhili.api.doctor.bean.CoronaryStentErrorCode;
import cn.xinzhili.api.doctor.bean.DiagnosisStage3;
import cn.xinzhili.api.doctor.bean.TreatmentApiInfo;
import cn.xinzhili.medical.api.InfoType;
import cn.xinzhili.xutils.core.FailureException;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang.StringUtils;

/**
 * @author by Loki on 17/3/21.
 */
public class AddDiagnosisApiRequest
    extends BaseMedicalDataApiRequest {

  private String stage1;
  private List<String> stage2;
  private List<DiagnosisStage3> stage3;
  private List<TreatmentApiInfo> treatments;

  public boolean diagnosisInvalid() {
    return StringUtils.isEmpty(stage1);
  }

  public String getStage1() {
    return stage1;
  }

  public void setStage1(String stage1) {
    this.stage1 = stage1;
  }

  public List<String> getStage2() {
    return stage2;
  }

  public void setStage2(List<String> stage2) {
    this.stage2 = stage2;
  }

  public List<DiagnosisStage3> getStage3() {
    return stage3;
  }

  public void setStage3(List<DiagnosisStage3> stage3) {
    this.stage3 = stage3;
  }

  public List<TreatmentApiInfo> getTreatments() {
    return treatments;
  }

  public void setTreatments(List<TreatmentApiInfo> treatments) {
    this.treatments = treatments;
  }

  public void checkStentSize() {
    if (treatments.stream().anyMatch(
        treatmentApiInfo -> treatmentApiInfo.getAttachedInfos().stream()
            .anyMatch(diagnosisTreatmentAttachedInfo ->
                Objects
                    .equals(diagnosisTreatmentAttachedInfo.getInfoType(), InfoType.CORONARY_STENT)
                    && Objects.nonNull(diagnosisTreatmentAttachedInfo.getInfoDatas())
                    && diagnosisTreatmentAttachedInfo.getInfoDatas().size() > 10))) {
      throw new FailureException(CoronaryStentErrorCode.OUT_OF_MAX_SIZE);
    }
  }
}
