package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.InspectionChartApiType;
import cn.xinzhili.medical.api.StandardDataInfo;
import java.util.List;
import lombok.Data;

@Data
public class StandardDataApiResponse {
  private InspectionChartApiType type;

  private List<StandardDataInfo> series;
}
