package cn.xinzhili.chat.client;

import cn.xinzhili.chat.bean.DpcMessageResponse;
import cn.xinzhili.dpc.api.DpcMessageType;
import cn.xinzhili.xutils.core.http.Response;
import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "dpc-service")
public interface DpcServiceClient {

  @GetMapping("/messages")
  Response<DpcMessageResponse> getChatList(
      @RequestParam(value = "sender", required = false) String sender,
      @RequestParam(value = "receiver") String receiver,
      @RequestParam(value = "type") List<DpcMessageType> type,
      @RequestParam(value = "page") Integer page,
      @RequestParam(value = "perPage") Integer perPage);

}
