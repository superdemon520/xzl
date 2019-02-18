package cn.xinzhili.api.doctor.util;

import cn.xinzhili.api.doctor.bean.ApiRiskFactor;
import cn.xinzhili.api.doctor.bean.PatientApiInfo;
import cn.xinzhili.api.doctor.bean.PatientApiProgress;
import cn.xinzhili.api.doctor.bean.UserGender;
import cn.xinzhili.api.doctor.bean.UserStatus;
import cn.xinzhili.api.doctor.bean.request.AddPatientApiRequest;
import cn.xinzhili.api.doctor.bean.request.GetPatientListApiRequest;
import cn.xinzhili.api.doctor.bean.request.UpdatePatientApiRequest;
import cn.xinzhili.api.doctor.bean.request.UpdatePatientBindingRequest;
import cn.xinzhili.api.doctor.bean.response.PatientDetailApiResponse;
import cn.xinzhili.user.api.AdviceLevel;
import cn.xinzhili.user.api.Gender;
import cn.xinzhili.user.api.MedicationStatus;
import cn.xinzhili.user.api.MetricsStatus;
import cn.xinzhili.user.api.PatientInfo;
import cn.xinzhili.user.api.PatientProgress;
import cn.xinzhili.user.api.RiskFactor;
import cn.xinzhili.user.api.ServiceLevel;
import cn.xinzhili.user.api.request.AddPatientRequest;
import cn.xinzhili.user.api.request.BatchUpdatePatientRelationRequest;
import cn.xinzhili.user.api.request.DelAssistantPatientRelationApiRequest;
import cn.xinzhili.user.api.request.GetPatientByCriteriaRequest;
import cn.xinzhili.user.api.request.PatientInsertType;
import cn.xinzhili.user.api.request.UpdatePatientAndRelationRequest;
import cn.xinzhili.user.api.request.UpdatePatientRequest;
import cn.xinzhili.user.api.response.PatientDetailResponse;
import cn.xinzhili.user.api.visual.PatientIntegratedStatus;
import cn.xinzhili.xutils.core.ErrorCode;
import cn.xinzhili.xutils.core.FailureException;
import cn.xinzhili.xutils.core.util.HashUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by marlin on 24/03/2017.
 */
public class PatientFactory {

  private static final Logger logger = LoggerFactory
      .getLogger(PatientFactory.class);

