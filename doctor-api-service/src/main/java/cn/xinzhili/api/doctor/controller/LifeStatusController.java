package cn.xinzhili.api.doctor.controller;

import cn.xinzhili.api.doctor.bean.response.LifeStatusApiResponse;
import cn.xinzhili.api.doctor.service.LifeStatusService;
import cn.xinzhili.api.doctor.util.LifeStatusFactory;
import cn.xinzhili.medical.api.LifeStatusInfo;
import cn.xinzhili.xutils.core.http.Response;
import cn.xinzhili.xutils.core.util.HashUtils;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ywb on 17/4/2017.
 */
@RestController
@RequestMapping("/life/status")
public class LifeStatusController {


  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private LifeStatusService lifeStatusService;

  @GetMapping(value = "/{patientId}")
  public Response<?> getLifeStatus(@PathVariable String patientId) {
    logger.info("patientId : {}",patientId);
    Long patientIdAfterDecoded = HashUtils.decode(patientId);
    List<LifeStatusInfo> records = lifeStatusService
        .getLatestLifeStatusByPatientid(patientIdAfterDecoded);
    LifeStatusApiResponse response = new LifeStatusApiResponse();
    response.setRecords(LifeStatusFactory.api(records));
    return Response.instanceSuccess(response);
  }

}
