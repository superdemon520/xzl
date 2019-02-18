package cn.xinzhili.chat.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;

@Configuration
@Getter
public class MqttConfiguration {

  @Value("${cn.xinzhili.mqtt.serverUri:tcp://mqtt.dev.xinzhili.cn:1883}")
  private String serverUri;

  @Value("${cn.xinzhili.mqtt.chat.clientId.publisher: backend-chat-publisher-local}")
  private String publisherClientId;

  @Bean
  public DefaultMqttPahoClientFactory mqttClientFactory() {
    DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
    factory.setServerURIs(serverUri.split(","));
    return factory;
  }


  @Bean
  public MqttPahoMessageHandler mqttOutbound() {
    MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(publisherClientId,
        mqttClientFactory());
    messageHandler.setAsync(true);
    return messageHandler;
  }


}
