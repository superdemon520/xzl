package cn.xinzhili.api.doctor.support;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ywb on 12/6/2017.
 */
@Configuration
public class Config {

  @Bean
  public MLComponent medicalLogicComponent(){
    MLComponent mlComponent = new MLComponent();
    mlComponent.setQueueName("medical_logic");
    return mlComponent;
  }
}
