package cn.xinzhili.api.doctor.client;

import cn.xinzhili.api.doctor.bean.response.RecordResponse;
import cn.xinzhili.mas.api.response.PlanListResponse;
import cn.xinzhili.mas.api.response.vis.VisRecordLineResponse;
import cn.xinzhili.xutils.core.http.Response;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author by Loki on 17/3/6.
 */
@FeignClient("mas-service")
public interface MasServiceClient {

  /**
   * 获取患者当前正在使用的服药计划
   *
   * @param userId 患者id
   * @return 返回list
   */
  @RequestMapping(value = "/plan", method = RequestMethod.GET,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  Response<PlanListResponse> searchPatientPlans(@RequestParam("userId") Long userId,
      @RequestParam(value = "since", required = false) Long since,
      @RequestParam(value = "until", required = false) Long until);

  /**
   * 获取服药计划时间轴，返回的实体bean 不一样
   *
   * @param userId 患者id
   * @param since 起始时间
   * @param until 结束时间
   * @return 返回时间轴的bean
   */
  @RequestMapping(value = "/medication/timeline/vis", method = RequestMethod.GET,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  Response<VisRecordLineResponse> getPatientRecordLine(
      @RequestParam("userId") long userId
      , @RequestParam("since") long since
      , @RequestParam("until") long until);


  /**
   * 获取用户的服药记录
   *
   * @param userId 用户id
   */
  @RequestMapping(method = RequestMethod.GET,
      value = "/records")
  Response<RecordResponse> getPatientMRecords(
      @RequestParam("userId") long userId,
      @RequestParam("start") long start,
      @RequestParam("end") long end);

}
