package cn.xinzhili.api.doctor.controller;

import cn.xinzhili.api.doctor.bean.BasicHospitalDetailApiResponse;
import cn.xinzhili.api.doctor.bean.ImageApiType;
import cn.xinzhili.api.doctor.bean.ImageListScope;
import cn.xinzhili.api.doctor.bean.InspectionChartApiType;
import cn.xinzhili.api.doctor.bean.ReportApiStatus;
import cn.xinzhili.api.doctor.bean.UserRole;
import cn.xinzhili.api.doctor.bean.request.AddDiagnosisApiRequest;
import cn.xinzhili.api.doctor.bean.request.DeleteImageTypeApiRequest;
import cn.xinzhili.api.doctor.bean.request.ImageReviewApiRequest;
import cn.xinzhili.api.doctor.bean.request.InspectionApiRequest;
import cn.xinzhili.api.doctor.bean.request.MedicalRecordApiRequest;
import cn.xinzhili.api.doctor.bean.request.SymptomApiRequest;
import cn.xinzhili.api.doctor.bean.request.UpdateDiagnosisApiRequest;
import cn.xinzhili.api.doctor.bean.response.BasicCoronaryStentApiResponse;
import cn.xinzhili.api.doctor.bean.response.BasicHospitalsApiResponse;
import cn.xinzhili.api.doctor.bean.response.BasicTreatmentsApiResponse;
import cn.xinzhili.api.doctor.bean.response.CardiogramResponse;
import cn.xinzhili.api.doctor.bean.response.DiagnosisApiResponse;
import cn.xinzhili.api.doctor.bean.response.ImageApiResponse;
import cn.xinzhili.api.doctor.bean.response.InspectionItemResponse;
import cn.xinzhili.api.doctor.bean.response.LatestInspectionApiResponse;
import cn.xinzhili.api.doctor.bean.response.MedicalRecordApiResponse;
import cn.xinzhili.api.doctor.bean.response.StandardDataApiResponse;
import cn.xinzhili.api.doctor.bean.response.SymptomApiResponse;
import cn.xinzhili.api.doctor.config.ApplicationConfiguration;
import cn.xinzhili.api.doctor.error.UserErrorCode;
import cn.xinzhili.api.doctor.service.DpcService;
import cn.xinzhili.api.doctor.service.MedicalService;
import cn.xinzhili.api.doctor.service.NotifyService;
import cn.xinzhili.api.doctor.service.UserService;
import cn.xinzhili.api.doctor.support.MLComponent;
import cn.xinzhili.api.doctor.util.AuthUtils;
import cn.xinzhili.api.doctor.util.DiagnosisFactory;
import cn.xinzhili.api.doctor.util.ImageFactory;
import cn.xinzhili.api.doctor.util.InspectionFactory;
import cn.xinzhili.api.doctor.util.MedicalRecordFactory;
import cn.xinzhili.api.doctor.util.SymptomFactory;
import cn.xinzhili.api.doctor.util.ValidationUtils;
import cn.xinzhili.medical.api.ImageStatus;
import cn.xinzhili.medical.api.ImageType;
import cn.xinzhili.medical.api.request.SymptomRequest;
import cn.xinzhili.medical.api.request.TabooRequest;
import cn.xinzhili.medical.api.response.BasicSymptomsResponse;
import cn.xinzhili.medical.api.response.MedicinesTaboosResponse;
import cn.xinzhili.medical.api.response.SymptomsResponse;
import cn.xinzhili.xutils.auth.CurrentUserId;
import cn.xinzhili.xutils.core.ErrorCode;
import cn.xinzhili.xutils.core.FailureException;
import cn.xinzhili.xutils.core.http.Response;
import cn.xinzhili.xutils.core.util.HashUtils;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by Loki on 17/3/10.
 */
@RestController
@Validated
public class MedicalController {

  private static final Logger logger = LoggerFactory
      .getLogger(MedicalController.class);


  @Autowired
  private MedicalService medicalService;
  @Autowired
  private DpcService dpcService;
  @Autowired
  private UserService userService;
  @Autowired
  private NotifyService notifyService;
  @Autowired
  private ApplicationConfiguration acf;

  @Autowired
  private MLComponent medicalLogicComponent;

  private void sendImageBurringMessage(ReportApiStatus status, String patientId, long operatorId) {
    if (status == ReportApiStatus.BLURRING) {
      long pid = HashUtils.decode(patientId);
      dpcService.sendImageReviewSystemMessage(operatorId, pid);
      notifyService.pushImageBlurringNotify(acf.getImageReviewSystemMessageContent(), pid);
    }
  }

