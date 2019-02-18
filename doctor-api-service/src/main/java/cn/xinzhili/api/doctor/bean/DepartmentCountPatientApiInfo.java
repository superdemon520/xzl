package cn.xinzhili.api.doctor.bean;

/**
 * Date: 13/02/2017 Time: 4:06 PM
 *
 * @author Gan Dong
 */

public class DepartmentCountPatientApiInfo {

  private String name;
  private Integer patientCount;


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getPatientCount() {
    return patientCount;
  }

  public void setPatientCount(Integer patientCount) {
    this.patientCount = patientCount;
  }

  @Override
  public String toString() {
    return "DepartmentInfo{" +
        ", name='" + name + '\'' +
        ", patientCount=" + patientCount +
        '}';
  }
}
