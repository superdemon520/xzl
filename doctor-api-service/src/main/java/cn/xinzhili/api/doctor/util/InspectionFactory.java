package cn.xinzhili.api.doctor.util;

import cn.xinzhili.api.doctor.bean.CustomizedInspectionInfo;
import cn.xinzhili.api.doctor.bean.DepartmentStandardApiInfo;
import cn.xinzhili.api.doctor.bean.ImageApiType;
import cn.xinzhili.api.doctor.bean.InspectionApiInfo;
import cn.xinzhili.api.doctor.bean.InspectionApiStatus;
import cn.xinzhili.api.doctor.bean.InspectionItem;
import cn.xinzhili.api.doctor.bean.InspectionStandardData;
import cn.xinzhili.api.doctor.bean.StandardRateApiInfo;
import cn.xinzhili.api.doctor.bean.request.AddInspectionReferenceRequest;
import cn.xinzhili.api.doctor.bean.request.InspectionApiRequest;
import cn.xinzhili.api.doctor.bean.request.InspectionApiRequest.ApiInspection;
import cn.xinzhili.api.doctor.bean.request.SendDoctorAdviceWrapperRequest;
import cn.xinzhili.api.doctor.bean.request.UpdateDoctorAdviceClientRequest;
import cn.xinzhili.api.doctor.bean.response.InspectionApiResponse;
import cn.xinzhili.api.doctor.bean.response.InspectionApiResponse.ItemInfo;
import cn.xinzhili.api.doctor.bean.response.LatestInspectionApiResponse;
import cn.xinzhili.api.doctor.bean.response.StatisticsStandardApiResponse;
import cn.xinzhili.medical.api.CustomizableItem;
import cn.xinzhili.medical.api.DepartmentStandardInfo;
import cn.xinzhili.medical.api.ImageDetailInfo;
import cn.xinzhili.medical.api.ImageType;
import cn.xinzhili.medical.api.InspectionInfo;
import cn.xinzhili.medical.api.InspectionReferenceInfo;
import cn.xinzhili.medical.api.ReferenceInfo;
import cn.xinzhili.medical.api.ReportInfo;
import cn.xinzhili.medical.api.ReportStatus;
import cn.xinzhili.medical.api.request.HandleCustomizableReferenceRequest;
import cn.xinzhili.medical.api.request.HandleCustomizableReferenceRequest.CustomizableReference;
import cn.xinzhili.medical.api.request.InspectionRequest;
import cn.xinzhili.medical.api.request.InspectionRequest.Inspection;
import cn.xinzhili.medical.api.response.InspectionResponse;
import cn.xinzhili.medical.api.response.LatestInspectionResponse;
import cn.xinzhili.medical.api.response.StatisticsStandardResponse;
import cn.xinzhili.xutils.core.ErrorCode;
import cn.xinzhili.xutils.core.FailureException;
import cn.xinzhili.xutils.core.util.HashUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author by Loki on 17/3/28.
 */
public class InspectionFactory {

  private static final Logger logger = LoggerFactory
      .getLogger(InspectionFactory.class);

  public static InspectionApiInfo api(InspectionInfo inspectionInfo, boolean replaceSign) {

    InspectionApiInfo apiInfo = new InspectionApiInfo();
    apiInfo.setMeasuredAt(inspectionInfo.getMeasuredAt());

    apiInfo.setName(
        replaceSign ? replaceCardiogramSign(inspectionInfo.getName()) : inspectionInfo.getName());
    apiInfo.setAbbreviation(inspectionInfo.getAbbreviation());
    apiInfo.setUnit(inspectionInfo.getUnit());
    if (inspectionInfo.getStatus() != null) {
      apiInfo.setStatus(InspectionApiStatus.valueOf(inspectionInfo.getStatus().name()));
    }
    if (inspectionInfo.getValue() != null) {
      apiInfo.setValue(String.valueOf(inspectionInfo.getValue() / 1000));
    }
    return apiInfo;
  }

