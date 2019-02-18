package cn.xinzhili.api.doctor.util;

import cn.xinzhili.api.doctor.UserFutureDoctorType;
import cn.xinzhili.api.doctor.bean.Assistant;
import cn.xinzhili.api.doctor.bean.Doctor;
import cn.xinzhili.api.doctor.bean.DoctorTitle;
import cn.xinzhili.api.doctor.bean.FutureDoctorApiInfo;
import cn.xinzhili.api.doctor.bean.Operator;
import cn.xinzhili.api.doctor.bean.User;
import cn.xinzhili.api.doctor.bean.UserGender;
import cn.xinzhili.api.doctor.bean.UserInviteStatus;
import cn.xinzhili.api.doctor.bean.UserReview;
import cn.xinzhili.api.doctor.bean.UserRole;
import cn.xinzhili.api.doctor.bean.UserStatus;
import cn.xinzhili.api.doctor.bean.request.AddAssistantApiRequest;
import cn.xinzhili.api.doctor.bean.request.AddDoctorApiRequest;
import cn.xinzhili.api.doctor.bean.request.AddStaffApiRequest;
import cn.xinzhili.api.doctor.bean.request.InitUserRequest;
import cn.xinzhili.api.doctor.bean.request.UpdateDoctorRequest;
import cn.xinzhili.api.doctor.bean.request.UpdateUserRequest;
import cn.xinzhili.user.api.FutureDoctorInfo;
import cn.xinzhili.user.api.Gender;
import cn.xinzhili.user.api.StaffInfo;
import cn.xinzhili.user.api.StaffRole;
import cn.xinzhili.user.api.StaffTitle;
import cn.xinzhili.user.api.UserReviewInfo;
import cn.xinzhili.user.api.request.AddStaffRequest;
import cn.xinzhili.user.api.request.InitStaffRequest;
import cn.xinzhili.user.api.request.UpdateStaffRequest;
import cn.xinzhili.user.api.response.FutureDoctorListResponse;
import cn.xinzhili.xutils.core.ErrorCode;
import cn.xinzhili.xutils.core.FailureException;
import cn.xinzhili.xutils.core.IntCodeEnum;
import cn.xinzhili.xutils.core.StringCodeEnum;
import cn.xinzhili.xutils.core.util.HashUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

/**
 * Date: 16/02/2017 Time: 10:20 PM
 *
 * @author Gan Dong
 */
public class UserInfoFactory {

  private static final Logger logger = LoggerFactory
      .getLogger(UserInfoFactory.class);

  private static final String regexStr = "\r|\n";

  /**
   * 根据user-service返回的staffDetail 得到给API的UserDetailResponse,
   *
   * @param staff 用户信息来源
   * @return 适合API返回的用户信息
   */
  public static User valueOf(StaffInfo staff) {
    if (staff == null) {
      logger.error("staff is invalid !");
      throw new FailureException(ErrorCode.INVALID_PARAMS);
    }
    List<StaffRole> roles = staff.getRoles();
    User user;
    if (Objects.isNull(roles)) {
      user = new User();
    } else if (roles.contains(StaffRole.DOCTOR)) {
      Doctor doctor = new Doctor();
      final StaffTitle title = staff.getTitle();
      if (title != null) {
        doctor.setTitle(DoctorTitle.valueOf(title.toString()));
      }
      user = doctor;
    } else if (roles.contains(StaffRole.ASSISTANT)) {
      user = new Assistant();
    } else if (roles.contains(StaffRole.OPERATOR)) {
      user = new Operator();
    } else {
      user = new User();
    }
    user.setName(staff.getName());
    if (staff.getSex() != null) {
      user.setSex(UserGender.valueOf(staff.getSex().name()));
    }
    if (staff.getStatus() != null) {
      user.setStatus(UserStatus.valueOf(staff.getStatus().name()));
    }
    user.setAvatar(staff.getAvatar());
    user.setTel(staff.getTel());
    user.setUsername(staff.getUsername());
    user.setBiography(staff.getBiography());
    user.setExperience(staff.getExperience());
    user.setCertificates(staff.getCertificates());
    user.setDepartmentName("");
    user.setExpertise(staff.getExpertise());
    user.setMeetingLecture(staff.getMeetingLecture());
    user.setHospitalIntroduction(staff.getHospitalIntroduction());
    user.setFirstProfessionCompany(staff.getFirstProfessionCompany());
    if (!CollectionUtils.isEmpty(staff.getRoles())) {
      // @formatter:off
      List<UserRole> userRoles = staff.getRoles().stream()
          .map(r -> UserRole.valueOf(r.name()))
          .collect(Collectors.toList());
      user.setRoles(userRoles);
      // @formatter:on
    }
    user.setQrCodeUrl(staff.getQrCodeUrl());
    if (Objects.nonNull(staff.getRelationshipStatus())) {
      user.setInviteStatus(UserInviteStatus.valueOf(staff.getRelationshipStatus().name()));
    }
    return user;
  }

