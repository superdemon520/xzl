package cn.xinzhili.api.doctor.bean;


public class DepartmentImagesApiInfo {

  private String departmentName;
  private int countImages;

  public String getDepartmentName() {
    return departmentName;
  }

  public void setDepartmentName(String departmentName) {
    this.departmentName = departmentName;
  }

  public int getCountImages() {
    return countImages;
  }

  public void setCountImages(int countImages) {
    this.countImages = countImages;
  }

  @Override
  public String toString() {
    return "DepartmentInfo{" +
        ", departmentName='" + departmentName + '\'' +
        ", countImages=" + countImages +
        '}';
  }
}