  private static String replaceCardiogramSign(String name) {

    if (StringUtils.isEmpty(name)) {
      return null;
    }
    return name.replaceAll("射血分数EF", "EF")
        .replaceAll("左室舒张期末内径LVD", "左室舒末内径")
        .replaceAll("室间隔厚度IVS", "室间隔厚度")
        .replaceAll("左房内径LA", "左房内径");
  }

  public static LatestInspectionApiResponse api(LatestInspectionResponse response) {

    LatestInspectionApiResponse latestResponse = new LatestInspectionApiResponse();
    List<LatestInspectionResponse.InspectionItem> itemsInfo = response.getItemsInfo();

    List<CustomizedInspectionInfo> result = itemsInfo.stream().map(t -> {
      CustomizedInspectionInfo apiInfo = new CustomizedInspectionInfo();
      apiInfo.setMeasuredAt(t.getMeasuredAt());
      apiInfo.setAbbreviation(t.getName().name());
      apiInfo.setName(
          t.getName().getDescription().replaceAll(CustomizableItem.GLU.getDescription(), "血糖")
              .replaceAll(CustomizableItem.UA.getDescription(), "尿酸")
              .replaceAll(CustomizableItem.TC.getDescription(), "总胆固醇")
              .replaceAll(CustomizableItem.TG.getDescription(), "甘油三酯")
              .replaceAll(CustomizableItem.LDL_C.getDescription(), "LDL_C"));
      apiInfo.setValue(getValueByCode(t.getName().getCode(), t.getValue()));//todo better
      if (t.getStatus() != null) {
        apiInfo.setStatus(InspectionApiStatus.valueOf(t.getStatus().name()));
      }
      //npe
      ReferenceInfo referenceInfo = t.getReferenceInfo();
      Integer customizedReferenceMax = referenceInfo.getCustomizedReferenceMax();
      Integer customizedReferenceMin = referenceInfo.getCustomizedReferenceMin();
      apiInfo.setCustomizedReferenceMax(
          customizedReferenceMax == null ? null : downScale(customizedReferenceMax));
      apiInfo.setCustomizedReferenceMin(
          customizedReferenceMin == null ? null : downScale(customizedReferenceMin));

      CustomizableItem item = t.getName();
      apiInfo.setSpliceSymbol(getInspectionSpliceSymbol(item.getDescription()));
      apiInfo.setUnifiedReference(joinInspectionReferenceValues(item.getDescription(),
          referenceInfo.getUnifiedReferenceMax(), referenceInfo.getUnifiedReferenceMin()));
      return apiInfo;

    }).collect(Collectors.toList());
    latestResponse.setInspections(result);
    return latestResponse;
  }

  private static String getValueByCode(String code, String value) {
    if (code.equals(CustomizableItem.UA.getCode()) && StringUtils.isNotEmpty(value)) {
      Double doubleValue = Double.parseDouble(value);
      return doubleValue.intValue() + "";
    } else {
      return value;
    }
  }

  private static String joinInspectionReferenceValues(String description, int max, int min) {
    if (StringUtils.isEmpty(description)) {
      return null;
    } else if (description.equals(CustomizableItem.BP.getDescription())) {
      return downScale(max).intValue() + "/" + downScale(min).intValue();
    } else if (description.equals(CustomizableItem.HEART_RATE.getDescription()) || description
        .equals(CustomizableItem.UA.getDescription())) {
      return downScale(min).intValue() + "-" + downScale(max).intValue();
    } else {
      return downScale(min) + "-" + downScale(max);
    }
  }

  private static String getInspectionSpliceSymbol(String description) {
    if (StringUtils.isEmpty(description)) {
      return null;
    } else if (description.equals(CustomizableItem.BP.getDescription())) {
      return "/";
    } else {
      return "-";
    }
  }


