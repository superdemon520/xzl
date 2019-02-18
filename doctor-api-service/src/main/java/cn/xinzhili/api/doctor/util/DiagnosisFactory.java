package cn.xinzhili.api.doctor.util;

import cn.xinzhili.api.doctor.bean.DiagnosisApiInfo;
import cn.xinzhili.api.doctor.bean.DiagnosisStage3;
import cn.xinzhili.api.doctor.bean.DiagnosisStage3Info;
import cn.xinzhili.api.doctor.bean.request.AddDiagnosisApiRequest;
import cn.xinzhili.api.doctor.bean.request.UpdateDiagnosisApiRequest;
import cn.xinzhili.api.doctor.bean.response.DiagnosisApiResponse;
import cn.xinzhili.medical.api.DiagnosisInfo;
import cn.xinzhili.medical.api.DiagnosisStage;
import cn.xinzhili.medical.api.DiagnosisStageInfo;
import cn.xinzhili.medical.api.ImageDetailInfo;
import cn.xinzhili.medical.api.ReportInfo;
import cn.xinzhili.medical.api.ReportStatus;
import cn.xinzhili.medical.api.request.AddDiagnosisRequest;
import cn.xinzhili.medical.api.request.UpdateDiagnosisRequest;
import cn.xinzhili.medical.api.response.DiagnosisResponse;
import cn.xinzhili.xutils.core.ErrorCode;
import cn.xinzhili.xutils.core.FailureException;
import cn.xinzhili.xutils.core.util.HashUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 * @author by Loki on 17/3/21.
 */
public class DiagnosisFactory {

  public static AddDiagnosisRequest of(
      AddDiagnosisApiRequest request) {

    if (request == null) {
      throw new FailureException(
          ErrorCode.INVALID_PARAMS,
          "add diagnosis api request is null !");
    }

    AddDiagnosisRequest diagnosisRequest =
        new AddDiagnosisRequest();

    if (!StringUtils.isEmpty(request.getPatientId())) {
      diagnosisRequest.setPatientId(
          HashUtils.decode(request.getPatientId()));
    }
    if (!StringUtils.isEmpty(request.getImageId())) {
      diagnosisRequest.setImageId(
          HashUtils.decode(request.getImageId()));
    }
    ReportInfo reportInfo = new ReportInfo();
    if (request.getStatus() != null) {
      reportInfo.setStatus(
          ReportStatus.valueOf(request.getStatus().name()));
    }
    reportInfo.setReportAt(request.getReportAt());
    diagnosisRequest.setReportInfo(reportInfo);
    diagnosisRequest.setStage1(
        HashUtils.decode(request.getStage1()));

    if (request.getStage2() != null) {
      diagnosisRequest.setStage2(
          batchDecode(request.getStage2()));
    }
    if (request.getStage3() != null) {

      List<DiagnosisStage3> stage3s = request.getStage3();
      List<DiagnosisStage> diagnosisStages = new ArrayList<>();

      for (DiagnosisStage3 stage3 : stage3s) {
        diagnosisStages.add(of(stage3));
      }

      diagnosisRequest.setStage3(diagnosisStages);
    }
    if (request.getTreatments() != null) {
      diagnosisRequest.setTreatments(
          TreatmentFactory.ofs(
              request.getTreatments()));
    }
    return diagnosisRequest;
  }


  public static UpdateDiagnosisRequest of(
      UpdateDiagnosisApiRequest request) {

    UpdateDiagnosisRequest diagnosisRequest = new UpdateDiagnosisRequest();

    String patientId = request.getPatientId();
    if (!StringUtils.isEmpty(patientId)) {
      diagnosisRequest.setPatientId(HashUtils.decode(patientId));
    }

    if (!StringUtils.isEmpty(request.getId())) {
      diagnosisRequest.setId(HashUtils.decode(request.getId()));
    }
    if (!StringUtils.isEmpty(request.getImageId())) {
      diagnosisRequest.setImageId(
          HashUtils.decode(request.getImageId()));
    }
    ReportInfo reportInfo = new ReportInfo();
    if (request.getStatus() != null) {
      reportInfo.setStatus(
          ReportStatus.valueOf(request.getStatus().name()));
    }
    reportInfo.setReportAt(request.getReportAt());
    diagnosisRequest.setReportInfo(reportInfo);
    diagnosisRequest.setStage1(
        HashUtils.decode(request.getStage1()));

    if (request.getStage2() != null) {
      diagnosisRequest.setStage2(
          batchDecode(request.getStage2()));
    }
    if (request.getStage3() != null) {
      List<DiagnosisStage3> stage3s = request.getStage3();
      List<DiagnosisStage> diagnosisStages = new ArrayList<>();

      for (DiagnosisStage3 stage3 : stage3s) {
        diagnosisStages.add(of(stage3));
      }

      diagnosisRequest.setStage3(diagnosisStages);
    }
    if (request.getTreatments() != null) {
      diagnosisRequest.setTreatments(
          TreatmentFactory.ofs(
              request.getTreatments()));
    }
    return diagnosisRequest;
  }

