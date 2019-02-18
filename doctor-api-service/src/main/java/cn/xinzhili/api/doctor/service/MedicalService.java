package cn.xinzhili.api.doctor.service;


import cn.xinzhili.api.doctor.bean.BasicHospitalDetailApiResponse;
import cn.xinzhili.api.doctor.bean.ImageApiInfo;
import cn.xinzhili.api.doctor.bean.ImageApiType;
import cn.xinzhili.api.doctor.bean.InspectionApiInfo;
import cn.xinzhili.api.doctor.bean.InspectionChartApiType;
import cn.xinzhili.api.doctor.bean.response.BasicCoronaryStentApiResponse;
import cn.xinzhili.api.doctor.bean.response.BasicHospitalsApiResponse;
import cn.xinzhili.api.doctor.bean.response.BasicTreatmentsApiResponse;
import cn.xinzhili.api.doctor.bean.response.CardiogramResponse;
import cn.xinzhili.api.doctor.bean.response.DiagnosisApiResponse;
import cn.xinzhili.api.doctor.bean.response.DiseaseApiResponse;
import cn.xinzhili.api.doctor.bean.response.ImageApiResponse;
import cn.xinzhili.api.doctor.bean.response.ImageTypeResponse;
import cn.xinzhili.api.doctor.bean.response.ImageTypeResponse.TypeInfo;
import cn.xinzhili.api.doctor.bean.response.InspectionApiResponse;
import cn.xinzhili.api.doctor.bean.response.InspectionItemResponse;
import cn.xinzhili.api.doctor.bean.response.LatestInspectionApiResponse;
import cn.xinzhili.api.doctor.bean.response.MedicalRecordApiResponse;
import cn.xinzhili.api.doctor.bean.response.StandardDataApiResponse;
import cn.xinzhili.api.doctor.bean.response.StatisticsImagesApiResponse;
import cn.xinzhili.api.doctor.bean.response.StatisticsStandardApiResponse;
import cn.xinzhili.api.doctor.bean.response.SymptomApiResponse;
import cn.xinzhili.api.doctor.client.MedicalServiceClient;
import cn.xinzhili.api.doctor.error.MedicalApiErrorCode;
import cn.xinzhili.api.doctor.util.BasicHospitalFactory;
import cn.xinzhili.api.doctor.util.CoronaryStentFactory;
import cn.xinzhili.api.doctor.util.DiagnosisFactory;
import cn.xinzhili.api.doctor.util.DiseaseFactory;
import cn.xinzhili.api.doctor.util.ImageFactory;
import cn.xinzhili.api.doctor.util.InspectionFactory;
import cn.xinzhili.api.doctor.util.MedicalRecordFactory;
import cn.xinzhili.api.doctor.util.SymptomFactory;
import cn.xinzhili.api.doctor.util.TreatmentFactory;
import cn.xinzhili.medical.api.BasicHospitalInfo;
import cn.xinzhili.medical.api.ImageDetailInfo;
import cn.xinzhili.medical.api.ImageStatus;
import cn.xinzhili.medical.api.ImageType;
import cn.xinzhili.medical.api.InspectionChartItem;
import cn.xinzhili.medical.api.InspectionInfo;
import cn.xinzhili.medical.api.InspectionScope;
import cn.xinzhili.medical.api.ReportInfo;
import cn.xinzhili.medical.api.SymptomInfo;
import cn.xinzhili.medical.api.error.MedicalErrorCode;
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
import cn.xinzhili.medical.api.response.MedicalRecordsResponse;
import cn.xinzhili.medical.api.response.MedicinesTaboosResponse;
import cn.xinzhili.medical.api.response.StandardDataResponse;
import cn.xinzhili.medical.api.response.StatisticsImagesResponse;
import cn.xinzhili.medical.api.response.StatisticsStandardResponse;
import cn.xinzhili.medical.api.response.SymptomsResponse;
import cn.xinzhili.medical.api.response.TreatmentResponse;
import cn.xinzhili.xutils.core.ErrorCode;
import cn.xinzhili.xutils.core.FailureException;
import cn.xinzhili.xutils.core.SystemException;
import cn.xinzhili.xutils.core.http.Response;
import cn.xinzhili.xutils.core.util.HashUtils;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Date: 09/03/2017 Time: 10:40 AM
 *
 * @author Gan Dong
 */
