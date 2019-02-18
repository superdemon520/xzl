package cn.xinzhili.infra.config;

import cn.xinzhili.infra.DoctorAuthenticationProvider;
import cn.xinzhili.infra.PatientAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.servlet.http.HttpServletResponse;

/**
 * Date: 16/7/7 Time: AM12:39
 *
 * @author gan
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private PatientAuthenticationProvider patientAuthenticationProvider;

  @Autowired
  private DoctorAuthenticationProvider doctorAuthenticationProvider;

  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // @formatter:off
    http
        .csrf().disable()
            .exceptionHandling()
            .authenticationEntryPoint((request, response, authException) -> response.sendError(
                HttpServletResponse.SC_UNAUTHORIZED))
        .and()
            .authorizeRequests()
            .antMatchers("/docs/**").permitAll()
            .antMatchers("/hystrix/**").permitAll()
            .antMatchers("/hystrix.stream/**").permitAll()
            .antMatchers("/info").permitAll()
            .antMatchers("/health").permitAll()
            .antMatchers("/metrics").permitAll()
            .antMatchers("/jolokia").permitAll()
            .antMatchers("/revoke").anonymous()
            .antMatchers("/**").authenticated()
        .and()
            .httpBasic();
    // @formatter:on
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    // @formatter:off
    auth.authenticationProvider(patientAuthenticationProvider)
        .authenticationProvider(doctorAuthenticationProvider)
        .inMemoryAuthentication()
            .withUser("18600010001")
                .password("000000")
                .authorities("PATIENT_READ")
        .and()
            .withUser("18600020002")
                .password("000000")
                .authorities("PATIENT_READ", "PATIENT_WRITE")
        .and()
            .withUser("13800010001")
                .password("000000")
                .authorities("DOCTOR_READ")
        .and()
            .withUser("13800020002")
                .password("000000")
                .authorities("DOCTOR_READ", "DOCTOR_WRITE");
    // @formatter:on
  }
}
