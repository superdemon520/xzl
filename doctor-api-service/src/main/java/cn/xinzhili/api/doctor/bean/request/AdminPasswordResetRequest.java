package cn.xinzhili.api.doctor.bean.request;

import static cn.xinzhili.api.doctor.util.ValidationUtils.isEmpty;

/**
 * 管理员重置某个人员账号密码的请求。
 *
 * @author Gan Dong
 */
public class AdminPasswordResetRequest {

  private String userId;

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public boolean valid() {
    return !isEmpty(userId);
  }

  @Override
  public String toString() {
    return "AdminPasswordResetRequest{" +
        "userId='" + userId + '\'' +
        '}';
  }
}
