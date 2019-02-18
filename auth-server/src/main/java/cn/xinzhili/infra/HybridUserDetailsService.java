package cn.xinzhili.infra;

import cn.xinzhili.infra.service.UserService;
import cn.xinzhili.infra.util.AuthUtils;
import cn.xinzhili.user.api.response.StaffAuthResponse;
import cn.xinzhili.xutils.core.http.Response;
import cn.xinzhili.xutils.core.http.ResponseStatus;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a workaround to Spring Security OAuth2 issue:
 * https://github.com/spring-projects/spring-security-oauth/issues/685
 * <p/>
 * It queries the underlying authentication service based on its client_id.
 * <p/>
 * Date: 16/8/2 Time: PM1:42
 *
 * @author Gan Dong
 */
@Service
public class HybridUserDetailsService implements UserDetailsService {

  private final Log logger = LogFactory.getLog(HybridUserDetailsService.class);

  @Autowired
  private UserService userService;

  @Override
  public UserDetails loadUserByUsername(String username)
      throws UsernameNotFoundException {
    if (username == null || username.isEmpty()) {
      throw new UsernameNotFoundException("Username is empty");
    }

    Authentication authentication = SecurityContextHolder.getContext()
        .getAuthentication();

    Object clientPrincipal = authentication.getPrincipal();

    if (clientPrincipal instanceof User) {
      if (CommonConsts.CLIENT_PATIENT
          .equalsIgnoreCase(((User) clientPrincipal).getUsername())) {
        // patient client
        UserDetails user = loadPatientUserDetails(username);
        if (user != null)
          return user;
      } else if (CommonConsts.CLIENT_DOCTOR
          .equalsIgnoreCase(((User) clientPrincipal).getUsername())) {
        // clinic client
        UserDetails user = loadDoctorUserDetails(username);
        if (user != null)
          return user;
      }
    }

    throw new UsernameNotFoundException(
        "Unauthorized client_id or username not found: " + username);
  }

  private UserDetails loadPatientUserDetails(String username) {
    Response response = userService.loadPatientUser(username);
    if (logger.isDebugEnabled())
      logger.debug("Loaded from patient details: " + response);
    if (response.getStatus() == ResponseStatus.SUCCESS) {
      List<GrantedAuthority> authorities = new ArrayList<>();

      authorities.add(new SimpleGrantedAuthority(CommonConsts.PATIENT_READ));
      authorities.add(new SimpleGrantedAuthority(CommonConsts.PATIENT_WRITE));
      return new User(username, "", authorities);
    }
    return null;
  }

  private UserDetails loadDoctorUserDetails(String username) {
    Response<StaffAuthResponse> response = userService.loadDoctorUser(username);
    if (logger.isDebugEnabled())
      logger.debug("Loaded from doctor details: " + response);
    if (response.getStatus() == ResponseStatus.SUCCESS) {
      StaffAuthResponse authResponse = response
          .getDataAs(StaffAuthResponse.class);
      if (authResponse == null) {
        return null;
      }
      return new User(username, "", AuthUtils.extractAuthorities(authResponse));
    }
    return null;
  }
}
