package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.DepartmentCountPatientApiInfo;
import java.util.List;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/4/10 下午5:23
 */
public class StatisticsPatientsApiResponse {

  private List<DepartmentCountPatientApiInfo> departmentCountPatientInfos;

  public List<DepartmentCountPatientApiInfo> getDepartmentCountPatientInfos() {
    return departmentCountPatientInfos;
  }

  public void setDepartmentCountPatientInfos(
      List<DepartmentCountPatientApiInfo> departmentCountPatientInfos) {
    this.departmentCountPatientInfos = departmentCountPatientInfos;
  }

  public StatisticsPatientsApiResponse(
      List<DepartmentCountPatientApiInfo> departmentCountPatientInfos) {
    this.departmentCountPatientInfos = departmentCountPatientInfos;
  }

  public StatisticsPatientsApiResponse() {
  }
}
