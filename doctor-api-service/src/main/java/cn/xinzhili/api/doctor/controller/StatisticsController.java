package cn.xinzhili.api.doctor.controller;

import cn.xinzhili.api.doctor.bean.StatisticAdviceDateType;
import cn.xinzhili.api.doctor.bean.response.StatisticsImagesApiResponse;
import cn.xinzhili.api.doctor.bean.response.StatisticsAdvicesApiResponse;
import cn.xinzhili.api.doctor.bean.response.StatisticsPatientsApiResponse;
import cn.xinzhili.api.doctor.bean.response.StatisticsStandardApiResponse;
import cn.xinzhili.api.doctor.service.DpcService;
import cn.xinzhili.api.doctor.service.MedicalService;
import cn.xinzhili.api.doctor.service.UserService;
import cn.xinzhili.xutils.auth.CurrentUserId;
import cn.xinzhili.xutils.core.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/4/10 下午4:10
 */
@RestController
@RequestMapping("/statistics")
public class StatisticsController {

  @Autowired
  private UserService userService;

  @Autowired
  private MedicalService medicalService;

  @Autowired
  private DpcService dpcService;

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/department/patients")
  public Response getPatients(@CurrentUserId Long uid,
      @RequestParam(value = "organizationId") String organizationId) {
    StatisticsPatientsApiResponse response = userService.getStatisticsPatients(organizationId, uid);
    return Response.instanceSuccess(response);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/department/images")
  public Response getDepartmentImages(
      @RequestParam(value = "organizationId") String organizationId) {
    StatisticsImagesApiResponse response = medicalService.getDepartmentImages(organizationId);
    return Response.instanceSuccess(response);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/department/standard/rate")
  public Response getDepartmentStandardRates(
      @RequestParam(value = "organizationId") String organizationId) {
    StatisticsStandardApiResponse response = medicalService
        .getDepartmentStandardRates(organizationId);
    return Response.instanceSuccess(response);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/department/workload")
  public Response getDoctorWorkload(@RequestParam(value = "organizationId") String organizationId,
      @RequestParam(value = "dateType", defaultValue = "ONE_MOUTH") StatisticAdviceDateType dateType) {
    StatisticsAdvicesApiResponse response = dpcService
        .getDepartmentAdvices(organizationId, dateType);
    return Response.instanceSuccess(response);
  }

}
