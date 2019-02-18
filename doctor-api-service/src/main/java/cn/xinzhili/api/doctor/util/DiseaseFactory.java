package cn.xinzhili.api.doctor.util;

import cn.xinzhili.api.doctor.bean.DiseaseApiInfo;
import cn.xinzhili.medical.api.DiseaseInfo;
import cn.xinzhili.xutils.core.ErrorCode;
import cn.xinzhili.xutils.core.FailureException;
import cn.xinzhili.xutils.core.util.HashUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 * @author by Loki on 17/3/21.
 */
public class DiseaseFactory {

  public static DiseaseApiInfo api(DiseaseInfo info) {

    if (info == null) {
      throw new FailureException(
          ErrorCode.INVALID_PARAMS, "disease info is null !");
    }

    DiseaseApiInfo apiInfo = new DiseaseApiInfo();
    if (info.getDiseaseId() != null) {
      apiInfo.setId(HashUtils.encode(info.getDiseaseId()));
    }
    if (!StringUtils.isEmpty(info.getName())) {
      apiInfo.setName(info.getName());
    }
    return apiInfo;
  }

  public static List<DiseaseApiInfo> apis(List<DiseaseInfo> infos) {

    if (infos == null) {
      throw new FailureException(
          ErrorCode.INVALID_PARAMS, "disease info list is null !");
    }
    List<DiseaseApiInfo> apiInfos = new ArrayList<>();
    for (DiseaseInfo info : infos) {
      apiInfos.add(api(info));
    }
    return apiInfos;
  }

}
