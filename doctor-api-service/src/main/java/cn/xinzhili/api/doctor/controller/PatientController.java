package cn.xinzhili.api.doctor.controller;

import static cn.xinzhili.api.doctor.util.AuthUtils.isUserOfRole;

import cn.xinzhili.api.doctor.bean.PatientApiInfo;
import cn.xinzhili.api.doctor.bean.UserRole;
import cn.xinzhili.api.doctor.bean.request.AddPatientApiRequest;
import cn.xinzhili.api.doctor.bean.request.DelAssistantPatientRelationRequest;
import cn.xinzhili.api.doctor.bean.request.GetPatientListApiRequest;
import cn.xinzhili.api.doctor.bean.request.UpdatePatientApiRequest;
import cn.xinzhili.api.doctor.bean.request.UpdatePatientBindingRequest;
import cn.xinzhili.api.doctor.bean.request.UpdatePatientRemarkApiRequest;
import cn.xinzhili.api.doctor.bean.response.PatientDetailApiResponse;
import cn.xinzhili.api.doctor.bean.response.PatientListApiResponse;
import cn.xinzhili.api.doctor.error.UserErrorCode;
import cn.xinzhili.api.doctor.service.UserService;
import cn.xinzhili.api.doctor.util.AuthUtils;
import cn.xinzhili.api.doctor.util.PatientFactory;
import cn.xinzhili.api.doctor.util.ValidationUtils;
import cn.xinzhili.user.api.PatientProgress;
import cn.xinzhili.user.api.PatientStatus;
import cn.xinzhili.user.api.StaffRole;
import cn.xinzhili.user.api.request.GetPatientByCriteriaRequest;
import cn.xinzhili.user.api.response.PatientVisualInfoResponse;
import cn.xinzhili.xutils.auth.CurrentUserId;
import cn.xinzhili.xutils.core.ErrorCode;
import cn.xinzhili.xutils.core.FailureException;
import cn.xinzhili.xutils.core.http.Response;
import cn.xinzhili.xutils.core.util.HashUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Date: 08/10/2016 Time: 2:02 PM
 *
 * @author Gan Dong
 */
@RestController
@RequestMapping("/user/patient")
public class PatientController {

  private static final Logger logger = LoggerFactory
      .getLogger(PatientController.class);

  @Autowired
  private UserService userService;

  private List<Long> IdsDecode(List<String> ids) {
    List<Long> idsDecoded = new ArrayList<>();
    for (String id : ids) {
      idsDecoded.add(HashUtils.decode(id));
    }
    return idsDecoded;
  }

  @PreAuthorize("hasAuthority('PATIENT_READ')")
  @GetMapping(value = "/{patientId}")
  public Response getPatientInfo(@PathVariable String patientId) {
    Authentication authentication = SecurityContextHolder.getContext()
        .getAuthentication();
    PatientDetailApiResponse patientDetail;
    if (AuthUtils.isUserOfRole(UserRole.ADMIN, authentication)) {
      patientDetail = userService.getPatientFromAllById(HashUtils.decode(patientId));
    } else {
      patientDetail = userService.getPatientById(HashUtils.decode(patientId));
    }

    if (patientDetail == null) {
      throw new FailureException(
          ErrorCode.REQUEST_FAILED, "获取患者详情失败");
    }
    return Response.instanceSuccess(patientDetail);
  }

