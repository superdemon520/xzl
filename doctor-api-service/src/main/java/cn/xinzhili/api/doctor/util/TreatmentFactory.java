package cn.xinzhili.api.doctor.util;

import cn.xinzhili.api.doctor.bean.AttachedApiInfo;
import cn.xinzhili.api.doctor.bean.BasicTreatmentApiInfo;
import cn.xinzhili.api.doctor.bean.TreatmentApiInfo;
import cn.xinzhili.api.doctor.bean.response.BasicTreatmentApiResponse;
import cn.xinzhili.api.doctor.bean.response.BasicTreatmentsApiResponse;
import cn.xinzhili.medical.api.AttachedInfo;
import cn.xinzhili.medical.api.DiagnosisTreatmentInfo;
import cn.xinzhili.medical.api.TreatmentInfo;
import cn.xinzhili.xutils.core.ErrorCode;
import cn.xinzhili.xutils.core.FailureException;
import cn.xinzhili.xutils.core.util.HashUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;

/**
 * @author by Loki on 17/3/21.
 */
public class TreatmentFactory {

  public static DiagnosisTreatmentInfo of(TreatmentApiInfo apiInfo) {

    if (apiInfo == null) {
      throw new IllegalArgumentException(
          "treatment api info is null !");
    }

    DiagnosisTreatmentInfo treatmentInfo = new DiagnosisTreatmentInfo();
    if (!StringUtils.isEmpty(apiInfo.getId())) {
      treatmentInfo.setId(HashUtils.decode(apiInfo.getId()));
    }
    treatmentInfo.setSituation(apiInfo.getSituation());
    treatmentInfo.setAttachedInfos(apiInfo.getAttachedInfos());
    return treatmentInfo;
  }

  public static List<DiagnosisTreatmentInfo> ofs(
      List<TreatmentApiInfo> apiInfos) {

    if (apiInfos == null) {
      throw new IllegalArgumentException(
          "treatment api info list is null !");
    }
    List<DiagnosisTreatmentInfo> treatmentInfos = new ArrayList<>();
    for (TreatmentApiInfo apiInfo : apiInfos) {
      treatmentInfos.add(of(apiInfo));
    }
    return treatmentInfos;
  }

  public static TreatmentApiInfo api(DiagnosisTreatmentInfo info) {
    if (info == null) {
      throw new FailureException(
          ErrorCode.INVALID_PARAMS, "treatment info is null !");
    }
    TreatmentApiInfo apiInfo = new TreatmentApiInfo();
    if (info.getId() != null) {
      apiInfo.setId(HashUtils.encode(info.getId()));
    }
    apiInfo.setSituation(info.getSituation());
    apiInfo.setAttachedInfos(info.getAttachedInfos());
    return apiInfo;
  }

  public static List<TreatmentApiInfo> apis(List<DiagnosisTreatmentInfo> infos) {

    if (infos == null) {
      throw new FailureException(
          ErrorCode.INVALID_PARAMS, "treatment info list is null !");
    }
    List<TreatmentApiInfo> apiInfos = new ArrayList<>();
    for (DiagnosisTreatmentInfo info : infos) {
      apiInfos.add(api(info));
    }
    return apiInfos;
  }

  public static BasicTreatmentsApiResponse apisBasicTreatments(List<TreatmentInfo> treatments) {
    if (Objects.isNull(treatments)) {
      throw new FailureException(
          ErrorCode.INVALID_PARAMS, "treatments is null !");
    }
    BasicTreatmentsApiResponse basicTreatmentsApiResponse = new BasicTreatmentsApiResponse();
    List<BasicTreatmentApiInfo> apiInfos = treatments.stream().map(t -> {
      BasicTreatmentApiInfo basicTreatmentApiInfo = new BasicTreatmentApiInfo();
      basicTreatmentApiInfo.setId(HashUtils.encode(t.getId()));
      basicTreatmentApiInfo.setName(t.getName());
      basicTreatmentApiInfo.setAttachedInfos(getEncodedAttachedApiInfos(t.getAttachedInfos()));
      return basicTreatmentApiInfo;
    }).collect(Collectors.toList());
    basicTreatmentsApiResponse.setTreatmentInfos(apiInfos);
    return basicTreatmentsApiResponse;
  }

  private static List<AttachedApiInfo> getEncodedAttachedApiInfos(
      List<AttachedInfo> attachedInfos) {
    if (Objects.isNull(attachedInfos)) {
      return null;
    } else {
      return attachedInfos.stream().map(ai -> {
        AttachedApiInfo attachedApiInfo = new AttachedApiInfo();
        attachedApiInfo.setInfoType(ai.getInfoType());
        attachedApiInfo
            .setIdList(Objects.isNull(ai.getIdList()) ? null
                : ai.getIdList().stream().map(HashUtils::encode).collect(
                    Collectors.toList()));
        return attachedApiInfo;
      }).collect(Collectors.toList());
    }
  }

  public static BasicTreatmentApiResponse apiBasicTreatment(TreatmentInfo treatment) {
    if (Objects.isNull(treatment)) {
      throw new FailureException(
          ErrorCode.INVALID_PARAMS, "treatment is null !");
    } else {
      BasicTreatmentApiInfo info = new BasicTreatmentApiInfo();
      info.setId(HashUtils.encode(treatment.getId()));
      info.setName(treatment.getName());
      info.setAttachedInfos(getEncodedAttachedApiInfos(treatment.getAttachedInfos()));
      return new BasicTreatmentApiResponse(info);
    }
  }
}