  public static List<Doctor> staffInfo2Doctor(List<StaffInfo> staffs) {
    List<Doctor> doctors = new ArrayList<>();
    for (StaffInfo staff : staffs) {
      Doctor doctor = (Doctor) valueOf(staff);
      if (staff.getId() != null) {
        doctor.setId(HashUtils.encode(staff.getId()));
      }
      doctors.add(doctor);
    }
    return doctors;
  }

  public static List<Assistant> staffInfo2Assistant(List<StaffInfo> staffs) {
    List<Assistant> assistants = new ArrayList<>();
    for (StaffInfo staff : staffs) {
      Assistant assistant = (Assistant) valueOf(staff);
      if (staff.getId() != null) {
        assistant.setId(HashUtils.encode(staff.getId()));
      }
      assistants.add(assistant);
    }
    return assistants;
  }

  public static Operator staff2Operator(StaffInfo staff) {
    Operator operator = (Operator) valueOf(staff);
    operator.setId(Objects.nonNull(staff.getId()) ? HashUtils.encode(staff.getId()) : null);
    return operator;
  }

  public static UpdateStaffRequest updateUser2UpdateStaff(UpdateUserRequest request) {

    if (request == null
        || StringUtils.isEmpty(request.getId())
        || StringUtils.isEmpty(request.getOrganizationId())) {
      logger.error("update user 2 update staff param is null !");
      throw new FailureException(ErrorCode.INVALID_PARAMS);
    }
    UpdateStaffRequest staffRequest = new UpdateStaffRequest();
    StaffInfo info = new StaffInfo();
    //如果是医生
    if (request instanceof UpdateDoctorRequest) {
      UpdateDoctorRequest updateDoctor = (UpdateDoctorRequest) request;
      DoctorTitle title = updateDoctor.getTitle();
      if (title != null) {
        info.setTitle(StringCodeEnum.getEnumForCode(StaffTitle.class, title.toString()));
      }
    }
    info.setId(HashUtils.decode(request.getId()));
    info.setAvatar(request.getAvatar());
    if (request.getSex() != null) {
      info.setSex(Gender.valueOf(request.getSex().name()));
    }
    info.setTel(request.getTel());
    info.setName(request.getName());
    info.setExperience(lineFeed2HtmlTag(request.getExperience()));
    info.setBiography(lineFeed2HtmlTag(request.getBiography()));
    info.setMeetingLecture(request.getMeetingLecture());
    info.setHospitalIntroduction(request.getHospitalIntroduction());
    info.setFirstProfessionCompany(request.getFirstProfessionCompany());
    staffRequest.setStaffInfo(info);
    if (!StringUtils.isEmpty(request.getDepartmentId())) {
      staffRequest.setDepartmentId(HashUtils.decode(request.getDepartmentId()));
    }
    staffRequest.setOrganizationId(HashUtils.decode(request.getOrganizationId()));
    info.setExpertise(lineFeed2HtmlTag(request.getExpertise()));
    return staffRequest;
  }


  public static AddStaffRequest ofDoctor(AddDoctorApiRequest request, Long adminId) {

    AddStaffRequest doctor = of(request, adminId);
    doctor.setName(request.getName());
    doctor.setTitle(StringCodeEnum.getEnumForCode(StaffTitle.class, request.getTitle().toString()));
    doctor.setRole(StaffRole.DOCTOR);
    return doctor;
  }

