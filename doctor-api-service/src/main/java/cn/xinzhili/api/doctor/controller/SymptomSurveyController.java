package cn.xinzhili.api.doctor.controller;

import cn.xinzhili.api.doctor.service.SymptomSurveyService;
import cn.xinzhili.medicine.api.response.SymptomSurveyListResponse;
import cn.xinzhili.xutils.core.http.Response;
import cn.xinzhili.xutils.core.util.HashUtils;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ywb on 12/9/2017.
 */
@RestController
public class SymptomSurveyController {

  private static final Logger logger = LoggerFactory.getLogger("SymptomSurveyController");

  @Autowired
  private SymptomSurveyService symptomSurveyService;

  @GetMapping("/patient/{patientId}/symptom/survey")
  public Response getSymptomSurvey(@PathVariable String patientId,
      @RequestParam(value = "ids", required = false) List<Long> ids) {
    Long patientID = HashUtils.decode(patientId);
    logger.info("add symptom survey,patientId->{},request->{}", patientId, ids);
    SymptomSurveyListResponse response = symptomSurveyService
        .find(patientID, ids);
    return Response.instanceSuccess(response);
  }


}
