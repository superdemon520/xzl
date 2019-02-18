package cn.xinzhili.api.doctor.support;

import cn.xinzhili.api.doctor.service.MedicalService;
import cn.xinzhili.medical.api.LogicalRuleType;
import cn.xinzhili.medical.api.request.MLRequest;
import cn.xinzhili.xutils.core.ErrorCode;
import cn.xinzhili.xutils.core.FailureException;
import cn.xinzhili.xutils.spring.queue.QueueConsumer;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by ywb on 8/6/2017.
 */

public class MLComponent extends QueueConsumer<MLRequest> {

  private static final Logger logger = LoggerFactory.getLogger(MLComponent.class);


  @Autowired
  private MedicalService medicalService;


  @Override
  protected void handle(MLRequest request) {
    medicalService.triggerML(request);
    logger.info("ML handle complete :request->{}",request);
  }


  private static MLRequest buildMlRequest(Long patientId) {

    return MLRequest.builder().patientId(patientId).logicalRuleTypes(
        Lists.newArrayList(LogicalRuleType.RECHECK_CORONARY, LogicalRuleType.DRUG_DISEASE)).build();
  }


  public void triggerML(Long patientId) {
    if (patientId == null) {
      throw new FailureException(ErrorCode.INVALID_PARAMS);
    }

    MLRequest request = buildMlRequest(patientId);
    try {
      queue.put(request);
      logger.info("request->{}",request);
    } catch (InterruptedException e) {
      logger.error("医学逻辑任务入队失败 {}", e);
    }
  }


}