@Service
public class MedicalService {

  private static final Logger logger = LoggerFactory.getLogger(MedicalService.class);

  private static final Integer DEFAULT_PAGE_AT = 1;
  private static final Integer BASIC_HOSPITAL_LIMIT = 25;
  @Autowired
  private UserService userService;
  @Autowired
  private DpcService dpcService;

  @Autowired
  private MedicalServiceClient medicalServiceClient;

  public ImageApiResponse getPatientLatestImages(long patientId) {

    Response<ImageResponse> response = medicalServiceClient.latestImages(patientId);
    if (response.isFailed()) {
      logger.warn("get latest image  fail ! patientId -> {}", patientId);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    }
    if (response.isError()) {
      logger.error("get latest image  fail ! patientId -> {}", patientId);
      throw new SystemException(ErrorCode.SERVER_ERROR);
    }
    ImageApiResponse apiResponse = new ImageApiResponse();
    apiResponse.setImages(forImageLatest(response.getDataAs(ImageResponse.class).getImages()));
    return apiResponse;

  }

  private List<ImageApiInfo> forImageLatest(List<ImageDetailInfo> source) {

    if (Objects.isNull(source)) {
      return null;
    }

    ImageDetailInfo ctDetail = new ImageDetailInfo();
    ImageDetailInfo gzDetail = new ImageDetailInfo();
    List<ImageApiInfo> result = new ArrayList<>();

    for (ImageDetailInfo detail : source) {
      ImageType type = detail.getImage().getType();
      if (Objects.nonNull(type) && ImageType.CT == type) {
        ctDetail = detail;
      } else if (Objects.nonNull(type) && ImageType.GZ == type) {
        gzDetail = detail;
      } else {
        result.add(ImageFactory.api(detail));
      }
    }
    result.add(ImageFactory.apiForImageLatestCt(ctDetail, gzDetail));
    return result;
  }


  public ImageApiResponse getImageList(long patientId, List<ImageType> type, ImageStatus status,
      Integer pageAt, Integer pageSize) {

    Response<ImageResponse> response = medicalServiceClient.getImageList(
        patientId, type, status, pageAt, pageSize);
    if (response.isFailed()) {
      logger.warn("get image list fail ! patientId -> {}", patientId);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    }
    if (response.isError()) {
      logger.error("get image list fail ! patientId -> {}", patientId);
      throw new SystemException(ErrorCode.SERVER_ERROR);
    }
    ImageResponse imageResponse = response.getDataAs(ImageResponse.class);
    ImageApiResponse apiResponse = new ImageApiResponse();
    apiResponse.setImages(ImageFactory.apis(imageResponse.getImages()));
    apiResponse.setTotal(imageResponse.getTotal());
    return apiResponse;
  }

  public void deleteImageType(List<ImageType> types, Long imageId) {
    if (types.isEmpty()) {
      return;
    }
    DeleteImageTypeRequest request = new DeleteImageTypeRequest();
    request.setImageId(imageId);
    request.setType(types);
    Response response = medicalServiceClient.deleteImageType(request);
    if (response.isFailed()) {
      if (response.getFailureCode() == MedicalErrorCode.IMAGE_TYPE_IS_NULL.getCode()) {
        throw new FailureException(MedicalErrorCode.IMAGE_TYPE_IS_NULL);
      }
      logger.warn("delete image type fail ! {},{}", request, response);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    } else if (response.isError()) {
      logger.error("delete image type error ! {},{}", request, response);
      throw new FailureException(ErrorCode.SERVER_ERROR);
    }
  }


  public MedicalRecordApiResponse getMedicalRecord(Long imageId, Long patientId) {

    Response<MedicalRecordsResponse> response = medicalServiceClient
        .getMedicalRecord(imageId, patientId);
    if (!response.isSuccessful()) {
      logger.warn("get medical record fail ! response -> {}", response);
      throw new FailureException(ErrorCode.INVALID_PARAMS, " get medical record   fail ！");
    }
    MedicalRecordsResponse record = response.getDataAs(MedicalRecordsResponse.class);
    return MedicalRecordFactory.api(record);
  }

