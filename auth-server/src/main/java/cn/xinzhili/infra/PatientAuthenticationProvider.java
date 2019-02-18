package cn.xinzhili.infra;

import static cn.xinzhili.infra.CommonConsts.DEVICE_TOKEN_KEY;
import static cn.xinzhili.infra.CommonConsts.DEVICE_TYPE_KEY;

import cn.xinzhili.infra.client.UserServiceClient;
import cn.xinzhili.infra.service.NotifyService;
import cn.xinzhili.infra.service.TokenService;
import cn.xinzhili.infra.service.UserService;
import cn.xinzhili.xutils.auth.AuthenticationUtils;
import cn.xinzhili.xutils.auth.UidToken;
import cn.xinzhili.xutils.core.http.Response;
import cn.xinzhili.xutils.core.http.ResponseStatus;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Date: 16/7/18 Time: PM7:08
 *
 * @author gan
 */
@Component
public class PatientAuthenticationProvider implements AuthenticationProvider {

  private final Logger logger = LoggerFactory
      .getLogger(PatientAuthenticationProvider.class);

  @Autowired
  private UserService userService;

  @Autowired
  private NotifyService notifyService;

  @Autowired
  private TokenService tokenService;

  @Override
  public Authentication authenticate(Authentication authentication)
      throws AuthenticationException {
    logger.debug(
        "==== Authenticating using PatientAuthenticationProvider: " +
            authentication);

    Response response = userService
        .authenticatePatient(String.valueOf(authentication.getPrincipal()),
            String.valueOf(authentication.getCredentials()));

    logger.debug("User authentication response: {}", response);

    if (response.getStatus() == ResponseStatus.SUCCESS) {
      tokenService.invalidateExistingClients(
          String.valueOf(authentication.getPrincipal()),
          AuthenticationUtils.getDetailsValue(authentication, DEVICE_TOKEN_KEY));
      return decorateAuthentication(authentication, response);
    } else if (response.getStatus() == ResponseStatus.FAIL) {
      throw new BadCredentialsException("Authentication failed.");
    } else {
      throw new BadCredentialsException("Server error");
    }
  }

  private Authentication decorateAuthentication(Authentication authentication,
      Response response) {
    long uid = getUidFromResponse(response);
    if (uid != 0) {
      bindUserDevice(authentication, uid);
    }

    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(CommonConsts.PATIENT_READ));
    authorities.add(new SimpleGrantedAuthority(CommonConsts.PATIENT_WRITE));
    UidToken token = new PatientUsernamePasswordAuthenticationToken(
        authentication.getPrincipal(), authentication.getCredentials(),
        authorities);
    token.setUid(uid);
    return token;
  }

  // TODO extract to DeviceService
  private void bindUserDevice(Authentication authentication, long uid) {
    String deviceToken = AuthenticationUtils
        .getDetailsValue(authentication, DEVICE_TOKEN_KEY);
    if (StringUtils.isEmpty(deviceToken)) {
      throw new BadCredentialsException("device_token is required");
    }
    String deviceType = AuthenticationUtils
        .getDetailsValue(authentication, DEVICE_TYPE_KEY);
    if (StringUtils.isEmpty(deviceType)) {
      throw new BadCredentialsException("device_type is required");
    }
    if (!deviceType.equalsIgnoreCase("ios") &&
        !deviceType.equalsIgnoreCase("android")) {
      throw new BadCredentialsException(
          "device_type must be either ios or android");
    }
    String subscriber = "p" + uid;
    Response bindResp = notifyService
        .bindDevice(deviceToken, deviceType, subscriber);
    if (bindResp.getStatus() != ResponseStatus.SUCCESS) {
      logger.error("绑定用户设备失败: {}, 请求结果: {}", uid, bindResp);
      throw new BadCredentialsException("Binding device failed.");
    }
  }

  private long getUidFromResponse(Response response) {
    long value = 0L;
    @SuppressWarnings("unchecked")
    Map<String, Object> data = response.getData();

//      if (data != null && data.containsKey("user")) {
//        Object userInfo = data.get("user");
//        if (userInfo instanceof Map) {
//          Object obj = ((Map) userInfo).get("id");
//          if (obj != null) {
//            token.setUid(Long.parseLong(obj.toString()));
//          }
//        }
//      }
    if (data != null && data.containsKey(UserServiceClient.PATIENT_UID_FIELD)) {
      Object userInfo = data.get(UserServiceClient.PATIENT_UID_FIELD);
      value = Long.parseLong(String.valueOf(userInfo));
    }
    return value;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    logger.info("Checking if patient authentication is applicable");
    return PatientUsernamePasswordAuthenticationToken.class
        .isAssignableFrom(authentication);
  }
}
