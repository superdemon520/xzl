package cn.xinzhili.api.doctor.util;

import cn.xinzhili.api.doctor.bean.DepartmentImagesApiInfo;
import cn.xinzhili.api.doctor.bean.ImageApiInfo;
import cn.xinzhili.api.doctor.bean.ImageApiStatus;
import cn.xinzhili.api.doctor.bean.ImageApiType;
import cn.xinzhili.api.doctor.bean.ImageCategory;
import cn.xinzhili.api.doctor.bean.request.ImageReviewApiRequest;
import cn.xinzhili.api.doctor.bean.response.StatisticsImagesApiResponse;
import cn.xinzhili.medical.api.DepartmentImagesInfo;
import cn.xinzhili.medical.api.ImageDetailInfo;
import cn.xinzhili.medical.api.ImageInfo;
import cn.xinzhili.medical.api.ImageType;
import cn.xinzhili.medical.api.ReportInfo;
import cn.xinzhili.medical.api.ReportStatus;
import cn.xinzhili.medical.api.request.ImageReviewInfoRequest;
import cn.xinzhili.medical.api.response.StatisticsImagesResponse;
import cn.xinzhili.xutils.core.ErrorCode;
import cn.xinzhili.xutils.core.FailureException;
import cn.xinzhili.xutils.core.util.HashUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

/**
 * @author by Loki on 17/3/13.
 */
public class ImageFactory {

  private static final Logger logger = LoggerFactory
      .getLogger(ImageFactory.class);

  public static ImageApiInfo api(ImageDetailInfo imageInfo) {

    if (imageInfo == null) {
      throw new FailureException(ErrorCode.INVALID_PARAMS, "image info is null !");
    }

    ImageApiInfo apiInfo = new ImageApiInfo();
    if (imageInfo.getImage() != null) {
      ImageInfo image = imageInfo.getImage();
      if (image.getId() != null) {
        apiInfo.setId(HashUtils.encode(image.getId()));
      }
      apiInfo.setUrl(image.getUrl());
      apiInfo.setThumbUrl(image.getThumbUrl());
      if (Objects.nonNull(image.getType())) {
        apiInfo.setType(ImageApiType.valueOf(image.getType().name()));
        //大类
        if (image.getType().getCode() / 100 == 2) {
          apiInfo.setCategory(ImageCategory.ANALYSIS);
        } else if (image.getType().getCode() / 100 == 3) {
          apiInfo.setCategory(ImageCategory.INSPECTION);
        } else {
          apiInfo.setCategory(ImageCategory.OTHER);
        }
        apiInfo.setTypeName(image.getType().getDescription());
      }
    }
    ReportInfo report = imageInfo.getReport();
    if (report != null) {
      apiInfo.setReportAt(report.getReportAt());
      apiInfo.setStatus(ImageApiStatus.valueOf(report.getStatus().name()));
      apiInfo.setHospital(BasicHospitalFactory.api(report.getHospital()));
    }
    apiInfo.setCount(imageInfo.getCount());
    return apiInfo;
  }

  /**
   * 在image/latest接口中 ct/冠造 合二为一 ［瓜皮需求］
   */
  public static ImageApiInfo apiForImageLatestCt(ImageDetailInfo ctInfo, ImageDetailInfo gzInfo) {

    ImageApiInfo apiInfo = new ImageApiInfo();

    apiInfo.setType(ImageApiType.CTGZ);
    apiInfo.setCategory(ImageCategory.INSPECTION);
    apiInfo.setTypeName("CT/冠造");

    ReportInfo ctInfoReport = ctInfo.getReport();
    ReportInfo gzInfoReport = gzInfo.getReport();

    if (Objects.isNull(ctInfoReport) && Objects.nonNull(gzInfoReport)) {
      apiInfo.setReportAt(gzInfoReport.getReportAt());
      apiInfo.setStatus(ImageApiStatus.valueOf(gzInfoReport.getStatus().name()));
    } else if (Objects.nonNull(ctInfoReport) && Objects.isNull(gzInfoReport)) {
      apiInfo.setReportAt(ctInfoReport.getReportAt());
      apiInfo.setStatus(ImageApiStatus.valueOf(ctInfoReport.getStatus().name()));
    } else if (Objects.nonNull(ctInfoReport)) {
      apiInfo.setReportAt(findTheEarlyOne(ctInfoReport.getReportAt(), gzInfoReport.getReportAt()));
      apiInfo.setStatus(ImageApiStatus
          .valueOf(getImageStatusFrom(ctInfoReport.getStatus(), gzInfoReport.getStatus()).name()));
    }
    apiInfo.setCount(ctInfo.getCount() + gzInfo.getCount());
    return apiInfo;
  }

  private static ReportStatus getImageStatusFrom(ReportStatus s1, ReportStatus s2) {
    if (Objects.isNull(s1)) {
      return s2;
    }
    if (Objects.isNull(s2)) {
      return s1;
    }

    List<ReportStatus> source = List.of(s1, s2);

    if (source.contains(ReportStatus.INVALID)) {
      return ReportStatus.INVALID;
    } else if (source.contains(ReportStatus.EXCEPTION)) {
      return ReportStatus.EXCEPTION;
    } else if (source.contains(ReportStatus.BLURRING)) {
      return ReportStatus.BLURRING;
    } else {
      return ReportStatus.NORMAL;
    }
  }

  private static Date findTheEarlyOne(Date d1, Date d2) {

    if (Objects.isNull(d1)) {
      return d2;
    }
    if (Objects.isNull(d2)) {
      return d1;
    }
    return d1.getTime() >= d2.getTime() ? d2 : d1;
  }


  public static List<ImageApiInfo> apis(List<ImageDetailInfo> images) {
    List<ImageApiInfo> imageApiInfoList = new ArrayList<>();
    for (ImageDetailInfo image : images) {
      imageApiInfoList.add(ImageFactory.api(image));
    }
    return imageApiInfoList;
  }

  public static ImageReviewInfoRequest of(ImageReviewApiRequest request) {

    ImageReviewInfoRequest reviewInfoRequest = new ImageReviewInfoRequest();

    if (!StringUtils.isEmpty(request.getImageId())) {
      reviewInfoRequest.setImageId(HashUtils.decode(request.getImageId()));
    }
    if (request.getType() != null) {
      reviewInfoRequest.setType(ImageType.valueOf(request.getType().name()));
    }
    ReportInfo reportInfo = new ReportInfo();
    if (request.getStatus() != null) {
      reportInfo.setStatus(ReportStatus.valueOf(request.getStatus().name()));
    }
    String patientId = request.getPatientId();
    if (StringUtils.isNotEmpty(patientId)) {
      reviewInfoRequest.setPatientId(HashUtils.decode(patientId));
    }
    reportInfo.setReportAt(request.getReportAt());
    reviewInfoRequest.setReportInfo(reportInfo);
    return reviewInfoRequest;
  }

  public static DepartmentImagesApiInfo api(DepartmentImagesInfo departmentImagesInfo) {
    DepartmentImagesApiInfo apiInfo = new DepartmentImagesApiInfo();
    BeanUtils.copyProperties(departmentImagesInfo, apiInfo);
    return apiInfo;
  }

  public static StatisticsImagesApiResponse api(StatisticsImagesResponse response) {
    if (Objects.isNull(response)) {
      logger.warn("statistics images response is null :{}", response);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    }
    List<DepartmentImagesApiInfo> apiInfos = response.getDepartmentImagesInfos().stream()
        .map(ImageFactory::api).collect(
            Collectors.toList());
    return new StatisticsImagesApiResponse(apiInfos);
  }
}
