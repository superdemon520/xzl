package cn.xinzhili.api.doctor.util;

import cn.xinzhili.api.doctor.bean.ConsultationInfo;
import cn.xinzhili.api.doctor.bean.ConsultationPatientApiInfo;
import cn.xinzhili.api.doctor.bean.request.AddConsultationApiRequest;
import cn.xinzhili.api.doctor.bean.request.UpdateConsultationApiRequest;
import cn.xinzhili.api.doctor.bean.response.ConsultationPatientListApiResponse;
import cn.xinzhili.dpc.api.AddConsultationRequest;
import cn.xinzhili.dpc.api.UpdateConsultationRequest;
import cn.xinzhili.user.api.ConsultationPatientInfo;
import cn.xinzhili.user.api.RiskFactor;
import cn.xinzhili.user.api.response.ConsultationPatientListResponse;
import cn.xinzhili.xutils.core.util.HashUtils;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.BeanUtils;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/4/2 上午10:24
 */
public class ConsultationFactory {

  private static final ObjectMapper mapper = new ObjectMapper();

  public static AddConsultationRequest of(AddConsultationApiRequest apiRequest) {
    AddConsultationRequest request = new AddConsultationRequest();
    BeanUtils.copyProperties(apiRequest, request);
    request.setPatientId(HashUtils.decode(apiRequest.getPatientId()));
    request.setDoctorId(HashUtils.decode(apiRequest.getDoctorId()));
    request.setConsultationDoctorId(HashUtils.decode(apiRequest.getConsultationDoctorId()));
    return request;
  }

  public static UpdateConsultationRequest of(UpdateConsultationApiRequest apiRequest) {
    UpdateConsultationRequest request = new UpdateConsultationRequest();
    request.setId(HashUtils.decode(apiRequest.getId()));
    request.setPatientId(HashUtils.decode(apiRequest.getPatientId()));
    request.setAnswer(apiRequest.getAnswer());
    return request;
  }

  public static ConsultationInfo Map2Consultation(Map<String, Object> consultationMap) {
    if (consultationMap == null || consultationMap.isEmpty()) {
      return new ConsultationInfo();
    }
    ConsultationInfo consultationInfo = mapper
        .convertValue(consultationMap, ConsultationInfo.class);
    //hash
    consultationInfo.setId(HashUtils.encode(Long.valueOf(consultationInfo.getId())));
    consultationInfo.setDoctorId(HashUtils.encode(Long.valueOf(consultationInfo.getDoctorId())));
    consultationInfo.setPatientId(HashUtils.encode(Long.valueOf(consultationInfo.getPatientId())));
    consultationInfo.setConsultationDoctorId(
        HashUtils.encode(Long.valueOf(consultationInfo.getConsultationDoctorId())));
    return consultationInfo;
  }

  public static ConsultationPatientApiInfo api(ConsultationPatientInfo info) {
    ConsultationPatientApiInfo apiInfo = new ConsultationPatientApiInfo();
    BeanUtils.copyProperties(info, apiInfo);
    apiInfo.setAge(MiscUtils.birthday2Age(info.getBirthday()));
    apiInfo.setId(HashUtils.encode(info.getId()));
    List<RiskFactor> riskFactor = info.getRiskFactor();
    apiInfo.setRiskFactor(Objects.isNull(riskFactor) ? List.of() : riskFactor);
    return apiInfo;
  }

  public static ConsultationPatientListApiResponse api(ConsultationPatientListResponse response) {
    ConsultationPatientListApiResponse apiResponse = new ConsultationPatientListApiResponse();
    apiResponse.setTotal(response.getTotal());
    if (response.getTotal() > 0) {
      apiResponse.setPatients(
          response.getPatients().stream().map(t -> api(t)).collect(Collectors.toList()));
    }
    return apiResponse;
  }
}