  public MedicalRecordApiResponse handleMedicalRecord(MedicalRecordRequest request) {

    Response<MedicalRecordsResponse> response = medicalServiceClient.handleMedicalRecord(request);
    if (!response.isSuccessful()) {
      logger.warn("handle medical fail ! request -> {},response -> {}", request, response);
      throw new FailureException(ErrorCode.INVALID_PARAMS, " handle  medical record   fail ！");
    }
    MedicalRecordsResponse record = response.getDataAs(MedicalRecordsResponse.class);
    return MedicalRecordFactory.api(record);
  }


  /***** diagnosis *****/
  public boolean addDiagnosis(AddDiagnosisRequest request) {

    Response response = medicalServiceClient.addDiagnosis(request);

    if (response.isFailed() && response.getFailureCode()
        == MedicalErrorCode.DIAGNOSIS_OVER_LIMIT.getCode()) {
      throw new FailureException(MedicalApiErrorCode.DIAGNOSIS_OVER_LIMIT);
    }

    if (response.isFailed() && response.getFailureCode()
        == MedicalErrorCode.DIAGNOSIS_STAGE1_EXIST.getCode()) {
      throw new FailureException(MedicalApiErrorCode.DIAGNOSIS_STAGE1_EXIST);
    }
    if (!response.isSuccessful()) {
      throw new FailureException(ErrorCode.INVALID_PARAMS, " add diagnosis fail ！" + response);
    }
    return true;
  }

  public boolean updateDiagnosis(UpdateDiagnosisRequest request) {

    Response response = medicalServiceClient.updateDiagnosis(request);
    if (response.isFailed()) {

      Integer failureCode = response.getFailureCode();
      if (failureCode != null && failureCode == MedicalErrorCode.DIAGNOSIS_STAGE1_EXIST.getCode()) {
        logger.warn("stage1 repeat ! request {},response {}", request, response);
        throw new FailureException(MedicalApiErrorCode.DIAGNOSIS_STAGE1_EXIST);
      }
      if (failureCode != null && failureCode == MedicalErrorCode.DIAGNOSIS_STAGE2_REPEAT
          .getCode()) {
        logger.warn("stage2 repeat ! request {},response {}", request, response);
        throw new FailureException(MedicalApiErrorCode.DIAGNOSIS_STAGE2_EXIST);
      }
      if (failureCode != null && failureCode == MedicalErrorCode.DIAGNOSIS_STAGE3_REPEAT
          .getCode()) {
        logger.warn("stage3 repeat ! request {},response {}", request, response);
        throw new FailureException(MedicalApiErrorCode.DIAGNOSIS_STAGE3_EXIST);
      }
    }

    if (response.isError()) {
      logger.error("update diagnosis error ! request {} ,response {}", request, response);
      throw new SystemException(ErrorCode.SERVER_ERROR, " update  diagnosis  error ！");
    }
    return true;
  }

  public DiagnosisApiResponse getDiagnosis(Long imageId, Long patientId) {

    Response<DiagnosisResponse> response =
        medicalServiceClient.getDiagnosis(imageId, patientId);
    if (!response.isSuccessful()) {
      throw new FailureException(ErrorCode.INVALID_PARAMS, " get  diagnosis  fail ！" + response);
    }
    DiagnosisResponse diagnosis = response.getDataAs(DiagnosisResponse.class);
    DiagnosisApiResponse diagnosisResponse = new DiagnosisApiResponse();
    if (diagnosis != null) {
      diagnosisResponse = DiagnosisFactory.api(diagnosis);
    }
    return diagnosisResponse;
  }

  public void deleteDiagnosis(long id) {
    Response response = medicalServiceClient.deleteDiagnosis(id);
    if (response.isFailed()) {
      logger.warn("delete diagnosis fail ! id {}", id);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    }
    if (response.isError()) {
      logger.error("delete diagnosis error ! id {}", id);
      throw new SystemException(ErrorCode.SERVER_ERROR);
    }
  }

