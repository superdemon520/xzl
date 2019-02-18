package cn.xinzhili.api.doctor.config;

import cn.xinzhili.xutils.auth.UidAwareJwtAccessTokenEnhancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;

/**
 * Date: 16/7/7 Time: PM3:40
 *
 * @author gan
 */
@Configuration
public class JwtConfiguration {

  @Autowired
  JwtAccessTokenConverter jwtAccessTokenConverter;

  @Bean
  @Qualifier("tokenStore")
  public TokenStore tokenStore() {
    return new JwtTokenStore(jwtAccessTokenConverter);
  }

  @Bean
  protected JwtAccessTokenConverter jwtTokenEnhancer(
      @Value("${oauth.jwt.publicKey.name}") String publicKeyName) {
    JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
    converter.setAccessTokenConverter(new UidAwareJwtAccessTokenEnhancer());
    Resource resource = new ClassPathResource(publicKeyName);
    String publicKey = null;
    try {
      publicKey = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    converter.setVerifierKey(publicKey);
    return converter;
  }
}
