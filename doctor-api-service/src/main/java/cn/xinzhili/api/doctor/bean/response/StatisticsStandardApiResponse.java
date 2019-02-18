package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.DepartmentStandardApiInfo;
import cn.xinzhili.medical.api.DepartmentStandardInfo;
import java.util.List;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/4/16 下午5:23
 */
public class StatisticsStandardApiResponse {

  private List<DepartmentStandardApiInfo> departmentStandardInfos;

  public List<DepartmentStandardApiInfo> getDepartmentStandardInfos() {

    return departmentStandardInfos;
  }

  public void setDepartmentStandardInfos(
      List<DepartmentStandardApiInfo> departmentStandardInfos) {
    this.departmentStandardInfos = departmentStandardInfos;
  }

  public StatisticsStandardApiResponse(
      List<DepartmentStandardApiInfo> departmentStandardInfos) {
    this.departmentStandardInfos = departmentStandardInfos;
  }

  public StatisticsStandardApiResponse() {
  }


}
