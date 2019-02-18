package cn.xinzhili.api.doctor.bean.request;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/3/23 下午6:00
 */
public class UpdateChatApiRequest {

  private String patientId;
  private Long commitTimes;

  public Long getCommitTimes() {
    return commitTimes;
  }

  public void setCommitTimes(Long commitTimes) {
    this.commitTimes = commitTimes;
  }

  public String getPatientId() {
    return patientId;
  }

  public void setPatientId(String patientId) {
    this.patientId = patientId;
  }
}
