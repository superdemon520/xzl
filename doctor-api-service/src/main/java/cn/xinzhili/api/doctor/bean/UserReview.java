package cn.xinzhili.api.doctor.bean;

import java.util.Objects;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author by ywb on 14/8/2017.
 */
public class UserReview {

  @NotEmpty
  private String userId;
  @NotNull
  private String organizationId;
  @NotNull
  private UserRole role;
  @NotNull
  private Boolean pass;

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getOrganizationId() {
    return organizationId;
  }

  public void setOrganizationId(String organizationId) {
    this.organizationId = organizationId;
  }

  public UserRole getRole() {
    return role;
  }

  public void setRole(UserRole role) {
    this.role = role;
  }

  public Boolean getPass() {
    return pass;
  }

  public void setPass(Boolean pass) {
    this.pass = pass;
  }

  @Override
  public String toString() {
    return "UserReview{" +
        "userId='" + userId + '\'' +
        ", organizationId='" + organizationId + '\'' +
        ", role=" + role +
        ", pass=" + pass +
        '}';
  }

  public boolean invalid(){
    return Objects.nonNull(getRole()) && getRole() == UserRole.ADMIN;
  }
}
