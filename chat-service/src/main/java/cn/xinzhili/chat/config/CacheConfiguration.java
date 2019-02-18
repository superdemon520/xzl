package cn.xinzhili.chat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class CacheConfiguration {

  @Bean
  public ConversionService conversionService() {
    return new DefaultConversionService();
  }


  @Bean
  public RedisTemplate<String, ?> redisStringTemplate(RedisConnectionFactory connectionFactory) {
    RedisSerializer<String> stringSerializer = new StringRedisSerializer();
    RedisTemplate<String, ?> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(connectionFactory);
    redisTemplate.setKeySerializer(stringSerializer);
    redisTemplate.setHashKeySerializer(stringSerializer);
    return redisTemplate;
  }

}
