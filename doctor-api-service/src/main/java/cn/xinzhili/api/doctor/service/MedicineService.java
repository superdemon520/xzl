package cn.xinzhili.api.doctor.service;

import cn.xinzhili.api.doctor.bean.MedicineInfo;
import cn.xinzhili.api.doctor.bean.response.MedicineExternalDataResponse;
import cn.xinzhili.api.doctor.bean.response.MedicineUnionDataResponse;
import cn.xinzhili.api.doctor.client.MedicineServiceClient;
import cn.xinzhili.api.doctor.util.MedicineFactory;
import cn.xinzhili.medicine.api.AdjustMedicineInfo;
import cn.xinzhili.medicine.api.ManualInfo;
import cn.xinzhili.medicine.api.response.AdjustMedicineListResponse;
import cn.xinzhili.medicine.api.response.ManualInfoListResponse;
import cn.xinzhili.xutils.core.ErrorCode;
import cn.xinzhili.xutils.core.FailureException;
import cn.xinzhili.xutils.core.http.Response;
import cn.xinzhili.xutils.core.util.HashUtils;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by @xin.
 */
@Service
public class MedicineService {

  private static final Logger logger = LoggerFactory.getLogger(MedicineService.class);

  private final MedicineServiceClient medicineServiceClient;

  @Autowired
  public MedicineService(MedicineServiceClient medicineServiceClient) {
    this.medicineServiceClient = medicineServiceClient;
  }

  public Response<MedicineExternalDataResponse> getMedicineByKey(String name) {
    Response<AdjustMedicineListResponse> response = medicineServiceClient.getAdjustByKey(name);
    if (!response.isSuccessful()) {
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    }
    AdjustMedicineListResponse data = response.getDataAs(AdjustMedicineListResponse.class);
    List<AdjustMedicineInfo> medicines = data.getMedicines();
    if (medicines == null) {
      return Response
          .instanceSuccess(MedicineExternalDataResponse.builder().medicines(null).build());
    }

    List<MedicineUnionDataResponse> apiMedicines = medicines.stream()
        .map(m -> MedicineUnionDataResponse.builder().id(HashUtils.encode(m.getId()))
            .name(m.getName()).build()).collect(Collectors.toList());

    return Response
        .instanceSuccess(MedicineExternalDataResponse.builder().medicines(apiMedicines).build());
  }

  public Optional<ManualInfo> getManualByMedicineId(String medicineId) {
    return medicineServiceClient
        .getManualByMedicineIds(Collections.singletonList(HashUtils.decode(medicineId)))
        .getDataAs(ManualInfoListResponse.class).getManualInfos().stream().findFirst();
  }

  public MedicineInfo getMedicineById(Long id) {
    Response response = medicineServiceClient.getMedicineById(id);
    if (response.isFailed()) {
      logger.warn("get medicine fail !, response: {}", response);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    } else if (response.isError()) {
      logger.error("get medicine error !, response: {}", response);
      throw new FailureException(ErrorCode.SERVER_ERROR);
    }
    @SuppressWarnings("unchecked")
    Map<String, Object> medicine =  (Map<String, Object>)response.getData()
        .get("medicine");
    return MedicineFactory.map2Medicine(medicine);
  }
}
