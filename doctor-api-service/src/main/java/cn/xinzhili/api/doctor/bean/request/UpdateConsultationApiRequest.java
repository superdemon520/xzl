package cn.xinzhili.api.doctor.bean.request;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/3/27 上午11:31
 */
public class UpdateConsultationApiRequest {

  @NotNull(message = "id不能为空")
  private String id;
  @NotEmpty(message = "会诊结果不能为空")
  private String answer;
  @NotNull(message = "会诊人员不能为空")
  private String patientId;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getPatientId() {
    return patientId;
  }

  public void setPatientId(String patientId) {
    this.patientId = patientId;
  }

  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }
}
