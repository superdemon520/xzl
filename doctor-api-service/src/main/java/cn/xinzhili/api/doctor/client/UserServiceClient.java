package cn.xinzhili.api.doctor.client;

import cn.xinzhili.api.doctor.bean.UserRole;
import cn.xinzhili.user.api.DepartmentInfo;
import cn.xinzhili.user.api.FutureDoctorInfo;
import cn.xinzhili.user.api.FutureDoctorType;
import cn.xinzhili.user.api.PatientInfo;
import cn.xinzhili.user.api.PatientProgress;
import cn.xinzhili.user.api.PatientStatus;
import cn.xinzhili.user.api.RiskFactor;
import cn.xinzhili.user.api.ServiceLevel;
import cn.xinzhili.user.api.StaffInfo;
import cn.xinzhili.user.api.StaffRelationshipStatus;
import cn.xinzhili.user.api.StaffRole;
import cn.xinzhili.user.api.StaffStatus;
import cn.xinzhili.user.api.UserReviewInfo;
import cn.xinzhili.user.api.request.AddDepartmentRequest;
import cn.xinzhili.user.api.request.AddFutureDoctorRequest;
import cn.xinzhili.user.api.request.AddPatientRequest;
import cn.xinzhili.user.api.request.AddStaffRequest;
import cn.xinzhili.user.api.request.BatchUpdatePatientRelationRequest;
import cn.xinzhili.user.api.request.DelAssistantPatientRelationApiRequest;
import cn.xinzhili.user.api.request.DeleteOperatorRequest;
import cn.xinzhili.user.api.request.GetImageUploadInfoRequest;
import cn.xinzhili.user.api.request.InitStaffRequest;
import cn.xinzhili.user.api.request.MoveDepartmentRequest;
import cn.xinzhili.user.api.request.PasswordResetRequest;
import cn.xinzhili.user.api.request.PatientBatchInsertRequest;
import cn.xinzhili.user.api.request.PatientRelativeNotifyRequest;
import cn.xinzhili.user.api.request.RenameDepartmentRequest;
import cn.xinzhili.user.api.request.ResetPendingMessageRequest;
import cn.xinzhili.user.api.request.StaffBatchInsertRequest;
import cn.xinzhili.user.api.request.UpdateFutureDoctorRequest;
import cn.xinzhili.user.api.request.UpdatePatientAndRelationRequest;
import cn.xinzhili.user.api.request.UpdatePatientRemarkRequest;
import cn.xinzhili.user.api.request.UpdatePatientRequest;
import cn.xinzhili.user.api.request.UpdatePendingMessageRequest;
import cn.xinzhili.user.api.request.UpdateStaffRequest;
import cn.xinzhili.user.api.request.UpdateUserCertificateRequest;
import cn.xinzhili.user.api.response.ConsultationPatientListResponse;
import cn.xinzhili.user.api.response.FutureDoctorListResponse;
import cn.xinzhili.user.api.response.ImagesUploadInfoResponse;
import cn.xinzhili.user.api.response.OrganizationDetailResponse;
import cn.xinzhili.user.api.response.OrganizationResponse;
import cn.xinzhili.user.api.response.PatientDetailResponse;
import cn.xinzhili.user.api.response.PatientIdsResponse;
import cn.xinzhili.user.api.response.PatientListResponse;
import cn.xinzhili.user.api.response.PatientVisualInfoResponse;
import cn.xinzhili.user.api.response.RegionListResponse;
import cn.xinzhili.user.api.response.StaffDetailResponse;
import cn.xinzhili.user.api.response.StaffListResponse;
import cn.xinzhili.user.api.response.StaffRelationshipListResponse;
import cn.xinzhili.user.api.visual.PatientIntegratedStatus;
import cn.xinzhili.xutils.core.http.Response;
import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Date: 16/02/2017 Time: 11:14 AM
 *
 * @author Gan Dong
 */
@FeignClient(value = "user-service")
public interface UserServiceClient {

