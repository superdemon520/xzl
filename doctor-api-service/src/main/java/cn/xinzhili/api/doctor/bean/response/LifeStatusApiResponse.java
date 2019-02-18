package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.LifeStatusApiInfo;
import java.util.List;

/**
 * Created by ywb on 18/4/2017.
 */
public class LifeStatusApiResponse {

  private List<LifeStatusApiInfo> records;

  public List<LifeStatusApiInfo> getRecords() {
    return records;
  }

  public void setRecords(List<LifeStatusApiInfo> records) {
    this.records = records;
  }
}
