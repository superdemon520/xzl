package cn.xinzhili.api.doctor.common;

import cn.xinzhili.xutils.auth.CurrentUserId;
import cn.xinzhili.xutils.auth.CurrentUsername;
import org.mockito.Mockito;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Collection;
import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;

/**
 * Utility to mock a context that has username and uid in a controller test.
 * <p/>
 * Usage:
 * <pre>
 *  private ContextMock contextMock = new ContextMock();
 *
 *  {@literal @}Before
 *  public void setUp() {
 *    this.mockMvc = MockMvcBuilders
 *        .standaloneSetup(this.userController)
 *        .setCustomArgumentResolvers(
 *            new AuthenticationPrincipalArgumentResolver(),
 *            contextMock.getMockUidResolver())
 *        .build();
 *  }
 *
 *  {@literal @}Test
 *  public void testCurrentUsername() {
 *     this.contextMock.withUsername("user42");
 *     // here goes this.mockMvc.perform()
 *  }
 *
 *  {@literal @}Test
 *  public void testCurrentUid() {
 *     this.contextMock.withUid(10086L);
 *     // here goes this.mockMvc.perform()
 *  }
 * </pre>
 * <p/>
 * <p/>
 * Date: 16/8/19 Time: AM11:59
 *
 * @author Gan Dong
 */
public class ContextMock {

  private Authentication authentication;

  private AuthenticationMockArgumentResolver resolver;

  public ContextMock() {
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    authentication = Mockito.mock(Authentication.class);
    given(securityContext.getAuthentication()).willReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    resolver = new AuthenticationMockArgumentResolver();
    resolver.setAuthentication(authentication);
  }

  public ContextMock withUsername(String username) {
    given(authentication.getPrincipal()).willReturn(username);
    return this;
  }

  public ContextMock withRole(String role) {
    Collection<? extends GrantedAuthority> authorities = Collections
        .singleton(new SimpleGrantedAuthority(role));
    doReturn(authorities).when(authentication).getAuthorities();
    return this;
  }

  public ContextMock withUid(long uid) {
    resolver.setUserId(uid);
    return this;
  }

  /**
   * Used to configure standalone MockMvcBuilder: <code>MockMvcBuilders#setCustomArgumentResolvers()</code>
   *
   * @return a mock resolver that is able to resolve the current uid as the one
   * passed in
   */
  public HandlerMethodArgumentResolver getMockUidResolver() {
    return this.resolver;
  }

  /**
   * custom argument resolver. Currently supports 1) {@literal @}CurrentUserId,
   * 2) {@literal @}CurrentUsername and 3) Authentication
   */
  static class AuthenticationMockArgumentResolver
      implements HandlerMethodArgumentResolver {

    private long userId;

    private Authentication authentication;

    public void setUserId(long userId) {
      this.userId = userId;
    }

    public void setAuthentication(
        Authentication authentication) {
      this.authentication = authentication;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
      return true;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
        ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory) throws Exception {
      if (parameter.hasParameterAnnotation(CurrentUserId.class) ||
          parameter.hasParameterAnnotation(
              CurrentUsername.class)) {
        return userId;
      } else if (parameter.getParameterType().equals(Authentication.class)) {
        // FIXME doesn't seem to work
        return authentication;
      }
      return null;
    }
  }
}
