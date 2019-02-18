package cn.xinzhili.infra.service;

import cn.xinzhili.infra.bean.NotificationRequest;
import cn.xinzhili.infra.client.NotifyServiceClient;
import cn.xinzhili.xutils.core.FailureException;
import cn.xinzhili.xutils.core.http.Response;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Date: 14/10/2016
 * Time: 5:52 PM
 *
 * @author Gan Dong
 */
@Service
public class NotifyService {

  private static final int NOTIFICATION_LOGOUT_TYPE = 2;

  private static final Logger logger = LoggerFactory
      .getLogger(NotifyService.class);

  @Autowired
  private NotifyServiceClient notifyServiceClient;

  @HystrixCommand(fallbackMethod = "defaultBindDevice")
  public Response bindDevice(String deviceToken, String deviceType,
      String subscriber) {
    Map<String, String> deviceInfo = new HashMap<>(3);
    deviceInfo.put("deviceToken", deviceToken);
    deviceInfo.put("deviceType", deviceType);
    deviceInfo.put("subscriber", subscriber);
    return notifyServiceClient.bindDevice(deviceInfo);
  }

  private Response defaultBindDevice(String deviceToken, String deviceType,
      String subscriber, Throwable t) {
    throw new FailureException("绑定用户设备失败")
        .add("deviceToken", deviceToken)
        .add("deviceType", deviceType)
        .add("subscriber", subscriber);
  }

  @HystrixCommand(fallbackMethod = "defaultUnbindDevice")
  public Response unbindDevice(String deviceToken, String subscriber) {
    Map<String, String> deviceInfo = new HashMap<>(2);
    deviceInfo.put("deviceToken", deviceToken);
    deviceInfo.put("subscriber", subscriber);
    return notifyServiceClient.unbindDevice(deviceInfo);
  }

  private Response defaultUnbindDevice(String deviceToken, String subscriber) {
    throw new FailureException("解绑用户设备失败")
        .add("deviceToken", deviceToken)
        .add("subscriber", subscriber);
  }

  /**
   * 给用户发推送，通知用户客户端
   *
   * @param subscriber 用户的subscriber
   * @param deviceToken 设备token，为可选参数
   */
  public void notifyUserToLogout(String subscriber, String deviceToken) {
    final String msg = "您已在其他设备登录";
    Map<String, Object> content = new HashMap<>(2);
    content.put("type", NOTIFICATION_LOGOUT_TYPE);
    content.put("data", Collections.singletonMap("content", msg));
    content = Collections.unmodifiableMap(content);

    NotificationRequest req = new NotificationRequest();
    req.setTitle("心之力");
    req.setAlert(msg);
    req.setContent(content);
    req.setReceivers(Collections.singletonList(subscriber));
    req.setDeviceToken(deviceToken);

    Response response = notifyServiceClient.notifyUser(req);
    if (!response.isSuccessful()) {
      // for now we do nothing other than logging
      logger.error("push notification failed: {}", response);
    }
  }
}