  public DiseaseApiResponse getDisease(String name) {

    Response<DiseaseResponse> response = medicalServiceClient.getDisease(name, null);
    if (!response.isSuccessful()) {
      throw new FailureException(ErrorCode.INVALID_PARAMS, " get disease fail ！" + response);
    }
    DiseaseResponse disease = response.getDataAs(DiseaseResponse.class);
    if (disease == null) {
      throw new FailureException(ErrorCode.REQUEST_FAILED, "疾病数据为空！");
    }
    DiseaseApiResponse apiResponse = new DiseaseApiResponse();
    apiResponse.setDisease(DiseaseFactory.apis(disease.getDiseases()));
    return apiResponse;
  }

  public boolean imageReview(ImageReviewInfoRequest request) {

    Response response = medicalServiceClient.reviewImage(request);
    if (!response.isSuccessful()) {
      throw new FailureException(ErrorCode.INVALID_PARAMS, " image review fail ！" + response);
    }
    return true;
  }

  /**
   * 获取所有的图片类型
   */
  public ImageTypeResponse getAllImageType() {

    List<ImageTypeResponse.TypeInfo> types =

        Stream.of(ImageApiType.values()).filter(i -> i != ImageApiType.CTGZ).map(t -> {
          TypeInfo typeInfo = new ImageTypeResponse().new TypeInfo();
          typeInfo.setKey(t.name());
          typeInfo.setName(ImageType.valueOf(t.name()).getDescription());
          return typeInfo;
        }).collect(Collectors.toList());

    ImageTypeResponse response = new ImageTypeResponse();
    response.setTypes(types);
    return response;
  }

  public void handleInspectionReference(HandleCustomizableReferenceRequest request) {

    Response response = medicalServiceClient.handleInspectionReference(request);
    if (!response.isSuccessful()) {
      logger.warn("handle inspection reference fail ! request -> {},response -> {}", request,
          response);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    }
  }

  public LatestInspectionApiResponse getLatestInspection(long patientId) {

    Response<LatestInspectionResponse> response =
        medicalServiceClient.getInspectionInfo(patientId, InspectionScope.LATEST);
    if (!response.isSuccessful()) {
      logger.warn("get latest inspection fail ! patientId -> {} ,response -> {}", patientId,
          response);
      throw new FailureException(ErrorCode.INVALID_PARAMS, "get latest inspection fail ！");
    }
    LatestInspectionResponse inspection = response.getDataAs(LatestInspectionResponse.class);
    return InspectionFactory.api(inspection);
  }

  public InspectionItemResponse getInspectionItemsByImageType(ImageApiType type) {

    Response<InspectionReferenceResponse> response =
        medicalServiceClient.getInspectionItemsByImageType(ImageType.valueOf(type.name()));

    if (!response.isSuccessful()) {
      logger.warn("get inspection items fail ! response -> {}", response);
      throw new FailureException(ErrorCode.REQUEST_FAILED,
          " get inspection items by image type fail !" + response);
    }
    InspectionReferenceResponse reference = response.getDataAs(InspectionReferenceResponse.class);
    InspectionItemResponse inspectionItemResponse = new InspectionItemResponse();
    inspectionItemResponse.setItems(InspectionFactory.api2Items(reference.getReferences()));
    return inspectionItemResponse;
  }

  public boolean handleInspection(InspectionRequest request) {

    Response response = medicalServiceClient.handleInspection(request);

    if (!response.isSuccessful()) {
      logger.warn("add inspection fail ! response -> {}", response);
      throw new FailureException(ErrorCode.REQUEST_FAILED, " add inspection fail !" + response);
    }
    if (!isHuYanDanOrJianChaDan(request.getType())) {
      return true;
    }
    //todo async
    ImageFeedbackRequest imageFeedbackRequest = ImageFeedbackRequest.builder()
        .patientId(request.getPatientId()).reportAt(request.getReportInfo().getReportAt())
        .imageId(String.valueOf(request.getImageId()))
        .imageTypes(Lists.newArrayList(request.getType())).build();
    medicalServiceClient.feedbackAfterReview(imageFeedbackRequest);
    return true;
  }

  private boolean isHuYanDanOrJianChaDan(ImageType imageType) {
    return imageType.getCode() / 100 == 2 || imageType.getCode() / 100 == 3;
  }


