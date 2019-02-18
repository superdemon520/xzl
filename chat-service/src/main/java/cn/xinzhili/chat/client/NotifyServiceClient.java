package cn.xinzhili.chat.client;

import cn.xinzhili.chat.bean.request.NotifyRequest;
import cn.xinzhili.xutils.core.http.Response;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("notify-service")
public interface NotifyServiceClient {

  @RequestMapping(value = "/push/notifies", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  Response pushNotify(@RequestBody NotifyRequest body);
}
