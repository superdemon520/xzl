package cn.xinzhili.api.doctor.bean.request;

import static cn.xinzhili.api.doctor.util.ValidationUtils.isEmpty;

/**
 * 未登录账号忘记密码时的重置密码请求。
 *
 * @author Gan Dong
 */
public class PasswordForgotRequest {

  private String username;

  private String newPassword;

  private String vcode;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getVcode() {
    return vcode;
  }

  public void setVcode(String vcode) {
    this.vcode = vcode;
  }

  public String getNewPassword() {
    return newPassword;
  }

  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }

  public boolean valid() {
    return !isEmpty(username) && !isEmpty(newPassword) && !isEmpty(vcode);
  }

  @Override
  public String toString() {
    return "PasswordForgotRequest{" +
        "username='" + username + '\'' +
        ", newPassword='" + newPassword + '\'' +
        ", vcode='" + vcode + '\'' +
        '}';
  }
}
