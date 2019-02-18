package cn.xinzhili.infra.config;

import cn.xinzhili.infra.CommonConsts;
import cn.xinzhili.infra.CustomResourceOwnerPasswordTokenGranter;
import cn.xinzhili.infra.HybridUserDetailsService;
import cn.xinzhili.xutils.auth.EnhancedRedisTokenStore;
import cn.xinzhili.xutils.auth.UidAwareJwtAccessTokenEnhancer;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

/**
 * Date: 16/7/6 Time: PM3:53
 *
 * @author gan
 */
@Configuration
@RefreshScope
@EnableAuthorizationServer
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {

  @Autowired
  @Qualifier("authenticationManagerBean")
  private AuthenticationManager authenticationManager;

  @Value("${oauth.accessToken.expires.minutes:720}")
  private int accessTokenExpiresMinutes;

  @Value("${oauth.refreshToken.expires.hours:720}")
  private int refreshTokenExpiresHours;

  @Value("${oauth.keystore.jwt.secret}")
  private String keyStoreSecret;

  @Value("${oauth.keystore.jwt.keyPair.alias}")
  private String keyPairAlias;

  @Value("${oauth.keystore.jwt.keyPair.secret}")
  private String keyPairSecret;

  @Value("${patient.access.token.expires.time:-1}")
  private int patientAccessTokenExpiresTime;

  @Value("${patient.refresh.token.expires.time:-1}")
  private int patientRefreshTokenExpiresTime;

  @Autowired
  private HybridUserDetailsService userDetailsService;

  @Autowired
  private JedisConnectionFactory connectionFactory;

  @Override
  public void configure(ClientDetailsServiceConfigurer clients)
      throws Exception {

    // @formatter:off
    clients.inMemory()
        .withClient(CommonConsts.CLIENT_PATIENT)
        .autoApprove(true)
        .authorities(CommonConsts.PATIENT_READ, CommonConsts.PATIENT_WRITE)
        .authorizedGrantTypes("authorization_code", "refresh_token", "implicit",
            "password", "client_credentials")
        .scopes(CommonConsts.SCOPE_PATIENT)
        .accessTokenValiditySeconds(patientAccessTokenExpiresTime)
        .refreshTokenValiditySeconds(patientRefreshTokenExpiresTime)
        .and().withClient(CommonConsts.CLIENT_DOCTOR)
        .autoApprove(true)
        .authorities(CommonConsts.DOCTOR_READ, CommonConsts.DOCTOR_WRITE)
        .authorizedGrantTypes("authorization_code", "refresh_token", "implicit",
            "password", "client_credentials")
        .scopes(CommonConsts.SCOPE_DOCTOR)
        .accessTokenValiditySeconds(accessTokenExpiresMinutes * 60)
        .refreshTokenValiditySeconds(refreshTokenExpiresHours * 3600);
    // @formatter:on
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints)
      throws Exception {
    endpoints.pathMapping("/oauth/token", "/token")
        .pathMapping("/oauth/authorize", "/authorize")
        .pathMapping("/oauth/confirm_access", "/confirm_access")
        .pathMapping("/oauth/check_token", "/check_token")
        .pathMapping("/oauth/token_key", "/token_key");
    endpoints.tokenStore(tokenStore()).tokenEnhancer(jwtTokenEnhancer())
        .reuseRefreshTokens(false) // so that we always use newest refresh token
        .authenticationManager(authenticationManager)
        .userDetailsService(userDetailsService);

    List<TokenGranter> tokenGranters = new ArrayList<>();
    tokenGranters
        .add(new CustomResourceOwnerPasswordTokenGranter(authenticationManager,
            endpoints.getTokenServices(), endpoints.getClientDetailsService(),
            endpoints.getOAuth2RequestFactory()));
    tokenGranters.add(new RefreshTokenGranter(endpoints.getTokenServices(),
        endpoints.getClientDetailsService(),
        endpoints.getOAuth2RequestFactory()));
    endpoints.tokenGranter(new CompositeTokenGranter(tokenGranters));
  }

  @Override
  public void configure(AuthorizationServerSecurityConfigurer security)
      throws Exception {
  }

  @Bean
  public TokenStore tokenStore() {
    return new EnhancedRedisTokenStore(connectionFactory);
  }

  @Bean
  protected JwtAccessTokenConverter jwtTokenEnhancer() {
    KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(
        new ClassPathResource("jwt.jks"), keyStoreSecret.toCharArray());
    JwtAccessTokenConverter converter = new UidAwareJwtAccessTokenEnhancer();
    converter.setKeyPair(keyStoreKeyFactory.getKeyPair(keyPairAlias, keyPairSecret.toCharArray()));
    return converter;
  }

  @Bean
  public ResourceBundleMessageSource source() {
    ResourceBundleMessageSource source = new ResourceBundleMessageSource();
    source.setBasenames("messages.properties");
    return source;
  }
}
