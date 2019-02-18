package cn.xinzhili.api.doctor.controller;

import static cn.xinzhili.api.doctor.util.AuthUtils.isUserOfRole;

import cn.xinzhili.api.doctor.bean.Doctor;
import cn.xinzhili.api.doctor.bean.FutureDoctorApiInfo;
import cn.xinzhili.api.doctor.bean.UserReview;
import cn.xinzhili.api.doctor.bean.UserRole;
import cn.xinzhili.api.doctor.bean.request.AddAssistantApiRequest;
import cn.xinzhili.api.doctor.bean.request.AddDoctorApiRequest;
import cn.xinzhili.api.doctor.bean.request.AddFutureDoctorApiRequest;
import cn.xinzhili.api.doctor.bean.request.AddOperatorApiRequest;
import cn.xinzhili.api.doctor.bean.request.DeleteOperatorApiRequest;
import cn.xinzhili.api.doctor.bean.request.GetStaffListRequest;
import cn.xinzhili.api.doctor.bean.request.InitUserRequest;
import cn.xinzhili.api.doctor.bean.request.PasswordForgotRequest;
import cn.xinzhili.api.doctor.bean.request.PasswordUpdateRequest;
import cn.xinzhili.api.doctor.bean.request.ResetStaffPwdByAdminRequest;
import cn.xinzhili.api.doctor.bean.request.StaffDepartmentRequest;
import cn.xinzhili.api.doctor.bean.request.UpdateAssistantRequest;
import cn.xinzhili.api.doctor.bean.request.UpdateDoctorRequest;
import cn.xinzhili.api.doctor.bean.request.UpdateFutureDoctorApiRequest;
import cn.xinzhili.api.doctor.bean.request.UpdateOperatorRequest;
import cn.xinzhili.api.doctor.bean.request.UpdateUserCertificateApiRequest;
import cn.xinzhili.api.doctor.bean.response.AssistantListResponse;
import cn.xinzhili.api.doctor.bean.response.AvatarUploadInfoResponse;
import cn.xinzhili.api.doctor.bean.response.CertificateUploadInfoResponse;
import cn.xinzhili.api.doctor.bean.response.DoctorListResponse;
import cn.xinzhili.api.doctor.bean.response.OperatorListResponse;
import cn.xinzhili.api.doctor.bean.response.PatientRegionListResponse;
import cn.xinzhili.api.doctor.bean.response.UserDetailResponse;
import cn.xinzhili.api.doctor.error.UserErrorCode;
import cn.xinzhili.api.doctor.service.NotifyService;
import cn.xinzhili.api.doctor.service.UserService;
import cn.xinzhili.api.doctor.util.UserInfoFactory;
import cn.xinzhili.api.doctor.util.ValidationUtils;
import cn.xinzhili.user.api.FutureDoctorType;
import cn.xinzhili.user.api.request.UpdateStaffRequest;
import cn.xinzhili.xutils.auth.CurrentUserId;
import cn.xinzhili.xutils.core.ErrorCode;
import cn.xinzhili.xutils.core.FailureException;
import cn.xinzhili.xutils.core.http.Response;
import cn.xinzhili.xutils.core.util.HashUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Date: 16/02/2017 Time: 10:48 AM
 *
 * @author Gan Dong
 */
@RestController
public class UserController {

  private static final Logger logger = LoggerFactory.getLogger(UserController.class);

  @Autowired
  private UserService userService;

  @Autowired
  private NotifyService notifyService;

  private List<Long> IdsDecode(List<String> ids) {
    List<Long> idsDecoded = new ArrayList<>();
    for (String id : ids) {
      idsDecoded.add(HashUtils.decode(id));
    }
    return idsDecoded;
  }

