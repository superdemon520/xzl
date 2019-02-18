package cn.xinzhili.api.doctor.bean;

import cn.xinzhili.user.api.ConsultationLevel;
import cn.xinzhili.user.api.Gender;
import cn.xinzhili.user.api.RiskFactor;
import java.util.List;

/**
 * @author by MarlinL on 8/16/16.
 */
public class ConsultationPatientApiInfo {

  private String id;
  private String name;
  private String area;
  private Integer age;
  private Gender sex;
  private ConsultationLevel pendingConsultationLevel;
  private List<RiskFactor> riskFactor;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getArea() {
    return area;
  }

  public void setArea(String area) {
    this.area = area;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public Gender getSex() {
    return sex;
  }

  public void setSex(Gender sex) {
    this.sex = sex;
  }

  public List<RiskFactor> getRiskFactor() {
    return riskFactor;
  }

  public void setRiskFactor(List<RiskFactor> riskFactor) {
    this.riskFactor = riskFactor;
  }

  public ConsultationLevel getPendingConsultationLevel() {
    return pendingConsultationLevel;
  }

  public void setPendingConsultationLevel(ConsultationLevel pendingConsultationLevel) {
    this.pendingConsultationLevel = pendingConsultationLevel;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "PatientInfo{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", area='" + area + '\'' +
        ", age=" + age +
        ", sex=" + sex +
        ", riskFactor=" + riskFactor +
        ", pendingConsultationLevel=" + pendingConsultationLevel +
        '}';
  }
}
