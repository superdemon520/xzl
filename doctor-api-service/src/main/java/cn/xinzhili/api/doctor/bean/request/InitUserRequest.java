package cn.xinzhili.api.doctor.bean.request;

import static cn.xinzhili.api.doctor.util.ValidationUtils.isEmpty;

import cn.xinzhili.api.doctor.bean.UserGender;
import cn.xinzhili.user.api.CertificateApi;
import java.util.List;
import java.util.Objects;
import lombok.Data;
import org.apache.commons.lang.StringUtils;


/**
 * 首次登陆账号完善个人信息请求。
 *
 * @author Gan Dong
 */
@Data
public class InitUserRequest {

  private String newPassword;

  private String name;

  private UserGender sex;

  /**
   * 简介
   */
  private String biography;

  /**
   * 擅长领域
   */
  private String expertise;

  /**
   * 职业经历
   */
  private String experience;

  private List<CertificateApi> certificates;

  private String avatar;


  private String meetingLecture;
  private String hospitalIntroduction;
  private String firstProfessionCompany;

  public boolean validDoctorOrAssistant() {
    return !isEmpty(newPassword) && !isEmpty(name) && sex != null && !isEmpty(biography)
        && !isEmpty(expertise) && !isEmpty(experience) && !isEmpty(avatar) && !isEmpty(
        meetingLecture) && !isEmpty(hospitalIntroduction) && !isEmpty(firstProfessionCompany);
  }

  public boolean validateDoctor() {
    return validDoctorOrAssistant() && certificates != null && !certificates.isEmpty() &&
        certificates.stream()
            .allMatch(
                cert -> !Objects.isNull(cert) && !Objects.isNull(cert.getType())
                    && !cert.getImageIds().isEmpty() && cert.getImageIds().stream()
                    .allMatch(StringUtils::isNotBlank));

  }

  public boolean validAdmin() {
    return !isEmpty(newPassword);
  }

  public boolean validOperator() {
    return isEmpty(firstProfessionCompany) && !isEmpty(newPassword);
  }


  @Override
  public String toString() {
    return "InitUserRequest{" +
        "newPassword='" + newPassword + '\'' +
        ", name='" + name + '\'' +
        ", sex=" + sex +
        ", biography='" + biography + '\'' +
        ", expertise='" + expertise + '\'' +
        ", experience='" + experience + '\'' +
        ", certificates=" + certificates +
        ", avatar='" + avatar + '\'' +
        ", meetingLecture='" + meetingLecture + '\'' +
        ", hospitalIntroduction='" + hospitalIntroduction + '\'' +
        ", firstProfessionCompany='" + firstProfessionCompany + '\'' +
        '}';
  }
}