  public static PatientApiInfo api(PatientInfo patientInfo) {

    if (patientInfo == null || patientInfo.getId() == null) {
      logger.error("patient info api param is null !");
      throw new FailureException(ErrorCode.INVALID_PARAMS);
    }
    PatientApiInfo apiInfo = new PatientApiInfo();
    apiInfo.setId(HashUtils.encode(patientInfo.getId()));

    apiInfo.setAge(MiscUtils.birthday2Age(patientInfo.getBirthday()));
    apiInfo.setName(patientInfo.getName());

    apiInfo.setBirthday(patientInfo.getBirthday());
    apiInfo.setAddress(patientInfo.getAddress());

    if (patientInfo.getSex() != null) {
      Integer sex = patientInfo.getSex().getCode();
      if (sex == Gender.FEMALE.getCode()) {
        apiInfo.setSex(UserGender.FEMALE);
      } else if (sex == Gender.MALE.getCode()) {
        apiInfo.setSex(UserGender.MALE);
      } else {
        apiInfo.setSex(UserGender.OTHER);
      }
    } else {
      apiInfo.setSex(UserGender.OTHER);
    }

    apiInfo.setAvatar(patientInfo.getAvatar());
    apiInfo.setTel(patientInfo.getTel());
    apiInfo.setDoctorName(patientInfo.getDoctorName());
    apiInfo.setAssistantName(patientInfo.getAssistantName());
    apiInfo.setOperatorName(patientInfo.getOperatorName());
    apiInfo.setServiceLevel(patientInfo.getServiceLevel());

    //统计信息
    MedicationStatus medicationStatus = patientInfo.getMedicationStatus();
    apiInfo.setMedicationStatus(
        Objects.isNull(medicationStatus) ? MedicationStatus.NORMAL : medicationStatus);

    MetricsStatus metricsStatus = patientInfo.getMetricsStatus();
    apiInfo.setMetricsStatus(Objects.isNull(metricsStatus) ? MetricsStatus.NORMAL : metricsStatus);

    Integer pendingDoctorMessage = patientInfo.getPendingDoctorMessage();
    apiInfo
        .setPendingDoctorMessage(Objects.isNull(pendingDoctorMessage) ? 0 : pendingDoctorMessage);

    Integer pendingAssistantMessage = patientInfo.getPendingAssistantMessage();
    apiInfo.setPendingAssistantMessage(
        Objects.isNull(pendingAssistantMessage) ? 0 : pendingAssistantMessage);

    Integer pendingOperatorMessage = patientInfo.getPendingOperatorMessage();
    apiInfo.setPendingOperatorMessage(
        Objects.isNull(pendingOperatorMessage) ? 0 : pendingOperatorMessage);

    AdviceLevel pendingDoctorAdviceLevel = patientInfo.getPendingDoctorAdviceLevel();
    apiInfo.setPendingDoctorAdviceLevel(
        Objects.isNull(pendingDoctorAdviceLevel) ? AdviceLevel.NONE : pendingDoctorAdviceLevel);

    Integer pendingDoctorAdviceCount = patientInfo.getPendingDoctorAdviceCount();
    apiInfo.setPendingDoctorAdviceCount(
        Objects.isNull(pendingDoctorAdviceCount) ? 0 : pendingDoctorAdviceCount);

    AdviceLevel pendingAssistantAdviceLevel = patientInfo.getPendingAssistantAdviceLevel();
    apiInfo.setPendingAssistantAdviceLevel(
        Objects.isNull(pendingAssistantAdviceLevel) ? AdviceLevel.NONE
            : pendingAssistantAdviceLevel);

    Integer pendingAssistantAdviceCount = patientInfo.getPendingAssistantAdviceCount();
    apiInfo.setPendingAssistantAdviceCount(
        Objects.isNull(pendingAssistantAdviceCount) ? 0 : pendingAssistantAdviceCount);
    //2018.4.3
    Integer pendingDoctorConsultation = patientInfo.getPendingDoctorConsultation();

    apiInfo.setPendingDoctorConsultation(
        Objects.isNull(pendingDoctorConsultation) ? 0 : pendingDoctorConsultation);

    apiInfo.setPendingConsultationLevel(patientInfo.getPendingConsultationLevel());

    List<RiskFactor> riskFactor = patientInfo.getRiskFactor();
    apiInfo.setRiskFactor(Objects.isNull(riskFactor) ? List.of() : riskFactor);

    Integer uncheckImageCount = patientInfo.getUncheckImageCount();
    apiInfo.setUnauditedImageCount(Objects.isNull(uncheckImageCount) ? 0 : uncheckImageCount);

    apiInfo.setRemark(patientInfo.getRemark());
    if (patientInfo.getProgress() != null) {
      apiInfo.setProgress(PatientApiProgress.valueOf(patientInfo.getProgress().name()));
    }
    apiInfo.setEthnicity(patientInfo.getEthnicity());
    apiInfo.setArea(patientInfo.getArea());
    apiInfo.setHeight(patientInfo.getHeight());
    apiInfo.setWeight(patientInfo.getWeight());
    apiInfo.setWaistline(patientInfo.getWaistline());
    apiInfo.setBmi(MiscUtils.bmiCalculate(patientInfo.getHeight(), patientInfo.getWeight()));
    apiInfo.setHasAssistant(patientInfo.isHasAssistant());
    if (patientInfo.getStatus() != null) {
      apiInfo.setStatus(UserStatus.valueOf(patientInfo.getStatus().name()));
    }
    apiInfo.setProvince(patientInfo.getProvince());
    apiInfo.setCity(patientInfo.getCity());
    apiInfo.setTown(patientInfo.getTown());
    return apiInfo;
  }

  public static List<PatientApiInfo> apis(
      List<PatientInfo> patientInfos) {

    List<PatientApiInfo> infos = new ArrayList<>();
    if (patientInfos != null) {
      for (cn.xinzhili.user.api.PatientInfo patientInfo : patientInfos) {
        PatientApiInfo info = api(patientInfo);
        infos.add(info);
      }
    }
    return infos;
  }