  public InspectionApiResponse getInspectionByImageId(long imageId, ImageApiType type) {

    Response<InspectionResponse> response =
        medicalServiceClient.getInspectionDetailByImageId(imageId, ImageType.valueOf(type.name()));

    if (response.isFailed()) {
      logger.warn("get inspection fail ! {}", response);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    } else if (response.isError()) {
      logger.error("get inspection error ! {}", response);
      throw new FailureException(ErrorCode.SERVER_ERROR);
    }
    return InspectionFactory.api2Response(response.getDataAs(InspectionResponse.class));
  }


  public SymptomApiResponse handleSymptom(SymptomRequest request) {

    Response<SymptomInfo> response =
        medicalServiceClient.symptom(request);

    if (!response.isSuccessful()) {
      logger.warn("handle symptom fail ! response -> {}", response);
      throw new FailureException(
          ErrorCode.REQUEST_FAILED, " handle symptom fail !" + response);
    }

    SymptomInfo symptom =
        response.getDataAs(SymptomInfo.class);

    if (symptom == null) {
      logger.warn("symptom is null ! response -> {}", response);
      throw new FailureException(
          ErrorCode.REQUEST_FAILED, "symptom is null !");
    }
    return SymptomFactory.api(symptom);
  }

  public SymptomApiResponse getSymptomByPatientId(long patientId) {

    Response<SymptomInfo> response
        = medicalServiceClient.getLatestSymptom(patientId);

    if (!response.isSuccessful()) {
      logger.warn("get symptom fail ! patientId -> {}, response -> {}", patientId, response);
      throw new FailureException(ErrorCode.REQUEST_FAILED, "get symptom fail !");
    }

    SymptomInfo symptom =
        response.getDataAs(SymptomInfo.class);

    if (symptom == null) {
      logger.warn("symptom is null ! response -> {}", response);
      throw new FailureException(
          ErrorCode.REQUEST_FAILED, "symptom is null !");
    }
    return SymptomFactory.api(symptom);
  }

  public CardiogramResponse getPatientCardiogram(long patientId, boolean replaceNameSign) {
    Response<InspectionResponse> response = medicalServiceClient
        .getPatientCardiogramData(patientId);
    if (!response.isSuccessful()) {
      logger.warn("get cardiogram fail ! patientId -> {}", patientId);
      throw new FailureException(ErrorCode.REQUEST_FAILED, "get cardiogram fail !");
    }
    InspectionResponse inspectionResponse = response.getDataAs(InspectionResponse.class);
    List<InspectionInfo> patientCardiogram = inspectionResponse.getInspections();
    if (patientCardiogram == null) {
      logger.warn("patient cardiogram is null ! patientId -> {}", patientId);
      throw new FailureException(ErrorCode.REQUEST_FAILED, "get cardiogram fail !");
    }
    LinkedList<InspectionApiInfo> cardiogramInfo = new LinkedList<>();

    cardiogramByOrderList().forEach(t -> cardiogramInfo
        .add(InspectionFactory.api(getCardiogramByOrder(patientCardiogram, t), replaceNameSign)));

    CardiogramResponse cardiogramResponse = new CardiogramResponse();
    cardiogramResponse.setCardiogramInfo(cardiogramInfo);
    BasicHospitalInfo basicHospitalInfo = Optional.ofNullable(inspectionResponse.getReportInfo())
        .orElse(new ReportInfo())
        .getHospital();
    if (basicHospitalInfo != null) {
      cardiogramResponse.setHospital(BasicHospitalFactory.api(basicHospitalInfo));
    }
    return cardiogramResponse;
  }

  //心脏超声按这个顺序显示
  private List<String> cardiogramByOrderList() {
    return List.of("左室舒张期末内径LVD", "左房内径LA", "室间隔厚度IVS", "射血分数EF");
  }

  private InspectionInfo getCardiogramByOrder(List<InspectionInfo> source, String key) {

    if (Objects.isNull(source) || Objects.isNull(key)) {
      return null;
    }
    return source.stream().filter(t -> t.getName().equals(key)).findFirst()
        .orElseThrow(() -> new FailureException(ErrorCode.REQUEST_FAILED));
  }

