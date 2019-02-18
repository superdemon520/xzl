package cn.xinzhili.infra.client;

import cn.xinzhili.infra.bean.NotificationRequest;
import cn.xinzhili.xutils.core.http.Response;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * Date: 14/10/2016
 * Time: 1:29 PM
 *
 * @author Gan Dong
 */
@FeignClient("notify-service")
public interface NotifyServiceClient {

  @RequestMapping(method = RequestMethod.POST, value = "/push/bind",
      consumes = MediaType.APPLICATION_JSON_VALUE)
  Response bindDevice(@RequestBody Map<String, String> deviceInfo);

  @RequestMapping(method = RequestMethod.POST, value = "/push/unbind",
      consumes = MediaType.APPLICATION_JSON_VALUE)
  Response unbindDevice(@RequestBody Map<String, String> deviceInfo);

  @RequestMapping(method = RequestMethod.POST, value = "/push/notifies",
      consumes = MediaType.APPLICATION_JSON_VALUE)
  Response notifyUser(@RequestBody NotificationRequest notification);
}