  private static InspectionItem api2Item(InspectionReferenceInfo referenceInfo) {

    if (referenceInfo == null) {
      throw new IllegalArgumentException("inspection reference info is null !");
    }

    InspectionItem item = new InspectionItem();
    if (referenceInfo.getId() != null) {
      item.setId(HashUtils.encode(referenceInfo.getId()));
    }
    item.setUnit(referenceInfo.getUnit());
    item.setAbbreviation(referenceInfo.getAbbreviation());
    item.setName(referenceInfo.getName());
    return item;
  }

  public static List<InspectionItem> api2Items(List<InspectionReferenceInfo> references) {

    if (references == null) {
      throw new IllegalArgumentException("inspection references is null !");
    }

    List<InspectionItem> inspectionItems = new ArrayList<>();
    for (InspectionReferenceInfo referenceInfo : references) {
      inspectionItems.add(api2Item(referenceInfo));
    }
    return inspectionItems;
  }

  public static InspectionRequest of(InspectionApiRequest request) {

    if (request == null) {
      throw new IllegalArgumentException("inspection api request is null !");
    }

    InspectionRequest inspectionRequest = new InspectionRequest();

    if (!StringUtils.isEmpty(request.getImageId())) {
      inspectionRequest.setImageId(HashUtils.decode(request.getImageId()));
    }

    if (!StringUtils.isEmpty(request.getPatientId())) {
      inspectionRequest.setPatientId(HashUtils.decode(request.getPatientId()));
    }

    if (request.getImageType() != null) {
      inspectionRequest.setType(ImageType.valueOf(request.getImageType().name()));
    }

    if (request.getInspections() != null) {
      inspectionRequest.setInspections(api2Inspections(request.getInspections()));
    }

    //report info
    ReportInfo reportInfo = new ReportInfo();
    if (request.getStatus() != null) {
      reportInfo.setStatus(ReportStatus.valueOf(request.getStatus().name()));
    }
    reportInfo.setReportAt(request.getReportAt());
    reportInfo.setHospital(BasicHospitalFactory.of(request.getHospital()));
    inspectionRequest.setReportInfo(reportInfo);

    return inspectionRequest;
  }

  private static List<Inspection> api2Inspections(List<ApiInspection> apiInspections) {

    if (apiInspections == null) {
      throw new IllegalArgumentException("api inspections is null !");
    }

    List<Inspection> inspections = new ArrayList<>();
    for (ApiInspection apiInspection : apiInspections) {

      Inspection inspection = new Inspection();

      if (!StringUtils.isEmpty(apiInspection.getReferenceId())) {
        inspection.setReferenceId(HashUtils.decode(apiInspection.getReferenceId()));
      }
      if (apiInspection.getValue() != null) {
        //转换单位 统一到数据库默认单位
        inspection.setValue(upScale(apiInspection.getValue() / UnitConversionEnum
            .getProportion(apiInspection.getAbbreviation(), apiInspection.getUnit())));
      }
      inspections.add(inspection);
    }
    return inspections;
  }

  public static InspectionApiResponse api2Response(InspectionResponse response) {

    if (response == null) {
      throw new FailureException(ErrorCode.INVALID_PARAMS, "inspection response is null !");
    }

    InspectionApiResponse apiResponse = new InspectionApiResponse();
    ImageDetailInfo imageDetailInfo = new ImageDetailInfo();
    imageDetailInfo.setImage(response.getImageInfo());
    imageDetailInfo.setReport(response.getReportInfo());
    apiResponse.setImageInfo(ImageFactory.api(imageDetailInfo));
    apiResponse.setInspections(api2ItemsInfo(response.getInspections()));
    apiResponse.setAllType(apiResponse.resolver(response.getTypes()));
    return apiResponse;

  }

  public static HandleCustomizableReferenceRequest of(AddInspectionReferenceRequest request) {

    CustomizableReference reference = new CustomizableReference(
        CustomizableItem.valueOf(request.getItem().name()), upScale(request.getReferenceMax()),
        upScale(request.getReferenceMin()));
    HandleCustomizableReferenceRequest result = new HandleCustomizableReferenceRequest();
    result.setReferences(Collections.singletonList(reference));
    result.setPatientId(HashUtils.decode(request.getPatientId()));
    return result;
  }

