package cn.xinzhili.api.doctor.client;

import cn.xinzhili.api.doctor.bean.response.NotifyResponse;
import cn.xinzhili.xutils.core.http.Response;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 推送通知
 *
 * @author Gan Dong
 */
@FeignClient("notify-service")
public interface NotifyServiceClient {

  /**
   * 对手机发送一个验证码
   *
   * @param phone 手机号 string
   */
  @RequestMapping(value = "/signin/code", method = RequestMethod.GET)
  Response requestVcode(@RequestParam("phone") String phone);

  /**
   * 验证手机的随机码是否正确
   *
   * @param body 手机号 随机码等信息, 其中 code 表示待验证的验证码；phone 表示手机号
   */
  @RequestMapping(value = "/signin/code",
      method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  Response verifyVcode(@RequestBody Map<String, Object> body);

  @RequestMapping(value = "/push/notifies", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  Response pushNotify(@RequestBody NotifyResponse body);

  /**
   * 发送给医生密码
   *
   * @param phone 手机号
   * @param hospitalName 医院名称
   * @param password 密码
   * @return 返回成功
   */
  @RequestMapping(value = "/sms/doctor")
  Response sendPassword(@RequestParam(value = "phone") String phone,
      @RequestParam(value = "hospitalName") String hospitalName,
      @RequestParam(value = "password") String password);
}
