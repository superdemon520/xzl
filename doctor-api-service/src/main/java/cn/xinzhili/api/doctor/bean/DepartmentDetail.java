package cn.xinzhili.api.doctor.bean;

/**
 * Date: 07/03/2017 Time: 5:03 PM
 *
 * @author Gan Dong
 */
public class DepartmentDetail extends Department {

  private int assistantCount;

  private int doctorCount;

  private int patientCount;

  private int operatorCount;

  public int getAssistantCount() {
    return assistantCount;
  }

  public void setAssistantCount(int assistantCount) {
    this.assistantCount = assistantCount;
  }

  public int getDoctorCount() {
    return doctorCount;
  }

  public void setDoctorCount(int doctorCount) {
    this.doctorCount = doctorCount;
  }

  public int getPatientCount() {
    return patientCount;
  }

  public void setPatientCount(int patientCount) {
    this.patientCount = patientCount;
  }

  public int getOperatorCount() {
    return operatorCount;
  }

  public void setOperatorCount(int operatorCount) {
    this.operatorCount = operatorCount;
  }
}
