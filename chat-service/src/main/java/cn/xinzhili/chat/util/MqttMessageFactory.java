package cn.xinzhili.chat.util;

import cn.xinzhili.chat.api.request.AddMessageRequest;
import cn.xinzhili.chat.bean.MessageBean;
import cn.xinzhili.chat.bean.MessageData;
import cn.xinzhili.xutils.core.util.HashUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MqttMessageFactory {

  private static final ObjectMapper mapper = new ObjectMapper();

  public static String generateMqttMessage(AddMessageRequest request) {

    MessageBean messageBean = new MessageBean();
    messageBean.setAvatar(request.getAvatar());
    messageBean.setName(request.getName());
    messageBean.setSenderRoleType(request.getSenderRoleType());
    messageBean.setReceiver(
        Objects.isNull(request.getReceiverId()) ? null : HashUtils.encode(request.getReceiverId()));
    messageBean.setSessionId(HashUtils.encode(request.getSessionId()));
    messageBean.setSendTime(System.currentTimeMillis());
    MessageData messageData = new MessageData();
    messageData.setContent(request.getContent());
    messageData.setType(request.getMessageType());
    messageBean.setSender(HashUtils.encode(request.getSenderId()));
    messageBean.setMessage(messageData);
    try {
      String result = mapper.writeValueAsString(messageBean);
      log.info("发送内容为：" + result);
      return result;
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e.getCause());
    }
  }

}
