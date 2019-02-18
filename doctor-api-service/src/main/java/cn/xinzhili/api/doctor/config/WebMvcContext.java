
package cn.xinzhili.api.doctor.config;

import cn.xinzhili.xutils.auth.ContextUidArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * Date: 08/10/2016
 * Time: 1:40 PM
 *
 * @author Gan Dong
 */
@Configuration
public class WebMvcContext extends WebMvcConfigurerAdapter {

  @Override
  public void addArgumentResolvers(
      List<HandlerMethodArgumentResolver> argumentResolvers) {
    argumentResolvers.add(new ContextUidArgumentResolver());
  }
}