  public static DiagnosisApiResponse api(DiagnosisResponse response) {

    if (response == null) {
      throw new FailureException(
          ErrorCode.INVALID_PARAMS, "diagnosis response is null !");
    }
    DiagnosisApiResponse apiResponse = new DiagnosisApiResponse();
    ImageDetailInfo imageDetailInfo = new ImageDetailInfo();
    imageDetailInfo.setImage(response.getImageInfo());
    imageDetailInfo.setReport(response.getReportInfo());
    apiResponse.setImageInfo(ImageFactory.api(imageDetailInfo));
    apiResponse.setDiagnosisInfo(apis(response.getDiagnosis()));
    apiResponse.setAllType(apiResponse.resolver(response.getTypes()));
    return apiResponse;
  }

  public static DiagnosisApiInfo api(DiagnosisInfo info) {

    if (info == null) {
      throw new FailureException(
          ErrorCode.INVALID_PARAMS, "diagnosis info is null !");
    }

    DiagnosisApiInfo apiInfo = new DiagnosisApiInfo();

    if (info.getId() != null) {
      apiInfo.setId(HashUtils.encode(info.getId()));
    }

    if (info.getStage1() != null) {
      apiInfo.setStage1(DiseaseFactory.api(info.getStage1()));
    }
    if (info.getStage2() != null) {
      apiInfo.setStage2(DiseaseFactory.apis(info.getStage2()));
    }
    if (info.getStage3() != null) {

      List<DiagnosisStageInfo> stageInfos = info.getStage3();
      List<DiagnosisStage3Info> stage3s = new ArrayList<>();

      for (DiagnosisStageInfo stageInfo : stageInfos) {
        stage3s.add(api(stageInfo));
      }
      apiInfo.setStage3(stage3s);
    }
    if (info.getTreatments() != null) {
      apiInfo.setTreatments(TreatmentFactory.apis(info.getTreatments()));
    }
    return apiInfo;
  }

  private static List<DiagnosisApiInfo> apis(List<DiagnosisInfo> infos) {

    if (infos == null) {
      throw new FailureException(
          ErrorCode.INVALID_PARAMS, "diagnosis info list is null !");
    }

    List<DiagnosisApiInfo> apiInfos = new ArrayList<>();
    for (DiagnosisInfo info : infos
        ) {
      apiInfos.add(api(info));
    }
    return apiInfos;
  }


  private static List<Long> batchDecode(List<String> toDecodeStr) {
    if (toDecodeStr == null) {
      throw new IllegalArgumentException(
          "to decode str list is null !");
    }
    List<Long> decoded = new ArrayList<>();
    for (String str : toDecodeStr) {
      decoded.add(HashUtils.decode(str));
    }
    return decoded;
  }

  private static DiagnosisStage of(DiagnosisStage3 stage3) {
    if (stage3 == null
        || StringUtils.isEmpty(stage3.getDiseaseId())
        || StringUtils.isEmpty(stage3.getFatherId())) {
      throw new FailureException(
          ErrorCode.INVALID_PARAMS, "stage3 is null !");
    }
    DiagnosisStage stage = new DiagnosisStage();
    stage.setFatherId(HashUtils.decode(stage3.getFatherId()));
    stage.setDiseaseId(HashUtils.decode(stage3.getDiseaseId()));
    return stage;
  }

  private static DiagnosisStage3Info api(DiagnosisStageInfo stageInfo) {

    if (stageInfo == null
        || stageInfo.getDiseaseId() == null
        || stageInfo.getFatherId() == null
        || StringUtils.isEmpty(stageInfo.getName())) {
      throw new FailureException(
          ErrorCode.INVALID_PARAMS, "diagnosis stage info param is null !");
    }

    DiagnosisStage3Info stage3Info = new DiagnosisStage3Info();
    stage3Info.setDiseaseId(HashUtils.encode(stageInfo.getDiseaseId()));
    stage3Info.setFatherId(HashUtils.encode(stageInfo.getFatherId()));
    stage3Info.setName(stageInfo.getName());
    return stage3Info;
  }
}
