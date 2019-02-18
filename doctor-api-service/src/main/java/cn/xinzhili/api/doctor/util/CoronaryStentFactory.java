package cn.xinzhili.api.doctor.util;

import cn.xinzhili.api.doctor.bean.BasicCoronaryStentApiInfo;
import cn.xinzhili.api.doctor.bean.response.BasicCoronaryStentApiResponse;
import cn.xinzhili.medical.api.CoronaryStentInfo;
import cn.xinzhili.xutils.core.util.HashUtils;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;

/**
 * @author by Loki on 17/3/21.
 */
public class CoronaryStentFactory {

  public static BasicCoronaryStentApiResponse apiCoronaryStentInfo(
      List<CoronaryStentInfo> coronaryStentInfo) {
    if (coronaryStentInfo == null) {
      throw new IllegalArgumentException(
          "coronaryStent api info is null !");
    }
    List<BasicCoronaryStentApiInfo> list = coronaryStentInfo.stream().map(csi -> {
      BasicCoronaryStentApiInfo basicCoronaryStentApiInfo = new BasicCoronaryStentApiInfo();
      BeanUtils.copyProperties(csi, basicCoronaryStentApiInfo, "id");
      basicCoronaryStentApiInfo.setId(HashUtils.encode(csi.getId()));
      return basicCoronaryStentApiInfo;
    }).collect(Collectors.toList());
    return new BasicCoronaryStentApiResponse(list);
  }


}