  @PreAuthorize("hasAuthority('PATIENT_READ')")
  @GetMapping
  public Response getPatientList(
      @CurrentUserId long userId,
      @RequestParam(value = "pageAt", defaultValue = "0") Integer pageAt,
      @RequestParam(value = "pageSize", defaultValue = "15") Integer pageSize,
      @ModelAttribute("request") GetPatientListApiRequest request) {

    //if patient's createdAtStart is after createdAtEnd  ,return empty response
    if (!request.checkCreatedAt()) {
      return Response.instanceSuccess(new PatientListApiResponse());
    }

    List<PatientStatus> statuses = new ArrayList<>();
    statuses.add(PatientStatus.NORMAL);
    StaffRole role = null;
    //set current userId into request
    Authentication authentication = SecurityContextHolder.getContext()
        .getAuthentication();
    if (AuthUtils.isUserOfRole(UserRole.DOCTOR, authentication)) {
      role = StaffRole.DOCTOR;
      request.setDoctorId(HashUtils.encode(userId));
    }else if (AuthUtils.isUserOfRole(UserRole.ASSISTANT, authentication)) {
      role = StaffRole.ASSISTANT;
      request.setAssistantId(HashUtils.encode(userId));
    }else if (AuthUtils.isUserOfRole(UserRole.ADMIN, authentication)) {
      statuses.add(PatientStatus.PENDING);
      role = StaffRole.ADMIN;
    }else if (AuthUtils.isUserOfRole(UserRole.OPERATOR, authentication)){
      role = StaffRole.OPERATOR;
      request.setOperatorId(HashUtils.encode(userId));
    }

    ValidationUtils.validatePaginationParams(pageAt, pageSize);
    GetPatientByCriteriaRequest ofRequest = PatientFactory.of(request);
    ofRequest.setStatuses(statuses);
    PatientListApiResponse response = userService.getPatientList(ofRequest, role,
        request.getCreatedAtStart(), request.getCreatedAtEnd(), pageAt, pageSize, userId);
    if (response == null) {
      throw new FailureException(
          ErrorCode.REQUEST_FAILED, "get patient list fail !");
    }
    return Response.instanceSuccess(response);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public Response addPatient(@RequestBody AddPatientApiRequest request) {

    if (request.invalid()) {
      logger.warn("add patient param invalid ! {}", request.toString());
      throw new FailureException(ErrorCode.INVALID_PARAMS);
    }
    userService.addPatient(PatientFactory.of(request));
    return Response.instanceSuccess();
  }

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping
  public Response deletePatients(@RequestBody List<String> ids) {

    boolean result = userService.deletePatient(IdsDecode(ids));
    if (!result) {
      logger.error("delete patients fail !");
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    }
    return Response.instanceSuccess();
  }

  @PreAuthorize("hasAuthority('PATIENT_WRITE')")
  @PatchMapping
  public Response updatePatient(
      @RequestBody UpdatePatientApiRequest request) {

    if (request.invalid()) {
      logger.error("request -> {}", request);
      throw new FailureException(
          ErrorCode.INVALID_PARAMS, "patient id is null !");
    }

    //check name
    if (!StringUtils.isEmpty(request.getName())
        && !ValidationUtils.isValidName(request.getName())) {
      logger.error("patient'name is invalid ! {}", request.getName());
      throw new FailureException(UserErrorCode.INVALID_USER_NAME);
    }

    Authentication authentication = SecurityContextHolder.getContext()
        .getAuthentication();
    if (AuthUtils.isUserOfRole(UserRole.ADMIN, authentication)) {

      userService.updatePatientRelatedInfo(PatientFactory.ofRelationRequest(request));

    } else if (AuthUtils.isUserOfRole(UserRole.OPERATOR, authentication)) {
      long patientId = HashUtils.decode(request.getId());
      userService.updatePatientWithSelective(patientId, PatientFactory.of(request));
    }
    return Response.instanceSuccess();
  }


  @PreAuthorize("hasRole('OPERATOR')")
  @PatchMapping("/remark/{patientId}")
  public Response updatePatientRemark(@PathVariable("patientId") String patientId,
      @RequestBody UpdatePatientRemarkApiRequest request) {

    PatientApiInfo patient = userService.updatePatientRemark(HashUtils.decode(patientId),
        request.getRemark(), PatientProgress.valueOf(request.getProgress().name()));
    return Response.instanceSuccess(patient);
  }

  @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
  @PatchMapping("/binding")
  public Response updatePatientsBindingRelation(@RequestBody UpdatePatientBindingRequest request) {
    if (request.invalid()) {
      throw new FailureException(ErrorCode.INVALID_PARAMS, "request is invalid !" + request);
    }
    userService.updatePatientsBinding(PatientFactory.of(request));
    return Response.instanceSuccess();
  }

  /**
   *
   * @param doctorId
   * @param request
   * @throws FailureException
   */
  @PreAuthorize("hasRole('DOCTOR')")
  @DeleteMapping("/binding/assistant")
  public Response deleteAssistantBindingRelation(@CurrentUserId Long doctorId,
      @RequestBody DelAssistantPatientRelationRequest request) {

    List<String> assistantIds = request.getAssistantIds();
    if (doctorId == null || assistantIds == null || assistantIds.isEmpty()) {
      throw new FailureException(
          ErrorCode.INVALID_PARAMS, "request is invalid !");
    }
    userService.deleteAssistantRelation(PatientFactory.of(doctorId, request.getAssistantIds()));
    return Response.instanceSuccess();
  }

  @PreAuthorize("hasAnyRole('DOCTOR','ASSISTANT')")
  @GetMapping("/visual/count")
  public Response getIndexVisualInfo(@CurrentUserId Long uid) {

    // get role
    Authentication authentication = SecurityContextHolder.getContext()
        .getAuthentication();

    UserRole role = null;
    if (isUserOfRole(UserRole.DOCTOR, authentication)) {
      role = UserRole.DOCTOR;
    } else if (isUserOfRole(UserRole.ASSISTANT, authentication)) {
      role = UserRole.ASSISTANT;
    }

    PatientVisualInfoResponse response = userService.getIndexVisualInfo(role, uid);
    return Response.instanceSuccess(response);
  }
}
