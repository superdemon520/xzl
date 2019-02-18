package cn.xinzhili.api.doctor.bean;

import cn.xinzhili.user.api.CertificateApi;
import java.util.List;
import lombok.Data;

/**
 * 用户信息
 */
@Data
public class User {

  private String username;
  private String name;
  private UserGender sex;
  private String avatar;
  private String departmentName;
  private String tel;
  private String expertise;
  private String biography;
  private String experience;
  private List<CertificateApi> certificates;
  private List<UserRole> roles;
  private UserStatus status;
  private UserInviteStatus inviteStatus;
  private String qrCodeUrl;
  private String meetingLecture;
  private String hospitalIntroduction;
  private String firstProfessionCompany;

}
