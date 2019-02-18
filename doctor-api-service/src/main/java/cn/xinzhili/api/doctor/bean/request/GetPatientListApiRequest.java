package cn.xinzhili.api.doctor.bean.request;

import cn.xinzhili.api.doctor.bean.ApiRiskFactor;
import cn.xinzhili.api.doctor.bean.PatientApiProgress;
import cn.xinzhili.api.doctor.bean.response.PatientApiIntegratedStatus;
import cn.xinzhili.user.api.ServiceLevel;
import java.util.Date;
import java.util.List;

/**
 * @author by Loki on 17/2/27.
 */
public class GetPatientListApiRequest {

  private String organizationId;
  private String departmentId;
  private String doctorId;
  private String assistantId;
  private String operatorId;
  private String keyword;
  private Boolean haveAssistant;
  private Integer ageMin;
  private Integer ageMax;
  private Integer province;
  private List<ApiRiskFactor> risks;
  private ServiceLevel serviceLevel;
  private Long createdAtStart;
  private Long createdAtEnd;
  //默认false，展示属于当前doctor的患者
  private boolean excludeDoctor;
  //默认false，展示属于当前assistant的患者
  private boolean excludeAssistant;
  //默认false，展示属于当前operator的患者
  private boolean excludeOperator;


  //没有患病
  private List<ApiRiskFactor> excludedRisks;
  //患者的综合状态 包括 消息状态 ，指标状态 ，用药状态
  private PatientApiIntegratedStatus integratedStatus;
  //是否包含年龄为null 的患者
  private boolean includesNullAge;

  private PatientApiProgress progress;

  /**
   * check patient's createdAtStart is before createdAtEnd
   *
   * @return is before
   */
  public boolean checkCreatedAt() {

    return !(createdAtStart != null && createdAtEnd != null)
        || new Date(createdAtStart).before(new Date(createdAtEnd));
  }

  public String getOrganizationId() {
    return organizationId;
  }

  public void setOrganizationId(String organizationId) {
    this.organizationId = organizationId;
  }

  public String getDepartmentId() {
    return departmentId;
  }

  public void setDepartmentId(String departmentId) {
    this.departmentId = departmentId;
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

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public Boolean getHaveAssistant() {
    return haveAssistant;
  }

  public void setHaveAssistant(Boolean haveAssistant) {
    this.haveAssistant = haveAssistant;
  }

  public Integer getAgeMin() {
    return ageMin;
  }

  public void setAgeMin(Integer ageMin) {
    this.ageMin = ageMin;
  }

  public Integer getAgeMax() {
    return ageMax;
  }

  public void setAgeMax(Integer ageMax) {
    this.ageMax = ageMax;
  }

  public Integer getProvince() {
    return province;
  }

  public void setProvince(Integer province) {
    this.province = province;
  }

  public List<ApiRiskFactor> getRisks() {
    return risks;
  }

  public void setRisks(List<ApiRiskFactor> risks) {
    this.risks = risks;
  }

  public ServiceLevel getServiceLevel() {
    return serviceLevel;
  }

  public void setServiceLevel(ServiceLevel serviceLevel) {
    this.serviceLevel = serviceLevel;
  }

  public Long getCreatedAtStart() {
    return createdAtStart;
  }

  public void setCreatedAtStart(Long createdAtStart) {
    this.createdAtStart = createdAtStart;
  }

  public Long getCreatedAtEnd() {
    return createdAtEnd;
  }

  public void setCreatedAtEnd(Long createdAtEnd) {
    this.createdAtEnd = createdAtEnd;
  }

  public boolean isExcludeDoctor() {
    return excludeDoctor;
  }

  public void setExcludeDoctor(boolean excludeDoctor) {
    this.excludeDoctor = excludeDoctor;
  }

  public boolean isExcludeAssistant() {
    return excludeAssistant;
  }

  public void setExcludeAssistant(boolean excludeAssistant) {
    this.excludeAssistant = excludeAssistant;
  }

  public boolean isExcludeOperator() {
    return excludeOperator;
  }

  public void setExcludeOperator(boolean excludeOperator) {
    this.excludeOperator = excludeOperator;
  }

  public List<ApiRiskFactor> getExcludedRisks() {
    return excludedRisks;
  }

  public void setExcludedRisks(List<ApiRiskFactor> excludedRisks) {
    this.excludedRisks = excludedRisks;
  }

  public PatientApiIntegratedStatus getIntegratedStatus() {
    return integratedStatus;
  }

  public void setIntegratedStatus(
      PatientApiIntegratedStatus integratedStatus) {
    this.integratedStatus = integratedStatus;
  }

  public boolean isIncludesNullAge() {
    return includesNullAge;
  }

  public void setIncludesNullAge(boolean includesNullAge) {
    this.includesNullAge = includesNullAge;
  }

  public PatientApiProgress getProgress() {
    return progress;
  }

  public void setProgress(PatientApiProgress progress) {
    this.progress = progress;
  }
}