  @PreAuthorize("hasAnyRole('DOCTOR','ASSISTANT','OPERATOR')")
  @GetMapping(value = "/image/latest")
  public Response getLatestImage(
      @CurrentUserId Long id,
      @RequestParam("patientId") String patientId) {

    if (!AuthUtils.isUserOfRole(UserRole.OPERATOR,
        SecurityContextHolder.getContext().getAuthentication())) {

      boolean isBinding = userService.checkBindRelation(id, HashUtils.decode(patientId));
      if (!isBinding) {
        throw new FailureException(UserErrorCode.BINDING_RELATION_INVALID, "不合法的绑定关系");
      }
    }
    ImageApiResponse response = medicalService.getPatientLatestImages(HashUtils.decode(patientId));
    return Response.instanceSuccess(response);
  }


  @PreAuthorize("hasAnyRole('DOCTOR','ASSISTANT','OPERATOR')")
  @GetMapping(value = "/image/detail")
  public Response getImageList(
      @CurrentUserId Long id,
      @RequestParam("patientId") String patientId,
      @RequestParam(value = "type", required = false) ImageApiType type,
      @RequestParam("scope") ImageListScope scope,
      @RequestParam(value = "pageAt", required = false) Integer pageAt,
      @RequestParam(value = "pageSize", required = false) Integer pageSize) {

    ValidationUtils.validatePaginationParams(pageAt, pageSize);

    if (!AuthUtils.isUserOfRole(UserRole.OPERATOR,
        SecurityContextHolder.getContext().getAuthentication())) {

      boolean isBinding = userService.checkBindRelation(id, HashUtils.decode(patientId));
      if (!isBinding) {
        throw new FailureException(UserErrorCode.BINDING_RELATION_INVALID, "不合法的绑定关系");
      }
    }

    ImageApiResponse response;
    List<ImageType> imageType = null;

    if (Objects.nonNull(type)) {
      if (type == ImageApiType.CTGZ) {
        imageType = List.of(ImageType.CT, ImageType.GZ);
      } else {
        imageType = List.of(ImageType.valueOf(type.name()));
      }
    }

    if (scope == ImageListScope.CHECKED) {
      response = medicalService.getImageList(HashUtils.decode(patientId), imageType,
          ImageStatus.CHECKED, pageAt, pageSize);
    } else {
      response = medicalService.getImageList(HashUtils.decode(patientId), imageType,
          ImageStatus.SUCCESS, pageAt, pageSize);
    }
    return Response.instanceSuccess(response);
  }

  @DeleteMapping("/image/type")
  @PreAuthorize("hasRole('OPERATOR')")
  public Response deleteImageType(@RequestBody DeleteImageTypeApiRequest request) {
    if (request.invalid()) {
      logger.warn("delete image type invalid ! {}", request);
      throw new FailureException(ErrorCode.INVALID_PARAMS);
    }
    medicalService.deleteImageType(
        request.getType().stream().map(t -> ImageType.valueOf(t.name()))
            .collect(Collectors.toList()), HashUtils.decode(request.getImageId()));
    return Response.instanceSuccess();
  }

  @PreAuthorize("hasRole('OPERATOR')")
  @PostMapping("/medicalrecord")
  public Response handleMedicalRecord(@CurrentUserId Long uid,
      @Valid @RequestBody MedicalRecordApiRequest request) {

    if (request.recordInvalid()) {
      logger.warn("handle medical record param is invalid ! request -> {}", request);
      throw new FailureException(ErrorCode.INVALID_PARAMS,
          "handle medical record param is invalid !");
    }
    MedicalRecordApiResponse response = medicalService
        .handleMedicalRecord(MedicalRecordFactory.of(request));
    sendImageBurringMessage(request.getStatus(), request.getPatientId(), uid);
    return Response.instanceSuccess(response);
  }

