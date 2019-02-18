package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.RecordInfo;
import java.util.List;

public class RecordResponse {

  private List<RecordInfo> records;

  public List<RecordInfo> getRecords() {
    return records;
  }

  public void setRecords(List<RecordInfo> records) {
    this.records = records;
  }
}
