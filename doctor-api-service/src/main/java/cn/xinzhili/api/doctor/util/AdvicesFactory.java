package cn.xinzhili.api.doctor.util;

import cn.xinzhili.api.doctor.bean.DepartmentAdvicesApiInfo;
import cn.xinzhili.api.doctor.bean.response.StatisticsAdvicesApiResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/4/17 下午3:10
 */
public class AdvicesFactory {

  private static final ObjectMapper mapper = new ObjectMapper();

  public static StatisticsAdvicesApiResponse list2Response(
      List<Map<String, Object>> statisticsWorkload) {
    List<DepartmentAdvicesApiInfo> apiInfos = statisticsWorkload.stream()
        .map(AdvicesFactory::map2Api).collect(Collectors.toList());
    return new StatisticsAdvicesApiResponse(apiInfos);
  }

  public static DepartmentAdvicesApiInfo map2Api(Map map) {
    DepartmentAdvicesApiInfo apiInfo = mapper.convertValue(map, DepartmentAdvicesApiInfo.class);
    return apiInfo;
  }
}