  @PreAuthorize("hasAnyRole('OPERATOR','DOCTOR','ASSISTANT')")
  @GetMapping("/medicalrecord")
  public Response getMedicalRecord(
      @RequestParam(value = "imageId", required = false) String imageId,
      @RequestParam(value = "patientId", required = false) String patientId) {

    if (StringUtils.isEmpty(imageId) && StringUtils.isEmpty(patientId)) {
      throw new FailureException(ErrorCode.INVALID_PARAMS, "get medical record param is invalid !");
    }
    Long imageId_ = null;
    Long patientId_ = null;
    if (!StringUtils.isEmpty(imageId)) {
      imageId_ = HashUtils.decode(imageId);
    }
    if (!StringUtils.isEmpty(patientId)) {
      patientId_ = HashUtils.decode(patientId);
    }
    MedicalRecordApiResponse response = medicalService.getMedicalRecord(imageId_, patientId_);
    return Response.instanceSuccess(response);
  }

  @PreAuthorize("hasRole('OPERATOR')")
  @PostMapping("/diagnosis")
  public Response addDiagnosis(@CurrentUserId Long uid,
      @RequestBody AddDiagnosisApiRequest request) {
    if (StringUtils.isEmpty(request.getPatientId())
        || request.diagnosisInvalid()
        || request.invalid()) {
      throw new FailureException(ErrorCode.INVALID_PARAMS,
          "add diagnosis api request is null !");
    }
    request.checkStentSize();
    boolean result = medicalService.addDiagnosis(DiagnosisFactory.of(request));
    if (!result) {
      throw new FailureException(ErrorCode.REQUEST_FAILED, "添加诊断失败！");
    }
    sendImageBurringMessage(request.getStatus(), request.getPatientId(), uid);
    //触发医学逻辑
    medicalLogicComponent.triggerML(HashUtils.decode(request.getPatientId()));
    return Response.instanceSuccess();
  }


  @PreAuthorize("hasRole('OPERATOR')")
  @PatchMapping("/diagnosis")
  public Response updateDiagnosis(@CurrentUserId Long uid,
      @RequestBody UpdateDiagnosisApiRequest request) {

    if (StringUtils.isEmpty(request.getId())
        || request.diagnosisInvalid()
        || request.invalid()) {
      throw new FailureException(ErrorCode.INVALID_PARAMS,
          "update diagnosis api request is null !");
    }
    boolean result = medicalService.updateDiagnosis(DiagnosisFactory.of(request));
    if (!result) {
      throw new FailureException(ErrorCode.REQUEST_FAILED, "更新诊断失败！");
    }
    sendImageBurringMessage(request.getStatus(), request.getPatientId(), uid);
    //触发医学逻辑
    medicalLogicComponent.triggerML(HashUtils.decode(request.getPatientId()));
    return Response.instanceSuccess();
  }

  @PreAuthorize("hasRole('OPERATOR')")
  @DeleteMapping("/diagnosis/{id}")
  public Response deleteDiagnosis(@PathVariable("id") String id) {
    medicalService.deleteDiagnosis(HashUtils.decode(id));
    return Response.instanceSuccess();
  }

  @PreAuthorize("hasAnyRole('OPERATOR','DOCTOR','ASSISTANT')")
  @GetMapping("/diagnosis")
  public Response getDiagnosis(
      @RequestParam(value = "imageId", required = false) String imageId,
      @RequestParam(value = "patientId", required = false) String patientId) {

    if (StringUtils.isEmpty(imageId) && StringUtils.isEmpty(patientId)) {
      throw new FailureException(ErrorCode.INVALID_PARAMS,
          "get diagnosis imageId and patientId all be null !");
    }
    DiagnosisApiResponse response = medicalService
        .getDiagnosis(!StringUtils.isEmpty(imageId) ? HashUtils.decode(imageId) : null,
            !StringUtils.isEmpty(patientId) ? HashUtils.decode(patientId) : null);
    if (response == null) {
      throw new FailureException(ErrorCode.REQUEST_FAILED, "获取大病历数据失败！");
    }
    return Response.instanceSuccess(response);
  }

  @PreAuthorize("hasRole('OPERATOR')")
  @GetMapping("/disease")
  public Response getDisease(@RequestParam("name") String name) {
    return Response.instanceSuccess(medicalService.getDisease(name));
  }

  @PreAuthorize("hasRole('OPERATOR')")
  @PostMapping("/image/review")
  public Response imageReview(
      @RequestBody ImageReviewApiRequest request) {

    if (request.reviewInfoInvalid() || request.invalid()) {
      throw new FailureException(ErrorCode.INVALID_PARAMS,
          "image review api request param is null !");
    }

    boolean result = medicalService.imageReview(ImageFactory.of(request));
    if (!result) {
      throw new FailureException(ErrorCode.REQUEST_FAILED, "审核图片失败！");
    }
    return Response.instanceSuccess();
  }

