package cn.xinzhili.infra.util;

import cn.xinzhili.user.api.StaffAuthority;
import cn.xinzhili.user.api.StaffRole;
import cn.xinzhili.user.api.response.StaffAuthResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 13/02/2017
 * Time: 11:35 AM
 *
 * @author Gan Dong
 */
public class AuthUtils {

  private static final String ROLE_PREFIX = "ROLE_";

  /**
   * for the given StaffAuthResponse, extracts authorities from the given input
   * response
   *
   * @param authResponse the given StaffAuthResponse to extract authorities
   *                     from
   * @return a list of authorities, as well as roles
   */
  public static List<GrantedAuthority> extractAuthorities(
      StaffAuthResponse authResponse) {
    List<GrantedAuthority> authorities = new ArrayList<>();
    for (StaffRole role : authResponse.getRoles()) {
      authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + role.getCode()));
    }
    for (StaffAuthority authority : authResponse.getAuthorities()) {
      authorities.add(new SimpleGrantedAuthority(authority.getCode()));
    }
    return authorities;
  }
}