  public static GetPatientByCriteriaRequest of(GetPatientListApiRequest request) {

    GetPatientByCriteriaRequest criteria = new GetPatientByCriteriaRequest();

    if (!StringUtils.isEmpty(request.getOrganizationId())) {
      criteria.setOrganizationId(HashUtils.decode(request.getOrganizationId()));
    }
    if (!StringUtils.isEmpty(request.getDepartmentId())) {
      criteria.setDepartmentId(HashUtils.decode(request.getDepartmentId()));
    }
    if (!StringUtils.isEmpty(request.getDoctorId())) {
      criteria.setDoctorId(HashUtils.decode(request.getDoctorId()));
    }
    if (!StringUtils.isEmpty(request.getAssistantId())) {
      criteria.setAssistantId(HashUtils.decode(request.getAssistantId()));
    }

    if (!StringUtils.isEmpty(request.getOperatorId())) {
      criteria.setOperatorId(HashUtils.decode(request.getOperatorId()));
    }
    criteria.setKeyword(request.getKeyword());
    criteria.setAgeMin(request.getAgeMin());
    criteria.setAgeMax(request.getAgeMax());
    criteria.setIncludesNullAge(request.isIncludesNullAge());
    criteria.setProvince(request.getProvince());

    criteria.setHaveAssistant(request.getHaveAssistant());
    criteria.setExcludeAssistant(request.isExcludeAssistant());
    criteria.setExcludeDoctor(request.isExcludeDoctor());
    criteria.setExcludeOperator(request.isExcludeOperator());
    /*
     * add by loki 03/28
     */
    if (request.getServiceLevel() != null) {
      criteria.setServiceLevel(ServiceLevel.valueOf(request.getServiceLevel().name()));
    }
    if (request.getRisks() != null) {
      List<ApiRiskFactor> risks = request.getRisks();
      List<RiskFactor> riskFactors = new ArrayList<>();
      for (ApiRiskFactor risk : risks) {
        riskFactors.add(RiskFactor.valueOf(risk.name()));
      }
      criteria.setRisks(riskFactors);
    }

    if (request.getExcludedRisks() != null) {
      List<ApiRiskFactor> excludedRisks = request.getExcludedRisks();
      List<RiskFactor> risks = new ArrayList<>();
      for (ApiRiskFactor risk : excludedRisks) {
        risks.add(RiskFactor.valueOf(risk.name()));
      }
      criteria.setExcludedRisks(risks);
    }
    if (request.getIntegratedStatus() != null) {
      criteria.setIntegratedStatus(
          PatientIntegratedStatus.valueOf(request.getIntegratedStatus().name()));
    }
    if (request.getProgress() != null) {
      criteria.setProgress(PatientProgress.valueOf(request.getProgress().name()));
    }
    return criteria;
  }

  public static AddPatientRequest of(AddPatientApiRequest request) {

    AddPatientRequest patientRequest = new AddPatientRequest();
    AddPatientRequest.PatientCell cell = new AddPatientRequest.PatientCell();
    cell.setTel(request.getTel());
    cell.setServiceLevel(request.getServiceLevel());
    patientRequest.setPatient(cell);
    if (!StringUtils.isEmpty(request.getDoctorId())) {
      patientRequest.setDoctorId(HashUtils.decode(request.getDoctorId()));
    }
    if (!StringUtils.isEmpty(request.getAssistantId())) {
      patientRequest.setAssistantId(HashUtils.decode(request.getAssistantId()));
    }
    if (!StringUtils.isEmpty(request.getOperatorId())) {
      patientRequest.setOperatorId(HashUtils.decode(request.getOperatorId()));
    }
    patientRequest.setFromType(PatientInsertType.DOCTOR);
    patientRequest.setOrganizationId(HashUtils.decode(request.getOrganizationId()));
    return patientRequest;
  }