  /**
   * get hospital detail info
   *
   * @param id hospital id
   * @param userId user id
   * @return detail bean
   */
  @RequestMapping(method = RequestMethod.GET, value = "/organization/{id}")
  Response<OrganizationDetailResponse> getOrgInfoOfUser(
      @PathVariable(value = "id") long id,
      @RequestParam(value = "userId") Long userId);


  /**
   * get hospitals info
   */
  @RequestMapping(value = "/organization", method = RequestMethod.GET)
  Response<OrganizationResponse> getOrganization(
      @RequestParam(value = "pageAt", required = false) Integer pageAt,
      @RequestParam(value = "pageSize", required = false) Integer pageSize);

  /**
   * Add the department with hospital id
   *
   * @param request department name
   * @return the department detail info
   */
  @RequestMapping(value = "/department", method = RequestMethod.POST
      , consumes = MediaType.APPLICATION_JSON_VALUE)
  Response<DepartmentInfo> addOrgDepartment(
      @RequestBody AddDepartmentRequest request);

  /**
   * Rename department name by id
   *
   * @param request department id and department name
   * @return result
   */
  @RequestMapping(value = "/department", method = RequestMethod.PATCH,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  Response renameDepartment(@RequestBody RenameDepartmentRequest request);

  /**
   * Delete a department.
   *
   * @param id department id
   * @return Success response if ok otherwise fail response
   */
  @RequestMapping(value = "/department/{id}", method = RequestMethod.DELETE)
  Response deleteDepartment(@RequestParam(value = "userId") long adminUserId,
      @PathVariable("id") long id);

  /**
   * get user details by its id
   *
   * @param id userId
   * @param withDepartment whether to get department detail info
   * @return user details
   */
  @RequestMapping(value = "/doctor/user/{id}", method = RequestMethod.GET)
  Response<StaffDetailResponse> getUserDetails(@PathVariable("id") long id,
      @RequestParam(value = "organizationId", required = false) Long organizationId,
      @RequestParam(value = "withDepartment") boolean withDepartment);

  /**
   * add user by user info
   *
   * @param request user info
   * @return status
   */
  @RequestMapping(value = "/doctor/user", method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  Response addUser(@RequestBody AddStaffRequest request);

  /**
   * doctor list
   *
   * @param title title
   * @param organizationId org id
   * @param departmentId dep id
   * @param adminUserId do this work's admin id
   * @param pageSize page size
   * @param pageAt page at   @return doctors
   */
  @RequestMapping(value = "/doctor/users/doctor", method = RequestMethod.GET,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  Response<StaffListResponse> getDoctorPagination(
      @RequestParam(value = "title", required = false) String title,
      @RequestParam("organizationId") long organizationId,
      @RequestParam("departmentId") long departmentId,
      @RequestParam("adminUserId") long adminUserId,
      @RequestParam("keyword") String keyword,
      @RequestParam(value = "statuses", required = false) List<StaffStatus> statuses,
      @RequestParam(value = "pageAt") Integer pageAt,
      @RequestParam(value = "pageSize") Integer pageSize);

  /**
   * update doctor
   *
   * @param request doctor info
   * @return new doctor info
   */
  @RequestMapping(value = "/doctor/user/doctor", method = RequestMethod.PATCH,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  Response<StaffInfo> updateDoctor(@RequestBody UpdateStaffRequest request);

  /**
   * delete doctors by id
   *
   * @param ids doctor ids
   * @return is success
   */
  @RequestMapping(value = "/doctor/users/doctor", method = RequestMethod.DELETE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  Response deleteDoctors(@RequestBody List<Long> ids);

  /**
   * assistant list
   *
   * @param organizationId org id
   * @param departmentId dep id
   * @param pageAt page at
   * @param pageSize page size
   * @param adminUserId do this work's admin id    @return assistants
   */
  @RequestMapping(value = "/doctor/users/assistant", method = RequestMethod.GET)
  Response<StaffListResponse> getAssistantPagination(
      @RequestParam("organizationId") long organizationId,
      @RequestParam("departmentId") long departmentId,
      @RequestParam(value = "doctorId", required = false) Long doctorId,
      @RequestParam("keyword") String keyword,
      @RequestParam(value = "isBindingPatient", required = false) Boolean isBindingPatient,
      @RequestParam(value = "statuses", required = false) List<StaffStatus> statuses,
      @RequestParam(value = "pageAt") Integer pageAt,
      @RequestParam(value = "pageSize") Integer pageSize,
      @RequestParam("adminUserId") long adminUserId);


  @RequestMapping(value = "/doctor/users/operator", method = RequestMethod.GET)
  Response<StaffListResponse> getOperatorPagination(
      @RequestParam("organizationId") long organizationId,
      @RequestParam("departmentId") long departmentId,
      @RequestParam(value = "relationshipStatuses", required = false) List<StaffRelationshipStatus> relationshipStatuses,
      @RequestParam("keyword") String keyword,
      @RequestParam(value = "isBindingPatient", required = false) Boolean isBindingPatient,
      @RequestParam(value = "statuses", required = false) List<StaffStatus> statuses,
      @RequestParam(value = "pageAt") Integer pageAt,
      @RequestParam(value = "pageSize") Integer pageSize,
      @RequestParam("adminUserId") long adminUserId);

  /**
   * update assistant
   *
   * @param request assistant info
   * @return new assistant info
   */
  @RequestMapping(value = "/doctor/user/assistant", method = RequestMethod.PATCH,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  Response<StaffInfo> updateAssistant(@RequestBody UpdateStaffRequest request);


  @RequestMapping(value = "/doctor/user/operator", method = RequestMethod.PATCH,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  Response<StaffInfo> updateOperator(@RequestBody UpdateStaffRequest request);

  /**
   * delete assistants by id
   *
   * @param ids assistant ids
   * @return is success
   */
  @RequestMapping(value = "/doctor/users/assistant", method = RequestMethod.DELETE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  Response deleteAssistants(@RequestBody List<Long> ids);


  @RequestMapping(value = "/doctor/users/operator", method = RequestMethod.DELETE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  Response deleteOperator(@RequestBody DeleteOperatorRequest request);

  /**
   * Move staffs from one department to another.
   *
   * @param mdr request bean
   * @return Response
   */
  @RequestMapping(value = "/doctor/users/department", method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  Response moveStaffDepartment(@RequestBody MoveDepartmentRequest mdr);

  /**
   * get patient list by criteria
   *
   * @param pageAt page at
   * @param pageSize page size
   * @return assistants
   */
  @RequestMapping(value = "/patient/criteria", method = RequestMethod.GET)
  Response<PatientListResponse> getPatientList(
      @RequestParam(value = "organizationId") Long organizationId,
      @RequestParam(value = "departmentId") Long departmentId,
      @RequestParam(value = "doctorId") Long doctorId,
      @RequestParam(value = "assistantId") Long assistantId,
      @RequestParam(value = "operatorId") Long operatorId,
      @RequestParam(value = "keyword") String keyword,
      @RequestParam(value = "province") Integer province,
      @RequestParam(value = "ageMin") Integer ageMin,
      @RequestParam(value = "ageMax") Integer ageMax,
      @RequestParam(value = "risks") List<RiskFactor> risks,
      @RequestParam(value = "serviceLevel") ServiceLevel level,
      @RequestParam(value = "createdAtStart") Long createdAtStart,
      @RequestParam(value = "createdAtEnd") Long createdAtEnd,
      @RequestParam(value = "pageAt", defaultValue = "0") Integer pageAt,
      @RequestParam(value = "pageSize", defaultValue = "15") Integer pageSize,
      @RequestParam(value = "haveAssistant") Boolean haveAssistant,
      @RequestParam(value = "excludeAssistant") boolean excludeAssistant,
      @RequestParam(value = "excludeDoctor") boolean excludeDoctor,
      @RequestParam(value = "excludeOperator") boolean excludeOperator,
      @RequestParam(value = "excludedRisks") List<RiskFactor> excludedRisks,
      @RequestParam(value = "integratedStatus") PatientIntegratedStatus integratedStatus,
      @RequestParam(value = "includesNullAge") boolean includesNullAge,
      @RequestParam(value = "progress") PatientProgress progress,
      @RequestParam(value = "statuses") List<PatientStatus> statuses,
      @RequestParam(value = "role") StaffRole role
  );

  /**
   * get patient id list by staff role and id
   *
   * @param role staff role
   * @param staffId staff id
   * @return patient ids response
   */
  @RequestMapping(value = "/patient/ids", method = RequestMethod.GET)
  Response<PatientIdsResponse> getPatientIds(
      @RequestParam(value = "role") UserRole role,
      @RequestParam(value = "staffId") Long staffId);

  /**
   * add patient
   *
   * @param request patient info
   * @return patient id
   */
  @RequestMapping(value = "/patient/user", method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  Response addPatient(@RequestBody AddPatientRequest request);

  @RequestMapping(value = "/patient/remark/{id}", method = RequestMethod.PATCH,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  Response<PatientInfo> updatePatientRemark(@PathVariable(value = "id") long id,
      @RequestBody UpdatePatientRemarkRequest body);

  /**
   * delete patients by id
   *
   * @param ids patient ids
   * @return is success
   */
  @RequestMapping(value = "/patient/users", method = RequestMethod.DELETE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  Response deletePatients(@RequestBody List<Long> ids);

  /**
   * 查询用户所有信息
   *
   * @param id 用户id
   * @return 返回用户所有信息
   */
  @RequestMapping(value = "/patient/user/{id}", method = RequestMethod.GET)
  Response<PatientDetailResponse> searchPatientById(
      @PathVariable(value = "id") long id);

  /**
   * update patient relative info include : name ,sex ,binding doctorId,binding assistantId
   *
   * @param request update patient relative info
   * @return is success
   */
  @RequestMapping(value = "/patient/user/related", method = RequestMethod.PATCH,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  Response updatePatientRelated(@RequestBody UpdatePatientAndRelationRequest request);

  /**
   * 部分更新患者患者信息
   *
   * @param body 更新的body
   * @param id 患者id
   * @return 返回状况
   */
  @RequestMapping(value = "/patient/user/{id}", method = RequestMethod.PATCH,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  Response updatePatientWithSelective(@RequestBody UpdatePatientRequest body,
      @PathVariable(value = "id") long id);

  /**
   * 批量更新患者的绑定信息
   *
   * @param request batch update patient binding relation
   * @return is success
   */
  @RequestMapping(value = "/patient/users/binding", method = RequestMethod.PATCH,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  Response batchUpdatePatientBindingRelation(
      @RequestBody BatchUpdatePatientRelationRequest request);

  /**
   * 医生端用户重置密码、修改密码接口
   *
   * @param id 目标人员用户id，不能为空
   * @param request 修改请求
   * @return 请求响应
   */
  @RequestMapping(value = "/doctor/user/{id}/password", method = RequestMethod.PATCH,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  Response resetPassword(@PathVariable(value = "id") long id,
      @RequestBody PasswordResetRequest request);

  /**
   * 医生端用户初始化用户信息接口
   *
   * @param id 用户id
   * @param request 请求payload
   * @return 返回结果
   */
  @RequestMapping(value = "/doctor/user/{id}/init", method = RequestMethod.PATCH,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  Response completeUserInfo(@PathVariable(value = "id") long id,
      @RequestBody InitStaffRequest request);

  @PatchMapping(value = "/doctor/user/{id}/certificate", consumes = MediaType.APPLICATION_JSON_VALUE)
  Response updateUserCertificate(@PathVariable(value = "id") long id,
      @RequestBody UpdateUserCertificateRequest request);

  /**
   * 通过username查找相关任务的所有信息
   *
   * @param username 用户登陆账户
   * @return 返回的staff 里有staffInfo
   */
  @RequestMapping(value = "/doctor/user", method = RequestMethod.GET)
  Response searchStaffByUsername(@RequestParam(value = "username") String username);


  /**
   * Update patient notification.
   *
   * @param request request body
   * @return Response
   */
  @RequestMapping(value = "/patient/notification/message/reset",
      method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
  Response resetPatientPendingMessage(@RequestBody ResetPendingMessageRequest request);

  @RequestMapping(value = "/patient/notification/message",
      method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
  Response updatePatientMessageNotification(@RequestBody UpdatePendingMessageRequest request);

  @RequestMapping(value = "/region", method = RequestMethod.GET)
  Response<RegionListResponse> getPatientRegionInfo(@RequestParam(value = "pid") int pid);

  @RequestMapping(value = "/patient/users/binding/assistant", method = RequestMethod.DELETE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  Response deleteAssistantRelation(DelAssistantPatientRelationApiRequest request);

  @RequestMapping(value = "patient/visual/count", method = RequestMethod.GET)
  Response<PatientVisualInfoResponse> getVisualCountInfo(@RequestParam("staffId") Long staffId,
      @RequestParam("role") StaffRole role);

  @RequestMapping(value = "/doctor/users/staffs", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  Response bulkInsertStaffs(@RequestBody StaffBatchInsertRequest request);

  @RequestMapping(value = "/patient/user/patients/{organizationId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  Response bulkInsertPatients(@PathVariable("organizationId") Long organizationId,
      @RequestBody PatientBatchInsertRequest request);

  /**
   * 申请图片上传url
   */
  @RequestMapping(method = RequestMethod.POST, value = "/images",
      consumes = MediaType.APPLICATION_JSON_VALUE)
  Response<ImagesUploadInfoResponse> getImagesUploadInfo(
      @RequestBody GetImageUploadInfoRequest request);

  @GetMapping(value = "/doctor/patient/region")
  Response<RegionListResponse> getPatientRegionInfo(@RequestParam("role") StaffRole role,
      @RequestParam("staffId") Long staffId);

  @PostMapping(value = "/relative/notify", consumes = MediaType.APPLICATION_JSON_VALUE)
  Response pushNotify(@RequestBody PatientRelativeNotifyRequest request);

  @PostMapping(value = "/doctor/admin/review/user", consumes = MediaType.APPLICATION_JSON_VALUE)
  Response reviewUser(@RequestBody UserReviewInfo userReviewInfo);

  @GetMapping(value = "/patient/user/v1/{id}")
  Response<PatientDetailResponse> getPatientFromAllById(@PathVariable("id") long id);

  @GetMapping(value = "/patient/consultation/patients")
  Response<ConsultationPatientListResponse> getPatientByConsultationDoctor(
      @RequestParam("doctorId") Long doctorId,
      @RequestParam(value = "pageAt", defaultValue = "1") Integer pageAt,
      @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
  );

  @GetMapping(value = "/doctor/consultation/doctor")
  Response<StaffListResponse> getConsultationDoctor(
      @RequestParam("doctorName") String doctorName,
      @RequestParam("organizationId") Long organizationId,
      @RequestParam("departmentId") Long departmentId,
      @RequestParam("userId") long userId,
      @RequestParam(value = "pageSize", defaultValue = "15") Integer pageSize,
      @RequestParam(value = "pageAt", defaultValue = "1") Integer pageAt);

  @GetMapping(value = "/doctor/staff/relationship")
  Response<StaffRelationshipListResponse> getStaffRelationship(
      @RequestParam("staffId") long staffId);

  @PostMapping(value = "/future/doctor", consumes = MediaType.APPLICATION_JSON_VALUE)
  Response addFutureDoctor(@RequestBody AddFutureDoctorRequest request);

  @PatchMapping(value = "/future/doctor", consumes = MediaType.APPLICATION_JSON_VALUE)
  Response<FutureDoctorInfo> updateFutureDoctor(@RequestBody UpdateFutureDoctorRequest request);

  @GetMapping(value = "/future/doctor")
  Response<FutureDoctorListResponse> getFutureDoctorByDoctorId(
      @RequestParam(value = "doctorId") long doctorId,
      @RequestParam(value = "type") FutureDoctorType type);

}