  /**
   * trigger medical logical
   */
  public void triggerML(MLRequest request) {
    Response response = medicalServiceClient.triggerML(request);
    if (!response.isSuccessful()) {
      logger.warn("\n触发医学逻辑失败 request->{},\nresponse ->{}", request, response);
    }
  }

  public MedicinesTaboosResponse getMedicineTaboo(Long patientId, List<Long> medicineIds,
      TabooRequest.Type type) {
    TabooRequest tabooRequest = TabooRequest.builder().patientId(patientId).type(type)
        .medicineIds(medicineIds).build();
    Response<MedicinesTaboosResponse> response = medicalServiceClient
        .checkMedicineTaboo(tabooRequest);
    if (response.isFailed()) {
      logger.warn("get medicine taboos failed patientId:{} medicineIds:{}", patientId, medicineIds);
      throw new FailureException(MedicalApiErrorCode.GET_MEDICINE_TABOO_FAILED);
    } else if (response.isError()) {
      logger.error("get medicine taboos error patientId {} medicineIds:{}", patientId, medicineIds);
      throw new FailureException(ErrorCode.SERVER_ERROR);
    }
    return response.getDataAs(MedicinesTaboosResponse.class);
  }

  public BasicSymptomsResponse getBasicSymptoms(Integer pageAt, Integer pageSize, String name) {
    Response<BasicSymptomsResponse> response = medicalServiceClient
        .findBasicSymptoms(pageAt, pageSize, name);
    if (!response.isSuccessful()) {
      throw new FailureException(ErrorCode.REQUEST_FAILED, "call medical fail");
    }
    return response.getDataAs(BasicSymptomsResponse.class);
  }

  /**
   * 模糊查询医院信息 默认25条数据
   */
  public BasicHospitalsApiResponse getBasicHospitals(String name) {
    Response<BasicHospitalsResponse> response = medicalServiceClient
        .findBasicHospitals(DEFAULT_PAGE_AT, BASIC_HOSPITAL_LIMIT, name);
    if (response.isFailed()) {
      logger.warn("get basic hospital failed name:{}", name);
      throw new FailureException(MedicalApiErrorCode.GET_MEDICINE_TABOO_FAILED);
    } else if (response.isError()) {
      logger.error("get basic hospital error name:{}", name);
      throw new FailureException(ErrorCode.SERVER_ERROR);
    }
    BasicHospitalsResponse basicHospitalsResponse = response
        .getDataAs(BasicHospitalsResponse.class);

    BasicHospitalsApiResponse apiResponse = new BasicHospitalsApiResponse();
    apiResponse.seBasicHospitalApiInfos(
        BasicHospitalFactory.apis(basicHospitalsResponse.getBasicHospitalInfos()));

    return apiResponse;
  }

  public BasicHospitalDetailApiResponse getHospitalDetail(String id) {
    Response<BasicHospitalDetailResponse> response = medicalServiceClient
        .findHospitalDetail(HashUtils.decode(id));
    if (response.isFailed()) {
      logger.warn("get basic hospital failed id:{}", id);
      throw new FailureException(MedicalApiErrorCode.GET_MEDICINE_TABOO_FAILED);
    } else if (response.isError()) {
      logger.error("get basic hospital error id:{}", id);
      throw new FailureException(ErrorCode.SERVER_ERROR);
    }
    BasicHospitalDetailResponse basicHospitalDetailResponse = response
        .getDataAs(BasicHospitalDetailResponse.class);

    BasicHospitalDetailApiResponse apiResponse = new BasicHospitalDetailApiResponse(
        BasicHospitalFactory.api(basicHospitalDetailResponse.getHospitalInfo()));
    return apiResponse;
  }

  public SymptomsResponse findSymptomHistory(Long patientId, Integer month, Integer year) {
    Response<SymptomsResponse> response = medicalServiceClient
        .findSymptomHistory(patientId, month, year);
    if (!response.isSuccessful()) {
      logger
          .warn("call medical fail!,patientId->{},year->{},month->{},response->{]", patientId, year,
              month, response);
      throw new FailureException(ErrorCode.REQUEST_FAILED, "call medical fail!");
    }
    return response.getDataAs(SymptomsResponse.class);

  }