  public static PatientDetailApiResponse api(PatientDetailResponse response) {

    PatientDetailApiResponse api = new PatientDetailApiResponse();
    PatientApiInfo patientApiInfo = api(response.getPatient());
    patientApiInfo.setDoctorName(response.getBoundDoctor().getName());
    api.setDoctorId(HashUtils.encode(response.getBoundDoctor().getId()));
    if (response.getBoundAssistant() != null) {
      api.setAssistantId(HashUtils.encode(response.getBoundAssistant().getId()));
      patientApiInfo.setAssistantName(response.getBoundAssistant().getName());
    }
    api.setOperatorId(HashUtils.encode(response.getBoundOperator().getId()));
    patientApiInfo.setOperatorName(response.getBoundOperator().getName());
    api.setPatientInfo(patientApiInfo);
    return api;
  }

  public static UpdatePatientRequest of(UpdatePatientApiRequest request) {
    if (request == null
        || request.getId() == null) {
      throw new FailureException(ErrorCode.INVALID_PARAMS,
          "update patient request id is null !");
    }
    UpdatePatientRequest result = new UpdatePatientRequest();
    result.setName(request.getName());
    if (request.getSex() != null) {
      result.setSex(Gender.valueOf(request.getSex().name()));
    }
    result.setEthnicity(request.getEthnicity());
    result.setHeight(request.getHeight());
    result.setWeight(request.getWeight());
    result.setWaistline(request.getWaistline());
    result.setProvince(request.getProvince());
    result.setCity(request.getCity());
    result.setTown(request.getTown());
    result.setAddress(request.getAddress());
    result.setMarriage(request.getMarriage());
    result.setBirthday(request.getBirthday());
    return result;
  }

  public static UpdatePatientAndRelationRequest ofRelationRequest(
      UpdatePatientApiRequest request) {

    UpdatePatientAndRelationRequest relationRequest =
        new UpdatePatientAndRelationRequest();
    if (!StringUtils.isEmpty(request.getId())) {
      relationRequest.setPatientId(HashUtils.decode(request.getId()));
    }
    if (!StringUtils.isEmpty(request.getDoctorId())) {
      relationRequest.setDoctorId(HashUtils.decode(request.getDoctorId()));
    }
    if (!StringUtils.isEmpty(request.getAssistantId())) {
      relationRequest.setAssistantId(HashUtils.decode(request.getAssistantId()));
    }
    if (!StringUtils.isEmpty(request.getOperatorId())) {
      relationRequest.setOperatorId(HashUtils.decode(request.getOperatorId()));
    }
    if (request.getSex() != null) {
      relationRequest.setSex(Gender.valueOf(request.getSex().name()));
    }
    relationRequest.setName(request.getName());
    relationRequest.setServiceLevel(request.getServiceLevel());
    relationRequest.setOrganizationId(HashUtils.decode(request.getOrganizationId()));
    return relationRequest;
  }

  public static BatchUpdatePatientRelationRequest of(
      UpdatePatientBindingRequest request) {

    BatchUpdatePatientRelationRequest relationRequest =
        new BatchUpdatePatientRelationRequest();

    if (!StringUtils.isEmpty(request.getAssistantId())) {
      relationRequest.setAssistantId(HashUtils.decode(request.getAssistantId()));
    }
    if (!StringUtils.isEmpty(request.getDoctorId())) {
      relationRequest.setDoctorId(HashUtils.decode(request.getDoctorId()));
    }

    if (!StringUtils.isEmpty(request.getOperatorId())) {
      relationRequest.setOperatorId(HashUtils.decode(request.getOperatorId()));
    }

    relationRequest.setPatientIds(request.getPatientIds().stream().map(HashUtils::decode).collect(
        Collectors.toList()));
    relationRequest.setOrganizationId(HashUtils.decode(request.getOrganizationId()));
    return relationRequest;
  }


  public static DelAssistantPatientRelationApiRequest of(Long doctorId, List<String> assistantIds) {
    DelAssistantPatientRelationApiRequest target = new DelAssistantPatientRelationApiRequest();
    target.setDoctorId(doctorId);
    List<Long> targetAssistantIds = new ArrayList<>();
    assistantIds.forEach(id -> {
      targetAssistantIds.add(HashUtils.decode(id));
    });
    target.setAssistantIds(targetAssistantIds);
    return target;
  }
}
