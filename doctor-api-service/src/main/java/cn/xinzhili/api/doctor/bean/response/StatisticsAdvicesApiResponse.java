package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.DepartmentAdvicesApiInfo;
import java.util.List;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/4/16 下午5:23
 */
public class StatisticsAdvicesApiResponse {

  private List<DepartmentAdvicesApiInfo> departmentAdvicesInfos;


  public StatisticsAdvicesApiResponse() {
  }

  public List<DepartmentAdvicesApiInfo> getDepartmentAdvicesInfos() {
    return departmentAdvicesInfos;
  }

  public void setDepartmentAdvicesInfos(
      List<DepartmentAdvicesApiInfo> departmentAdvicesInfos) {
    this.departmentAdvicesInfos = departmentAdvicesInfos;
  }

  public StatisticsAdvicesApiResponse(
      List<DepartmentAdvicesApiInfo> departmentAdvicesInfos) {

    this.departmentAdvicesInfos = departmentAdvicesInfos;
  }
}
