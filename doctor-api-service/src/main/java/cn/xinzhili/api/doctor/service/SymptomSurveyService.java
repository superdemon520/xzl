package cn.xinzhili.api.doctor.service;

import cn.xinzhili.api.doctor.client.MedicineServiceClient;
import cn.xinzhili.medicine.api.response.SymptomSurveyListResponse;
import cn.xinzhili.xutils.core.ErrorCode;
import cn.xinzhili.xutils.core.FailureException;
import cn.xinzhili.xutils.core.http.Response;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ywb on 12/9/2017.
 */
@Service
public class SymptomSurveyService {

  private static final Logger logger = LoggerFactory.getLogger("SymptomSurveyService");

  @Autowired
  private MedicineServiceClient medicineServiceClient;

  public SymptomSurveyListResponse find(Long patientId, List<Long> ids) {
    Response<SymptomSurveyListResponse> response = medicineServiceClient
        .findSymptomSurveys(patientId, ids);
    if(!response.isSuccessful()) {
      logger.warn("find fail,patientId->{},ids->{}",patientId,ids);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    }
    return response.getDataAs(SymptomSurveyListResponse.class);
  }
}
