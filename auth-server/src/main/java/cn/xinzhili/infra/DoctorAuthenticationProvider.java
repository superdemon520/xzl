package cn.xinzhili.infra;

import static cn.xinzhili.infra.CommonConsts.DEVICE_TOKEN_KEY;
import static cn.xinzhili.infra.CommonConsts.DEVICE_TYPE_KEY;

import cn.xinzhili.infra.service.NotifyService;
import cn.xinzhili.infra.service.UserService;
import cn.xinzhili.infra.util.AuthUtils;
import cn.xinzhili.user.api.response.StaffAuthResponse;
import cn.xinzhili.xutils.auth.AuthenticationUtils;
import cn.xinzhili.xutils.auth.UidToken;
import cn.xinzhili.xutils.core.SystemException;
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

/**
 * Date: 16/7/19 Time: PM1:04
 *
 * @author gan
 */
@Component
public class DoctorAuthenticationProvider implements AuthenticationProvider {

  private static final Logger logger = LoggerFactory
      .getLogger(DoctorAuthenticationProvider.class);

  @Autowired
  private UserService userService;

  @Autowired
  private NotifyService notifyService;

  @Override
  public Authentication authenticate(Authentication authentication)
      throws AuthenticationException {
    logger.debug(
        "==== Authenticating using ClinicAuthenticationProvider: " +
            authentication);
    if (authentication.getPrincipal() == null ||
        authentication.getCredentials() == null) {
      throw new BadCredentialsException("Username and password cannot be null");
    }
    Response<StaffAuthResponse> response = userService
        .authenticateDoctor(String.valueOf(authentication.getPrincipal()),
            String.valueOf(authentication.getCredentials()));

    logger.debug("User authentication response: {}", response);

    if (response.getStatus() == ResponseStatus.SUCCESS) {

      StaffAuthResponse authResponse = response
          .getDataAs(StaffAuthResponse.class);
      if (authResponse == null) {
        logger.error("response data is null or invalid: {}", response);
        throw new SystemException("Internal Error");
      }
      long uid = authResponse.getUserId();

      if (uid != 0) {
        mayBindUserDevice(authentication, uid);
      }

      List<GrantedAuthority> authorities = AuthUtils
          .extractAuthorities(authResponse);
      UidToken token = new DoctorUsernamePasswordAuthenticationToken(
          authentication.getPrincipal(), authentication.getCredentials(),
          authorities);
      token.setUid(uid);
      return token;
    } else if (response.getStatus() == ResponseStatus.FAIL) {
      throw new BadCredentialsException("Authentication failed.");
    } else {
      throw new BadCredentialsException("Server error");
    }
  }

  private void mayBindUserDevice(Authentication authentication, long uid) {
    String deviceToken = AuthenticationUtils
        .getDetailsValue(authentication, DEVICE_TOKEN_KEY);
    String deviceType = AuthenticationUtils
        .getDetailsValue(authentication, DEVICE_TYPE_KEY);
    if (StringUtils.isEmpty(deviceToken) || StringUtils.isEmpty(deviceType)) {
      if (logger.isDebugEnabled()) {
        logger.debug(
            "Not binding device because device information (device_token={}, device_type={}) is missing. ",
            deviceToken, deviceType);
      }
      return;
    }
    if (!deviceType.equalsIgnoreCase("ios") &&
        !deviceType.equalsIgnoreCase("android")) {
      throw new BadCredentialsException(
          "device_type must be either ios or android");
    }
    String subscriber = "d" + uid;
    Response bindResp = notifyService
        .bindDevice(deviceToken, deviceType, subscriber);
    if (bindResp.getStatus() != ResponseStatus.SUCCESS) {
      logger.error("绑定用户设备失败: {}, 请求结果: {}", uid, bindResp);
      throw new BadCredentialsException("Binding device failed.");
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    logger.info("Checking if clinic authentication is applicable");
    return DoctorUsernamePasswordAuthenticationToken.class
        .isAssignableFrom(authentication);
  }
}
