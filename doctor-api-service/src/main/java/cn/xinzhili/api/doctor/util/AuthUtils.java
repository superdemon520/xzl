package cn.xinzhili.api.doctor.util;

import cn.xinzhili.api.doctor.bean.UserRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Date: 07/03/2017
 * Time: 11:31 PM
 *
 * @author Gan Dong
 */
public class AuthUtils {

  private static final String ROLE_PREFIX = "ROLE_";

  /**
   * Tests if the user underlying by the given Authentication is of the given
   * role.
   * <p>
   * Note it returns <code>FALSE</code> if the authentication is empty or is not
   * backed by a username (principal).
   *
   * @param role           the user role
   * @param authentication the authentication object
   * @return true if the underlying user is of the given role
   */
  public static boolean isUserOfRole(UserRole role,
      Authentication authentication) {

    if ((authentication == null) || (authentication.getPrincipal() == null)) {
      return false;
    }

    Collection<? extends GrantedAuthority> authorities = authentication
        .getAuthorities();

    if (authorities == null) {
      return false;
    }

    String roleName = ROLE_PREFIX + role.toString();
    for (GrantedAuthority grantedAuthority : authorities) {
      if (roleName.equals(grantedAuthority.getAuthority())) {
        return true;
      }
    }

    return false;
  }
}
