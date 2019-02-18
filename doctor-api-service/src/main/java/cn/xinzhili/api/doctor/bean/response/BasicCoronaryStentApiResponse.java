package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.BasicCoronaryStentApiInfo;
import java.util.List;

public class BasicCoronaryStentApiResponse {

  private List<BasicCoronaryStentApiInfo> coronaryStentApiInfos;

  public BasicCoronaryStentApiResponse(
      List<BasicCoronaryStentApiInfo> coronaryStentApiInfos) {
    this.coronaryStentApiInfos = coronaryStentApiInfos;
  }

  public List<BasicCoronaryStentApiInfo> getCoronaryStentApiInfos() {
    return coronaryStentApiInfos;
  }

  public void setCoronaryStentApiInfos(
      List<BasicCoronaryStentApiInfo> coronaryStentApiInfos) {
    this.coronaryStentApiInfos = coronaryStentApiInfos;
  }
}
