package cn.xinzhili.api.doctor.controller;

import static cn.xinzhili.api.doctor.config.ConfigConsts.prefixWithContext;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cn.xinzhili.api.doctor.bean.AttachedApiInfo;
import cn.xinzhili.api.doctor.bean.BasicCoronaryStentApiInfo;
import cn.xinzhili.api.doctor.bean.BasicHospitalApiInfo;
import cn.xinzhili.api.doctor.bean.BasicHospitalDetailApiResponse;
import cn.xinzhili.api.doctor.bean.BasicTreatmentApiInfo;
import cn.xinzhili.api.doctor.bean.CustomizedInspectionInfo;
import cn.xinzhili.api.doctor.bean.DiagnosisApiInfo;
import cn.xinzhili.api.doctor.bean.DiagnosisStage3;
import cn.xinzhili.api.doctor.bean.DiagnosisStage3Info;
import cn.xinzhili.api.doctor.bean.DiseaseApiInfo;
import cn.xinzhili.api.doctor.bean.ImageApiInfo;
import cn.xinzhili.api.doctor.bean.ImageApiStatus;
import cn.xinzhili.api.doctor.bean.ImageApiType;
import cn.xinzhili.api.doctor.bean.ImageCategory;
import cn.xinzhili.api.doctor.bean.InspectionApiInfo;
import cn.xinzhili.api.doctor.bean.InspectionApiStatus;
import cn.xinzhili.api.doctor.bean.InspectionChartApiType;
import cn.xinzhili.api.doctor.bean.InspectionItem;
import cn.xinzhili.api.doctor.bean.LatestInspectionType;
import cn.xinzhili.api.doctor.bean.ReportApiStatus;
import cn.xinzhili.api.doctor.bean.TreatmentApiInfo;
import cn.xinzhili.api.doctor.bean.medicalrecord.ApiFamilyHistoryDisease;
import cn.xinzhili.api.doctor.bean.medicalrecord.ApiQuitStatus;
import cn.xinzhili.api.doctor.bean.medicalrecord.DiseaseApiCure;
import cn.xinzhili.api.doctor.bean.medicalrecord.DiseaseApiNature;
import cn.xinzhili.api.doctor.bean.medicalrecord.DiseaseApiStatus;
import cn.xinzhili.api.doctor.bean.medicalrecord.DrinkingApiLevel;
import cn.xinzhili.api.doctor.bean.medicalrecord.MedicalRecordInfo;
import cn.xinzhili.api.doctor.bean.medicalrecord.OtherDiagnosisExtraDisease;
import cn.xinzhili.api.doctor.bean.medicalrecord.PharmacyApiStatus;
import cn.xinzhili.api.doctor.bean.medicalrecord.SmokingApiLevel;
import cn.xinzhili.api.doctor.bean.request.AddDiagnosisApiRequest;
import cn.xinzhili.api.doctor.bean.request.DeleteImageTypeApiRequest;
import cn.xinzhili.api.doctor.bean.request.ImageReviewApiRequest;
import cn.xinzhili.api.doctor.bean.request.InspectionApiRequest;
import cn.xinzhili.api.doctor.bean.request.InspectionApiRequest.ApiInspection;
import cn.xinzhili.api.doctor.bean.request.MedicalRecordApiRequest;
import cn.xinzhili.api.doctor.bean.request.SymptomApiRequest;
import cn.xinzhili.api.doctor.bean.request.UpdateDiagnosisApiRequest;
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
import cn.xinzhili.api.doctor.bean.response.InspectionApiResponse.ItemInfo;
import cn.xinzhili.api.doctor.bean.response.InspectionItemResponse;
import cn.xinzhili.api.doctor.bean.response.LatestInspectionApiResponse;
import cn.xinzhili.api.doctor.bean.response.MedicalRecordApiResponse;
import cn.xinzhili.api.doctor.bean.response.StandardDataApiResponse;
import cn.xinzhili.api.doctor.bean.response.SymptomApiResponse;
import cn.xinzhili.api.doctor.common.ContextMock;
import cn.xinzhili.api.doctor.config.ConfigConsts;
import cn.xinzhili.api.doctor.service.DpcService;
import cn.xinzhili.api.doctor.service.MedicalService;
import cn.xinzhili.api.doctor.service.UserService;
import cn.xinzhili.api.doctor.support.MLComponent;
import cn.xinzhili.medical.api.BasicSymptomInfo;
import cn.xinzhili.medical.api.Category;
import cn.xinzhili.medical.api.DiagnosisTreatementSituation;
import cn.xinzhili.medical.api.DiagnosisTreatmentAttachedInfo;
import cn.xinzhili.medical.api.DiagnosisTreatmentAttachedInfoData;
import cn.xinzhili.medical.api.ImageStatus;
import cn.xinzhili.medical.api.ImageType;
import cn.xinzhili.medical.api.InfoType;
import cn.xinzhili.medical.api.InspectionChartReferenceInfo;
import cn.xinzhili.medical.api.InspectionChartStandardInfo;
import cn.xinzhili.medical.api.StandardDataCommon;
import cn.xinzhili.medical.api.StandardDataInfo;
import cn.xinzhili.medical.api.StandardItem;
import cn.xinzhili.medical.api.SymptomContent.PatientSymptom;
import cn.xinzhili.medical.api.SymptomInfo;
import cn.xinzhili.medical.api.TreatmentAttach;
import cn.xinzhili.medical.api.request.AddDiagnosisRequest;
import cn.xinzhili.medical.api.request.ImageReviewInfoRequest;
import cn.xinzhili.medical.api.request.InspectionRequest;
import cn.xinzhili.medical.api.request.MLRequest;
import cn.xinzhili.medical.api.request.MedicalRecordRequest;
import cn.xinzhili.medical.api.request.SymptomRequest;
import cn.xinzhili.medical.api.request.TabooRequest.Type;
import cn.xinzhili.medical.api.request.UpdateDiagnosisRequest;
import cn.xinzhili.medical.api.response.BasicSymptomsResponse;
import cn.xinzhili.medical.api.response.MedicinesTaboosResponse;
import cn.xinzhili.medical.api.response.MedicinesTaboosResponse.MedicineTaboos;
import cn.xinzhili.medical.api.response.SymptomsResponse;
import cn.xinzhili.xutils.core.util.HashUtils;
import cn.xinzhili.xutils.spring.interceptor.ExceptionResolver;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.codehaus.jackson.map.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * @author by Loki on 17/3/13.
 */
@RunWith(MockitoJUnitRunner.class)
public class MedicalControllerTest {

  @Rule
  public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(
      "build/generated-snippets");

  private MockMvc mockMvc;

  @InjectMocks
  private MedicalController medicalController;

  @InjectMocks
  private ExceptionResolver exceptionResolver;

  @Mock
  private MedicalService medicalService;
  @Mock
  private DpcService dpcService;
  @Mock
  private UserService userService;
  @Mock
  private MLComponent medicalLogicComponent;

  private Long mockUserId = 66666L;

  private ObjectMapper mapper;

  private ContextMock contextMock = new ContextMock().withUid(mockUserId);

  private String imageUrl;
  private String imageThumbUrl;

  private MedicalRecordInfo medicalRecordInfo;
  private ImageApiInfo imageInfo;
  private List<ImageApiType> types;

  private List<PatientSymptom> content;
  private SymptomApiResponse symptomApiResponse;
  private SymptomApiRequest symptomApiRequest;
  private SymptomInfo symptomInfo;
  private DiagnosisTreatementSituation situation;

  /**
   * 医院基础数据相关
   */
  private BasicHospitalApiInfo basicHospitalApiInfo;

