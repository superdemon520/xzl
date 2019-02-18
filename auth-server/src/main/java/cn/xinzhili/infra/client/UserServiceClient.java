package cn.xinzhili.infra.client;

import cn.xinzhili.user.api.response.StaffAuthResponse;
import cn.xinzhili.xutils.core.http.Response;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Date: 16/7/19 Time: PM3:53
 *
 * @author gan
 */
@FeignClient("user-service")
public interface UserServiceClient {

  String PATIENT_UID_FIELD = "userId";

  @RequestMapping(method = RequestMethod.GET, value = "/patient/auth")
  Response authenticatePatient(@RequestParam("tel") String tel,
      @RequestParam("password") String password);

  @RequestMapping(method = RequestMethod.GET, value = "/patient/user")
  Response loadPatientUser(@RequestParam("tel") String tel);

  @RequestMapping(method = RequestMethod.GET, value = "/doctor/auth")
  Response<StaffAuthResponse> authenticateDoctor(@RequestParam("username") String username,
      @RequestParam("password") String password);

  @RequestMapping(method = RequestMethod.GET, value = "/doctor/authority")
  Response<StaffAuthResponse> loadDoctorUser(@RequestParam("username") String username);


}
