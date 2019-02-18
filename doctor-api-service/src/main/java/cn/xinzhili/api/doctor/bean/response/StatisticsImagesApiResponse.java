package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.DepartmentImagesApiInfo;
import java.util.List;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/4/10 下午5:23
 */
public class StatisticsImagesApiResponse {

  private List<DepartmentImagesApiInfo> countImagesInfos;

  public List<DepartmentImagesApiInfo> getCountImagesInfos() {
    return countImagesInfos;
  }

  public void setCountImagesInfos(
      List<DepartmentImagesApiInfo> countImagesInfos) {
    this.countImagesInfos = countImagesInfos;
  }

  public StatisticsImagesApiResponse() {
  }

  public StatisticsImagesApiResponse(
      List<DepartmentImagesApiInfo> countImagesInfos) {
    this.countImagesInfos = countImagesInfos;
  }
}