  @PreAuthorize("hasRole('OPERATOR')")
  @GetMapping("/image/type")
  public Response getAllImageType() {
    return Response.instanceSuccess(medicalService.getAllImageType());
  }

  @PreAuthorize("hasAnyRole('DOCTOR','ASSISTANT')")
  @GetMapping("/inspection")
  public Response getInspection(@CurrentUserId long id,
      @RequestParam(value = "patientId") String patientId) {

    long _patientId = HashUtils.decode(patientId);
    boolean isBinding = userService.checkBindRelation(id, _patientId);
    if (!isBinding) {
      throw new FailureException(UserErrorCode.BINDING_RELATION_INVALID, "不合法的绑定关系");
    }
    LatestInspectionApiResponse response = medicalService.getLatestInspection(_patientId);
    return Response.instanceSuccess(response);
  }


  @PreAuthorize("hasRole('OPERATOR')")
  @GetMapping("/inspection/item")
  public Response getInspectionItem(@RequestParam(value = "imageType") ImageApiType imageType) {
    InspectionItemResponse response = medicalService.getInspectionItemsByImageType(imageType);
    return Response.instanceSuccess(response);
  }


  @PreAuthorize("hasRole('OPERATOR')")
  @PostMapping("/inspection")
  public Response inspection(@CurrentUserId Long uid,
      @Valid @RequestBody InspectionApiRequest request) {

    if (request.invalid() || request.apiInspectionInvalid()) {
      logger.warn("add inspection request -> {}", request);
      throw new FailureException(ErrorCode.INVALID_PARAMS,
          "inspection  api request param is invalid !");
    }
    boolean result = medicalService.handleInspection(InspectionFactory.of(request));
    if (!result) {
      logger.error("add inspection fail ! request -> {}", request);
      throw new FailureException(ErrorCode.REQUEST_FAILED, "add inspection fail ！");
    }
    sendImageBurringMessage(request.getStatus(), request.getPatientId(), uid);
    return Response.instanceSuccess();
  }


  @PreAuthorize("hasRole('OPERATOR')")
  @GetMapping("/inspection/detail")
  public Response getInspectionByImageId(
      @RequestParam(value = "imageId") String imageId,
      @RequestParam(value = "type") ImageApiType type) {

    return Response
        .instanceSuccess(medicalService.getInspectionByImageId(HashUtils.decode(imageId), type));
  }

  @PreAuthorize("hasAnyRole('DOCTOR','ASSISTANT','OPERATOR')")
  @GetMapping("/inspection/cardiogram")
  public Response getCardiogram(
      @RequestParam(value = "patientId") String patientId) {

    boolean replaceNameSign = false;

    if (!AuthUtils.isUserOfRole(UserRole.OPERATOR,
        SecurityContextHolder.getContext().getAuthentication())) {
      replaceNameSign = true;
    }

    CardiogramResponse response =
        medicalService.getPatientCardiogram(HashUtils.decode(patientId), replaceNameSign);
    return Response.instanceSuccess(response);
  }


  @PreAuthorize("hasAnyRole('DOCTOR','ASSISTANT','OPERATOR')")
  @PostMapping("/symptom")
  public Response symptom(@CurrentUserId Long uid, @RequestBody @Valid SymptomApiRequest request) {

    SymptomRequest symptomRequest = SymptomFactory.of(request);
    symptomRequest.setCreatedBy(uid);
    SymptomApiResponse response = medicalService.handleSymptom(symptomRequest);
    return Response.instanceSuccess(response);
  }

  @PreAuthorize("hasAnyRole('DOCTOR','ASSISTANT','OPERATOR')")
  @GetMapping("/symptom")
  public Response getLatestSymptom(
      @RequestParam(value = "patientId") String patientId) {

    SymptomApiResponse response =
        medicalService.getSymptomByPatientId(HashUtils.decode(patientId));

    return Response.instanceSuccess(response);
  }

  @PreAuthorize("hasAnyRole('DOCTOR','ASSISTANT','OPERATOR')")
  @GetMapping("/symptom/history")
  public Response getSymptomHistory(@RequestParam(value = "patientId") String patientId,
      Integer month, Integer year) {
    SymptomsResponse response = medicalService
        .findSymptomHistory(HashUtils.decode(patientId), month, year);
    return Response.instanceSuccess(response);
  }

