package cn.xinzhili.api.doctor.bean;


import cn.xinzhili.medical.api.StandardRateInfo;
import java.util.List;

public class DepartmentStandardApiInfo {

  private String departmentName;
  private List<StandardRateApiInfo> standardRateInfos;

  public String getDepartmentName() {
    return departmentName;
  }

  public void setDepartmentName(String departmentName) {
    this.departmentName = departmentName;
  }

  public List<StandardRateApiInfo> getStandardRateInfos() {
    return standardRateInfos;
  }

  public void setStandardRateInfos(
      List<StandardRateApiInfo> standardRateInfos) {
    this.standardRateInfos = standardRateInfos;
  }

  @Override
  public String toString() {
    return "DepartmentInfo{" +
        ", departmentName='" + departmentName + '\'' +
        ", standardRateInfos=" + standardRateInfos +
        '}';
  }
}
