package cn.xinzhili.api.doctor.client;

import cn.xinzhili.api.doctor.bean.response.MedicineExternalResponse;
import cn.xinzhili.medicine.api.response.AdjustMedicineListResponse;
import cn.xinzhili.medicine.api.response.ManualInfoListResponse;
import cn.xinzhili.medicine.api.response.SymptomSurveyListResponse;
import cn.xinzhili.xutils.core.http.Response;
import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by @xin.
 */
@FeignClient(value = "medicine-service")
public interface MedicineServiceClient {

  @RequestMapping(value = "/external/medicine/{name}", method = RequestMethod.GET)
  Response<MedicineExternalResponse> getMedicineByKey(
      @PathVariable(value = "name") String name);

  @RequestMapping(value = "/patient/{patientId}/symptom/survey", method = RequestMethod.GET)
  Response<SymptomSurveyListResponse> findSymptomSurveys(@PathVariable("patientId") Long patientId,
      @RequestParam(value = "ids", required = false) List<Long> ids);

  @RequestMapping(value = "/manual", method = RequestMethod.GET)
  Response<ManualInfoListResponse> getManualByMedicineIds(
      @RequestParam("medicineIds") List<Long> medicineIds);

  @GetMapping("/medicine/adjust")
  Response<AdjustMedicineListResponse> getAdjustByKey(@RequestParam("name") String name);

  @GetMapping("/admin/medicine/{id}")
  Response getMedicineById(@PathVariable("id") Long id);

}