  /**
   * 查询用户详情。如果当前用户为医生用户，且查询的是当前登录用户的详情（即userId为空），那 么同时查询此用户所在科室的详情
   *
   * @param id 登录用户id
   * @param userId 如果不为空，则查询此userId，否则查询当前登录用户id
   * @return 欲查询用户详情
   */
  @GetMapping("/user")
  public Response<UserDetailResponse> getUserDetail(
      @CurrentUserId Long id,
      @RequestParam(value = "organizationId", required = false) String organizationId,
      @RequestParam(value = "userId", required = false) String userId) {
    Long uid = id;
    if (!StringUtils.isEmpty(userId)) {
      uid = HashUtils.decode(userId);
    }

    boolean withDepartment = false;
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (StringUtils.isEmpty(userId) &&
        (isUserOfRole(UserRole.DOCTOR, authentication)
            || isUserOfRole(UserRole.ASSISTANT, authentication)
            || isUserOfRole(UserRole.OPERATOR, authentication))) {
      withDepartment = true;
    }
    Long orgId = StringUtils.isEmpty(organizationId) ? null : HashUtils.decode(organizationId);
    return Response.instanceSuccess(userService.getUserDetails(uid, orgId, withDepartment));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/user/doctor")
  public Response addDoctor(@CurrentUserId Long uid, @RequestBody AddDoctorApiRequest request) {

    if (request.invalid()) {
      logger.warn("param is invalid ! {}", request.toString());
      throw new FailureException(UserErrorCode.INVALID_USER_NAME);
    }
    userService.addStaff(UserInfoFactory.ofDoctor(request, uid));
    return Response.instanceSuccess();
  }

  @PreAuthorize("hasAnyRole('ASSISTANT','ADMIN')")
  @GetMapping("/user/doctor")
  public Response getDoctor(@CurrentUserId Long uid,
      @ModelAttribute("request") GetStaffListRequest request) {

    if (request.invalid()) {
      logger.warn("get doctor param invalid ! {}", request.toString());
      throw new FailureException(ErrorCode.INVALID_PARAMS);
    }

    ValidationUtils.validatePaginationParams(request.getPageAt(), request.getPageSize());
    DoctorListResponse response = userService.getDoctorPagination(uid, request);
    return Response.instanceSuccess(response);
  }

  @PreAuthorize("hasRole('DOCTOR')")
  @GetMapping("/consultation/doctor")
  public Response getConsultationDoctor(@CurrentUserId Long uid,
      @RequestParam(value = "doctorName", required = false) String doctorName,
      @RequestParam(value = "organizationId") String organizationId,
      @RequestParam(value = "departmentId", required = false) String departmentId,
      @RequestParam(value = "pageSize", required = false) Integer pageSize,
      @RequestParam(value = "pageAt", required = false) Integer pageAt) {

    ValidationUtils.validatePaginationParams(pageAt, pageSize);

    DoctorListResponse response = userService
        .getConsultationDoctor(doctorName, HashUtils.decode(organizationId),
            departmentId == null ? null : HashUtils.decode(departmentId), uid, pageSize, pageAt);
    return Response.instanceSuccess(response);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/user/doctor")
  public Response deleteDoctor(@RequestBody List<String> ids) {
    userService.deleteDoctor(IdsDecode(ids));
    return Response.instanceSuccess();
  }

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/user/operator")
  public Response deleteOperator(@RequestBody DeleteOperatorApiRequest request) {

    if (request.invalid()) {
      logger.warn("delete operator param invalid ! {}", request.toString());
      throw new FailureException(ErrorCode.INVALID_PARAMS);
    }

    userService.deleteOperator(IdsDecode(request.getOperatorId()),
        HashUtils.decode(request.getOrganizationId()));
    return Response.instanceSuccess();
  }


  @PreAuthorize("hasAnyRole('DOCTOR','ADMIN')")
  @PatchMapping("/user/doctor")
  public Response updateDoctor(@CurrentUserId long uid,
      @RequestBody UpdateDoctorRequest request) {

    UpdateStaffRequest staffRequest =
        UserInfoFactory.updateUser2UpdateStaff(request);
    staffRequest.setAdminUserId(uid);

    Authentication authentication = SecurityContextHolder.getContext()
        .getAuthentication();

    if (isUserOfRole(UserRole.ADMIN, authentication)
        && StringUtils.isEmpty(request.getId())) {
      throw new FailureException(
          ErrorCode.INVALID_PARAMS, "doctor id is null !");
    }

    if (isUserOfRole(UserRole.DOCTOR, authentication)) {
      request.setId(HashUtils.encode(uid));
    }

    //check name
    if (!StringUtils.isEmpty(request.getName())
        && !ValidationUtils.isValidName(request.getName())) {
      logger.error("doctor'name is invalid ! {}", request.getName());
      throw new FailureException(UserErrorCode.INVALID_USER_NAME);
    }

    Doctor doctor = userService.updateDoctor(staffRequest);
    if (doctor == null) {
      logger.error("update doctor {}  fail !", request.getId());
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    }
    return Response.instanceSuccess().withDataEntry("doctor", doctor);
  }

  @PreAuthorize("hasAnyRole('DOCTOR','ASSISTANT','OPERATOR')")
  @PatchMapping("/user/certificate")
  public Response updateUserCertificate(@CurrentUserId Long uid,
      @RequestBody UpdateUserCertificateApiRequest request) {

    if (request.invalid()) {
      logger.error("user certificate request is invalid ! {}", request);
      throw new FailureException(ErrorCode.INVALID_PARAMS);
    }
    userService.updateUserCertificate(uid, request);
    return Response.instanceSuccess();
  }

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/user/assistant")
  public Response deleteAssistant(@RequestBody List<String> ids) {

    userService.deleteAssistant(IdsDecode(ids));
    return Response.instanceSuccess();
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/user/assistant")
  public Response addAssistant(@CurrentUserId Long uid,
      @RequestBody AddAssistantApiRequest request) {

    if (request.invalid() || request.nameInvalid()) {
      logger.warn("param invalid ! {}", request.toString());
      throw new FailureException(UserErrorCode.INVALID_USER_NAME);
    }
    userService.addStaff(UserInfoFactory.ofAssistant(request, uid));
    return Response.instanceSuccess();
  }

  @PreAuthorize("hasAnyRole('ASSISTANT','ADMIN')")
  @PatchMapping("/user/assistant")
  public Response updateAssistant(@CurrentUserId long uid,
      @RequestBody UpdateAssistantRequest request) {

    if (request.invalid()) {
      logger.warn("assistant'name is invalid ! {}", request.getName());
      throw new FailureException(UserErrorCode.INVALID_USER_NAME);
    }

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    boolean isAdmin = isUserOfRole(UserRole.ADMIN, authentication);

    if (isAdmin && StringUtils.isEmpty(request.getId())) {
      logger.warn("assistant id is null ! {}", request.toString());
      throw new FailureException(ErrorCode.INVALID_PARAMS);
    }

    if (isUserOfRole(UserRole.ASSISTANT, authentication)) {
      request.setId(HashUtils.encode(uid));
    }

    UpdateStaffRequest staffRequest = UserInfoFactory.updateUser2UpdateStaff(request);
    staffRequest.setAdminUserId(isAdmin ? uid : null);
    return Response.instanceSuccess()
        .withDataEntry("assistant", userService.updateAssistant(staffRequest));
  }


  @PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
  @PatchMapping("/user/operator")
  public Response updateOperator(@CurrentUserId long uid,
      @RequestBody UpdateOperatorRequest request) {

    if (request.invalid()) {
      logger.warn("operator param invalid ! {}", request.toString());
      throw new FailureException(UserErrorCode.INVALID_USER_NAME);
    }

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    boolean isAdmin = isUserOfRole(UserRole.ADMIN, authentication);

    if (isAdmin && StringUtils.isEmpty(request.getId())) {
      logger.warn("operator is is null ! {}", request.toString());
      throw new FailureException(ErrorCode.INVALID_PARAMS);
    }

    if (isUserOfRole(UserRole.ASSISTANT, authentication)) {
      request.setId(HashUtils.encode(uid));
    }
    UpdateStaffRequest staffRequest = UserInfoFactory.updateUser2UpdateStaff(request);
    staffRequest.setAdminUserId(isAdmin ? uid : null);
    return Response.instanceSuccess()
        .withDataEntry("operator", userService.updateOperator(staffRequest));
  }

  @PreAuthorize("hasAnyRole('DOCTOR','ADMIN')")
  @GetMapping("/user/assistant")
  public Response getAssistantPagination(@CurrentUserId Long uid,
      @ModelAttribute("request") GetStaffListRequest request) {

    if (request.invalid()) {
      logger.warn("get assistant param invalid ! {}", request.toString());
      throw new FailureException(ErrorCode.INVALID_PARAMS);
    }

    ValidationUtils.validatePaginationParams(request.getPageAt(), request.getPageSize());

    // check doctorId
    String doctorId = request.getDoctorId();
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (!StringUtils.isEmpty(doctorId) && isUserOfRole(UserRole.DOCTOR, authentication)
        && uid != HashUtils.decode(doctorId)) {
      throw new FailureException(ErrorCode.INVALID_PARAMS, "doctorId is invalid !");
    }
    AssistantListResponse response = userService.getAssistantPagination(uid, request);
    return Response.instanceSuccess(response);
  }


  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/user/operator")
  public Response getOperatorPagination(@CurrentUserId Long uid,
      @ModelAttribute("request") GetStaffListRequest request) {

    if (request.invalid()) {
      logger.warn("get operator param invalid ! {}", request.toString());
      throw new FailureException(ErrorCode.INVALID_PARAMS);
    }

    ValidationUtils.validatePaginationParams(request.getPageAt(), request.getPageSize());
    OperatorListResponse response = userService.getOperatorPagination(uid, request);
    return Response.instanceSuccess(response);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/user/operator")
  public Response addOperator(@CurrentUserId Long uid, @RequestBody AddOperatorApiRequest request) {

    if (request.invalid()) {
      logger.warn("param invalid ! {}", request.toString());
      throw new FailureException(UserErrorCode.INVALID_USER_NAME);
    }
    userService.addStaff(UserInfoFactory.ofOperator(request, uid));
    return Response.instanceSuccess();
  }


  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping(value = "/user/department")
  public Response moveStaffDepartment(@CurrentUserId long adminUserId,
      @RequestBody StaffDepartmentRequest request) {
    if (request.invalid()) {
      throw new FailureException(ErrorCode.INVALID_PARAMS);
    }
    return userService.moveStaffDepartment(adminUserId, request);
  }

  /**
   * 管理员重置员工密码
   *
   * @param adminUserId 登录的管理员id
   * @return 是否成功
   */
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping(value = "/user/password/{id}")
  public Response resetUserPasswordByAdmin(@CurrentUserId Long adminUserId,
      @PathVariable String id, @RequestBody ResetStaffPwdByAdminRequest request) {
    if (StringUtils.isEmpty(id) || request.invalid()) {
      throw new FailureException(ErrorCode.INVALID_PARAMS);
    }
    final Long userId = HashUtils.decode(id);
    final Long organizationId = HashUtils.decode(request.getOrganizationId());
    userService.resetUserPasswordByAdmin(userId, adminUserId, organizationId);
    return Response.instanceSuccess();
  }

  /**
   * 用户首次登录时完善个人信息
   *
   * @param userId 当前登录用户id
   * @param request 密码
   */
  @PostMapping(value = "/user/init")
  public Response initUser(@CurrentUserId Long userId, @RequestBody InitUserRequest request) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (isUserOfRole(UserRole.DOCTOR, authentication)) {

      if (!request.validateDoctor()) {
        throw new FailureException(ErrorCode.INVALID_PARAMS, "invalid request params: " + request);
      }

    } else if (isUserOfRole(UserRole.ASSISTANT, authentication)) {
      if (!request.validDoctorOrAssistant()) {
        throw new FailureException(ErrorCode.INVALID_PARAMS, "invalid request params: " + request);
      }
    } else if (isUserOfRole(UserRole.ADMIN, authentication)) {
      if (!request.validAdmin()) {
        throw new FailureException(ErrorCode.INVALID_PARAMS, "invalid request params: " + request);
      }
    } else if (isUserOfRole(UserRole.OPERATOR, authentication)) {
      if (!request.validOperator()) {
        throw new FailureException(ErrorCode.INVALID_PARAMS, "invalid request params: " + request);
      }
    }
    //check name
    if (!ValidationUtils.isValidName(request.getName())) {
      logger.warn("user'name is invalid ! {}", request.getName());
      throw new FailureException(UserErrorCode.INVALID_USER_NAME);
    }
    userService.completeStaffInfo(userId, request);
    return Response.instanceSuccess();
  }

  /**
   * 管理员审核医生/助理
   *
   * @param userReview 被审核信息
   * @return 是否成功
   */
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping(value = "/admin/review/user")
  public Response adminReviewUser(@Valid @RequestBody UserReview userReview) {
    if (userReview.invalid()) {
      logger.warn("user role invalid ! {}", userReview);
      throw new FailureException(ErrorCode.INVALID_PARAMS);
    }
    userService.reviewUser(userReview);
    return Response.instanceSuccess();
  }

  /**
   * 用户自己重置自己的密码 (未登录用户), A.K.A 忘记密码
   *
   * @param request 包括用户名、验证码和新密码
   * @return 响应
   */
  @PostMapping(value = "/user/password")
  public Response resetUserPassword(@RequestBody PasswordForgotRequest request) {
    if (request == null || !request.valid()) {
      throw new FailureException(ErrorCode.INVALID_PARAMS, "invalid request params: " + request);
    }
    userService
        .resetUserPassword(request.getUsername(), request.getNewPassword(), request.getVcode());
    return Response.instanceSuccess();
  }

  /**
   * 用户自己修改自己的密码（已登录用户)
   *
   * @param userId 当前登录用户
   * @param request 修改请求
   * @return 响应
   */
  @PatchMapping(value = "/user/password")
  public Response updateUserPassword(@CurrentUserId Long userId,
      @RequestBody PasswordUpdateRequest request) {
    if (request == null || !request.valid()) {
      throw new FailureException(ErrorCode.INVALID_PARAMS, "invalid input params: " + request);
    }
    userService.updateUserPassword(userId, request.getNewPassword(), request.getOriginalPassword());
    return Response.instanceSuccess();
  }

  /**
   * 发送验证码
   *
   * @param body 请求body
   * @return 返回结果
   */
  @PostMapping(value = "/user/verification")
  public Response sendVcode(@RequestBody Map<String, String> body) {
    if (body == null || body.get("phone") == null) {
      throw new FailureException(ErrorCode.INVALID_PARAMS);
    }
    String phone = body.get("phone");
    if (!cn.xinzhili.user.api.util.ValidationUtils.isPhoneNumberValid(phone)) {
      throw new FailureException(ErrorCode.INVALID_PARAMS, "phone number is not right");
    }
    notifyService.sendVcode(phone);
    return Response.instanceSuccess();
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping(value = "/admin/staff/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public Response handleStaffFileUpload(@CurrentUserId Long adminUserId,
      @RequestParam MultipartFile file, @RequestParam String departmentId,
      @RequestParam String organizationId,
      @RequestParam UserRole role) throws IOException {
    try (InputStream is = file.getInputStream()) {
      return userService.handleStaffFileUpload(is, adminUserId, HashUtils.decode(departmentId),
          HashUtils.decode(organizationId), role);
    }
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping(value = "/admin/patient/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public Response handlePatientFileUpload(@RequestParam("organizationId") String organizationId,
      @RequestParam MultipartFile file) throws IOException {
    try (InputStream is = file.getInputStream()) {
      return userService.handlePatientFileUpload(HashUtils.decode(organizationId), is);
    }
  }

  @PreAuthorize("hasAnyRole('DOCTOR','ASSISTANT','OPERATOR')")
  @GetMapping(value = "/user/avatar")
  public Response getAvatarUploadInfo(@RequestParam("filename") String filename) {
    if (StringUtils.isEmpty(filename)) {
      logger.warn("filename is invalid !");
      throw new FailureException(ErrorCode.INVALID_PARAMS);
    }
    AvatarUploadInfoResponse response = userService.getAvatarUploadInfo(filename);
    return Response.instanceSuccess(response);
  }

  @PreAuthorize("hasAnyRole('DOCTOR','ASSISTANT','OPERATOR')")
  @GetMapping(value = "/user/certificate")
  public Response getCertificateUploadInfo(@RequestParam("filename") String filename) {
    if (StringUtils.isEmpty(filename)) {
      logger.warn("filename is empty ! ");
      throw new FailureException(ErrorCode.INVALID_PARAMS);
    }
    CertificateUploadInfoResponse response = userService
        .getCertificateUploadInfo(Collections.singletonList(filename));
    return Response.instanceSuccess(response);
  }

  @PreAuthorize("hasAnyRole('DOCTOR')")
  @GetMapping(value = "/consultation/patients")
  public Response getPatientByConsultationDoctor(
      @CurrentUserId Long userId,
      @RequestParam(value = "pageAt", defaultValue = "1") Integer pageAt,
      @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
  ) {
    return Response
        .instanceSuccess(userService.getPatientByConsultationDoctor(userId, pageAt, pageSize));
  }

  @PreAuthorize("hasAnyRole('DOCTOR','ASSISTANT')")
  @GetMapping(value = "/user/patient/region")
  public Response getPatientRegionInfo(@CurrentUserId Long staffId) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserRole role;
    if (isUserOfRole(UserRole.DOCTOR, authentication)) {
      role = UserRole.DOCTOR;
    } else if (isUserOfRole(UserRole.ASSISTANT, authentication)) {
      role = UserRole.ASSISTANT;
    } else {
      logger.warn("staff invalid ! staffId {}", staffId);
      throw new FailureException(ErrorCode.INVALID_PARAMS);
    }
    PatientRegionListResponse response = userService.getStaffPatientRegion(role, staffId);
    return Response.instanceSuccess(response);
  }


  @PreAuthorize("hasRole('OPERATOR')")
  @GetMapping(value = "/user/operator/institution")
  public Response getAllOrganizationId(@CurrentUserId Long operatorId) {
    return Response.instanceSuccess(userService.getAllOperatorOrganization(operatorId));
  }

  @PreAuthorize("hasAnyRole('DOCTOR')")
  @PostMapping(value = "/future/doctor")
  public Response addFutureDoctor(@CurrentUserId Long uid,
      @RequestBody @Valid AddFutureDoctorApiRequest request) {
    userService.addFutureDoctor(uid, request);
    return Response.instanceSuccess();
  }

  @PreAuthorize("hasAnyRole('DOCTOR')")
  @PatchMapping(value = "/future/doctor")
  public Response updateFutureDoctor(@CurrentUserId Long uid,
      @RequestBody @Valid UpdateFutureDoctorApiRequest request) {

    FutureDoctorApiInfo futureDoctorApiInfo = userService.updateFutureDoctor(uid, request);
    return Response.instanceSuccess(futureDoctorApiInfo);
  }

  @PreAuthorize("hasAnyRole('DOCTOR')")
  @GetMapping(value = "/future/doctor")
  public Response getFutureDoctorByDoctorId(@CurrentUserId Long uid,
      @RequestParam(value = "type") FutureDoctorType type) {
    FutureDoctorApiInfo futureDoctorApiInfo = userService
        .getFutureDoctorByDoctorId(uid, type);
    if (futureDoctorApiInfo == null) {
      return Response.instanceSuccess();
    }
    return Response.instanceSuccess(futureDoctorApiInfo);
  }
}
