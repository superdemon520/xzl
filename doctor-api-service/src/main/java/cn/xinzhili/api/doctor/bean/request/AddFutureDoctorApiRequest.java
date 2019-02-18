package cn.xinzhili.api.doctor.bean.request;

import cn.xinzhili.api.doctor.UserFutureDoctorType;
import cn.xinzhili.api.doctor.bean.DoctorTitle;
import cn.xinzhili.api.doctor.bean.UserGender;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddFutureDoctorApiRequest {

  private String avatar;

  private String name;
  @NotNull
  private UserGender sex;

  private DoctorTitle title;

  private String department;

  private String biography;

  private String expertise;

  private String experience;

  private String meetingLecture;

  private String hospitalIntroduction;

  @NotNull
  private UserFutureDoctorType type;

  private String hospitalName;
}
