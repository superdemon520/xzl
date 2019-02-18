package cn.xinzhili.infra.controller;

import cn.xinzhili.infra.service.NotifyService;
import cn.xinzhili.infra.service.TokenService;
import cn.xinzhili.xutils.core.ErrorCode;
import cn.xinzhili.xutils.core.http.Response;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Date: 07/02/2017
 * Time: 1:49 PM
 *
 * @author Gan Dong
 */
@RestController
public class LogoutController {

  private static final Logger logger = LoggerFactory
      .getLogger(LogoutController.class);

  @Autowired
  private NotifyService notifyService;

  @Autowired
  private TokenService tokenService;

  @PostMapping(value = "/revoke")
  public ResponseEntity<Response> revokeToken(HttpServletRequest request) {
    String authHeader = request.getHeader("Authorization");
    if (authHeader != null &&
        StringUtils.startsWithIgnoreCase(authHeader, "Bearer ")) {
      String tokenValue = StringUtils
          .removeStartIgnoreCase(authHeader, "Bearer ").trim();
      logger.info("About to revoke token: {}", tokenValue);

      tokenService.revokeRefreshToken(tokenValue);

      return ResponseEntity.ok(Response.instanceSuccess());
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(Response.instanceFail(
            ErrorCode.INVALID_PARAMS));
  }
}
