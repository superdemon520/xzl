package cn.xinzhili.api.doctor.service;

import cn.xinzhili.api.doctor.client.MedicalServiceClient;
import cn.xinzhili.medical.api.LifeStatusInfo;
import cn.xinzhili.medical.api.response.LifeStatusResponse;
import cn.xinzhili.xutils.core.ErrorCode;
import cn.xinzhili.xutils.core.SystemException;
import cn.xinzhili.xutils.core.http.Response;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ywb on 17/4/2017.
 */
@Service
public class LifeStatusService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private MedicalServiceClient medicalServiceClient;

  public List<LifeStatusInfo> getLatestLifeStatusByPatientid(Long patientId) {
    Response<LifeStatusResponse> response = medicalServiceClient.getLatestLifeStatusByPatientid(patientId);
    if (!response.isSuccessful()) {
      logger.error("call medical-service fail ! patient->{} ,response : {}", patientId, response);
      throw new SystemException(ErrorCode.REQUEST_FAILED);
    }
    LifeStatusResponse lifeStatusResponse = response.getDataAs(LifeStatusResponse.class);
    return lifeStatusResponse.getRecords();
  }

}