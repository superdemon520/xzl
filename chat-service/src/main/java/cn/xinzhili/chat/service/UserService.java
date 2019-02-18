package cn.xinzhili.chat.service;

import cn.xinzhili.chat.api.RoleType;
import cn.xinzhili.chat.bean.UserBean;
import cn.xinzhili.chat.client.UserServiceClient;
import cn.xinzhili.chat.util.UserFactory;
import cn.xinzhili.user.api.UserRole;
import cn.xinzhili.user.api.error.UserErrorCode;
import cn.xinzhili.user.api.response.PatientDetailResponse;
import cn.xinzhili.user.api.response.PatientRelationResponse;
import cn.xinzhili.user.api.response.StaffDetailResponse;
import cn.xinzhili.xutils.core.FailureException;
import cn.xinzhili.xutils.core.http.Response;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired
  private UserServiceClient userServiceClient;

  @Autowired
  private RedisBasicService redisBasicService;
  private final static String PUB_KEY = "USER";

  public PatientRelationResponse getRelationByUserId(Long userId, RoleType roleType, Long orgId) {
    Response<PatientRelationResponse> response = userServiceClient
        .getPatientStaffRelationship(userId, getUserRoleByRoleType(roleType), orgId);
    if (response.isSuccessful()) {
      return response.getDataAs(PatientRelationResponse.class);
    } else {
      throw new FailureException(UserErrorCode.PATIENT_NO_BIND_MEDICAL_PERSON);
    }
  }

  private UserRole getUserRoleByRoleType(RoleType roleType) {
    switch (roleType) {
      case DOCTOR:
        return UserRole.DOCTOR;
      case PATIENT:
        return UserRole.PATIENT;
      case OPERATOR:
        return UserRole.OPERATOR;
      case ASSISTANT:
        return UserRole.ASSISTANT;
      default:
        return null;
    }
  }

  public PatientDetailResponse getPatientBindInfos(Long patientId) {
    Response<PatientDetailResponse> patientBy = userServiceClient.getPatientBy(patientId);
    if (patientBy.isSuccessful()) {
      return patientBy.getDataAs(PatientDetailResponse.class);
    } else {
      throw new FailureException("获取医患信息失败");
    }
  }

  public UserBean getUserDetail(Long userId, RoleType roleType) {
    UserBean user = (UserBean) redisBasicService.getRedis(PUB_KEY, roleType.name() + userId);
    if (Objects.isNull(user)) {
      if (roleType.equals(RoleType.PATIENT)) {
        user = UserFactory.of(getPatientBindInfos(userId));
      } else {
        user = UserFactory.of(getDoctorDetail(userId));
      }
      redisBasicService.setRedis(PUB_KEY, roleType.name() + userId, user);
    }
    return user;
  }

  public StaffDetailResponse getDoctorDetail(Long userId) {

    Response<StaffDetailResponse> response = userServiceClient
        .findStaffByUserId(userId, false, null);
    if (response.isSuccessful()) {
      return response.getDataAs(StaffDetailResponse.class);
    } else {
      throw new FailureException("获取医患信息失败");
    }
  }


}
