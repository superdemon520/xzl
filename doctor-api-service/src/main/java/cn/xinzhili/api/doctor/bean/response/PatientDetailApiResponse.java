package cn.xinzhili.api.doctor.bean.response;


import cn.xinzhili.api.doctor.bean.PatientApiInfo;
import org.apache.commons.lang.StringUtils;

/**
 * @author by Loki on 17/3/6.
 */
public class PatientDetailApiResponse {

  private PatientApiInfo patientInfo;
  private String doctorId;
  private String assistantId;
  private String operatorId;

  public boolean hasAssistant() {
    return StringUtils.isNotEmpty(assistantId);
  }

  public PatientApiInfo getPatientInfo() {
    return patientInfo;
  }

  public void setPatientInfo(PatientApiInfo patientInfo) {
    this.patientInfo = patientInfo;
  }

  public String getDoctorId() {
    return doctorId;
  }

  public void setDoctorId(String doctorId) {
    this.doctorId = doctorId;
  }

  public String getAssistantId() {
    return assistantId;
  }

  public void setAssistantId(String assistantId) {
    this.assistantId = assistantId;
  }

  public String getOperatorId() {
    return operatorId;
  }

  public void setOperatorId(String operatorId) {
    this.operatorId = operatorId;
  }
}