  public static AddStaffRequest ofAssistant(AddAssistantApiRequest request, Long adminId) {

    AddStaffRequest assistant = of(request, adminId);
    assistant.setName(request.getName());
    assistant.setRole(StaffRole.ASSISTANT);
    return assistant;
  }

  public static AddStaffRequest ofOperator(AddStaffApiRequest request, Long adminId) {

    AddStaffRequest operator = of(request, adminId);
    operator.setRole(StaffRole.OPERATOR);
    return operator;
  }

  private static AddStaffRequest of(AddStaffApiRequest request, Long adminId) {

    AddStaffRequest staff = new AddStaffRequest();

    staff.setUsername(request.getTel());
    staff.setAdminUserId(adminId);
    if (request.getSex() != null) {
      staff.setSex(Gender.valueOf(request.getSex().name()));
    } else {
      staff.setSex(Gender.OTHER);
    }
    staff.setDepartmentId(HashUtils.decode(request.getDepartmentId()));
    staff.setOrganizationId(HashUtils.decode(request.getOrganizationId()));
    return staff;
  }

  public static InitStaffRequest of(InitUserRequest request) {
    if (request == null) {
      throw new FailureException(
          ErrorCode.INVALID_PARAMS, "init user request is null !");
    }
    InitStaffRequest staffRequest = InitStaffRequest.builder()
        .newPassword(request.getNewPassword())
        .name(request.getName())
        .avatar(request.getAvatar())
        .biography(lineFeed2HtmlTag(request.getBiography()))
        .experience(lineFeed2HtmlTag(request.getExperience()))
        .expertise(lineFeed2HtmlTag(request.getExpertise()))
        .certificates(request.getCertificates())
        .meetingLecture(request.getMeetingLecture())
        .hospitalIntroduction(request.getHospitalIntroduction())
        .build();
    if (request.getSex() != null) {
      staffRequest.setGender(IntCodeEnum.getEnumForName(Gender.class, request.getSex().name()));
    }
    return staffRequest;
  }

  private static String lineFeed2HtmlTag(String value) {
    return StringUtils.isEmpty(value) ? null : value.replaceAll(regexStr, "<br>");
  }

  public static UserReviewInfo toUserReviewInfo(UserReview userReview) {
    UserReviewInfo userReviewInfo = new UserReviewInfo();
    userReviewInfo.setUserId(HashUtils.decode(userReview.getUserId()));
    userReviewInfo.setRole(StaffRole.valueOf(userReview.getRole().name()));
    userReviewInfo.setOrganizationId(HashUtils.decode(userReview.getOrganizationId()));
    userReviewInfo.setPass(userReview.getPass());
    return userReviewInfo;
  }

  public static FutureDoctorApiInfo getFutureDoctorApiInfos(
      FutureDoctorListResponse futureDoctorListResponse) {
    FutureDoctorApiInfo futureDoctorApiInfo = null;
    if (CollectionUtils.isNotEmpty(futureDoctorListResponse.getFutureDoctorInfos())) {
      futureDoctorApiInfo = futureDoctorListResponse
          .getFutureDoctorInfos().stream().map(e -> getFutureDoctorApiInfo(e)).findFirst().get();
    }
    return futureDoctorApiInfo;
  }

  public static FutureDoctorApiInfo getFutureDoctorApiInfo(FutureDoctorInfo futureDoctorInfo) {
    FutureDoctorApiInfo futureDoctorApiInfo = new FutureDoctorApiInfo();
    BeanUtils.copyProperties(futureDoctorInfo, futureDoctorApiInfo);
    futureDoctorApiInfo.setSex(UserGender.valueOf(futureDoctorInfo.getSex().name()));
    futureDoctorApiInfo.setTitle(DoctorTitle.valueOf(futureDoctorInfo.getTitle().name()));
    futureDoctorApiInfo.setType(UserFutureDoctorType.valueOf(futureDoctorInfo.getType().name()));
    futureDoctorApiInfo.setId(HashUtils.encode(futureDoctorInfo.getId()));
    futureDoctorApiInfo.setDoctorId(HashUtils.encode(futureDoctorInfo.getDoctorId()));
    futureDoctorApiInfo.setHospitalIntroduction(futureDoctorInfo.getHospital());
    futureDoctorApiInfo.setExperience(futureDoctorInfo.getAchievement());
    return futureDoctorApiInfo;
  }
}
