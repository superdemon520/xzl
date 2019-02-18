package cn.xinzhili.api.doctor.controller;

import cn.xinzhili.api.doctor.bean.response.ManualInfoResponse;
import cn.xinzhili.api.doctor.bean.response.MedicineExternalDataResponse;
import cn.xinzhili.api.doctor.service.MedicineService;
import cn.xinzhili.medicine.api.ManualInfo;
import cn.xinzhili.xutils.core.http.Response;
import cn.xinzhili.xutils.core.util.HashUtils;
import java.util.Optional;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by @xin.
 */
@RestController
@RequestMapping("/medicine")
@Validated
public class MedicineController {

  @Autowired
  private MedicineService medicineService;

  @GetMapping(value = "/{name}")
  public Response<MedicineExternalDataResponse> getMedicineByKey(
      @PathVariable String name) {
    return medicineService.getMedicineByKey(name);
  }

  @GetMapping(value = "/manual/{medicineId}")
  public Response<ManualInfoResponse> getManualByMedicineIds(
      @PathVariable("medicineId") String medicineId) {
    if (StringUtils.isEmpty(medicineId) || HashUtils.decode(medicineId) == 0) {
      return Response.instanceSuccess(ManualInfoResponse.newBuilder().build());
    }
    Optional<ManualInfo> manualInfo = medicineService.getManualByMedicineId(medicineId);
    return Response
        .instanceSuccess(
            ManualInfoResponse.newBuilder().withManualInfo(manualInfo.orElse(null)).build());
  }
}