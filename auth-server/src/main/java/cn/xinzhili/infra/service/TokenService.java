package cn.xinzhili.infra.service;

import static cn.xinzhili.infra.CommonConsts.DEVICE_TOKEN_KEY;

import cn.xinzhili.infra.CommonConsts;
import cn.xinzhili.xutils.auth.AuthenticationUtils;
import cn.xinzhili.xutils.auth.EnhancedRedisTokenStore;
import cn.xinzhili.xutils.auth.UidToken;
import cn.xinzhili.xutils.core.http.Response;
import cn.xinzhili.xutils.core.http.ResponseStatus;
import java.util.Objects;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

/**
 * Date: 24/02/2017
 * Time: 1:48 PM
 *
 * @author Gan Dong
 */
@Service
public class TokenService {

  private static final Logger logger = LoggerFactory
      .getLogger(TokenService.class);

  @Autowired
  private NotifyService notifyService;

  @Autowired
  private TokenStore tokenStore;

  public void invalidateExistingClients(String username, String currentDeviceToken) {
    // revoke previous access tokens for this username
    tokenStore
        .findTokensByClientIdAndUserName(CommonConsts.CLIENT_PATIENT, username)
        .forEach(tokenStore::removeAccessToken);

    // revoke previous refresh tokens for this username
    if (tokenStore instanceof EnhancedRedisTokenStore) {
      EnhancedRedisTokenStore redisTokenStore = (EnhancedRedisTokenStore) tokenStore;
      redisTokenStore
          .findRefreshTokensByClientIdAndUserName(CommonConsts.CLIENT_PATIENT,
              username)
          .forEach(refreshToken -> {
            OAuth2Authentication auth = redisTokenStore
                .readAuthenticationForRefreshToken(refreshToken);
            if (auth != null) {
              final String deviceToken = AuthenticationUtils
                  .getDetailsValue(auth.getUserAuthentication(), DEVICE_TOKEN_KEY);
              if (!Objects.equals(deviceToken, currentDeviceToken)) {
                final boolean notifyUser = true;
                // clients logged in on other devices need to be notified
                unbindUserDevice(auth.getUserAuthentication(), notifyUser);
              }
            }
            redisTokenStore.removeRefreshToken(refreshToken);
          });
    }
    // TODO publish a logout event to unbind all devices for this username
    // add it when clients can handle such events to logout
  }

  /**
   * 吊销用户的刷新token，用于用户主动退出客户端。
   *
   * @param tokenValue refresh token的内容
   */
  public void revokeRefreshToken(String tokenValue) {
    OAuth2RefreshToken refreshToken = tokenStore.readRefreshToken(tokenValue);

    if (refreshToken != null) {
      OAuth2Authentication authentication = tokenStore
          .readAuthenticationForRefreshToken(refreshToken);
      tokenStore.removeAccessTokenUsingRefreshToken(refreshToken);
      tokenStore.removeRefreshToken(refreshToken);

      Authentication userAuthentication = authentication
          .getUserAuthentication();
      final boolean notifyUser = false;
      // user initiated the revoke, no need to notify back again
      unbindUserDevice(userAuthentication, notifyUser);
    }
  }

  private void unbindUserDevice(Authentication authentication,
      boolean notifyUser) {
    String deviceToken = AuthenticationUtils
        .getDetailsValue(authentication, DEVICE_TOKEN_KEY);
    if (StringUtils.isEmpty(deviceToken)) {
      // no-op
      return;
    }
    // we only want to unbind when it has uid in the token
    if (authentication instanceof UidToken) {
      UidToken uidToken = (UidToken) authentication;
      long uid = uidToken.getUid();
      if (uid == 0L) {
        // no-op
        return;
      }
      // FIXME - only support unbinding patient app for now
      String subscriber = "p" + uid;
      if (notifyUser) {
        notifyService.notifyUserToLogout(subscriber, deviceToken);
      }
      Response unbindResp = notifyService.unbindDevice(deviceToken, subscriber);
      if (unbindResp.getStatus() != ResponseStatus.SUCCESS) {
        logger.error("解绑用户设备失败: {}, 请求结果: {}", uid, unbindResp);
        throw new BadCredentialsException("Unbinding device failed.");
      }
    }
  }
}
