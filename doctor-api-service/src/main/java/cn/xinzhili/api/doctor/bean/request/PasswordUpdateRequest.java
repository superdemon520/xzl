package cn.xinzhili.api.doctor.bean.request;

import static cn.xinzhili.api.doctor.util.ValidationUtils.isEmpty;

/**
 * 已登录用户更改密码请求
 *
 * @author Gan Dong
 */
public class PasswordUpdateRequest {

  private String originalPassword;

  private String newPassword;

  public String getOriginalPassword() {
    return originalPassword;
  }

  public void setOriginalPassword(String originalPassword) {
    this.originalPassword = originalPassword;
  }

  public String getNewPassword() {
    return newPassword;
  }

  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }

  public boolean valid() {
    return !isEmpty(newPassword) && !isEmpty(originalPassword);
  }

  @Override
  public String toString() {
    return "PasswordUpdateRequest{" +
        "originalPassword='" + originalPassword + '\'' +
        ", newPassword='" + newPassword + '\'' +
        '}';
  }
}
