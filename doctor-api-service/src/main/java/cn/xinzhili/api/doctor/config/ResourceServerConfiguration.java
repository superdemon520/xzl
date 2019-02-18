package cn.xinzhili.api.doctor.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * Date: 16/7/7 Time: PM4:05
 *
 * @author gan
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends
    ResourceServerConfigurerAdapter {

  @Autowired
  TokenStore tokenStore;

  @Autowired
  JwtAccessTokenConverter tokenConverter;

  @Override
  public void configure(HttpSecurity http) throws Exception {
    //@formatter:off
    http
        .csrf().disable()
        .authorizeRequests()
        .antMatchers("/docs/**").permitAll()
        .antMatchers("/hystrix/**").permitAll()
        .antMatchers("/hystrix.stream/**").permitAll()
        .antMatchers("/info").permitAll()
        .antMatchers("/health").permitAll()
        .antMatchers("/metrics").permitAll()
        .antMatchers(HttpMethod.POST, "/user/verification").permitAll()
        .antMatchers(HttpMethod.POST, "/user").permitAll()
        .antMatchers(HttpMethod.POST,"/user/password").permitAll()
//        .antMatchers(HttpMethod.GET, "/**").hasAuthority("DOCTOR_READ")
//        .antMatchers(HttpMethod.POST, "/**").hasAuthority("DOCTOR_WRITE")
        .antMatchers(HttpMethod.GET, "/user/patient").hasAuthority("PATIENT_READ")
        .antMatchers(HttpMethod.POST, "/user/patient").hasAuthority("PATIENT_WRITE")
        .anyRequest().authenticated();
    //@formatter:on
  }

  @Override
  public void configure(ResourceServerSecurityConfigurer resources)
      throws Exception {
    resources.resourceId("doctor_web").tokenStore(tokenStore);
  }
}
