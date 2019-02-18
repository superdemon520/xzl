package cn.xinzhili.api.doctor.bean;


import java.util.List;

public class DepartmentAdvicesApiInfo {

  private String departmentName;
  private List<CountAdvicesApiInfo> countAdvicesInfos;

  public String getDepartmentName() {
    return departmentName;
  }

  public void setDepartmentName(String departmentName) {
    this.departmentName = departmentName;
  }

  public List<CountAdvicesApiInfo> getCountAdvicesInfos() {
    return countAdvicesInfos;
  }

  public void setCountAdvicesInfos(
      List<CountAdvicesApiInfo> countAdvicesInfos) {
    this.countAdvicesInfos = countAdvicesInfos;
  }

  @Override
  public String toString() {
    return "DepartmentAdvicesApiInfo{" +
        ", departmentName='" + departmentName + '\'' +
        ", countAdvicesInfos=" + countAdvicesInfos +
        '}';
  }
}
