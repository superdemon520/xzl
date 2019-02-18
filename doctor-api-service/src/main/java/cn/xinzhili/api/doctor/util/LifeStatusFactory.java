package cn.xinzhili.api.doctor.util;

import cn.xinzhili.api.doctor.bean.LifeStatusApiInfo;
import cn.xinzhili.medical.api.LifeStatusInfo;
import cn.xinzhili.xutils.core.util.HashUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ywb on 18/4/2017.
 */
public class LifeStatusFactory {


  public static List<LifeStatusApiInfo> api(List<LifeStatusInfo> records) {
    if (records == null || records.isEmpty()) {
      return null;
    }
    return records.stream().map(
        record -> {
          LifeStatusApiInfo info = new LifeStatusApiInfo();
          info.setContent(record.getContent());
          info.setCreatedAt(record.getCreatedAt());
          info.setPatientId(HashUtils.encode(record.getPatientId()));
          info.setType(record.getType());
          return info;
        }).collect(Collectors.toList());
  }
}
