package cn.xinzhili.api.doctor.client;


import cn.xinzhili.medical.api.ImageStatus;
import cn.xinzhili.medical.api.ImageType;
import cn.xinzhili.medical.api.InspectionChartItem;
import cn.xinzhili.medical.api.InspectionScope;
import cn.xinzhili.medical.api.SymptomInfo;
import cn.xinzhili.medical.api.request.AddDiagnosisRequest;
import cn.xinzhili.medical.api.request.DeleteImageTypeRequest;
import cn.xinzhili.medical.api.request.HandleCustomizableReferenceRequest;
import cn.xinzhili.medical.api.request.ImageFeedbackRequest;
import cn.xinzhili.medical.api.request.ImageReviewInfoRequest;
import cn.xinzhili.medical.api.request.InspectionRequest;
import cn.xinzhili.medical.api.request.MLRequest;
import cn.xinzhili.medical.api.request.MedicalRecordRequest;
import cn.xinzhili.medical.api.request.SymptomRequest;
import cn.xinzhili.medical.api.request.TabooRequest;
import cn.xinzhili.medical.api.request.UpdateDiagnosisRequest;
import cn.xinzhili.medical.api.response.BasicHospitalDetailResponse;
import cn.xinzhili.medical.api.response.BasicHospitalsResponse;
import cn.xinzhili.medical.api.response.BasicSymptomsResponse;
import cn.xinzhili.medical.api.response.CoronaryStentInfoListResponse;
import cn.xinzhili.medical.api.response.DiagnosisResponse;
import cn.xinzhili.medical.api.response.DiseaseResponse;
import cn.xinzhili.medical.api.response.ImageResponse;
import cn.xinzhili.medical.api.response.InspectionReferenceResponse;
import cn.xinzhili.medical.api.response.InspectionResponse;
import cn.xinzhili.medical.api.response.LatestInspectionResponse;
import cn.xinzhili.medical.api.response.LifeStatusResponse;
import cn.xinzhili.medical.api.response.MedicalRecordsResponse;
import cn.xinzhili.medical.api.response.MedicinesTaboosResponse;
import cn.xinzhili.medical.api.response.StandardDataResponse;
import cn.xinzhili.medical.api.response.StatisticsImagesResponse;
import cn.xinzhili.medical.api.response.StatisticsStandardResponse;
import cn.xinzhili.medical.api.response.SymptomsResponse;
import cn.xinzhili.medical.api.response.TreatmentResponse;
import cn.xinzhili.xutils.core.http.Response;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Date: 09/03/2017 Time: 10:40 AM
 *
 * @author Gan Dong
 */
@FeignClient("medical-service")
public interface MedicalServiceClient {


  /**
   * 患者最新的已审核图片
   *
   * @param patientId 患者id
   * @return 图片数据
   */
  @RequestMapping(method = RequestMethod.GET, value = "/image/latest")
  Response<ImageResponse> latestImages(
      @RequestParam("patientId") Long patientId);

  /**
   * 患者某类型图片列表
   *
   * @param patientId 患者id
   * @param type 图片类型
   * @return 图片数据
   */
  @RequestMapping(method = RequestMethod.GET, value = "/images")
  Response<ImageResponse> getImageList(
      @RequestParam("patientId") Long patientId,
      @RequestParam(value = "type", required = false) List<ImageType> type,
      @RequestParam("status") ImageStatus status,
      @RequestParam(value = "pageAt", required = false) Integer pageAt,
      @RequestParam(value = "pageSize", required = false) Integer pageSize);

