package cn.xinzhili.api.doctor.bean;

import cn.xinzhili.api.doctor.UserFutureDoctorType;
import lombok.Data;

@Data
public class FutureDoctorApiInfo {

  private String id;

  private String doctorId;

  private String avatar;

  private String name;

  private UserGender sex;

  private DoctorTitle title;

  private String department;

  private String biography;

  private String expertise;

  private String experience;

  private String meetingLecture;

  private String hospitalIntroduction;

  private UserFutureDoctorType type;

  private String hospitalName;
}
