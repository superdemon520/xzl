package cn.xinzhili.api.doctor.bean.request;

import cn.xinzhili.api.doctor.bean.UserGender;
import cn.xinzhili.api.doctor.util.ValidationUtils;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

/**
 * @author by Loki on 17/2/24.
 */
@Data
public class UpdateUserRequest {

  private String id;
  private String name;
  private UserGender sex;
  private String avatar;
  private String tel;
  private String expertise;
  private String biography;
  private String experience;
  private String departmentId;
  private String organizationId;
  private String meetingLecture;
  private String hospitalIntroduction;
  private String firstProfessionCompany;

  @Override
  public String toString() {
    return "UpdateUserRequest{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", sex=" + sex +
        ", avatar='" + avatar + '\'' +
        ", tel='" + tel + '\'' +
        ", expertise='" + expertise + '\'' +
        ", biography='" + biography + '\'' +
        ", experience='" + experience + '\'' +
        ", departmentId='" + departmentId + '\'' +
        ", organizationId='" + organizationId + '\'' +
        ", meetingLecture='" + meetingLecture + '\'' +
        ", hospitalIntroduction='" + hospitalIntroduction + '\'' +
        ", firstProfessionCompany='" + firstProfessionCompany + '\'' +
        '}';
  }

  public boolean invalid() {
    return !StringUtils.isEmpty(getName()) && !ValidationUtils.isValidName(getName());
  }
}
