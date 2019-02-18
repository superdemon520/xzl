package cn.xinzhili.api.doctor.util;

import cn.xinzhili.api.doctor.bean.BasicHospitalApiInfo;
import cn.xinzhili.medical.api.BasicHospitalInfo;
import cn.xinzhili.xutils.core.ErrorCode;
import cn.xinzhili.xutils.core.FailureException;
import cn.xinzhili.xutils.core.util.HashUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by MarlinL on 15/02/2017.
 */
public class BasicHospitalFactory {


  public static BasicHospitalApiInfo api(BasicHospitalInfo info) {

    if (Objects.isNull(info)) {
      return null;
    }

    BasicHospitalApiInfo result = new BasicHospitalApiInfo();
    if (Objects.nonNull(info.getId())) {
      result.setId(HashUtils.encode(info.getId()));
    }
    result.setName(info.getName());
    result.setAbbreviation(info.getAbbreviation());
    result.setProvince(info.getProvince());
    result.setCity(info.getCity());
    result.setCounty(info.getCounty());
    result.setTownship(info.getTownship());
    return result;
  }

  public static BasicHospitalInfo of(BasicHospitalApiInfo apiInfo) {

    if (Objects.isNull(apiInfo)) {
      return null;
    }

    BasicHospitalInfo info = new BasicHospitalInfo();
    String id = apiInfo.getId();
    if (Objects.nonNull(id)) {
      info.setId(HashUtils.decode(id));
    }
    info.setName(apiInfo.getName());
    info.setAbbreviation(apiInfo.getAbbreviation());
    info.setProvince(apiInfo.getProvince());
    info.setCounty(apiInfo.getCounty());
    info.setCity(apiInfo.getCity());
    info.setTownship(apiInfo.getTownship());
    return info;
  }

  public static List<BasicHospitalApiInfo> apis(List<BasicHospitalInfo> infos) {

    if (infos == null) {
      throw new FailureException(
          ErrorCode.INVALID_PARAMS, "disease info list is null !");
    }
    List<BasicHospitalApiInfo> apiInfos = new ArrayList<>();
    for (BasicHospitalInfo info : infos) {
      apiInfos.add(api(info));
    }
    return apiInfos;
  }

}
