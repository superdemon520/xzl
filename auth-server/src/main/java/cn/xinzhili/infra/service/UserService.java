package cn.xinzhili.infra.service;

import cn.xinzhili.infra.client.UserServiceClient;
import cn.xinzhili.user.api.response.StaffAuthResponse;
import cn.xinzhili.xutils.core.FailureException;
import cn.xinzhili.xutils.core.http.Response;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Date: 14/10/2016
 * Time: 5:52 PM
 *
 * @author Gan Dong
 */
@Service
public class UserService {

  private static final Logger logger = LoggerFactory
      .getLogger(UserService.class);

  @Autowired
  private UserServiceClient userServiceClient;

  @HystrixCommand(fallbackMethod = "defaultAuthenticatePatient")
  public Response authenticatePatient(String username, String password) {
    return userServiceClient.authenticatePatient(username, password);
  }

  private Response defaultAuthenticatePatient(String username, String password,
      Throwable t) {
    logger.error("验证患者用户名密码失败 -> ", t);
    throw new FailureException("验证患者用户名密码失败");
  }

  @HystrixCommand(fallbackMethod = "defaultLoadPatientUser")
  public Response loadPatientUser(String username) {
    return userServiceClient.loadPatientUser(username);
  }

  private Response defaultLoadPatientUser(String username, Throwable t) {
    logger.error("查询患者用户失败 -> ", t);
    throw new FailureException("查询患者用户失败");
  }

  @HystrixCommand(fallbackMethod = "defaultAuthenticateDoctor")
  public Response<StaffAuthResponse> authenticateDoctor(String username,
      String password) {
    return userServiceClient.authenticateDoctor(username, password);
  }

  private Response<StaffAuthResponse> defaultAuthenticateDoctor(String username,
      String password,
      Throwable t) {
    logger.error("验证医生用户名密码失败 -> ", t);
    throw new FailureException("验证医生用户名密码失败");
  }

  @HystrixCommand(fallbackMethod = "defaultLoadDoctorUser")
  public Response<StaffAuthResponse> loadDoctorUser(String username) {
    return userServiceClient.loadDoctorUser(username);
  }

  private Response<StaffAuthResponse> defaultLoadDoctorUser(String username,
      Throwable t) {
    logger.error("查询医生用户失败 -> ", t);
    throw new FailureException("查询医生用户失败");
  }
}