  private static List<ItemInfo> api2ItemsInfo(List<InspectionInfo> inspections) {

    if (inspections == null) {
      throw new FailureException(ErrorCode.INVALID_PARAMS, "inspections is null !");
    }

    List<ItemInfo> items = new ArrayList<>();

    for (InspectionInfo info : inspections) {

      ItemInfo itemInfo = new ItemInfo();

      if (info.getId() != null) {
        itemInfo.setId(HashUtils.encode(info.getId()));
      }

      if (info.getReferenceId() != null) {
        itemInfo.setReferenceId(HashUtils.encode(info.getReferenceId()));
      }
      if (info.getValue() != null) {
        itemInfo.setValue(downScale(info.getValue()));
      }
      items.add(itemInfo);
    }
    return items;

  }

  public static CustomizableReference newCustomizableReference(InspectionStandardData source) {
    CustomizableReference reference = new CustomizableReference();
    reference.setItem(CustomizableItem.valueOf(source.getItem()));
    reference.setReferenceMax(upScale(source.getCustomizedReferenceMax()));
    reference.setReferenceMin(upScale(source.getCustomizedReferenceMin()));
    return reference;
  }


  private static Integer upScale(Double value) {
    if (value == null) {
      throw new FailureException(ErrorCode.INVALID_PARAMS, "up scale value is null !");
    }
    Double v = value * 1000;
    return v.intValue();
  }

  private static Double downScale(Integer value) {
    if (value == null) {
      throw new FailureException(ErrorCode.INVALID_PARAMS, "down scale value is null !");
    }
    return (Math.round(value) / 1000.0);
  }

  public static UpdateDoctorAdviceClientRequest of(SendDoctorAdviceWrapperRequest sendRequest) {
    UpdateDoctorAdviceClientRequest updateRequest = new UpdateDoctorAdviceClientRequest();
    updateRequest.setPatientId(sendRequest.getPatientId());
    sendRequest.getInspectionStandards().getInspectionStandardList().forEach(
        inspectionStandard -> inspectionStandard.getEditIns().forEach(
            inspectionStandardData -> inspectionStandardData
                .setName(convertInspectionName(inspectionStandardData.getName()))));
    updateRequest.setInspectionStandards(sendRequest.getInspectionStandards());
    return updateRequest;
  }

  public static String convertInspectionName(String showName) {
    switch (showName) {
      case "血糖":
        return CustomizableItem.GLU.getDescription();
      case "总胆固醇":
        return CustomizableItem.TC.getDescription();
      case "尿酸":
        return CustomizableItem.UA.getDescription();
      case "LDL-C":
        return CustomizableItem.LDL_C.getDescription();
      case "甘油三酯":
        return CustomizableItem.TG.getDescription();
      default:
        return showName;
    }
  }

  private static DepartmentStandardApiInfo api(DepartmentStandardInfo info) {
    DepartmentStandardApiInfo apiInfo = new DepartmentStandardApiInfo();
    apiInfo.setDepartmentName(info.getDepartmentName());
    List<StandardRateApiInfo> standardRateInfos = info.getStandardRateInfos().stream().map(t -> {
      StandardRateApiInfo standardRate = new StandardRateApiInfo();
      standardRate.setInspectionName(t.getInspectionName());
      standardRate.setStandardRate(t.getStandardRate());
      return standardRate;
    }).collect(Collectors.toList());
    apiInfo.setStandardRateInfos(standardRateInfos);
    return apiInfo;
  }

  public static StatisticsStandardApiResponse api(StatisticsStandardResponse response) {
    List<DepartmentStandardApiInfo> apiInfos = response.getDepartmentStandardInfos().stream()
        .map(InspectionFactory::api).collect(
            Collectors.toList());
    return new StatisticsStandardApiResponse(apiInfos);
  }


}