  /*********************处理方式部分*************************/

  public BasicTreatmentsApiResponse getBasicTreaments(String name, Integer pageAt,
      Integer pageSize) {
    Response<TreatmentResponse> response = medicalServiceClient
        .findBasicTreatments(name, pageAt, pageSize);
    if (response.isFailed()) {
      logger.warn("get basic treatment failed name:{}", name);
      throw new FailureException(MedicalApiErrorCode.GET_TREATMENT_FAILED);
    } else if (response.isError()) {
      logger.error("get basic treatment error name:{}", name);
      throw new FailureException(ErrorCode.SERVER_ERROR);
    }
    TreatmentResponse treatmentResponse = response.getDataAs(TreatmentResponse.class);
    return TreatmentFactory.apisBasicTreatments(treatmentResponse.getTreatments());
  }

  public BasicCoronaryStentApiResponse getBasicStents(String name, List<String> idList,
      Integer pageAt, Integer pageSize) {
    List<Long> ids = null;
    if (Objects.nonNull(idList) && !idList.isEmpty()) {
      ids = idList.stream().map(HashUtils::decode).collect(Collectors.toList());
    }
    Response<CoronaryStentInfoListResponse> response = medicalServiceClient
        .findBasicCoronaryStents(name, ids, pageAt, pageSize);
    if (response.isFailed()) {
      logger.warn("get basic coronary stent failed name:{}", name);
      throw new FailureException(MedicalApiErrorCode.GET_STENT_FAILED);
    } else if (response.isError()) {
      logger.error("get basic coronary stent error name:{}", name);
      throw new FailureException(ErrorCode.SERVER_ERROR);
    }
    CoronaryStentInfoListResponse coronaryStentInfoListResponse = response
        .getDataAs(CoronaryStentInfoListResponse.class);
    return CoronaryStentFactory
        .apiCoronaryStentInfo(coronaryStentInfoListResponse.getCoronaryStentInfos());
  }

  public StatisticsImagesApiResponse getDepartmentImages(String organizationId) {
    Response<StatisticsImagesResponse> response = medicalServiceClient
        .getDepartmentImages(HashUtils.decode(organizationId));
    if (response.isFailed()) {
      logger.warn("get statistics images fail:{}", response);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    } else if (response.isError()) {
      logger.error("get statistics images error:{}", response);
      throw new FailureException(ErrorCode.SERVER_ERROR);
    }
    return ImageFactory.api(response
        .getDataAs(StatisticsImagesResponse.class));
  }

  public StatisticsStandardApiResponse getDepartmentStandardRates(String organizationId) {
    Response<StatisticsStandardResponse> response = medicalServiceClient
        .getDepartmentStandardRate(HashUtils.decode(organizationId));
    if (response.isFailed()) {
      logger.warn("get statistics standard rate fail:{}", response);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    } else if (response.isError()) {
      logger.error("get statistics standard rate error:{}", response);
      throw new FailureException(ErrorCode.SERVER_ERROR);
    }
    return InspectionFactory.api(response
        .getDataAs(StatisticsStandardResponse.class));
  }

  public StandardDataApiResponse getInspectionChart(long patientId, InspectionChartApiType type,
      Long startTime,
      Long endTime) {

    Response<StandardDataResponse> response =
        medicalServiceClient
            .getInspectionChart(patientId, InspectionChartItem.valueOf(type.name()), startTime,
                endTime);

    if (response.isFailed()) {
      logger.warn("get inspection items fail ! response -> {}", response);
      throw new FailureException(ErrorCode.REQUEST_FAILED,
          " get inspection items by image type fail !" + response);
    } else if (response.isError()) {
      logger.error("get statistics standard rate error:{}", response);
      throw new FailureException(ErrorCode.SERVER_ERROR);
    }
    StandardDataResponse standardDataResponse = response.getDataAs(StandardDataResponse.class);
    StandardDataApiResponse apiResponse = new StandardDataApiResponse();
    apiResponse.setType(InspectionChartApiType.valueOf(standardDataResponse.getType().name()));
    apiResponse.setSeries(standardDataResponse.getSeries());
    return apiResponse;
  }


}
