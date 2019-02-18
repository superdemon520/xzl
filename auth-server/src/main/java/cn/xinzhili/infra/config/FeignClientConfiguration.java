package cn.xinzhili.infra.config;

import feign.Logger.Level;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Date: 03/04/2017
 * Time: 12:44 PM
 *
 * @author Gan Dong
 */
@Configuration
@RefreshScope
public class FeignClientConfiguration {

  @Value("${logging.level.feign:INFO}")
  private String feignLoggingLevel;

  @Bean
  Level feignLoggerLevel() {
    if ("DEBUG".equalsIgnoreCase(feignLoggingLevel)) {
      return Level.FULL;
    } else {
      return Level.NONE;
    }
  }

}
