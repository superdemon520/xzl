package cn.xinzhili.chat.util;

import cn.xinzhili.chat.api.request.AddMessageRequest;
import cn.xinzhili.chat.api.response.TopicResponse;
import cn.xinzhili.chat.bean.TopicGenerateBean;
import cn.xinzhili.xutils.core.util.HashUtils;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TopicFactory {

  public static String generateMqttTopic(AddMessageRequest request, String env) {
    String result =
        "/" + env.trim() + "/" + HashUtils.encode(request.getOrganizationId()) + "/im/receive/"
            + HashUtils.encode(request.getSessionId());
    log.info("topic -------> " + result);
    return result;
  }
}