  @Before
  public void setUp() {

    imageUrl = "https://s3.cn-north-1.amazonaws.com.cn/medical-images/d030a9f5-5303-4551-9248-705c8cadf0b4";
    imageThumbUrl = "https://s3.cn-north-1.amazonaws.com.cn/medical-images/d030a9f5-5303-4551-9248-705c8cadf0b4";

    this.mockMvc = MockMvcBuilders
        .standaloneSetup(this.medicalController)
        .setControllerAdvice(exceptionResolver)
        .setCustomArgumentResolvers(
            new AuthenticationPrincipalArgumentResolver(),
            contextMock.getMockUidResolver())
        .apply(MockMvcRestDocumentation
            .documentationConfiguration(restDocumentation)
            .uris()
            .withHost(ConfigConsts.API_HOST)
            .withPort(ConfigConsts.API_PORT)
        )
        .build();

    mapper = new ObjectMapper();

    situation = new DiagnosisTreatementSituation();
    situation.setName("处理方式");
    TreatmentAttach treatmentAttach = new TreatmentAttach();
    treatmentAttach.setInfoType(InfoType.CORONARY_STENT);
    treatmentAttach.setInfoNames(List.of("支架1", "支架2"));
    situation.setAttach(List.of(treatmentAttach));

    medicalRecordInfo = new MedicalRecordInfo();

    //高血压
    medicalRecordInfo.setHypertension(DiseaseApiStatus.WELL);
    medicalRecordInfo.setHypertensionSince(10);
    medicalRecordInfo.setHypertensionPharmacy(PharmacyApiStatus.IRREGULAR);
    //高血脂
    medicalRecordInfo.setHyperlipemia(DiseaseApiStatus.SICK);
    medicalRecordInfo.setHyperlipemiaSince(10);
    medicalRecordInfo.setHyperlipemiaPharmacy(PharmacyApiStatus.REGULAR);
    //高尿酸
    medicalRecordInfo.setHyperglycemia(DiseaseApiStatus.SICK);
    medicalRecordInfo.setHyperglycemiaSince(12);
    medicalRecordInfo.setHyperglycemiaPharmacy(PharmacyApiStatus.UNKNOWN);
    //高尿酸
    medicalRecordInfo.setHyperuricemia(DiseaseApiStatus.SICK);
    medicalRecordInfo.setHyperuricemiaSince(13);
    medicalRecordInfo.setHyperuricemiaPharmacy(PharmacyApiStatus.IRREGULAR);

    //十二指肠
    medicalRecordInfo.setDuodenalUlcer(DiseaseApiStatus.SICK);
    medicalRecordInfo.setDuodenalUlcerSince(29);
    medicalRecordInfo.setDuodenalUlcerCure(DiseaseApiCure.CURED);

    //呼吸睡眠暂停综合症
    medicalRecordInfo.setSleepApnea(DiseaseApiStatus.SICK);
    medicalRecordInfo.setSleepApneaSince(22);
    medicalRecordInfo.setSleepApneaCure(DiseaseApiCure.CURED);

    //肾病综合征
    medicalRecordInfo.setNephroticSyndrome(DiseaseApiStatus.SICK);
    medicalRecordInfo.setNephroticSyndromeSince(22);
    medicalRecordInfo.setNephroticSyndromeCure(DiseaseApiCure.CURED);

    //幽门杆菌
    medicalRecordInfo.setPylori(DiseaseApiStatus.SICK);
    medicalRecordInfo.setPyloriStatus(DiseaseApiNature.FEMININE);

    //其他疾病
    OtherDiagnosisExtraDisease otherDiagnosisExtraDisease = new OtherDiagnosisExtraDisease();
    otherDiagnosisExtraDisease.setDiseaseId("5Z7g7r");
    otherDiagnosisExtraDisease.setDiseaseName("阑尾炎");
    otherDiagnosisExtraDisease.setSince(100);
    medicalRecordInfo.setExtraDisease(Collections.singletonList(otherDiagnosisExtraDisease));

    //吸烟
    medicalRecordInfo.setSmoking(DiseaseApiStatus.SICK);
    medicalRecordInfo.setSmokingSince(27);
    medicalRecordInfo.setSmokingLevel(SmokingApiLevel.LEVEL_FOUR);
    medicalRecordInfo.setQuitSmoking(ApiQuitStatus.QUITED);
    medicalRecordInfo.setQuitSmokingSince(10);

    //饮酒
    medicalRecordInfo.setDrinking(DiseaseApiStatus.SICK);
    medicalRecordInfo.setDrinkingSince(21);
    medicalRecordInfo.setDrinkingLevel(DrinkingApiLevel.LEVEL_ONE);
    medicalRecordInfo.setQuitDrinking(ApiQuitStatus.REMAIN);
    medicalRecordInfo.setQuitDrinkingSince(22);

    //过敏史
    medicalRecordInfo.setAllergy(DiseaseApiStatus.SICK);
    medicalRecordInfo.setAllergyInfo("酒精过敏");

    //家族史
    medicalRecordInfo.setFamilyHistory(DiseaseApiStatus.SICK);
    List<ApiFamilyHistoryDisease> familyHistoryDiseases = new ArrayList<ApiFamilyHistoryDisease>() {{

      add(ApiFamilyHistoryDisease.HYPERGLYCEMIA);
      add(ApiFamilyHistoryDisease.HYPERLIPEMIA);
      add(ApiFamilyHistoryDisease.HYPERTENSION);
      add(ApiFamilyHistoryDisease.CORONARY);
//      add(ApiFamilyHistoryDisease.APOPLEXY);
      add(ApiFamilyHistoryDisease.PERIPHERALVASCULAR);
      add(ApiFamilyHistoryDisease.GOUT);
      add(ApiFamilyHistoryDisease.TUMOUR);

    }};
    medicalRecordInfo.setFatherFamilyHistory(familyHistoryDiseases);
    medicalRecordInfo.setMotherFamilyHistory(familyHistoryDiseases);
    medicalRecordInfo.setBrotherFamilyHistory(familyHistoryDiseases);

    basicHospitalApiInfo = new BasicHospitalApiInfo();
    basicHospitalApiInfo.setId("5AvRAr");
    basicHospitalApiInfo.setName("北京人民医院");
    basicHospitalApiInfo.setAbbreviation("北京医院");
    basicHospitalApiInfo.setProvince("北京");
    basicHospitalApiInfo.setCity("北京");
    basicHospitalApiInfo.setCounty("北京");
    basicHospitalApiInfo.setTownship("西城");

    imageInfo = new ImageApiInfo();
    imageInfo.setType(ImageApiType.BCG);
    imageInfo.setStatus(ImageApiStatus.NORMAL);
    imageInfo.setReportAt(new Date());
    imageInfo.setHospital(basicHospitalApiInfo);
    imageInfo.setCategory(ImageCategory.ANALYSIS);
    imageInfo.setType(ImageApiType.BCG);
    imageInfo.setTypeName("大病历");
    imageInfo.setCount(100);
    imageInfo.setUrl(imageUrl);
    imageInfo.setThumbUrl(imageThumbUrl);
    imageInfo.setHospital(basicHospitalApiInfo);
    doNothing().when(medicalLogicComponent).add(any(MLRequest.class));

    content = Collections
        .singletonList(new PatientSymptom(1111L, "出血", new Date()));
    symptomInfo = new SymptomInfo();
    symptomInfo.setContent(content);
    symptomInfo.setNote("test");
    symptomInfo.setRecordedDay(Date.from(
        LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
    symptomApiResponse = new SymptomApiResponse();
    symptomApiRequest = new SymptomApiRequest();
    symptomApiResponse.setContent(content);
    symptomApiResponse.setRecordedDay(new Date());
    symptomApiResponse.setNote("test");
    symptomApiRequest.setContent(content);
    symptomApiRequest.setNote("test");
    symptomApiRequest.setPatientId("5AvRAr");

    types = List.of(ImageApiType.BCG, ImageApiType.CT);
  }

  @Test
  public void testGetLatestImage() throws Exception {

    ImageApiResponse response = new ImageApiResponse();
    List<ImageApiInfo> images = new ArrayList<>();
    ImageApiInfo image = new ImageApiInfo();
    image.setType(ImageApiType.BCG);
    image.setTypeName(ImageType.BCG.getDescription());
    image.setUrl(imageUrl);
    image.setThumbUrl(imageThumbUrl);
    image.setCategory(ImageCategory.ANALYSIS);
    image.setReportAt(new Date());
    image.setCount(1000);
    image.setStatus(ImageApiStatus.EXCEPTION);
    images.add(image);
    response.setImages(images);

    given(userService.checkBindRelation(any(Long.class), any(Long.class))).willReturn(true);
    given(medicalService.getPatientLatestImages(any(Long.class))).willReturn(response);
    this.contextMock.withUid(mockUserId);

    this.mockMvc
        .perform(MockMvcRequestBuilders.get("/image/latest")
            .param("patientId", "5Z7g7r")
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("get_latest_image",
            requestParameters(
                parameterWithName("patientId").description("患者id")),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("结果数据"),
                fieldWithPath("data.images[].category").
                    description("图片大类：ANALYSIS/化验单, INSPECTION/检查单,OTHER/其他类型"),
                fieldWithPath("data.images[].type").description("图片类型"),
                fieldWithPath("data.images[].typeName").
                    description("图片类型名称"),
                fieldWithPath("data.images[].status").
                    description(" 图片状态：  NORMAL/正常,  EXCEPTION/异常 ,BLURRING/不清晰"),
                fieldWithPath("data.images[].reportAt").description("图片检查时间"),
                fieldWithPath("data.images[].count").description("改类图片数量"),
                fieldWithPath("data.images[].url").description("原图访问链接"),
                fieldWithPath("data.images[].thumbUrl").description("缩略图访问链接")
            )))
        .andDo(print())
        .andReturn();
  }


  @Test
  public void testGetImageList() throws Exception {

    ImageApiResponse response = new ImageApiResponse();
    List<ImageApiInfo> images = new ArrayList<>();
    ImageApiInfo image = new ImageApiInfo();
    image.setId("5Z7g7r");
    image.setType(ImageApiType.BCG);
    image.setTypeName(ImageType.BCG.getDescription());
    image.setUrl(imageUrl);
    image.setThumbUrl(imageThumbUrl);
    image.setCategory(ImageCategory.ANALYSIS);
    image.setReportAt(new Date());
    image.setCount(1000);
    image.setStatus(ImageApiStatus.EXCEPTION);
    images.add(image);
    response.setImages(images);
    response.setTotal(100);

    given(userService.checkBindRelation(any(Long.class), any(Long.class))).willReturn(true);
    given(medicalService
        .getImageList(any(Long.class), anyListOf(ImageType.class), any(ImageStatus.class)
            , any(Integer.class), any(Integer.class))).willReturn(response);
    this.contextMock.withUid(mockUserId);

    this.mockMvc
        .perform(MockMvcRequestBuilders.get("/image/detail")
            .param("patientId", "5Z7g7r")
            .param("type", ImageApiType.DBL.name())
            .param("scope", "CHECKED")
            .param("pageAt", "1")
            .param("pageSize", "14")
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("get_image_list",
            requestParameters(
                parameterWithName("patientId").description("患者id"),
                parameterWithName("type").description("图片类型"),
                parameterWithName("scope").description("图片状态：CHECKED/已审核,UNCHECKED/未审核"),
                parameterWithName("pageAt").description("页码,不传则返回全部"),
                parameterWithName("pageSize").description("每页数,不传则返回全部")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("结果数据"),
                fieldWithPath("data.images[].id").description("图片id"),
                fieldWithPath("data.images[].category").
                    description("图片大类：ANALYSIS/化验单, INSPECTION/检查单,OTHER/其他类型"),
                fieldWithPath("data.images[].type").description("图片类型"),
                fieldWithPath("data.images[].typeName").
                    description("图片类型名称"),
                fieldWithPath("data.images[].status").
                    description(" 图片状态：  NORMAL/正常,  EXCEPTION/异常 ,BLURRING/不清晰"),
                fieldWithPath("data.images[].reportAt").description("图片检查时间"),
                fieldWithPath("data.images[].count").description("改类图片数量"),
                fieldWithPath("data.images[].url").description("原图访问链接"),
                fieldWithPath("data.images[].thumbUrl").description("缩略图访问链接"),
                fieldWithPath("data.total").description("图片总量")
            )))
        .andDo(print())
        .andReturn();
  }


  @Test
  public void testDeleteImageType() throws Exception {

    DeleteImageTypeApiRequest request = new DeleteImageTypeApiRequest();
    request.setImageId(HashUtils.encode(10000L));
    request.setType(List.of(ImageApiType.BCG));

    doNothing().when(medicalService).deleteImageType(anyListOf(ImageType.class), anyLong());

    this.mockMvc
        .perform(delete(prefixWithContext("/image/type"))
            .contextPath(ConfigConsts.API_CONTEXT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("delete_image_type",
            requestFields(
                fieldWithPath("imageId").description("图片id"),
                fieldWithPath("type").description("待删除图片类型")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("结果数据")
            )))
        .andDo(print())
        .andReturn();
  }

  @Test
  public void testGetMedicalRecord() throws Exception {

    MedicalRecordApiResponse response = new MedicalRecordApiResponse();
    response.setMedicalRecordInfo(medicalRecordInfo);
    response.setImageInfo(imageInfo);
    response.setAllType(types);

    given(medicalService.getMedicalRecord(any(Long.class), any(Long.class))).willReturn(response);

    this.mockMvc
        .perform(get("/medicalrecord")
            .param("imageId", "rY37V8")
            .param("patientId", "5Z7g7r")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("get_medical_record",
            requestParameters(
                parameterWithName("imageId").description("图片id [图片id和患者id不能同时为空] "),
                parameterWithName("patientId").description("图片id")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("结果数据"),
                fieldWithPath("data.imageInfo.type").description("图片类型"),
                fieldWithPath("data.imageInfo.typeName").description("图片类型名称"),
                fieldWithPath("data.imageInfo.reportAt").description("图片检查时间"),
                fieldWithPath("data.imageInfo.hospital.id").description("医院信息.id"),
                fieldWithPath("data.imageInfo.hospital.name").description("医院信息.名称"),
                fieldWithPath("data.imageInfo.hospital.abbreviation").description("医院信息.简称"),
                fieldWithPath("data.imageInfo.hospital.province").description("医院信息.省份"),
                fieldWithPath("data.imageInfo.hospital.city").description("医院信息.市区"),
                fieldWithPath("data.imageInfo.hospital.county").description("医院信息.县区"),
                fieldWithPath("data.imageInfo.hospital.township").description("医院信息.乡镇"),
                fieldWithPath("data.imageInfo.url").description("原图链接"),
                fieldWithPath("data.imageInfo.thumbUrl").description("缩略图链接"),
                fieldWithPath("data.imageInfo.status")
                    .description("被审核图片状态:INVALID/拍照内容不正确,NORMAL/正常,EXCEPTION/异常,BLURRING/不清晰"),

                fieldWithPath("data.medicalRecordInfo.hypertension")
                    .description("是否患有高血压:WELL/未患病,SICK/患病"),
                fieldWithPath("data.medicalRecordInfo.hypertensionSince").description("高血压患病多少年"),
                fieldWithPath("data.medicalRecordInfo.hypertensionPharmacy")
                    .description("高血压是否规律用药:IRREGULAR/不规律,REGULAR/规律,NONE/未用药,UNKNOWN/不详"),
                fieldWithPath("data.medicalRecordInfo.hyperlipemia")
                    .description("是否患有高血脂:WELL/未患病,SICK/患病"),
                fieldWithPath("data.medicalRecordInfo.hyperlipemiaSince").description("高血脂多少年"),
                fieldWithPath("data.medicalRecordInfo.hyperlipemiaPharmacy")
                    .description("高血脂是否规律用药:IRREGULAR/不规律,REGULAR/规律,NONE/未用药,UNKNOWN/不详"),
                fieldWithPath("data.medicalRecordInfo.hyperglycemia")
                    .description("是否患有高血糖:WELL/未患病,SICK/患病"),
                fieldWithPath("data.medicalRecordInfo.hyperglycemiaSince").description("高血糖多少年"),
                fieldWithPath("data.medicalRecordInfo.hyperglycemiaPharmacy")
                    .description("高血糖是否规律用药:IRREGULAR/不规律,REGULAR/规律,NONE/未用药,UNKNOWN/不详"),
                fieldWithPath("data.medicalRecordInfo.hyperuricemia")
                    .description("是否患有高尿酸:WELL/未患病,SICK/患病"),
                fieldWithPath("data.medicalRecordInfo.hyperuricemiaSince").description("高尿酸患病几年"),
                fieldWithPath("data.medicalRecordInfo.hyperuricemiaPharmacy")
                    .description("高尿酸是否规律用药:IRREGULAR/不规律,REGULAR/规律,NONE/未用药,UNKNOWN/不详"),

                fieldWithPath("data.medicalRecordInfo.smoking")
                    .description("是否吸烟:WELL/未患病,SICK/患病"),
                fieldWithPath("data.medicalRecordInfo.smokingSince").description("吸烟几年"),
                fieldWithPath("data.medicalRecordInfo.smokingLevel")
                    .description(
                        "吸烟频率:LEVEL_ZERO/不吸烟,LEVEL_ONE/1-5支每天,LEVEL_TWO/5-10支每天,LEVEL_THREE/10-20支每天,LEVEL_FOUR/20支以上每天"),
                fieldWithPath("data.medicalRecordInfo.quitSmoking")
                    .description("是否戒烟:QUITED/REMAIN"),

                fieldWithPath("data.medicalRecordInfo.drinking")
                    .description("是否喝酒:WELL/未患病,SICK/患病"),
                fieldWithPath("data.medicalRecordInfo.drinkingSince").description("喝酒几年"),
                fieldWithPath("data.medicalRecordInfo.drinkingLevel")
                    .description(
                        "喝酒频率:LEVEL_ZERO/不喝酒,LEVEL_ONE/少量,LEVEL_TWO/多量,LEVEL_THREE/超量"),
                fieldWithPath("data.medicalRecordInfo.quitDrinking")
                    .description("是否戒酒:QUITED/REMAIN"),

                fieldWithPath("data.medicalRecordInfo.allergy")
                    .description("是否有过敏:WELL/未患病,SICK/患病"),
                fieldWithPath("data.medicalRecordInfo.allergyInfo").description("过敏信息"),

                fieldWithPath("data.medicalRecordInfo.familyHistory")
                    .description("是否有家族史:WELL/未患病,SICK/患病"),
                fieldWithPath("data.medicalRecordInfo.fatherFamilyHistory[]")
                    .description("hypertension／高血压  ,hyperlipemia／高血脂 , hyperglycemia／糖尿病 ,"
                        + "coronary／冠心病 , peripheralVascular／外周血管病 ,gout/痛风 ,tumour/肿瘤"),

                fieldWithPath("data.medicalRecordInfo.motherFamilyHistory[]")
                    .description("hypertension／高血压  ,hyperlipemia／高血脂 , hyperglycemia／糖尿病 ,"
                        + "coronary／冠心病 , peripheralVascular／外周血管病 ,gout/痛风 ,tumour/肿瘤"),

                fieldWithPath("data.medicalRecordInfo.brotherFamilyHistory[]")
                    .description("hypertension／高血压  ,hyperlipemia／高血脂 , hyperglycemia／糖尿病 ,"
                        + "coronary／冠心病 , peripheralVascular／外周血管病 ,gout/痛风 ,tumour/肿瘤"),

                fieldWithPath("data.medicalRecordInfo.duodenalUlcer")
                    .description("是否患有胃十二指肠溃疡:WELL/未患病,SICK/患病"),
                fieldWithPath("data.medicalRecordInfo.duodenalUlcerSince").description("胃十二指肠溃疡几年"),
                fieldWithPath("data.medicalRecordInfo.duodenalUlcerCure")
                    .description("十二指肠是否治愈:CURED／治愈,UNCURED/未治愈,UNKNOWN/不详"),

                fieldWithPath("data.medicalRecordInfo.sleepApnea")
                    .description("是否患有呼吸睡眠暂停综合症:WELL/未患病,SICK/患病"),
                fieldWithPath("data.medicalRecordInfo.sleepApneaSince").description("呼吸睡眠暂停综合症几年"),
                fieldWithPath("data.medicalRecordInfo.sleepApneaCure")
                    .description("呼吸睡眠暂停综合症是否治愈:CURED／治愈,UNCURED/未治愈,UNKNOWN/不详"),

                fieldWithPath("data.medicalRecordInfo.nephroticSyndrome")
                    .description("是否患有肾功能不全:WELL/未患病,SICK/患病"),
                fieldWithPath("data.medicalRecordInfo.nephroticSyndromeSince")
                    .description("肾功能不全几年"),
                fieldWithPath("data.medicalRecordInfo.nephroticSyndromeCure")
                    .description("肾功能不全是否治愈:CURED／治愈,UNCURED/未治愈,UNKNOWN/不详"),

                fieldWithPath("data.medicalRecordInfo.renalArteryStenosis")
                    .description("是否患有肾动脉狭窄:WELL/未患病,SICK/患病"),
                fieldWithPath("data.medicalRecordInfo.renalArteryStenosisSince")
                    .description("肾动脉狭窄几年"),
                fieldWithPath("data.medicalRecordInfo.renalArteryStenosisCure")
                    .description("肾动脉狭窄是否治愈:CURED／治愈,UNCURED/未治愈,UNKNOWN/不详"),

                fieldWithPath("data.medicalRecordInfo.pylori")
                    .description("是否具有幽门螺杆菌:WELL/未患病,SICK/患病"),
                fieldWithPath("data.medicalRecordInfo.pyloriStatus")
                    .description("幽门螺杆菌性质:FEMININE/阴性,MASCULINE/阳性"),
                fieldWithPath("data.medicalRecordInfo.extraDisease[].diseaseId")
                    .description("疾病id"),
                fieldWithPath("data.medicalRecordInfo.extraDisease[].diseaseName")
                    .description("疾病名"),
                fieldWithPath("data.medicalRecordInfo.extraDisease[].since").description("患病几年"),
                fieldWithPath("data.medicalRecordInfo.extraDisease[].diseaseCure")
                    .description("是否治愈:CURED／治愈,UNCURED/未治愈,UNKNOWN/不详"),
                fieldWithPath("data.allType[]").description("图片所有类型")
            )))
        .andDo(print())
        .andReturn();
  }


  @Test
  public void testHandleMedicalRecord() throws Exception {

    MedicalRecordApiRequest request = new MedicalRecordApiRequest();
    request.setMedicalRecordInfo(medicalRecordInfo);
    request.setReportAt(new Date());
    request.setImageId("rY37V8");
    request.setStatus(ReportApiStatus.INVALID);
    request.setPatientId("5Z7g7r");
    request.setHospital(basicHospitalApiInfo);
    MedicalRecordApiResponse response = new MedicalRecordApiResponse();

    response.setMedicalRecordInfo(medicalRecordInfo);
    response.setImageInfo(imageInfo);
    response.setAllType(types);
    given(medicalService.handleMedicalRecord(any(MedicalRecordRequest.class))).willReturn(response);
    doNothing().when(dpcService).sendImageReviewSystemMessage(anyLong(), anyLong());
    this.contextMock.withUid(mockUserId);

    this.mockMvc
        .perform(post(prefixWithContext("/medicalrecord"))
            .contextPath(ConfigConsts.API_CONTEXT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("handle_medical_record",
            requestFields(
                fieldWithPath("imageId").description("图片id"),
                fieldWithPath("patientId").description("患者id"),
                fieldWithPath("status").
                    description(
                        "图片审核状态(如果图片id存在，则状态必须存在):INVALID/拍照内容不正确,NORMAL/正常,EXCEPTION/异常,BLURRING/不清晰"),
                fieldWithPath("reportAt").description("图片检查时间(大病历可为空)"),
                fieldWithPath("hospital.id").description("医院信息.id"),
                fieldWithPath("hospital.name").description("医院信息.名称"),
                fieldWithPath("hospital.abbreviation").description("医院信息.简称"),
                fieldWithPath("hospital.province").description("医院信息.省份"),
                fieldWithPath("hospital.city").description("医院信息.城市"),
                fieldWithPath("hospital.county").description("医院信息.县区"),
                fieldWithPath("hospital.township").description("医院信息.乡镇"),
                fieldWithPath("medicalRecordInfo.hypertension")
                    .description("是否患有高血压:WELL/未患病,SICK/患病"),
                fieldWithPath("medicalRecordInfo.hypertensionSince").description("高血压患病多少年"),
                fieldWithPath("medicalRecordInfo.hypertensionPharmacy")
                    .description("高血压是否规律用药:IRREGULAR/不规律,REGULAR/规律,NONE/未用药,UNKNOWN/不详"),
                fieldWithPath("medicalRecordInfo.hyperlipemia")
                    .description("是否患有高血脂:WELL/未患病,SICK/患病"),
                fieldWithPath("medicalRecordInfo.hyperlipemiaSince").description("高血脂多少年"),
                fieldWithPath("medicalRecordInfo.hyperlipemiaPharmacy")
                    .description("高血脂是否规律用药:IRREGULAR/不规律,REGULAR/规律,NONE/未用药,UNKNOWN/不详"),
                fieldWithPath("medicalRecordInfo.hyperglycemia")
                    .description("是否患有高血糖:WELL/未患病,SICK/患病"),
                fieldWithPath("medicalRecordInfo.hyperglycemiaSince").description("高血糖多少年"),
                fieldWithPath("medicalRecordInfo.hyperglycemiaPharmacy")
                    .description("高血糖是否规律用药:IRREGULAR/不规律,REGULAR/规律,NONE/未用药,UNKNOWN/不详"),
                fieldWithPath("medicalRecordInfo.hyperuricemia")
                    .description("是否患有高尿酸:WELL/未患病,SICK/患病"),
                fieldWithPath("medicalRecordInfo.hyperuricemiaSince").description("高尿酸患病几年"),
                fieldWithPath("medicalRecordInfo.hyperuricemiaPharmacy")
                    .description("高尿酸是否规律用药:IRREGULAR/不规律,REGULAR/规律,NONE/未用药,UNKNOWN/不详"),

                fieldWithPath("medicalRecordInfo.duodenalUlcer")
                    .description("是否患有胃十二指肠溃疡:WELL/未患病,SICK/患病"),
                fieldWithPath("medicalRecordInfo.duodenalUlcerSince").description("胃十二指肠溃疡几年"),
                fieldWithPath("medicalRecordInfo.duodenalUlcerCure")
                    .description("十二指肠是否治愈:CURED／治愈,UNCURED/未治愈,UNKNOWN/不详"),

                fieldWithPath("medicalRecordInfo.sleepApnea")
                    .description("是否患有呼吸睡眠暂停综合症:WELL/未患病,SICK/患病"),
                fieldWithPath("medicalRecordInfo.sleepApneaSince").description("呼吸睡眠暂停综合症几年"),
                fieldWithPath("medicalRecordInfo.sleepApneaCure")
                    .description("呼吸睡眠暂停综合症是否治愈:CURED／治愈,UNCURED/未治愈,UNKNOWN/不详"),

                fieldWithPath("medicalRecordInfo.nephroticSyndrome")
                    .description("是否患有肾功能不全:WELL/未患病,SICK/患病"),
                fieldWithPath("medicalRecordInfo.nephroticSyndromeSince").description("肾功能不全几年"),
                fieldWithPath("medicalRecordInfo.nephroticSyndromeCure")
                    .description("肾功能不全是否治愈:CURED／治愈,UNCURED/未治愈,UNKNOWN/不详"),

                fieldWithPath("medicalRecordInfo.renalArteryStenosis")
                    .description("是否患有肾动脉狭窄:WELL/未患病,SICK/患病"),
                fieldWithPath("medicalRecordInfo.renalArteryStenosisSince").description("肾动脉狭窄几年"),
                fieldWithPath("medicalRecordInfo.renalArteryStenosisCure")
                    .description("肾动脉狭窄是否治愈:CURED／治愈,UNCURED/未治愈,UNKNOWN/不详"),

                fieldWithPath("medicalRecordInfo.pylori").description("是否具有幽门螺杆菌:WELL/未患病,SICK/患病"),
                fieldWithPath("medicalRecordInfo.pyloriStatus")
                    .description("幽门螺杆菌性质:FEMININE/阴性,MASCULINE/阳性"),
                fieldWithPath("medicalRecordInfo.extraDisease[].diseaseId").description("疾病id"),
                fieldWithPath("medicalRecordInfo.extraDisease[].diseaseName").description("疾病名"),
                fieldWithPath("medicalRecordInfo.extraDisease[].since").description("患病几年"),
                fieldWithPath("medicalRecordInfo.extraDisease[].diseaseCure")
                    .description("是否治愈:CURED／治愈,UNCURED/未治愈,UNKNOWN/不详"),

                fieldWithPath("medicalRecordInfo.smoking").description("是否吸烟:WELL/未患病,SICK/患病"),
                fieldWithPath("medicalRecordInfo.smokingSince").description("吸烟几年"),
                fieldWithPath("medicalRecordInfo.smokingLevel")
                    .description(
                        "吸烟频率:LEVEL_ZERO/不吸烟,LEVEL_ONE/1-5支每天,LEVEL_TWO/5-10支每天,LEVEL_THREE/10-20支每天,LEVEL_FOUR/20支以上每天"),
                fieldWithPath("medicalRecordInfo.quitSmoking").description("是否戒烟:QUITED/REMAIN"),
                fieldWithPath("medicalRecordInfo.quitSmokingSince").description("戒烟几年"),

                fieldWithPath("medicalRecordInfo.drinking").description("是否喝酒:WELL/未患病,SICK/患病"),
                fieldWithPath("medicalRecordInfo.drinkingSince").description("喝酒几年"),
                fieldWithPath("medicalRecordInfo.drinkingLevel")
                    .description(
                        "喝酒频率:LEVEL_ZERO/不喝酒,LEVEL_ONE/少量,LEVEL_TWO/多量,LEVEL_THREE/超量"),
                fieldWithPath("medicalRecordInfo.quitDrinking").description("是否戒酒:QUITED/REMAIN"),
                fieldWithPath("medicalRecordInfo.quitDrinkingSince").description("戒酒几年"),

                fieldWithPath("medicalRecordInfo.allergy").description("是否有过敏:WELL/未患病,SICK/患病"),
                fieldWithPath("medicalRecordInfo.allergyInfo").description("过敏信息"),

                fieldWithPath("medicalRecordInfo.familyHistory")
                    .description("是否有家族史:WELL/未患病,SICK/患病"),

                fieldWithPath("medicalRecordInfo.fatherFamilyHistory[]")
                    .description("hypertension／高血压  ,hyperlipemia／高血脂 , hyperglycemia／糖尿病 ,"
                        + "coronary／冠心病 , peripheralVascular／外周血管病 ,gout/痛风 ,tumour/肿瘤"),

                fieldWithPath("medicalRecordInfo.motherFamilyHistory[]")
                    .description("hypertension／高血压  ,hyperlipemia／高血脂 , hyperglycemia／糖尿病 ,"
                        + "coronary／冠心病 ,   peripheralVascular／外周血管病 ,gout/痛风 ,tumour/肿瘤"),

                fieldWithPath("medicalRecordInfo.brotherFamilyHistory[]")
                    .description("hypertension／高血压  ,hyperlipemia／高血脂 , hyperglycemia／糖尿病 ,"
                        + "coronary／冠心病 ,  peripheralVascular／外周血管病 ,gout/痛风 ,tumour/肿瘤")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("成功数据,字段参照get方法")
            )
        ))
        .andDo(print())
        .andReturn();
  }


  @Test
  public void testAddDiagnosis() throws Exception {

    AddDiagnosisApiRequest request = new AddDiagnosisApiRequest();
    request.setReportAt(new Date());
    request.setImageId("rY37V8");
    request.setStatus(ReportApiStatus.INVALID);
    request.setPatientId("5Z7g7r");

    request.setStage1("5RWMvr");
    request.setStage2(Arrays.asList("reL745", "8N4Ekr"));

    DiagnosisStage3 stage3 = new DiagnosisStage3();
    stage3.setDiseaseId("8Vbqm8");
    stage3.setFatherId("8Xb7Br");
    request.setStage3(Collections.singletonList(stage3));

    TreatmentApiInfo apiInfo1 = new TreatmentApiInfo();
    apiInfo1.setId("reL745");
    apiInfo1.setSituation("situation");
    DiagnosisTreatmentAttachedInfo attachedInfo = new DiagnosisTreatmentAttachedInfo();
    attachedInfo.setHospitalId("hospitalId");
    attachedInfo.setHospitalName("hospitalName");
    attachedInfo.setInfoType(InfoType.CORONARY_STENT);
    attachedInfo.setTreatedAt(1233333L);
    DiagnosisTreatmentAttachedInfoData data = new DiagnosisTreatmentAttachedInfoData();
    data.setId(1);
    data.setHospitalId("id");
    data.setHospitalName("name");
    data.setLocation("location");
    data.setName("name");
    data.setSize("size");
    data.setTreatedAt(1222222L);
    attachedInfo.setInfoDatas(List.of(data));
    apiInfo1.setAttachedInfos(List.of(attachedInfo));

    request.setTreatments(Arrays.asList(apiInfo1, apiInfo1));
    given(medicalService.addDiagnosis(any(AddDiagnosisRequest.class))).willReturn(true);
    doNothing().when(dpcService).sendImageReviewSystemMessage(anyLong(), anyLong());
    this.contextMock.withUid(mockUserId);

    this.mockMvc
        .perform(post(prefixWithContext("/diagnosis"))
            .contextPath(ConfigConsts.API_CONTEXT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("add_diagnosis",
            requestFields(
                fieldWithPath("imageId").description("图片ID(可选)"),
                fieldWithPath("patientId").description("患者id"),
                fieldWithPath("status").
                    description(
                        "图片审核状态(如果图片id存在，则状态必须存在):INVALID/拍照内容不正确,NORMAL/正常,EXCEPTION/异常,BLURRING/不清晰"),
                fieldWithPath("reportAt").description("图片检查时间(大病历可为空)"),
                fieldWithPath("hospital").description("医院信息"),
                fieldWithPath("stage1").description("一级诊断疾病id"),
                fieldWithPath("stage2[]").description("二级诊断疾病id"),
                fieldWithPath("stage3[].diseaseId").description("三级诊断疾病id"),
                fieldWithPath("stage3[].fatherId").description("三级诊断关联的二级诊断id"),
                fieldWithPath("treatments[].id").description("治疗方式id(添加时不需要，更新时需要)"),
                fieldWithPath("treatments[].situation").description("治疗方式名称"),
                fieldWithPath("treatments[].attachedInfos[].infoType").description(
                    "处理方式.附加信息类型 NO_TREATMENT_ATTACH ---> 无额外附加信息，直接取hospitalid,treatedAt;"
                        + " CORONARY_STENT ---> 支架附加信息 从infoDatas中取值"),
                fieldWithPath("treatments[].attachedInfos[].hospitalId").description("处理方式.医院id"),
                fieldWithPath("treatments[].attachedInfos[].hospitalName").description("处理方式.医院名称"),
                fieldWithPath("treatments[].attachedInfos[].treatedAt").description("处理方式.处理时间"),
                fieldWithPath("treatments[].attachedInfos[].infoDatas[].id")
                    .description("处理方式.附加信息id"),
                fieldWithPath("treatments[].attachedInfos[].infoDatas[].name")
                    .description("处理方式.附加信息名称"),
                fieldWithPath("treatments[].attachedInfos[].infoDatas[].size")
                    .description("处理方式.支架尺寸"),
                fieldWithPath("treatments[].attachedInfos[].infoDatas[].location")
                    .description("处理方式.支架位置"),
                fieldWithPath("treatments[].attachedInfos[].infoDatas[].hospitalId")
                    .description("处理方式.支架处理医院"),
                fieldWithPath("treatments[].attachedInfos[].infoDatas[].hospitalName")
                    .description("处理方式.支架处理医院名称"),
                fieldWithPath("treatments[].attachedInfos[].infoDatas[].treatedAt")
                    .description("处理方式.支架处理时间")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("")
            )
        ))
        .andDo(print())
        .andReturn();
  }

  @Test
  public void testUpdateDiagnosis() throws Exception {

    UpdateDiagnosisApiRequest request = new UpdateDiagnosisApiRequest();
    request.setReportAt(new Date());
    request.setImageId("rY37V8");
    request.setStatus(ReportApiStatus.INVALID);
    request.setPatientId("5Z7g7r");
    request.setId("5RWMvr");
    request.setStage1("5RWMvr");
    request.setStage2(Arrays.asList("reL745", "8N4Ekr"));

    DiagnosisStage3 stage3 = new DiagnosisStage3();
    stage3.setDiseaseId("8Vbqm8");
    stage3.setFatherId("8Xb7Br");
    request.setStage3(Collections.singletonList(stage3));

    TreatmentApiInfo apiInfo1 = new TreatmentApiInfo();
    apiInfo1.setId("reL745");
    apiInfo1.setSituation("situation");
    DiagnosisTreatmentAttachedInfo attachedInfo = new DiagnosisTreatmentAttachedInfo();
    attachedInfo.setHospitalId("hospitalId");
    attachedInfo.setHospitalName("hospitalName");
    attachedInfo.setInfoType(InfoType.CORONARY_STENT);
    attachedInfo.setTreatedAt(1233333L);
    DiagnosisTreatmentAttachedInfoData data = new DiagnosisTreatmentAttachedInfoData();
    data.setId(1);
    data.setHospitalId("id");
    data.setHospitalName("name");
    data.setLocation("location");
    data.setName("name");
    data.setSize("size");
    data.setTreatedAt(1222222L);
    attachedInfo.setInfoDatas(List.of(data));
    apiInfo1.setAttachedInfos(List.of(attachedInfo));

    request.setTreatments(Arrays.asList(apiInfo1, apiInfo1));
    given(medicalService.updateDiagnosis(any(UpdateDiagnosisRequest.class))).willReturn(true);
    doNothing().when(dpcService).sendImageReviewSystemMessage(anyLong(), anyLong());
    this.contextMock.withUid(mockUserId);

    this.mockMvc
        .perform(patch(prefixWithContext("/diagnosis"))
            .contextPath(ConfigConsts.API_CONTEXT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("update_diagnosis",
            requestFields(
                fieldWithPath("imageId").description("图片ID(可选)"),
                fieldWithPath("patientId").description("患者id"),
                fieldWithPath("status").
                    description(
                        "图片审核状态(如果图片id存在，则状态必须存在):INVALID/拍照内容不正确,NORMAL/正常,EXCEPTION/异常,BLURRING/不清晰"),
                fieldWithPath("reportAt").description("图片检查时间(大病历可为空)"),
                fieldWithPath("hospital").description("医院信息"),
                fieldWithPath("id").description("待更新数据id"),
                fieldWithPath("stage1").description("一级诊断疾病id"),
                fieldWithPath("stage2[]").description("二级诊断疾病id"),
                fieldWithPath("stage3[].diseaseId").description("三级诊断疾病id"),
                fieldWithPath("stage3[].fatherId").description("三级诊断疾病所关联二级诊断id"),
                fieldWithPath("treatments[].id").description("治疗方式id(添加时不需要，更新时需要)"),
                fieldWithPath("treatments[].situation").description("治疗方式名称"),
                fieldWithPath("treatments[].attachedInfos[].infoType").description(
                    "处理方式.附加信息类型 NO_TREATMENT_ATTACH ---> 无额外附加信息，直接取hospitalid,treatedAt;"
                        + " CORONARY_STENT ---> 支架附加信息 从infoDatas中取值"),
                fieldWithPath("treatments[].attachedInfos[].hospitalId").description("处理方式.医院id"),
                fieldWithPath("treatments[].attachedInfos[].hospitalName").description("处理方式.医院名称"),
                fieldWithPath("treatments[].attachedInfos[].treatedAt").description("处理方式.处理时间"),
                fieldWithPath("treatments[].attachedInfos[].infoDatas[].id")
                    .description("处理方式.附加信息id"),
                fieldWithPath("treatments[].attachedInfos[].infoDatas[].name")
                    .description("处理方式.附加信息名称"),
                fieldWithPath("treatments[].attachedInfos[].infoDatas[].size")
                    .description("处理方式.支架尺寸"),
                fieldWithPath("treatments[].attachedInfos[].infoDatas[].location")
                    .description("处理方式.支架位置"),
                fieldWithPath("treatments[].attachedInfos[].infoDatas[].hospitalId")
                    .description("处理方式.支架处理医院"),
                fieldWithPath("treatments[].attachedInfos[].infoDatas[].hospitalName")
                    .description("处理方式.支架处理医院名称"),
                fieldWithPath("treatments[].attachedInfos[].infoDatas[].treatedAt")
                    .description("处理方式.支架处理时间")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("")
            )
        ))
        .andDo(print())
        .andReturn();
  }

  @Test
  public void testGetDiagnosis() throws Exception {

    DiagnosisApiResponse response = new DiagnosisApiResponse();
    DiseaseApiInfo diseaseInfo1 = new DiseaseApiInfo();
    diseaseInfo1.setId("8N4Ekr");
    diseaseInfo1.setName("周围神经炎");

    DiseaseApiInfo diseaseInfo2 = new DiseaseApiInfo();
    diseaseInfo2.setId("ldVj25");
    diseaseInfo2.setName("重症肌无力");

    TreatmentApiInfo treatmentApiInfo1 = new TreatmentApiInfo();
    treatmentApiInfo1.setId("reL745");
    treatmentApiInfo1.setSituation("situation");
    DiagnosisTreatmentAttachedInfo attachedInfo = new DiagnosisTreatmentAttachedInfo();
    attachedInfo.setHospitalId("hospitalId");
    attachedInfo.setHospitalName("hospitalName");
    attachedInfo.setInfoType(InfoType.CORONARY_STENT);
    attachedInfo.setTreatedAt(1233333L);
    DiagnosisTreatmentAttachedInfoData data = new DiagnosisTreatmentAttachedInfoData();
    data.setId(1);
    data.setHospitalId("id");
    data.setHospitalName("name");
    data.setLocation("location");
    data.setName("name");
    data.setSize("size");
    data.setTreatedAt(1222222L);
    attachedInfo.setInfoDatas(List.of(data));
    treatmentApiInfo1.setAttachedInfos(List.of(attachedInfo));

    DiagnosisApiInfo apiInfo1 = new DiagnosisApiInfo();
    apiInfo1.setId("rzz4xr");
    apiInfo1.setStage1(diseaseInfo1);
    apiInfo1.setStage2(Arrays.asList(diseaseInfo1, diseaseInfo2));

    DiagnosisStage3Info stage3Info = new DiagnosisStage3Info();
    stage3Info.setDiseaseId("rY36M8");
    stage3Info.setFatherId("8n17xl");
    stage3Info.setName("间歇性休眠");

    apiInfo1.setStage3(Collections.singletonList(stage3Info));
    apiInfo1.setTreatments(Arrays.asList(treatmentApiInfo1, treatmentApiInfo1));

    DiagnosisApiInfo apiInfo2 = new DiagnosisApiInfo();
    apiInfo2.setId("8n17xl");
    apiInfo2.setStage1(diseaseInfo1);
    apiInfo2.setStage2(Arrays.asList(diseaseInfo1, diseaseInfo2));

    DiagnosisStage3Info stage3Info2 = new DiagnosisStage3Info();
    stage3Info2.setDiseaseId("8mZ7y5");
    stage3Info2.setFatherId("l3RjXr");
    stage3Info2.setName("脑溢血");
    apiInfo2.setStage3(Collections.singletonList(stage3Info2));

    apiInfo2.setTreatments(Arrays.asList(treatmentApiInfo1, treatmentApiInfo1));

    response.setDiagnosisInfo(Arrays.asList(apiInfo1, apiInfo2));
    response.setImageInfo(imageInfo);
    response.setAllType(types);
    given(medicalService.getDiagnosis(any(Long.class), any(Long.class))).willReturn(response);

    this.mockMvc
        .perform(get("/diagnosis")
            .param("imageId", "rY37V8")
            .param("patientId", "8Vbqm8")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("get_diagnosis",
            requestParameters(
                parameterWithName("imageId").description("图片id,［图片id和患者id不能都为空］"),
                parameterWithName("patientId").description("患者id,［图片id和患者id不能都为空］")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("结果数据"),
                fieldWithPath("data.imageInfo.type").description("图片类型"),
                fieldWithPath("data.imageInfo.typeName").description("图片类型名称"),
                fieldWithPath("data.imageInfo.reportAt").description("图片检查时间"),
                fieldWithPath("data.imageInfo.hospital.id").description("被审核图片的医院id"),
                fieldWithPath("data.imageInfo.hospital.name").description("被审核图片的医院全称"),
                fieldWithPath("data.imageInfo.hospital.abbreviation").description("被审核图片的医院简称"),
                fieldWithPath("data.imageInfo.hospital.province").description("被审核图片的医院省份"),
                fieldWithPath("data.imageInfo.hospital.city").description("被审核图片的医院城市"),
                fieldWithPath("data.imageInfo.hospital.county").description("被审核图片的医院县区"),
                fieldWithPath("data.imageInfo.hospital.township").description("被审核图片的医院乡镇"),
                fieldWithPath("data.imageInfo.url").description("原图链接"),
                fieldWithPath("data.imageInfo.thumbUrl").description("缩略图链接"),
                fieldWithPath("data.imageInfo.status")
                    .description("被审核图片状态:INVALID/拍照内容不正确,NORMAL/正常,EXCEPTION/异常,BLURRING/不清晰"),
                fieldWithPath("data.diagnosisInfo[].id").description("诊断id"),
                fieldWithPath("data.diagnosisInfo[].stage1.id").description("一级诊断疾病id"),
                fieldWithPath("data.diagnosisInfo[].stage1.name").description("一级诊断疾病名"),
                fieldWithPath("data.diagnosisInfo[].stage2[].id").description("二级诊断疾病id"),
                fieldWithPath("data.diagnosisInfo[].stage2[].name").description("二级诊断疾病名"),
                fieldWithPath("data.diagnosisInfo[].stage3[].diseaseId").description("三级诊断疾病id"),
                fieldWithPath("data.diagnosisInfo[].stage3[].fatherId").description("三级诊断关联二级诊断id"),
                fieldWithPath("data.diagnosisInfo[].stage3[].name").description("三级诊断疾病名"),
                fieldWithPath("data.diagnosisInfo[].treatments[].id").description("治疗方式id"),
                fieldWithPath("data.diagnosisInfo[].treatments[].situation").description("治疗方式描述"),
                fieldWithPath("data.diagnosisInfo[].treatments[].attachedInfos[].infoType")
                    .description(
                        "处理方式.附加信息类型 NO_TREATMENT_ATTACH ---> 无额外附加信息，直接取hospitalid,treatedAt;"
                            + " CORONARY_STENT ---> 支架附加信息 从infoDatas中取值"),
                fieldWithPath("data.diagnosisInfo[].treatments[].attachedInfos[].hospitalId")
                    .description("处理方式.医院id"),
                fieldWithPath("data.diagnosisInfo[].treatments[].attachedInfos[].hospitalName")
                    .description("处理方式.医院名称"),
                fieldWithPath("data.diagnosisInfo[].treatments[].attachedInfos[].treatedAt")
                    .description("处理方式.处理时间"),
                fieldWithPath("data.diagnosisInfo[].treatments[].attachedInfos[].infoDatas[].id")
                    .description("处理方式.附加信息id"),
                fieldWithPath("data.diagnosisInfo[].treatments[].attachedInfos[].infoDatas[].name")
                    .description("处理方式.附加信息名称"),
                fieldWithPath("data.diagnosisInfo[].treatments[].attachedInfos[].infoDatas[].size")
                    .description("处理方式.支架尺寸"),
                fieldWithPath(
                    "data.diagnosisInfo[].treatments[].attachedInfos[].infoDatas[].location")
                    .description("处理方式.支架位置"),
                fieldWithPath(
                    "data.diagnosisInfo[].treatments[].attachedInfos[].infoDatas[].hospitalId")
                    .description("处理方式.支架处理医院"),
                fieldWithPath(
                    "data.diagnosisInfo[].treatments[].attachedInfos[].infoDatas[].hospitalName")
                    .description("处理方式.支架处理医院名称"),
                fieldWithPath(
                    "data.diagnosisInfo[].treatments[].attachedInfos[].infoDatas[].treatedAt")
                    .description("处理方式.支架处理时间"),
                fieldWithPath("data.allType[]").description("图片所有类型"))))
        .andDo(print())
        .andReturn();
  }

  @Test
  public void testDeleteDiagnosis() throws Exception {

    doNothing().when(medicalService).deleteDiagnosis(any(Long.class));

    mockMvc
        .perform(RestDocumentationRequestBuilders.delete("/diagnosis/{id}", "rY37V8"))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("delete_diagnosis",
            pathParameters(
                parameterWithName("id").description("诊断id")),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("结果数据")
            )))
        .andDo(print())
        .andReturn();
  }

  @Test
  public void testGetDisease() throws Exception {

    DiseaseApiResponse response = new DiseaseApiResponse();
    DiseaseApiInfo apiInfo1 = new DiseaseApiInfo();
    apiInfo1.setId("8N4Ekr");
    apiInfo1.setName("周围神经炎");

    DiseaseApiInfo apiInfo2 = new DiseaseApiInfo();
    apiInfo2.setId("ldVnX5");
    apiInfo2.setName("周围神经病");
    response.setDisease(Arrays.asList(apiInfo1, apiInfo2));
    given(medicalService.getDisease(any(String.class))).willReturn(response);

    this.mockMvc
        .perform(get("/disease")
            .param("name", "周围")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("get_disease",
            requestParameters(
                parameterWithName("name").description("疾病名")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("结果数据"),
                fieldWithPath("data.disease[].id").description("疾病id"),
                fieldWithPath("data.disease[].name").description("疾病名称")
            )))
        .andDo(print())
        .andReturn();
  }


  @Test
  public void testImageReview() throws Exception {

    ImageReviewApiRequest request = new ImageReviewApiRequest();
    request.setStatus(ReportApiStatus.EXCEPTION);
    request.setImageId("5awyGl");
    request.setReportAt(new Date());
    request.setType(ImageApiType.BCG);
    request.setPatientId("lOxgq8");
    request.setHospital(basicHospitalApiInfo);
    given(medicalService.imageReview(any(ImageReviewInfoRequest.class))).willReturn(true);
    this.contextMock.withUid(mockUserId);

    this.mockMvc
        .perform(post(prefixWithContext("/image/review"))
            .contextPath(ConfigConsts.API_CONTEXT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("image_review",
            requestFields(
                fieldWithPath("imageId").description("图片id"),
                fieldWithPath("patientId").description("患者id(可选)"),
                fieldWithPath("status").
                    description("图片审核状态:INVALID/拍照内容不正确,NORMAL/正常,EXCEPTION/异常,BLURRING/不清晰"),
                fieldWithPath("reportAt").description("图片检查时间"),
                fieldWithPath("hospital.id").description("医院信息.id"),
                fieldWithPath("hospital.name").description("医院信息.名称"),
                fieldWithPath("hospital.abbreviation").description("医院信息.简称"),
                fieldWithPath("hospital.province").description("医院信息.省份"),
                fieldWithPath("hospital.city").description("医院信息.市区"),
                fieldWithPath("hospital.county").description("医院信息.县区"),
                fieldWithPath("hospital.township").description("医院信息.乡镇"),

                fieldWithPath("type").description("图片类型:(大病历和诊断除外)")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("")
            )
        ))
        .andDo(print())
        .andReturn();
  }

  @Test
  public void testGetAllImageType() throws Exception {

    ImageTypeResponse response = new ImageTypeResponse();
    List<ImageTypeResponse.TypeInfo> types =

        Stream.of(ImageApiType.values()).filter(i -> i != ImageApiType.CTGZ).map(t -> {
          TypeInfo typeInfo = new ImageTypeResponse().new TypeInfo();
          typeInfo.setKey(t.name());
          typeInfo.setName(ImageType.valueOf(t.name()).getDescription());
          return typeInfo;
        }).collect(Collectors.toList());
    response.setTypes(types);
    given(medicalService.getAllImageType()).willReturn(response);

    this.mockMvc
        .perform(get("/image/type")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("get_image_type",
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("结果数据"),
                fieldWithPath("data.types[].key")
                    .description("图片类型key值,进行图片相关请求时传参使用"),
                fieldWithPath("data.types[].name").description("图片类型名称")
            )))
        .andDo(print())
        .andReturn();
  }

  @Test
  public void testGetInspection() throws Exception {

    LatestInspectionApiResponse response = new LatestInspectionApiResponse();

    List<CustomizedInspectionInfo> inspections = new ArrayList<>();

    CustomizedInspectionInfo apiInfo = new CustomizedInspectionInfo();
    apiInfo.setName(LatestInspectionType.BLOOD_PRESSURE.getDescription());
    apiInfo.setUnit("mmHg");
    apiInfo.setValue("120/80(80)");
    apiInfo.setMeasuredAt(new Date().getTime());
    apiInfo.setStatus(InspectionApiStatus.HIGH);
    apiInfo.setCustomizedReferenceMax(1.2D);
    apiInfo.setCustomizedReferenceMin(22D);
    apiInfo.setUnifiedReference("22/22222");
    apiInfo.setSpliceSymbol("/");
    apiInfo.setAbbreviation("UA");
    inspections.add(apiInfo);
    response.setInspections(inspections);

    given(userService.checkBindRelation(anyLong(), anyLong())).willReturn(true);
    given(medicalService.getLatestInspection(any(Long.class))).willReturn(response);

    this.mockMvc
        .perform(get("/inspection")
            .param("patientId", "5Z7g7r")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("get_inspection",
            requestParameters(
                parameterWithName("patientId").description("患者id")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("结果数据"),
                fieldWithPath("data.inspections[].name")
                    .description("项目名"),
                fieldWithPath("data.inspections[].measuredAt").description("测量时间"),
                fieldWithPath("data.inspections[].value").description("实际值"),
                fieldWithPath("data.inspections[].name").description("项目名"),
                fieldWithPath("data.inspections[].abbreviation").description("缩略名"),
                fieldWithPath("data.inspections[].status").description("状态:HIGH,LOW,NORMAL"),
                fieldWithPath("data.inspections[].unifiedReference").description("建议值"),
                fieldWithPath("data.inspections[].customizedReferenceMax").description("达标最大值"),
                fieldWithPath("data.inspections[].customizedReferenceMin").description("达标最小值"),
                fieldWithPath("data.inspections[].spliceSymbol").description("拼接符号"),
                fieldWithPath("data.inspections[].unit").description("单位")
            )))
        .andDo(print())
        .andReturn();
  }

  @Test
  public void testGetInspectionItem() throws Exception {

    InspectionItemResponse response =
        new InspectionItemResponse();

    InspectionItem item = new InspectionItem();
    item.setId("ldVj25");
    item.setName("尿潜血");
    item.setAbbreviation("BLD");
    item.setUnit("n/m");
    response.setItems(Collections.singletonList(item));
    given(medicalService.getInspectionItemsByImageType(any(ImageApiType.class)))
        .willReturn(response);

    this.mockMvc
        .perform(get("/inspection/item")
            .param("imageType", "NCG")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("get_inspection_item",
            requestParameters(
                parameterWithName("imageType").description("图片类型")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("结果数据"),
                fieldWithPath("data.items[].id").description("项目id"),
                fieldWithPath("data.items[].name")
                    .description("项目名"),
                fieldWithPath("data.items[].abbreviation").description("缩写"),
                fieldWithPath("data.items[].unit").description("单位")
            )))
        .andDo(print())
        .andReturn();
  }

  @Test
  public void testGetInspectionCardiogram() throws Exception {

    CardiogramResponse response = new CardiogramResponse();
    InspectionApiInfo apiInfo = new InspectionApiInfo();
    apiInfo.setValue("22");
    apiInfo.setUnit("mm/l");
    apiInfo.setName("EF");
    apiInfo.setAbbreviation("UA");
    apiInfo.setMeasuredAt(new Date().getTime());

    response.setCardiogramInfo(Collections.singletonList(apiInfo));
    response.setHospital(basicHospitalApiInfo);

    given(medicalService.getPatientCardiogram(anyLong(), anyBoolean()))
        .willReturn(response);

    this.mockMvc
        .perform(get("/inspection/cardiogram")
            .param("patientId", "5Z7g7r")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("get_inspection_cardiogram",
            requestParameters(
                parameterWithName("patientId").description("患者id")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("结果数据"),
                fieldWithPath("data.cardiogramInfo[].value").description("项目值"),
                fieldWithPath("data.cardiogramInfo[].name")
                    .description("项目名"),
                fieldWithPath("data.cardiogramInfo[].measuredAt").description("测量时间"),
                fieldWithPath("data.cardiogramInfo[].abbreviation").description("缩略名"),
                fieldWithPath("data.cardiogramInfo[].unit").description("单位"),
                fieldWithPath("data.hospital.id").description("被审核图片的医院id"),
                fieldWithPath("data.hospital.name").description("被审核图片的医院全称"),
                fieldWithPath("data.hospital.abbreviation").description("被审核图片的医院简称"),
                fieldWithPath("data.hospital.province").description("被审核图片的医院省份"),
                fieldWithPath("data.hospital.city").description("被审核图片的医院城市"),
                fieldWithPath("data.hospital.county").description("被审核图片的医院县区"),
                fieldWithPath("data.hospital.township").description("被审核图片的医院乡镇")
            ))
        )
        .andDo(print())
        .andReturn();
  }

  @Test
  public void testInspection() throws Exception {

    InspectionApiRequest request = new InspectionApiRequest();
    request.setPatientId("5bey28");
    request.setImageType(ImageApiType.BCG);
    request.setImageId("lvPVZ5");
    request.setStatus(ReportApiStatus.EXCEPTION);
    request.setReportAt(new Date());
    ApiInspection inspection = new ApiInspection();
    inspection.setReferenceId("5xzAWl");
    inspection.setValue(12.00D);
    inspection.setAbbreviation("GLU");
    inspection.setUnit("mg/dl");
    request.setInspections(Collections.singletonList(inspection));
    request.setHospital(basicHospitalApiInfo);

    given(medicalService.handleInspection(any(InspectionRequest.class))).willReturn(true);
    doNothing().when(dpcService).sendImageReviewSystemMessage(anyLong(), anyLong());
    this.contextMock.withUid(mockUserId);

    this.mockMvc
        .perform(post(prefixWithContext("/inspection"))
            .contextPath(ConfigConsts.API_CONTEXT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("add_inspection",
            requestFields(
                fieldWithPath("imageId").description("图片ID"),
                fieldWithPath("patientId").description("患者id"),
                fieldWithPath("reportAt").description("图片检查时间(大病历可为空)"),
                fieldWithPath("hospital.id").description("医院信息.id"),
                fieldWithPath("hospital.name").description("医院信息.名称"),
                fieldWithPath("hospital.abbreviation").description("医院信息.简称"),
                fieldWithPath("hospital.province").description("医院信息.省份"),
                fieldWithPath("hospital.city").description("医院信息.城市"),
                fieldWithPath("hospital.county").description("医院信息.县区"),
                fieldWithPath("hospital.township").description("医院信息.乡镇"),
                fieldWithPath("status").
                    description("图片审核状态(如果图片id存在，则状态必须存在):INVALID/拍照内容不正确,NORMAL/正常,EXCEPTION/异常"),
                fieldWithPath("reportAt").description("图片检查时间"),
                fieldWithPath("imageType").description("图片类型"),
                fieldWithPath("inspections[].referenceId").description("生化项目参考id"),
                fieldWithPath("inspections[].value").description("生化项目值(max : 10000,min : -10)"),
                fieldWithPath("inspections[].unit").description("生化项目单位"),
                fieldWithPath("inspections[].abbreviation").description("生化项目缩写")

            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("")
            )
        ));
  }


  @Test
  public void testGetInspectionDetail() throws Exception {

    InspectionApiResponse response =
        new InspectionApiResponse();

    response.setImageInfo(imageInfo);

    ItemInfo itemInfo = new ItemInfo();
    itemInfo.setId("50zXkl");
    itemInfo.setReferenceId("5xzAWl");
    itemInfo.setValue(12.345);

    response.setInspections(Collections.singletonList(itemInfo));
    response.setAllType(types);

    given(medicalService.getInspectionByImageId(any(Long.class), any(ImageApiType.class)))
        .willReturn(response);

    this.mockMvc
        .perform(get("/inspection/detail")
            .param("imageId", "rDAXgl")
            .param("type", "ZD")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("get_inspection_detail",
            requestParameters(
                parameterWithName("imageId").description("图片id"),
                parameterWithName("type").description("图片类型")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("结果数据"),
                fieldWithPath("data.imageInfo.type").description("图片类型"),
                fieldWithPath("data.imageInfo.typeName").description("图片类型名称"),
                fieldWithPath("data.imageInfo.reportAt").description("图片检查时间"),
                fieldWithPath("data.imageInfo.hospital.id").description("医院信息.id"),
                fieldWithPath("data.imageInfo.hospital.name").description("医院信息.名称"),
                fieldWithPath("data.imageInfo.hospital.abbreviation").description("医院信息.简称"),
                fieldWithPath("data.imageInfo.hospital.province").description("医院信息.省份"),
                fieldWithPath("data.imageInfo.hospital.city").description("医院信息.城市"),
                fieldWithPath("data.imageInfo.hospital.county").description("医院信息.县区"),
                fieldWithPath("data.imageInfo.hospital.township").description("医院信息.乡镇"),
                fieldWithPath("data.imageInfo.url").description("原图链接"),
                fieldWithPath("data.imageInfo.thumbUrl").description("缩略图链接"),
                fieldWithPath("data.imageInfo.status")
                    .description("被审核图片状态:INVALID/拍照内容不正确,NORMAL/正常,EXCEPTION/异常"),
                fieldWithPath("data.inspections[].id").description("项目id"),
                fieldWithPath("data.inspections[].referenceId").description("项目参考项id"),
                fieldWithPath("data.inspections[].value").description("项目值"),
                fieldWithPath("data.allType[]").description("图片所有类型")
            )))
        .andDo(print())
        .andReturn();
  }


  @Test
  public void testSymptom() throws Exception {

    symptomApiResponse.setContent(content);

    symptomApiResponse.setNote("test");
    given(medicalService.handleSymptom(any(SymptomRequest.class))).willReturn(symptomApiResponse);
    this.contextMock.withUid(mockUserId);

    this.mockMvc
        .perform(post(prefixWithContext("/symptom"))
            .contextPath(ConfigConsts.API_CONTEXT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(symptomApiRequest)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("handle_symptom",
            requestFields(
                fieldWithPath("patientId").description("患者id"),
                fieldWithPath("content[].symptomId").description("患者症状ARRAY"),
                fieldWithPath("content[].name").description("患者症状name"),
                fieldWithPath("content[].illAt").description("患者症状出现时间"),
                fieldWithPath("note").description("备注")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data.content[].symptomId").description("患者症状id"),
                fieldWithPath("data.content[].name").description("患者症状name"),
                fieldWithPath("data.content[].illAt").description("患者症状出现时间"),
                fieldWithPath("data.recordedDay").description("采集时间"),
                fieldWithPath("data.note").description("备注")
            )
        ));
  }

  @Test
  public void testGetBasicSymptom() throws Exception {
    BasicSymptomInfo basicSymptomInfo = new BasicSymptomInfo();
    basicSymptomInfo.setId(11L);
    basicSymptomInfo.setName("出血");
    BasicSymptomsResponse basicSymptomsResponse = new BasicSymptomsResponse(
        Collections.singletonList(basicSymptomInfo), 1);
    given(
        medicalService.getBasicSymptoms(any(Integer.class), any(Integer.class), any(String.class)))
        .willReturn(basicSymptomsResponse);
    this.mockMvc
        .perform(get("/basic/symptom")
            .param("pageAt", "1")
            .param("pageSize", "15")
            .param("name", "出血")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("get_basic_symptom",
            requestParameters(
                parameterWithName("pageAt").description("当前页码"),
                parameterWithName("pageSize").description("当前页大小"),
                parameterWithName("name").description("名字")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data.total").description("总条数"),
                fieldWithPath("data.basicSymptomInfos[].name").description("症状名"),
                fieldWithPath("data.basicSymptomInfos[].id").description("症状id")
            )

        ));

  }

  @Test
  public void testGetSymptom() throws Exception {

    given(medicalService.getSymptomByPatientId(any(Long.class))).willReturn(symptomApiResponse);

    this.mockMvc
        .perform(get("/symptom")
            .param("patientId", "rDAXgl")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("get_symptom",
            requestParameters(
                parameterWithName("patientId").description("患者id")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data.content[].symptomId").description("患者症状id"),
                fieldWithPath("data.content[].name").description("患者症状name"),
                fieldWithPath("data.content[].illAt").description("患者症状出现时间"),
                fieldWithPath("data.recordedDay").description("采集时间"),
                fieldWithPath("data.note").description("备注")
            )

        ));
  }

  @Test
  public void testGetSymptomHistory() throws Exception {

    SymptomsResponse response = new SymptomsResponse(Collections.singletonList(symptomInfo));
    given(
        medicalService.findSymptomHistory(any(Long.class), any(Integer.class), any(Integer.class)))
        .willReturn(response);

    this.mockMvc
        .perform(get("/symptom/history")
            .param("patientId", "rDAXgl")
            .param("month", "10")
            .param("year", "2017")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("get_symptom_history",
            requestParameters(
                parameterWithName("patientId").description("患者id"),
                parameterWithName("month").description("月份，若空，表示当前月份"),
                parameterWithName("year").description("年份，若空，表示当前年份")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data.symptomInfos[]content[].symptomId").description("患者症状ARRAY"),
                fieldWithPath("data.symptomInfos[]content[].name").description("患者症状name"),
                fieldWithPath("data.symptomInfos[]content[].illAt").description("患者症状出现时间"),
                fieldWithPath("data.symptomInfos[].note").description("备注"),
                fieldWithPath("data.symptomInfos[].recordedDay").description("采集日期")

            )

        ));
  }

  @Test
  public void testGetMedicineTaboo() throws Exception {

    MedicinesTaboosResponse response = new MedicinesTaboosResponse();
    List<MedicineTaboos> medicineTaboos = new ArrayList<>();
    for (int i = 0; i < 2; i++) {
      MedicineTaboos medicineTaboo = new MedicineTaboos();
      medicineTaboo.setMedicineId(1001L + i);
      medicineTaboo.setMedicineName("name" + i);
      medicineTaboo.setReasons(Arrays.asList("reason1", "reason2"));
      medicineTaboo.setCategory(Category.CAUTION);
      medicineTaboos.add(medicineTaboo);
    }
    response.setMedicineTaboos(medicineTaboos);

    given(
        medicalService.getMedicineTaboo(any(Long.class), anyListOf(Long.class), any(Type.class)))
        .willReturn(response);

    this.mockMvc
        .perform(get("/taboo")
            .param("patientId", "rDAXgl")
            .param("medicineIds", "rDAXgl,5bey28")
            .param("type", "ADJUST_ME")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("get_taboo",
            requestParameters(
                parameterWithName("patientId").description("患者id"),
                parameterWithName("medicineIds").description("药品id列表"),
                parameterWithName("type").description("查询来源标识").optional()
            ),
            responseFields(
                fieldWithPath("status").type("String").description("请求是否成功"),
                fieldWithPath("data").type("Object").description("结果数据"),
                fieldWithPath("data.medicineTaboos[].medicineId").type("String")
                    .description("药品id"),
                fieldWithPath("data.medicineTaboos[].medicineName").type("String")
                    .description("药品名称"),
                fieldWithPath("data.medicineTaboos[].reasons[]").type("String").description("禁忌原因"),
                fieldWithPath("data.medicineTaboos[].category").type("String")
                    .description("禁忌症类型，CAUTION/慎用，DISABLED/禁用")
            )

        ));
  }

  @Test
  public void testGetBasicHospitals() throws Exception {
    BasicHospitalsApiResponse apiResponse = new BasicHospitalsApiResponse();
    List<BasicHospitalApiInfo> basicHospitalApiInfos = List.of(basicHospitalApiInfo);
    apiResponse.seBasicHospitalApiInfos(basicHospitalApiInfos);
    given(medicalService.getBasicHospitals(anyString()))
        .willReturn(apiResponse);

    this.mockMvc
        .perform(get("/basic/hospital")
            .param("name", "人民医院")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("get_basic_hospital",
            requestParameters(
                parameterWithName("name").description("医院名称:模糊查询前25条数据")),

            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("结果数据"),
                fieldWithPath("data.basicHospitalApiInfos[].id").description("医院id"),
                fieldWithPath("data.basicHospitalApiInfos[].name").description("医院名称"),
                fieldWithPath("data.basicHospitalApiInfos[].abbreviation").description("医院简称"),
                fieldWithPath("data.basicHospitalApiInfos[].province").description("省份"),
                fieldWithPath("data.basicHospitalApiInfos[].city").description("市区"),
                fieldWithPath("data.basicHospitalApiInfos[].county").description("县区"),
                fieldWithPath("data.basicHospitalApiInfos[].township").description("乡镇")
            )));
  }


  @Test
  public void testGetBasicTreament() throws Exception {
    BasicTreatmentsApiResponse apiResponse = new BasicTreatmentsApiResponse();
    BasicTreatmentApiInfo basicTreatmentApiInfo = new BasicTreatmentApiInfo();
    AttachedApiInfo attachedApiInfo = new AttachedApiInfo();
    attachedApiInfo.setIdList(List.of("1", "2"));
    attachedApiInfo.setInfoType(InfoType.CORONARY_STENT);
    basicTreatmentApiInfo.setAttachedInfos(List.of(attachedApiInfo));
    basicTreatmentApiInfo.setName("treatment");
    basicTreatmentApiInfo.setId("rDAXgl");
    apiResponse.setTreatmentInfos(List.of(basicTreatmentApiInfo));

    given(medicalService.getBasicTreaments(anyString(), anyInt(), anyInt()))
        .willReturn(apiResponse);

    this.mockMvc
        .perform(get("/basic/treatment")
            .param("name", "处理方式")
            .param("pageAt", "1")
            .param("pageSize", "10")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("get_basic_treatment",
            requestParameters(
                parameterWithName("name").description("处理方式名称"),
                parameterWithName("pageAt").description("页码数"),
                parameterWithName("pageSize").description("页含量")),

            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("结果数据"),
                fieldWithPath("data.treatmentInfos[].id").description("处理方式id"),
                fieldWithPath("data.treatmentInfos[].name").description("处理方式名称"),
                fieldWithPath("data.treatmentInfos[].attachedInfos[]")
                    .description("处理方式关联信息 null标识没有关联信息"),
                fieldWithPath("data.treatmentInfos[].attachedInfos[].infoType")
                    .description("处理方式关联信息类型  CORONARY_STENT --> 支架信息"),
                fieldWithPath("data.treatmentInfos[].attachedInfos[].idList")
                    .description("处理方式关联信息id列表 null表示查询所有 list有值则查询list中id对应内容")
            )));
  }

  @Test
  public void testGetBasicCoronaryStent() throws Exception {
    BasicCoronaryStentApiInfo basicCoronaryStentApiInfo = new BasicCoronaryStentApiInfo();
    basicCoronaryStentApiInfo.setId("rDAXgl");
    basicCoronaryStentApiInfo.setBaseCoat("coat");
    basicCoronaryStentApiInfo.setCoatingDrug("coatdrug");
    basicCoronaryStentApiInfo.setCompany("company");
    basicCoronaryStentApiInfo.setMaterial("material");
    basicCoronaryStentApiInfo.setName("name");
    basicCoronaryStentApiInfo.setPolymericCarrier("carrier");
    basicCoronaryStentApiInfo.setThickness("thickness");
    BasicCoronaryStentApiResponse apiResponse = new BasicCoronaryStentApiResponse(
        List.of(basicCoronaryStentApiInfo));

    given(medicalService.getBasicStents(anyString(), anyListOf(String.class), anyInt(), anyInt()))
        .willReturn(apiResponse);

    this.mockMvc
        .perform(get("/basic/stent")
            .param("name", "支架名称")
            .param("idList", "1,2,3")
            .param("pageAt", "1")
            .param("pageSize", "10")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("get_basic_stent",
            requestParameters(
                parameterWithName("name").description("支架名称"),
                parameterWithName("idList").description("支架id列表,"),
                parameterWithName("pageAt").description("页码数"),
                parameterWithName("pageSize").description("页含量")),

            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("结果数据"),
                fieldWithPath("data.coronaryStentApiInfos[].id").description("支架id"),
                fieldWithPath("data.coronaryStentApiInfos[].company").description("支架厂商"),
                fieldWithPath("data.coronaryStentApiInfos[].name").description("支架名称"),
                fieldWithPath("data.coronaryStentApiInfos[].thickness").description("支架厚度"),
                fieldWithPath("data.coronaryStentApiInfos[].coatingDrug").description("涂覆"),
                fieldWithPath("data.coronaryStentApiInfos[].polymericCarrier").description("载体"),
                fieldWithPath("data.coronaryStentApiInfos[].baseCoat").description("底涂")
            )));
  }

  @Test
  public void testGetHospitalDetail() throws Exception {
    BasicHospitalDetailApiResponse apiResponse = new BasicHospitalDetailApiResponse(
        basicHospitalApiInfo);
    given(medicalService.getHospitalDetail(anyString())).willReturn(apiResponse);
    this.mockMvc
        .perform(get("/basic/hospital/{id}", "5bey28")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("get_hospital_detail",
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data.hospital.id").description("医院id"),
                fieldWithPath("data.hospital.name").description("医院全称"),
                fieldWithPath("data.hospital.abbreviation").description("医院简称"),
                fieldWithPath("data.hospital.province").description("医院省份"),
                fieldWithPath("data.hospital.city").description("医院城市"),
                fieldWithPath("data.hospital.county").description("医院县区"),
                fieldWithPath("data.hospital.township").description("医院乡镇")
            ))
        )
        .andDo(print())
        .andReturn();
  }

  @Test
  public void testGetStandardData() throws Exception {

    List<StandardDataCommon> standardDataCommonsHigh = new ArrayList<>();
    StandardDataCommon standardDataCommon1 = new StandardDataCommon();
    standardDataCommon1.setMeasuredAt(1526601600000L);
    standardDataCommon1.setValue("130");

    StandardDataCommon standardDataCommon2 = new StandardDataCommon();
    standardDataCommon2.setMeasuredAt(1526256000000L);
    standardDataCommon2.setValue("140");

    standardDataCommonsHigh.add(standardDataCommon1);
    standardDataCommonsHigh.add(standardDataCommon2);

    List<InspectionChartStandardInfo> standardReferenceInfos = new ArrayList<>();
    InspectionChartStandardInfo inspectionChartStandardInfo = new InspectionChartStandardInfo();
    inspectionChartStandardInfo.setStandardCreatedAt(1526515200000L);
    inspectionChartStandardInfo.setStandardMaxValue("120.0");
    inspectionChartStandardInfo.setStandardMinValue("86.0");
    standardReferenceInfos.add(inspectionChartStandardInfo);

    InspectionChartReferenceInfo standardReferenceInfo = new InspectionChartReferenceInfo();
    standardReferenceInfo.setReferenceCreatedAt(1525873311526L);
    standardReferenceInfo.setReferenceMaxValue("128.0");
    standardReferenceInfo.setReferenceMinValue("88.0");

    StandardDataInfo standardDataInfoHigh = new StandardDataInfo();
    standardDataInfoHigh.setStandardValue(standardReferenceInfos);
    standardDataInfoHigh.setReferenceValue(standardReferenceInfo);
    standardDataInfoHigh.setData(standardDataCommonsHigh);
    standardDataInfoHigh.setDataType(StandardItem.HIGH_BP);

    List<StandardDataCommon> standardDataCommonsLow = new ArrayList<>();
    StandardDataCommon standardDataCommon3 = new StandardDataCommon();
    standardDataCommon3.setMeasuredAt(1526601600000L);
    standardDataCommon3.setValue("80");

    StandardDataCommon standardDataCommon4 = new StandardDataCommon();
    standardDataCommon4.setMeasuredAt(1526256000000L);
    standardDataCommon4.setValue("50");
    standardDataCommonsLow.add(standardDataCommon3);
    standardDataCommonsLow.add(standardDataCommon4);

    StandardDataInfo standardDataInfoLow = new StandardDataInfo();
    standardDataInfoLow.setStandardValue(standardReferenceInfos);
    standardDataInfoLow.setReferenceValue(standardReferenceInfo);
    standardDataInfoLow.setData(standardDataCommonsLow);
    standardDataInfoLow.setDataType(StandardItem.LOW_BP);

    List<StandardDataInfo> standardDataInfos = new ArrayList<>();
    standardDataInfos.add(standardDataInfoHigh);
    standardDataInfos.add(standardDataInfoLow);

    StandardDataApiResponse response = new StandardDataApiResponse();
    response.setType(InspectionChartApiType.BP);
    response.setSeries(standardDataInfos);

    given(medicalService
        .getInspectionChart(any(Long.class), any(InspectionChartApiType.class), any(Long.class),
            any(Long.class)))
        .willReturn(response);

    this.mockMvc
        .perform(get("/inspection/chart")
            .param("patientId", "8WAJEr")
            .param("type", "BP")
            .param("startTime", "1525104000000")
            .param("endTime", "1527609600000"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("inspection_chart",
            requestParameters(
                parameterWithName("patientId").description("患者Id"),
                parameterWithName("type").description("查询类型"),
                parameterWithName("startTime").description("查询起始时间"),
                parameterWithName("endTime").description("查询终止时间")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("结果数据"),
                fieldWithPath("data.series[]").description("达标数据"),
                fieldWithPath("data.type").description(
                    "达标类型   BP(\"BP\", \"血压\"),\n"
                        + "  HEART_RATE(\"HEART-RATE\", \"心率\"),\n"
                        + "  GLU_BEFORE_BREAKFAST(\"GLU_BEFORE_BREAKFAST\", \"空腹血糖\"),\n"
                        + "  GLU_AFTER_BREAKFAST(\"GLU_AFTER_BREAKFAST\", \"早餐后血糖\"),\n"
                        + "  GLU_BEFORE_LUNCH(\"GLU_BEFORE_LUNCH\", \"午餐前血糖\"),\n"
                        + "  GLU_AFTER_LUNCH(\"GLU_AFTER_LUNCH\", \"午餐后血糖\"),\n"
                        + "  GLU_BEFORE_DINNER(\"GLU_BEFORE_DINNER\", \"晚餐前血糖\"),\n"
                        + "  GLU_AFTER_DINNER(\"GLU_AFTER_DINNER\", \"晚餐后血糖\"),\n"
                        + "  GLU_BEFORE_SLEEP(\"GLU_BEFORE_SLEEP\", \"睡前血糖\"),\n"
                        + "  TC(\"TC\", \"总胆固醇TCH\"),\n"
                        + "  TG(\"TG\", \"甘油三酯TG\"),\n"
                        + "  LDL_C(\"LDL-C\", \"低密度脂蛋白LDL-C（低密度脂蛋白胆固醇）\"),\n"
                        + "  UA(\"UA\", \"尿酸UA\")"),
                fieldWithPath("data.series[].data[].value").description("测量值"),
                fieldWithPath("data.series[].data[].measuredAt").description("测试时间"),
                fieldWithPath("data.series[].dataType")
                    .description("达标子类型 HIGH_BP(\"HIGH_BP\", \"高血压\"),\n"
                        + "  LOW_BP(\"LOW_BP\", \"低血压\"),\n"
                        + "  SINGLETON(\"SINGLETON\",\"默认值\");"),
                fieldWithPath("data.series[].referenceValue.referenceMaxValue")
                    .description("建议最大值"),
                fieldWithPath("data.series[].referenceValue.referenceMinValue")
                    .description("建议最小值"),
                fieldWithPath("data.series[].referenceValue.referenceCreatedAt")
                    .description("建议时间"),
                fieldWithPath("data.series[].standardValue[].standardMaxValue")
                    .description("达标最大值"),
                fieldWithPath("data.series[].standardValue[].standardMinValue")
                    .description("达标最小值"),
                fieldWithPath("data.series[].standardValue[].standardCreatedAt").description("达标时间")
            )));
  }

}
