package cn.xinzhili.api.doctor.controller;

import cn.xinzhili.api.doctor.bean.PlanScope;
import cn.xinzhili.api.doctor.bean.response.PlanResponse;
import cn.xinzhili.api.doctor.bean.response.RecordListApiResponse;
import cn.xinzhili.api.doctor.error.UserErrorCode;
import cn.xinzhili.api.doctor.service.MasService;
import cn.xinzhili.api.doctor.service.UserService;
import cn.xinzhili.mas.api.response.vis.VisRecordLineResponse;
import cn.xinzhili.xutils.auth.CurrentUserId;
import cn.xinzhili.xutils.core.ErrorCode;
import cn.xinzhili.xutils.core.FailureException;
import cn.xinzhili.xutils.core.http.Response;
import cn.xinzhili.xutils.core.util.HashUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author by Loki on 17/3/6.
 */
@RestController
public class MasController {

  private static final Logger logger = LoggerFactory
      .getLogger(MasController.class);
  @Autowired
  private MasService masService;
  @Autowired
  private UserService userService;

  @PreAuthorize("hasAnyRole('DOCTOR','ASSISTANT')")
  @GetMapping(value = "/plan")
  public Response getPlanByPatientId(@CurrentUserId Long id,
      @RequestParam("patientId") String patientId,
      @RequestParam("scope") PlanScope scope) {

    boolean isBinding = userService.checkBindRelation(id, HashUtils.decode(patientId));
    if (!isBinding) {
      throw new FailureException(UserErrorCode.BINDING_RELATION_INVALID, "不合法的绑定关系");
    }
    PlanResponse latestPlan = masService.getPlanByPatientId(HashUtils.decode(patientId), scope);
    if (latestPlan == null) {
      logger.error("get patient plan fail !");
      throw new FailureException(ErrorCode.REQUEST_FAILED,
          "获取患者最近一条服药计划失败！");
    }
    return Response.instanceSuccess(latestPlan);
  }

  @PreAuthorize("hasAnyRole('DOCTOR','ASSISTANT')")
  @GetMapping(value = "/record")
  public Response getRecordByPatientId(@CurrentUserId Long id,
      @RequestParam("patientId") String patientId,
      @RequestParam(value = "end", required = false) Long end) {
    boolean isBinding = userService.checkBindRelation(
        id, HashUtils.decode(patientId));
    if (!isBinding) {
      throw new FailureException(UserErrorCode.BINDING_RELATION_INVALID,
          "不合法的绑定关系");
    }
    RecordListApiResponse records =
        masService.getRecordsByPatientId(HashUtils.decode(patientId), end);
    if (records == null) {
      logger.error("get patient records fail !");
      throw new FailureException(ErrorCode.REQUEST_FAILED,
          "获取患者服药纪录失败！");
    }
    return Response.instanceSuccess(records);
  }

  @PreAuthorize("hasAnyRole('DOCTOR','ASSISTANT')")
  @GetMapping(value = "/plan/timeline")
  public Response getPatientTimeLine(@CurrentUserId Long id
      , @RequestParam("patientId") String patientId
      , @RequestParam("start") long start
      , @RequestParam("end") long end) {
    long patientLongId = HashUtils.decode(patientId);
    boolean isBound = userService.checkBindRelation(
        id, patientLongId);
    if (!isBound) {
      logger.warn("没有绑定用户,id:{},patientId:{}", id, patientId);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    }
    VisRecordLineResponse result = masService
        .lineDataForFontEnd(patientLongId, start, end);
    return Response.instanceSuccess(result);

  }

}