  @RequestMapping(value = "/medicalrecord", method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  Response<MedicalRecordsResponse> handleMedicalRecord(@RequestBody MedicalRecordRequest request);

  @RequestMapping(value = "/medicalrecord", method = RequestMethod.GET)
  Response<MedicalRecordsResponse> getMedicalRecord(
      @RequestParam(value = "imageId", required = false) Long imageId,
      @RequestParam(value = "patientId", required = false) Long patientId);

  /**
   * 添加诊断
   *
   * @param request 新建诊断数据
   * @return 是否成功
   */
  @RequestMapping(value = "/diagnosis", method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  Response addDiagnosis(@RequestBody AddDiagnosisRequest request);

  /**
   * 修改诊断
   *
   * @param request 修改诊断数据
   * @return 是否成功
   */
  @RequestMapping(value = "/diagnosis", method = RequestMethod.PATCH,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  Response updateDiagnosis(@RequestBody UpdateDiagnosisRequest request);

  @DeleteMapping(value = "/diagnosis/{id}")
  Response deleteDiagnosis(@PathVariable("id") Long id);

  /**
   * 查询诊断
   *
   * @param imageId 图片ID
   * @param patientId 患者id
   * @return 符合条件诊断数据
   */
  @RequestMapping(value = "/diagnosis", method = RequestMethod.GET)
  Response<DiagnosisResponse> getDiagnosis(
      @RequestParam(value = "imageId", required = false) Long imageId,
      @RequestParam(value = "patientId", required = false) Long patientId);

  /**
   * 根据疾病名  模糊查询疾病列表
   *
   * @param name 疾病名
   * @return 疾病列表
   */
  @RequestMapping(value = "/disease", method = RequestMethod.GET)
  Response<DiseaseResponse> getDisease(@RequestParam("name") String name,
      @RequestParam("count") Integer count);

  /**
   * 添加或者更新图片审核信息
   *
   * @param request 审核信息
   * @return 是否成功
   */
  @RequestMapping(value = "/review", method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  Response reviewImage(@RequestBody ImageReviewInfoRequest request);


  /**
   * 获取生化数据 `
   *
   * @param patientId 患者id
   * @param scope LATEST/最新  NORMAL/历史纪录
   * @return inspection record
   */
  @RequestMapping(value = "/inspection", method = RequestMethod.GET)
  Response<LatestInspectionResponse> getInspectionInfo(
      @RequestParam(value = "patientId") Long patientId,
      @RequestParam(value = "scope") InspectionScope scope);

  @RequestMapping(method = RequestMethod.GET, value = "/life/status/{patientId}",
      consumes = MediaType.APPLICATION_JSON_VALUE)
  Response<LifeStatusResponse> getLatestLifeStatusByPatientid(
      @PathVariable("patientId") Long patientId);

  @RequestMapping(value = "/inspection/item", method = RequestMethod.GET)
  Response<InspectionReferenceResponse> getInspectionItemsByImageType(
      @RequestParam(value = "type") ImageType type);

  @RequestMapping(value = "/inspection", method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  Response handleInspection(@RequestBody InspectionRequest request);

  @RequestMapping(value = "/inspection/reference", method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  Response handleInspectionReference(@RequestBody HandleCustomizableReferenceRequest request);

  @RequestMapping(value = "/inspection/detail", method = RequestMethod.GET)
  Response<InspectionResponse> getInspectionDetailByImageId(
      @RequestParam(value = "imageId") Long imageId,
      @RequestParam(value = "type") ImageType type);

  @RequestMapping(value = "/symptom", method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  Response<SymptomInfo> symptom(@RequestBody SymptomRequest request);

  @RequestMapping(value = "/symptom/latest", method = RequestMethod.GET)
  Response<SymptomInfo> getLatestSymptom(
      @RequestParam(value = "patientId") Long patientId);

  @RequestMapping(value = "/inspection/cardiogram", method = RequestMethod.GET)
  Response<InspectionResponse> getPatientCardiogramData(
      @RequestParam(value = "patientId") long patientId);

  @RequestMapping(value = "/rule/trigger", method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  Response triggerML(@RequestBody MLRequest request);

  @RequestMapping(value = "/rule/image/feedback", method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  Response feedbackAfterReview(@RequestBody ImageFeedbackRequest imageFeedbackRequest);

  @RequestMapping(method = RequestMethod.POST, value = "/medicine/taboo",
      consumes = MediaType.APPLICATION_JSON_VALUE)
  Response<MedicinesTaboosResponse> checkMedicineTaboo(@RequestBody TabooRequest tabooRequest);

  @GetMapping(value = "/basic/symptom")
  Response<BasicSymptomsResponse> findBasicSymptoms(@RequestParam("pageAt") Integer pageAt,
      @RequestParam("pageSize") Integer pageSize, @RequestParam("name") String name);

  /**
   * 根据医院名称模糊查找医院
   */
  @GetMapping(value = "/basic/hospital")
  Response<BasicHospitalsResponse> findBasicHospitals(@RequestParam("pageAt") Integer pageAt,
      @RequestParam("pageSize") Integer pageSize, @RequestParam("name") String name);

  @GetMapping(value = "/basic/hospital/{id}")
  Response<BasicHospitalDetailResponse> findHospitalDetail(@PathVariable("id") long id);

  @GetMapping(value = "/symptom/history")
  Response<SymptomsResponse> findSymptomHistory(@RequestParam("patientId") Long patientId,
      @RequestParam(value = "month", required = false) Integer month,
      @RequestParam(value = "year", required = false) Integer year);

  @GetMapping("/treatment")
  Response<TreatmentResponse> findBasicTreatments(
      @RequestParam(value = "name", required = false) String name,
      @Min(1) @RequestParam(value = "pageAt", defaultValue = "1") int pageAt,
      @Max(100) @RequestParam(value = "pageSize", defaultValue = "15") int pageSize);

  @GetMapping("/stent")
  Response<CoronaryStentInfoListResponse> findBasicCoronaryStents(
      @RequestParam(value = "name", required = false) String name
      , @RequestParam(value = "ids", required = false) List<Long> ids
      , @RequestParam(value = "pageAt", defaultValue = "1") Integer pageAt,
      @RequestParam(value = "pageSize", defaultValue = "7") Integer pageSize);

  @GetMapping("/statistics/department/images")
  Response<StatisticsImagesResponse> getDepartmentImages(
      @RequestParam("organizationId") Long organizationId);

  @GetMapping("/statistics/department/standard/rate")
  Response<StatisticsStandardResponse> getDepartmentStandardRate(
      @RequestParam("organizationId") Long organizationId);


  @DeleteMapping(value = "/image/type", consumes = MediaType.APPLICATION_JSON_VALUE)
  Response deleteImageType(@RequestBody DeleteImageTypeRequest request);


  @GetMapping(value = "/inspection/chart")
  public Response<StandardDataResponse> getInspectionChart(
      @RequestParam(value = "patientId") long patientId,
      @RequestParam(value = "type") InspectionChartItem type,
      @RequestParam(value = "startTime", required = false) Long startTime,
      @RequestParam(value = "endTime", required = false) Long endTime);

}