  @PreAuthorize("hasAnyRole('DOCTOR','ASSISTANT','OPERATOR')")
  @GetMapping("basic/symptom")
  public Response getBasicSymptom(
      @RequestParam(value = "pageAt", defaultValue = "1") @Min(1) Integer pageAt,
      @RequestParam(value = "pageSize", defaultValue = "15") @Min(1) @Max(50) Integer pageSize,
      @RequestParam(value = "name", required = false) String name) {
    BasicSymptomsResponse response = medicalService.getBasicSymptoms(pageAt, pageSize, name);
    return Response.instanceSuccess(response);
  }

  /**
   * 根据医院名称模糊查询所有医院
   */
  @PreAuthorize("hasAnyRole('OPERATOR')")
  @GetMapping("/basic/hospital")
  public Response getBasicHospital(
      @RequestParam(value = "name") String name) {
    BasicHospitalsApiResponse response = medicalService.getBasicHospitals(name);
    return Response.instanceSuccess(response);
  }

  @PreAuthorize("hasAnyRole('DOCTOR','ASSISTANT','OPERATOR')")
  @GetMapping("/basic/hospital/{id}")
  public Response getHospitalDetail(@PathVariable(value = "id") String id) {
    BasicHospitalDetailApiResponse response = medicalService.getHospitalDetail(id);
    return Response.instanceSuccess(response);
  }

  @PreAuthorize("hasAnyRole('DOCTOR','ASSISTANT')")
  @RequestMapping(value = "/taboo", method = RequestMethod.GET)
  public Response getMedicineTaboo(
      @RequestParam String patientId,
      @RequestParam List<String> medicineIds,
      @RequestParam(defaultValue = "ADJUST_ME") TabooRequest.Type type) {
    if (StringUtils.isEmpty(patientId) || medicineIds.isEmpty()) {
      throw new FailureException(ErrorCode.INVALID_PARAMS);
    }
    List<Long> decodedMedicineIds = medicineIds.stream()
        .map(id -> StringUtils.isNotEmpty(id) ? HashUtils.decode(id) : 0)
        .filter(aLong -> aLong != 0)
        .collect(Collectors.toList());
    if (decodedMedicineIds.isEmpty()) {
      return Response.instanceSuccess(new MedicinesTaboosResponse());
    }
    MedicinesTaboosResponse response = medicalService
        .getMedicineTaboo(HashUtils.decode(patientId), decodedMedicineIds, type);
    return Response.instanceSuccess(response);
  }

  /*********************处理方式部分*************************/

  /**
   * 根据处理方式名称模糊查询所有处理方式
   */
  @PreAuthorize("hasAnyRole('OPERATOR')")
  @GetMapping("/basic/treatment")
  public Response getBasicTreament(
      @RequestParam(value = "name") String name,
      @RequestParam(value = "pageAt", defaultValue = "1") @Min(1) Integer pageAt,
      @RequestParam(value = "pageSize", defaultValue = "15") @Min(1) @Max(50) Integer pageSize) {
    BasicTreatmentsApiResponse response = medicalService.getBasicTreaments(name, pageAt, pageSize);
    return Response.instanceSuccess(response);
  }

  /**
   * 根据支架名称模糊查询idList支架
   */
  @PreAuthorize("hasAnyRole('OPERATOR')")
  @GetMapping("/basic/stent")
  public Response getBasicCoronaryStent(
      @RequestParam(value = "name") String name,
      @RequestParam(value = "idList", required = false) List<String> idList,
      @RequestParam(value = "pageAt", defaultValue = "1") @Min(1) Integer pageAt,
      @RequestParam(value = "pageSize", defaultValue = "15") @Min(1) @Max(50) Integer pageSize) {
    BasicCoronaryStentApiResponse response = medicalService
        .getBasicStents(name, idList, pageAt, pageSize);
    return Response.instanceSuccess(response);
  }

  @PreAuthorize("hasAnyRole('DOCTOR','ASSISTANT')")
  @GetMapping("/inspection/chart")
  public Response getInspectionChart(@CurrentUserId long id,
      @RequestParam(value = "patientId") String patientId,
      @RequestParam(value = "type") InspectionChartApiType type,
      @RequestParam(value = "startTime", required = false) Long startTime,
      @RequestParam(value = "endTime", required = false) Long endTime) {

    StandardDataApiResponse response = medicalService
        .getInspectionChart(HashUtils.decode(patientId), type, startTime, endTime);
    return Response.instanceSuccess(response);
  }


}
