package cn.xinzhili.api.doctor.util;

import cn.xinzhili.api.doctor.bean.request.SymptomApiRequest;
import cn.xinzhili.api.doctor.bean.response.SymptomApiResponse;
import cn.xinzhili.medical.api.SymptomInfo;
import cn.xinzhili.medical.api.request.SymptomRequest;
import cn.xinzhili.xutils.core.util.HashUtils;
import org.springframework.beans.BeanUtils;

/**
 * @author by Loki on 17/4/20.
 */
public class SymptomFactory {

  public static SymptomRequest of(SymptomApiRequest request) {

    SymptomRequest symptomRequest = new SymptomRequest();
    BeanUtils.copyProperties(request,symptomRequest);
    symptomRequest.setPatientId(HashUtils.decode(request.getPatientId()));

    return symptomRequest;
  }


  public static SymptomApiResponse api(SymptomInfo symptomInfo) {
    SymptomApiResponse response = new SymptomApiResponse();
    BeanUtils.copyProperties(symptomInfo,response);
    return response;
  }
}
