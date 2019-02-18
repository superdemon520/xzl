package cn.xinzhili.chat.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class AppConfiguration {

  @Value("${spring.cloud.config.profile:local}")
  private String env;

}
