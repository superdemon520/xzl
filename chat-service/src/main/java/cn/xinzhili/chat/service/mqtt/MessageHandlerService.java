package cn.xinzhili.chat.service.mqtt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
public class MessageHandlerService {

  @Autowired
  private MqttPahoMessageHandler publisher;

  public void publishMessage(String topic, String message) {
    Message<String> result = MessageBuilder.withPayload(message).setHeader(MqttHeaders.TOPIC, topic)
        .build();
    publisher.handleMessage(result);
  }


}
