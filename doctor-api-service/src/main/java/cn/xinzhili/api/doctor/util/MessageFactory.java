package cn.xinzhili.api.doctor.util;

import cn.xinzhili.api.doctor.bean.DoctorApiMessage;
import cn.xinzhili.api.doctor.bean.MessageDirection;
import cn.xinzhili.api.doctor.service.DpcService.DoctorMessagePrefix;
import cn.xinzhili.xutils.core.ErrorCode;
import cn.xinzhili.xutils.core.FailureException;
import cn.xinzhili.xutils.core.util.HashUtils;
import java.io.IOException;
import java.util.Map;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author by Loki on 17/4/10.
 */
public class MessageFactory {

  private static final Logger logger = LoggerFactory.getLogger(MessageFactory.class);

  private static final String PATIENT_PRE_ID = "p_";

  private static final ObjectMapper mapper = new ObjectMapper();

  public static DoctorApiMessage Map2ApiMassage(Map messageMap) {

    if (messageMap == null || messageMap.isEmpty()) {
      throw new FailureException(ErrorCode.REQUEST_FAILED, "doctor chat message is null !");
    }
    DoctorApiMessage apiMessage = mapper.convertValue(messageMap, DoctorApiMessage.class);

    //get direction
    String sender = apiMessage.getSender();
    if (sender.startsWith(PATIENT_PRE_ID)) {
      apiMessage.setDirection(MessageDirection.INBOUND);
    } else {
      apiMessage.setDirection(MessageDirection.OUTBOUND);
    }

    //hash
    apiMessage.setId(HashUtils.encode(Long.valueOf(apiMessage.getId())));
    String receiver = DoctorMessagePrefix.removeFrom(apiMessage.getReceiver());
    apiMessage.setReceiver(HashUtils.encode(Long.valueOf(receiver)));
    sender = DoctorMessagePrefix.removeFrom(sender);
    apiMessage.setSender(HashUtils.encode(Long.valueOf(sender)));
    return apiMessage;
  }

  public static String jsonToString(Object o) {
    String jsonString = null;
    try {
      jsonString = mapper.writeValueAsString(o);
    } catch (IOException e) {
      logger.warn("对象{}转换字符串失败", o);
    }
    return jsonString;
  }

}
