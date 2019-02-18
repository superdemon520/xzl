package cn.xinzhili.api.doctor.service;

import cn.xinzhili.api.doctor.bean.Assistant;
import cn.xinzhili.api.doctor.bean.Department;
import cn.xinzhili.api.doctor.bean.Doctor;
import cn.xinzhili.api.doctor.bean.FutureDoctorApiInfo;
import cn.xinzhili.api.doctor.bean.ImageUploadInfo;
import cn.xinzhili.api.doctor.bean.Institution;
import cn.xinzhili.api.doctor.bean.Operator;
import cn.xinzhili.api.doctor.bean.PatientApiInfo;
import cn.xinzhili.api.doctor.bean.StaffUploadBean;
import cn.xinzhili.api.doctor.bean.UnreadApiStatus;
import cn.xinzhili.api.doctor.bean.User;
import cn.xinzhili.api.doctor.bean.UserChatStatus;
import cn.xinzhili.api.doctor.bean.UserInviteStatus;
import cn.xinzhili.api.doctor.bean.UserReview;
import cn.xinzhili.api.doctor.bean.UserRole;
import cn.xinzhili.api.doctor.bean.UserStatus;
import cn.xinzhili.api.doctor.bean.request.AddFutureDoctorApiRequest;
import cn.xinzhili.api.doctor.bean.request.GetStaffListRequest;
import cn.xinzhili.api.doctor.bean.request.InitUserRequest;
import cn.xinzhili.api.doctor.bean.request.StaffDepartmentRequest;
import cn.xinzhili.api.doctor.bean.request.UpdateFutureDoctorApiRequest;
import cn.xinzhili.api.doctor.bean.request.UpdateUserCertificateApiRequest;
import cn.xinzhili.api.doctor.bean.response.AssistantListResponse;
import cn.xinzhili.api.doctor.bean.response.AvatarUploadInfoResponse;
import cn.xinzhili.api.doctor.bean.response.CertificateUploadInfoResponse;
import cn.xinzhili.api.doctor.bean.response.ConsultationPatientListApiResponse;
import cn.xinzhili.api.doctor.bean.response.DoctorListResponse;
import cn.xinzhili.api.doctor.bean.response.InstitutionDetailResponse;
import cn.xinzhili.api.doctor.bean.response.InstitutionResponse;
import cn.xinzhili.api.doctor.bean.response.OperatorListResponse;
import cn.xinzhili.api.doctor.bean.response.PatientDetailApiResponse;
import cn.xinzhili.api.doctor.bean.response.PatientListApiResponse;
import cn.xinzhili.api.doctor.bean.response.PatientRegionListResponse;
import cn.xinzhili.api.doctor.bean.response.StatisticsPatientsApiResponse;
import cn.xinzhili.api.doctor.bean.response.UserDetailResponse;
import cn.xinzhili.api.doctor.client.NotifyServiceClient;
import cn.xinzhili.api.doctor.client.UserServiceClient;
import cn.xinzhili.api.doctor.error.InstitutionErrorCode;
import cn.xinzhili.api.doctor.error.NotifyErrorCode;
import cn.xinzhili.api.doctor.error.PatientErrorCode;
import cn.xinzhili.api.doctor.error.UserErrorCode;
import cn.xinzhili.api.doctor.util.ConsultationFactory;
import cn.xinzhili.api.doctor.util.DepartmentFactory;
import cn.xinzhili.api.doctor.util.HospitalFactory;
import cn.xinzhili.api.doctor.util.InstitutionFactory;
import cn.xinzhili.api.doctor.util.PatientFactory;
import cn.xinzhili.api.doctor.util.UserInfoFactory;
import cn.xinzhili.user.api.BatchInsertPatient;
import cn.xinzhili.user.api.BatchInsertStaff;
import cn.xinzhili.user.api.DepartmentInfo;
import cn.xinzhili.user.api.FutureDoctorInfo;
import cn.xinzhili.user.api.FutureDoctorType;
import cn.xinzhili.user.api.Gender;
import cn.xinzhili.user.api.ImageScope;
import cn.xinzhili.user.api.PatientInfo;
import cn.xinzhili.user.api.PatientProgress;
import cn.xinzhili.user.api.PatientRelativeNotifyCategory;
import cn.xinzhili.user.api.RegionInfo;
import cn.xinzhili.user.api.ServiceLevel;
import cn.xinzhili.user.api.StaffInfo;
import cn.xinzhili.user.api.StaffRelationshipStatus;
import cn.xinzhili.user.api.StaffRole;
import cn.xinzhili.user.api.StaffStatus;
import cn.xinzhili.user.api.StaffTitle;
import cn.xinzhili.user.api.error.DepartmentErrorCode;
import cn.xinzhili.user.api.error.StaffErrorCode;
import cn.xinzhili.user.api.request.AddDepartmentRequest;
import cn.xinzhili.user.api.request.AddFutureDoctorRequest;
import cn.xinzhili.user.api.request.AddPatientRequest;
import cn.xinzhili.user.api.request.AddStaffRequest;
import cn.xinzhili.user.api.request.BatchUpdatePatientRelationRequest;
import cn.xinzhili.user.api.request.DelAssistantPatientRelationApiRequest;
import cn.xinzhili.user.api.request.DeleteOperatorRequest;
import cn.xinzhili.user.api.request.GetImageUploadInfoRequest;
import cn.xinzhili.user.api.request.GetPatientByCriteriaRequest;
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
import cn.xinzhili.user.api.response.ImagesUploadInfoResponse.UploadInfo;
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
import cn.xinzhili.xutils.core.ErrorCode;
import cn.xinzhili.xutils.core.FailureException;
import cn.xinzhili.xutils.core.SystemException;
import cn.xinzhili.xutils.core.http.Response;
import cn.xinzhili.xutils.core.util.HashUtils;
import cn.xinzhili.xutils.core.util.ResponseUtils;
import com.google.common.base.MoreObjects;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by MarlinL on 15/02/2017. Date: 16/02/2017 Time: 11:12 AM
 *
 * @author Gan Dong
 */
@Service
public class UserService {

  private static final Logger logger = LoggerFactory.getLogger(UserService.class);

  @Autowired
  private UserServiceClient userServiceClient;
  @Autowired
  private DpcService dpcService;
  @Autowired
  private NotifyServiceClient notifyServiceClient;
  @Autowired
  private ChatService chatService;


  private static final String TEL_REG_EXP = "^1([34578])[0-9]\\d{8}$";

  private static void telExistedException(Response response) {
    //手机号被注册
    if (response != null
        && response.isFailed()
        && response.getFailureCode() == StaffErrorCode.STAFF_EXIST.getCode()) {
      logger.error("this tel is already exist !");
      throw new FailureException(UserErrorCode.TEL_ALREADY_EXIST);
    }
  }

  public Department addOrgDepartment(
      String departmentName, String orgId, Long userId) {
    AddDepartmentRequest request = new AddDepartmentRequest();
    request.setDepartmentName(departmentName);
    request.setOrganizationId(HashUtils.decode(orgId));
    request.setAdminUserId(userId);
    Response<DepartmentInfo> response =
        userServiceClient.addOrgDepartment(request);
    if (response.isFailed()) {
      if (response.getFailureCode() ==
          DepartmentErrorCode.DEPARTMENT_ALREADY_EXIST.getCode()) {
        throw new FailureException(
            InstitutionErrorCode.DEPARTMENT_ALREADY_EXISTS);
      }
    } else if (response.isError()) {
      logger.error("添加部门发生异常：request-> departmentName={},orgid={} response->{}", departmentName,
          orgId, response);
      throw new SystemException(ErrorCode.SERVER_ERROR);
    }
    return HospitalFactory
        .departmentOf(response.getDataAs(DepartmentInfo.class));
  }

  public boolean renameOrgDepartment(String name, String depId,
      Long adminUserId) {
    RenameDepartmentRequest request = new RenameDepartmentRequest();
    request.setDepartmentName(name);
    request.setId(HashUtils.decode(depId));
    request.setAdminUserId(adminUserId);
    Response response = userServiceClient.renameDepartment(request);
    defaultDataCheck(response);
    if (response.isFailed()) {
      if (response.getFailureCode() ==
          DepartmentErrorCode.DEPARTMENT_ALREADY_EXIST.getCode()) {
        throw new FailureException(
            InstitutionErrorCode.DEPARTMENT_ALREADY_EXISTS);
      }
    } else if (response.isError()) {
      logger.error("修改部门名称发生异常：{}", response);
      throw new SystemException(ErrorCode.SERVER_ERROR);
    }
    return true;
  }

  private OrganizationDetailResponse getOrgInfo(String id, Long userId) {
    Response<OrganizationDetailResponse> response = userServiceClient
        .getOrgInfoOfUser(HashUtils.decode(id), userId);

    if (response.isFailed()) {
      logger.warn("get organization info fail ! orgId {}, userId {}", id, userId);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    } else if (response.isError()) {
      logger.error("get organization info fail ! orgId {}, userId {}", id, userId);
      throw new FailureException(ErrorCode.SERVER_ERROR);
    }
    return response.getDataAs(OrganizationDetailResponse.class);
  }

  public InstitutionDetailResponse getOrgInfoOfUser(String id, Long userId) {
    return HospitalFactory.of(getOrgInfo(id, userId));
  }

  public StatisticsPatientsApiResponse getStatisticsPatients(String id, Long userId) {
    return InstitutionFactory.api(getOrgInfo(id, userId));
  }

  public UserDetailResponse getUserDetails(Long userId, Long organizationId,
      boolean withDepartment) {
    if (userId == null) {
      throw new IllegalArgumentException("userId should not be null");
    }
    Response<StaffDetailResponse> resp = userServiceClient
        .getUserDetails(userId, organizationId, withDepartment);

    if (resp.isFailed()) {

      Integer failureCode = resp.getFailureCode();
      if (StaffErrorCode.STAFF_NOT_EXIST.getCode() == failureCode) {
        logger.warn("staff not found ! {}", userId);
        throw new FailureException(UserErrorCode.NOT_FOUND_STAFF);
      }
      logger.warn("get staff fail ! {},{}", userId, resp);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    } else if (resp.isError()) {

      logger.error("get staff error !{},{}", userId, resp);
      throw new FailureException(ErrorCode.SERVER_ERROR);
    }

    StaffDetailResponse staffDetail = resp.getDataAs(StaffDetailResponse.class);

    if (Objects.isNull(staffDetail.getUser())) {
      logger.warn("staff status invalid ! {},{}", userId, resp);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    }
    UserDetailResponse userDetailResponse = new UserDetailResponse();
    userDetailResponse.setUser(UserInfoFactory.valueOf(staffDetail.getUser()));
    if (Objects.nonNull(staffDetail.getOrganization())) {
      userDetailResponse.setInstitution(InstitutionFactory.valueOf(staffDetail.getOrganization()));
    }
    if (Objects.nonNull(staffDetail.getReviewOrgId())) {
      userDetailResponse.setReviewOrgId(HashUtils.encode(staffDetail.getReviewOrgId()));
    }
    if (withDepartment && staffDetail.getDepartment() != null) {
      Department department = DepartmentFactory.valueOf(staffDetail.getDepartment());
      userDetailResponse.setDepartment(department);
    }
    return userDetailResponse;
  }


  public void addStaff(AddStaffRequest request) {

    Response response = userServiceClient.addUser(request);
    telExistedException(response);
    if (response.isFailed()) {
      logger.warn("add staff fail ! request :{} ,response : {}", request, response);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    } else if (response.isError()) {
      logger.error("add staff error ! request {} ,response {}", request, response);
      throw new FailureException(ErrorCode.SERVER_ERROR);
    }
  }

  public DoctorListResponse getDoctorPagination(long adminUserId, GetStaffListRequest request) {

    Long organizationId = HashUtils.decode(request.getOrganizationId());
    Long departmentId = HashUtils.decode(request.getDepartmentId());

    Response<StaffListResponse> response = userServiceClient.getDoctorPagination(
        request.getTitle(), organizationId, departmentId, adminUserId, request.getKeyword(),
        ofStaffStatus(request.getStatus()), request.getPageAt(), request.getPageSize());

    if (response.isFailed()) {
      logger.warn("get doctor list fail ,response {}!", response);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    } else if (response.isError()) {
      logger.error("get doctor list error ,{}", response);
      throw new FailureException(ErrorCode.SERVER_ERROR);
    }

    StaffListResponse doctorsInfo = response.getDataAs(StaffListResponse.class);
    List<Doctor> doctors = UserInfoFactory.staffInfo2Doctor(doctorsInfo.getStaffs());
    DoctorListResponse result = new DoctorListResponse();
    result.setDoctors(doctors);
    result.setTotal(doctorsInfo.getTotal());
    return result;
  }

  public DoctorListResponse getConsultationDoctor(
      String doctorName, Long organizationId, Long departmentId,
      Long userId, Integer pageSize, Integer pageAt) {

    Response<StaffListResponse> response = userServiceClient
        .getConsultationDoctor(doctorName, organizationId, departmentId, userId
            , pageSize, pageAt);
    if (response.isFailed()) {
      logger.warn("get consultation doctor list fail ,response : {}", response);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    } else if (response.isError()) {
      logger.error("get consultation doctor list error ,response {}", response);
      throw new FailureException(ErrorCode.SERVER_ERROR);
    }
    StaffListResponse doctorsInfo = response.getDataAs(StaffListResponse.class);
    List<Doctor> doctors = UserInfoFactory.staffInfo2Doctor(doctorsInfo.getStaffs());
    DoctorListResponse result = new DoctorListResponse();
    result.setDoctors(doctors);
    result.setTotal(doctorsInfo.getTotal());
    return result;
  }

  public AssistantListResponse getAssistantPagination(long adminUserId,
      GetStaffListRequest request) {

    Long organizationId = HashUtils.decode(request.getOrganizationId());
    Long departmentId = HashUtils.decode(request.getDepartmentId());
    Long doctorId =
        Objects.isNull(request.getDoctorId()) ? null : HashUtils.decode(request.getDoctorId());

    Response<StaffListResponse> response = userServiceClient
        .getAssistantPagination(organizationId, departmentId, doctorId, request.getKeyword(),
            request.getIsBindingPatient(), ofStaffStatus(request.getStatus()), request.getPageAt(),
            request.getPageSize(), adminUserId);

    if (response.isFailed()) {
      logger.warn("get assistant list fail ,response {}!", response);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    } else if (response.isError()) {
      logger.error("get assistant list error ,{}", response);
      throw new FailureException(ErrorCode.SERVER_ERROR);
    }

    StaffListResponse assistantsInfo = response.getDataAs(StaffListResponse.class);
    List<Assistant> assistants = UserInfoFactory.staffInfo2Assistant(assistantsInfo.getStaffs());
    assistants.forEach(assistant -> {
      cn.xinzhili.chat.api.UserStatus assistantChatStatus = chatService
          .getAssistantChatStatus(adminUserId, HashUtils.decode(assistant.getId()));
      assistant.setChatStatus(UserChatStatus.valueOf(assistantChatStatus.toString()));
    });
    AssistantListResponse result = new AssistantListResponse();
    result.setAssistants(assistants);
    result.setTotal(assistantsInfo.getTotal());
    return result;
  }


  public OperatorListResponse getOperatorPagination(long adminUserId, GetStaffListRequest request) {

    Long organizationId = HashUtils.decode(request.getOrganizationId());
    Long departmentId = HashUtils.decode(request.getDepartmentId());

    Response<StaffListResponse> response = userServiceClient.getOperatorPagination(organizationId
        , departmentId, of(request.getInviteStatuses()), request.getKeyword(),
        request.getIsBindingPatient(), ofStaffStatus(request.getStatus()), request.getPageAt(),
        request.getPageSize(), adminUserId);

    if (response.isFailed()) {
      logger.warn("get operator list fail ,response {}!", response);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    } else if (response.isError()) {
      logger.error("get operator list error ,{}", response);
      throw new FailureException(ErrorCode.SERVER_ERROR);
    }
    StaffListResponse operator = response.getDataAs(StaffListResponse.class);
    List<Operator> operators = operator.getStaffs().stream()
        .map(UserInfoFactory::staff2Operator).collect(Collectors.toList());
    return new OperatorListResponse(operators, operator.getTotal());
  }

  private List<StaffStatus> ofStaffStatus(List<UserStatus> status) {

    if (Objects.isNull(status)) {
      return null;
    }
    return status.stream().map(s -> StaffStatus.valueOf(s.name())).collect(Collectors.toList());
  }

  private List<StaffRelationshipStatus> of(List<UserInviteStatus> source) {
    return Objects.isNull(source) ? List.of()
        : source.stream().map(t -> StaffRelationshipStatus.valueOf(t.name()))
            .collect(Collectors.toList());
  }

  public void deleteDoctor(List<Long> ids) {

    Response response = userServiceClient.deleteDoctors(ids);
    defaultDataCheck(response);
    if (response.isFailed()) {

      if (response.getFailureCode() == StaffErrorCode.DOCTOR_HAS_PATIENTS.getCode()) {
        logger.warn("doctor has patient ,can't be delete ! response {}", response);
        throw new FailureException(UserErrorCode.DOCTOR_HAS_PATIENT);
      }
      logger.warn("doctor assistant fail ! {}", response);
      throw new FailureException(ErrorCode.REQUEST_FAILED);

    } else if (response.isError()) {
      logger.error("doctor assistants error {} !", response);
      throw new FailureException(ErrorCode.SERVER_ERROR);
    }
  }


  public void deleteAssistant(List<Long> ids) {
    Response response = userServiceClient.deleteAssistants(ids);
    defaultDataCheck(response);
    if (response.isFailed()) {

      if (response.getFailureCode() == StaffErrorCode.ASSISTANT_HAS_PATIENT.getCode()) {
        logger.warn("assistant has patient ,can't be delete ! response {}", response);
        throw new FailureException(UserErrorCode.ASSISTANT_HAS_PATIENT);
      }
      logger.warn("delete assistant fail ! {}", response);
      throw new FailureException(ErrorCode.REQUEST_FAILED);

    } else if (response.isError()) {
      logger.error("delete assistants error {} !", response);
      throw new FailureException(ErrorCode.SERVER_ERROR);
    }
  }

  public void deleteOperator(List<Long> operatorId, Long organizationId) {

    DeleteOperatorRequest request = new DeleteOperatorRequest();
    request.setOperatorId(operatorId);
    request.setOrganizationId(organizationId);
    Response response = userServiceClient.deleteOperator(request);

    defaultDataCheck(response);
    if (response.isFailed()) {

      if (response.getFailureCode() == StaffErrorCode.OPERATOR_HAS_PATIENTS.getCode()) {
        logger.warn("operator has patient ,can't be delete ! response {}", response);
        throw new FailureException(UserErrorCode.OPERATOR_HAS_PATIENT);
      }
      logger.warn("delete operator fail ! {}", response);
      throw new FailureException(ErrorCode.REQUEST_FAILED);

    } else if (response.isError()) {
      logger.error("delete operator error {} !", response);
      throw new FailureException(ErrorCode.SERVER_ERROR);
    }
  }


  public Response deleteDepartment(long userId, String departmentId) {
    return userServiceClient
        .deleteDepartment(userId, HashUtils.decode(departmentId));
  }

  public Doctor updateDoctor(UpdateStaffRequest request) {
    return (Doctor) updateStaff(request, StaffRole.DOCTOR);
  }

  public Assistant updateAssistant(UpdateStaffRequest request) {
    return (Assistant) updateStaff(request, StaffRole.ASSISTANT);
  }

  public Operator updateOperator(UpdateStaffRequest request) {
    if (Objects.nonNull(request.getStaffInfo().getFirstProfessionCompany())) {
      logger.warn("operator not first profession company! request:{}", request);
      throw new FailureException(ErrorCode.INVALID_PARAMS);
    }
    return (Operator) updateStaff(request, StaffRole.OPERATOR);
  }

  @SuppressWarnings("unchecked")
  private User updateStaff(UpdateStaffRequest request, StaffRole role) {

    Response<StaffInfo> response = Response.instanceFail();

    if (role == StaffRole.DOCTOR) {
      response = userServiceClient.updateDoctor(request);
    } else if (role == StaffRole.OPERATOR) {
      response = userServiceClient.updateOperator(request);
    } else if (role == StaffRole.ASSISTANT) {
      response = userServiceClient.updateAssistant(request);
    }
    telExistedException(response);
    defaultDataCheck(response);
    if (response.isFailed()) {

      Integer failureCode = response.getFailureCode();
      if (StaffErrorCode.NO_OPERATOR_IN_DEPARTMENT.getCode() == failureCode) {
        throw new FailureException(UserErrorCode.NO_OPERATOR_IN_DEPARTMENT);
      } else if (StaffErrorCode.OPERATOR_HAS_PATIENTS.getCode() == failureCode) {
        throw new FailureException(UserErrorCode.OPERATOR_HAS_PATIENT);
      }

      logger.warn("update staff fail ! request {} , response {}", request, response);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    } else if (response.isError()) {
      logger.error("update staff error ! request {}, response {}", request, response);
      throw new FailureException(ErrorCode.SERVER_ERROR);
    }
    return UserInfoFactory.valueOf(response.getDataAs(StaffInfo.class));
  }

  public Response moveStaffDepartment(long adminUserId, StaffDepartmentRequest sdr) {
    MoveDepartmentRequest request = new MoveDepartmentRequest();
    request.setAdminUserId(adminUserId);
    request.setDepartmentId(HashUtils.decode(sdr.getDepartmentId()));
    List<Long> ids = sdr.getStaffIds().stream().map(HashUtils::decode)
        .collect(Collectors.toList());
    request.setStaffIds(ids);
    request.setStaffRole(StaffRole.valueOf(sdr.getUserRole().name()));
    return userServiceClient.moveStaffDepartment(request);
  }

  public PatientListApiResponse getPatientList(
      GetPatientByCriteriaRequest request, StaffRole role,
      Long createdAtStart, Long createdAtEnd, int pageAt, int pageSize, Long userId) {

    Response<PatientListResponse> response =
        userServiceClient.getPatientList(request.getOrganizationId(), request.getDepartmentId(),
            request.getDoctorId(), request.getAssistantId(), request.getOperatorId(),
            request.getKeyword(),
            request.getProvince(), request.getAgeMin(), request.getAgeMax(), request.getRisks(),
            request.getServiceLevel(), createdAtStart, createdAtEnd, pageAt, pageSize,
            request.getHaveAssistant(), request.isExcludeAssistant(), request.isExcludeDoctor(),
            request.isExcludeOperator(),
            request.getExcludedRisks(), request.getIntegratedStatus(), request.isIncludesNullAge(),
            request.getProgress(), request.getStatuses(), role);
    if (!response.isSuccessful()) {
      logger.error("get patient list fail !");
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    }
    PatientListResponse patientListInfo = response
        .getDataAs(PatientListResponse.class);
    if (patientListInfo == null) {
      throw new SystemException(ErrorCode.SERVER_ERROR, "患者列表数据错误");
    }
    List<PatientApiInfo> patients = PatientFactory
        .apis(patientListInfo.getPatients());
    patients.forEach(patientApiInfo -> {
      //todo need fix this carefully
//      UnreadStatus unreadStatusByPatient = chatService
//          .getUnreadStatusByPatient(HashUtils.decode(patientApiInfo.getId()), userId, role);
      patientApiInfo.setUnreadStatus(UnreadApiStatus.BLOCK);
    });
    PatientListApiResponse result =
        new PatientListApiResponse();
    result.setPatients(patients);
    result.setTotal(patientListInfo.getTotal());
    return result;
  }

  public void addPatient(AddPatientRequest request) {

    Response response = userServiceClient.addPatient(request);

    if (response.isFailed()) {

      if (response.getFailureCode()
          == cn.xinzhili.user.api.error.UserErrorCode.USER_PHONE_NUMBER_EXISTS.getCode()) {
        logger.warn("this tel is already exist ! {}", request.getPatient());
        throw new FailureException(UserErrorCode.TEL_ALREADY_EXIST);
      } else if (response.getFailureCode() == StaffErrorCode.STAFF_NOT_EXIST.getCode()) {
        logger.warn("staff invalid ! {}", request);
        throw new FailureException(UserErrorCode.INVALID_STAFF_STATUS);
      }

      logger.warn("add patient fail ! {}", response);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    } else if (response.isError()) {
      logger.error("add patient error ! {}", response);
      throw new FailureException(ErrorCode.SERVER_ERROR);
    }
  }

  public boolean deletePatient(List<Long> ids) {
    Response response = userServiceClient.deletePatients(ids);
    if (!response.isSuccessful()) {
      logger.error("delete patients by id fail ! response -> {}", response);
      throw new FailureException(ErrorCode.REQUEST_FAILED, "delete patients fail !");
    }
    return true;
  }


  public PatientDetailApiResponse getPatientById(long patientId) {
    Response<PatientDetailResponse> response = userServiceClient.searchPatientById(patientId);
    if (!response.isSuccessful()) {
      logger.warn("get patient by id fail ,patient={},response->{}", patientId, response);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    }
    PatientDetailResponse patientDetail = response.getDataAs(PatientDetailResponse.class);
    if (patientDetail == null) {
      throw new FailureException(ErrorCode.REQUEST_FAILED, "患者的详情为空");
    }
    return PatientFactory.api(patientDetail);
  }

  /**
   * 检验患者 和医生或者医生助理的绑定关系
   *
   * @param medicalPersonId 医生／医生助理id
   * @param patientId 患者id
   * @return 是否存在绑定关系
   */
  public boolean checkBindRelation(long medicalPersonId, long patientId) {
    PatientDetailApiResponse response = getPatientById(patientId);

    if (response == null
        || response.getDoctorId() == null) {
      throw new FailureException(ErrorCode.REQUEST_FAILED,
          "患者" + patientId + "数据有误！");
    }
    return medicalPersonId == HashUtils.decode(response.getDoctorId())
        || medicalPersonId == HashUtils.decode(response.getOperatorId())
        || (response.getAssistantId() != null
        && medicalPersonId == HashUtils.decode(response.getAssistantId()) || dpcService
        .getBindByConsultationAndPatient(medicalPersonId, patientId));
  }

  /**
   * update patient related info
   *
   * @param request to update data
   * @return is success
   */
  @HystrixCommand(fallbackMethod = "defaultUpdatePatientRelatedInfo",
      ignoreExceptions = {FailureException.class, SystemException.class})
  public boolean updatePatientRelatedInfo(UpdatePatientAndRelationRequest request) {

    Response response =
        userServiceClient.updatePatientRelated(request);
    if (!response.isSuccessful()) {
      logger
          .warn("update patient related info fail !,request->{},response -> {}", request, response);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    }
    return true;
  }

  private boolean defaultUpdatePatientRelatedInfo(
      UpdatePatientAndRelationRequest request, Throwable t) {
    logger.warn("update patient related info fail ,request->{], throwable-> ", request, t);
    throw new FailureException(ErrorCode.SERVER_ERROR, "更新患者信息失败");
  }

  /**
   * 选择性更新患者的个人信息
   *
   * @param id 患者id
   * @param param 请求参数
   */
  public void updatePatientWithSelective(long id, UpdatePatientRequest param) {
    Response response = userServiceClient.updatePatientWithSelective(param, id);
    if (response.isFailed()) {
      if (response.getFailureCode()
          == cn.xinzhili.user.api.error.UserErrorCode.PATIENT_REGION_INFO_ERROR.getCode()) {
        throw new FailureException(PatientErrorCode.PATIENT_REGION_INFO_ERROR);
      }
      throw new FailureException(PatientErrorCode.UPDATE_PATIENT_FAIL);
    }
    if (response.isError()) {
      throw new SystemException(ErrorCode.SERVER_ERROR);
    }
  }

  /**
   * batch update patients binding relation
   */
  public void updatePatientsBinding(BatchUpdatePatientRelationRequest request) {
    Response response = userServiceClient.batchUpdatePatientBindingRelation(request);
    if (response.isFailed()) {
      logger.warn("call user-service fail,request -> {},response -> {}", request, response);
      if (response.getFailureCode() == StaffErrorCode.STAFF_NOT_EXIST.getCode()) {
        throw new FailureException(UserErrorCode.INVALID_STAFF_STATUS);
      } else {
        throw new FailureException(ErrorCode.REQUEST_FAILED,
            "update patient binding relation fail !" + response);
      }
    } else if (response.isError()) {
      logger.error("call user-service fail,request -> {},response -> {}", request, response);
      throw new SystemException(ErrorCode.SERVER_ERROR);
    }
  }

  public PatientApiInfo updatePatientRemark(Long patientId, String remark,
      PatientProgress progress) {

    UpdatePatientRemarkRequest request = new UpdatePatientRemarkRequest();
    request.setRemark(remark);
    request.setProgress(progress);
    Response<PatientInfo> response = userServiceClient.updatePatientRemark(patientId, request);
    if (response.isFailed()) {
      logger.warn("update patient remark fail ! {}", response);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    } else if (response.isError()) {
      logger.error("update patient remark error ! {}", response);
      throw new FailureException(ErrorCode.SERVER_ERROR);
    }
    return PatientFactory.api(response.getDataAs(PatientInfo.class));
  }

  /**
   * del assistant-patient relation by doctor
   *
   * @param request doctor and assistant info
   * @return is success
   */
  @HystrixCommand(fallbackMethod = "defaultDelAssistantBinding",
      ignoreExceptions = {FailureException.class, SystemException.class})
  public boolean deleteAssistantRelation(DelAssistantPatientRelationApiRequest request) {
    if (request == null) {
      throw new FailureException(ErrorCode.INVALID_PARAMS,
          "delete binding assistant info is null ！");
    }
    Response response =
        userServiceClient.deleteAssistantRelation(request);
    if (!response.isSuccessful()) {
      logger.warn("call user-service fail,request -> {},response -> {}", request, response);
      throw new FailureException(
          ErrorCode.REQUEST_FAILED,
          "delete assistant binding relation fail !" + response);
    }
    return true;
  }


  private boolean defaultDelAssistantBinding(
      DelAssistantPatientRelationApiRequest request, Throwable t) {
    throw new SystemException(
        ErrorCode.SERVER_ERROR, "删除医助患者绑定信息失败 -> " + t);
  }

  /**
   * get organization info
   *
   * @return organizations info
   */
  @HystrixCommand(fallbackMethod = "defaultGetOrganization",
      ignoreExceptions = {FailureException.class, SystemException.class})
  public InstitutionResponse getOrganization(Integer pageAt, Integer pageSize) {

    Response<OrganizationResponse> response =
        userServiceClient.getOrganization(pageAt, pageSize);
    if (!response.isSuccessful()) {
      logger.warn("call user-service fail,pageAt={},pageSize={} ,response -> {}", pageAt, pageSize,
          response);
      throw new FailureException(
          ErrorCode.REQUEST_FAILED,
          "get organizations info fail !" + response);
    }

    OrganizationResponse organizations =
        response.getDataAs(OrganizationResponse.class);
    if (organizations == null) {
      throw new FailureException(
          ErrorCode.REQUEST_FAILED, "organizations info is null !");
    }
    InstitutionResponse institutionResponse = new InstitutionResponse();
    institutionResponse.setInstitutions(
        InstitutionFactory.valueOf(organizations.getOrganizations()));
    return institutionResponse;
  }

  private InstitutionResponse defaultGetOrganization(
      Integer pageAt, Integer pageSize, Throwable t) {
    throw new SystemException(
        ErrorCode.SERVER_ERROR, "查询机构信息列表失败 -> " + t);
  }


  /**
   * 获取某个角色的员工（医助或者无医助医生）所管理的患者id列表 <p> TODO 分页
   *
   * @param role 员工角色，医生或者医助
   * @param id 员工的id
   * @return 患者id列表
   */
  public List<Long> getManagedPatientIds(UserRole role, Long id) {
    if (role == null || id == null) {
      throw new IllegalArgumentException("role and id cannot be null");
    }
    Response<PatientIdsResponse> response = userServiceClient
        .getPatientIds(role, id);
    if (response.isFailed()) {
      if (response.getFailureCode() ==
          cn.xinzhili.user.api.error.UserErrorCode.USER_NOT_FOUND.getCode()) {
        throw new FailureException(UserErrorCode.NOT_FOUND_STAFF,
            "user id not found: " + id);
      }
      throw new FailureException(ErrorCode.REQUEST_FAILED, "查询用户id失败: " + id);
    } else if (!response.isSuccessful()) {
      logger
          .error("get managed patientIds failed, role = {}, id = {}", role, id);
      throw new SystemException(ErrorCode.REQUEST_FAILED,
          "get managed patientIds failed");
    }
    PatientIdsResponse ids = response.getDataAs(PatientIdsResponse.class);
    return ids.getIds();
  }

  /**
   * 管理员重置员工密码
   *
   * @param userId 被重置的医生或者医助的id
   * @param adminUserId 管理员的用户id
   * @return 是否重置成功
   */
  public boolean resetUserPasswordByAdmin(long userId, long adminUserId, long organizationId) {
    final String newPassword = RandomStringUtils.randomAlphabetic(6);
    PasswordResetRequest request = new PasswordResetRequest();
    request.setNewPassword(newPassword);
    request.setIssuedBy(adminUserId);
    request.setOrganizationId(organizationId);
    return staffResetPwd(userId, request);
  }

  /**
   * 用户忘记密码时重置密码
   *
   * @param username 用户名
   * @param password 密码
   * @param vcode 验证码
   * @return 是否重置成功
   */
  public boolean resetUserPassword(String username, String password, String vcode) {
    Map<String, Object> verifyVcodeParam = new HashMap<>();
    verifyVcodeParam.put("phone", username);
    verifyVcodeParam.put("code", vcode);
    Response response = notifyServiceClient.verifyVcode(verifyVcodeParam);
    if (!response.isSuccessful()) {
      throw new FailureException(NotifyErrorCode.SMS_VERIFICATION_CODE_INCORRECT);
    }
    Response<?> staffResponse = userServiceClient.searchStaffByUsername(username);
    if (!staffResponse.isSuccessful()) {
      throw new FailureException(UserErrorCode.NOT_FOUND_STAFF);
    }
    StaffInfo info = ResponseUtils.getDataEntryAs(StaffInfo.class, staffResponse, "staff");
    PasswordResetRequest resetRequest = new PasswordResetRequest();
    resetRequest.setNewPassword(password);
    return staffResetPwd(info.getId(), resetRequest);
  }

  /**
   * 用户自己修改自己的密码（已登录用户）
   *
   * @param userId 用户id
   * @param newPassword 新密码
   * @param originalPassword 原密码
   * @return 是否重置成功
   */
  public boolean updateUserPassword(Long userId, String newPassword, String originalPassword) {
    if (userId == null || StringUtils.isEmpty(newPassword) || StringUtils
        .isEmpty(originalPassword)) {
      throw new SystemException(ErrorCode.INVALID_PARAMS,
          "request is expected to be validated, but is not");
    }
    if (Objects.equals(newPassword, originalPassword)) {
      throw new FailureException(UserErrorCode.NEW_PASSWORD_MUST_BE_DIFFERENT,
          "passwords must be different");
    }
    PasswordResetRequest request = new PasswordResetRequest();
    request.setNewPassword(newPassword);
    request.setOriginalPassword(originalPassword);
    return staffResetPwd(userId, request);
  }

  private boolean staffResetPwd(Long userId, PasswordResetRequest request) {
    Response response = userServiceClient.resetPassword(userId, request);
    if (response.isSuccessful()) {
      return true;
    }
    defaultDataCheck(response);
    logger.warn("failed to reset password: request = {}, response = {}", request, response);
    if (response.isFailed()) {
      if (response.getFailureCode() == StaffErrorCode.PASSWORD_NOT_MATCH.getCode()) {
        throw new FailureException(UserErrorCode.PASSWORD_INCORRECT);
      } else if (response.getFailureCode() == StaffErrorCode.STAFF_NOT_EXIST.getCode()) {
        throw new FailureException(UserErrorCode.NOT_FOUND_STAFF, "user not found: " + userId);
      } else if (response.getFailureCode() == StaffErrorCode.UN_AUTH_FOR_STAFF_ACTION.getCode()) {
        throw new FailureException(UserErrorCode.INVALID_STAFF_STATUS);
      } else {
        throw new FailureException(UserErrorCode.PASSWORD_UPDATE_FAILED);
      }
    }
    throw new SystemException(ErrorCode.SERVER_ERROR, "unexpected error: " + response);
  }

  /**
   * 用户首次登录重置密码, 完善个人信息等请求
   *
   * @param userId 用户id
   * @param request 信息DTO, 不能为空，必须已经验证过
   * @return 是否修改成功
   */
  public boolean completeStaffInfo(long userId, InitUserRequest request) {

    Response response =
        userServiceClient.completeUserInfo(userId, UserInfoFactory.of(request));

    if (response.isSuccessful()) {
      return true;
    }
    defaultDataCheck(response);
    logger.warn("failed to reset password: request = {}, response = {}", request, response);
    if (response.isFailed()) {
      Integer code = response.getFailureCode();
      if (code == StaffErrorCode.STAFF_PASSWORD_INVALID.getCode()) {
        throw new FailureException(UserErrorCode.PASSWORD_FORMAT_INVALID);
      } else if (code == StaffErrorCode.STAFF_STATUS_INVALID.getCode()) {
        throw new FailureException(UserErrorCode.USER_STATUS_NOT_AVAILABLE);
      } else {
        throw new FailureException(UserErrorCode.PASSWORD_UPDATE_FAILED);
      }
    }
    throw new SystemException(ErrorCode.SERVER_ERROR, "unexpected error: " + response);
  }

  public void updateUserCertificate(long userId, UpdateUserCertificateApiRequest request) {

    UpdateUserCertificateRequest certificateRequest = new UpdateUserCertificateRequest();
    certificateRequest.setCertificates(request.getCertificates());
    Response response = userServiceClient.updateUserCertificate(userId, certificateRequest);

    defaultDataCheck(response);
    if (response.isFailed()) {
      logger.warn("update user certificate fail ! request {} ,response {}", request, response);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    } else if (response.isError()) {
      logger.error("update user certificate error ! request {} ,response {}", request, response);
      throw new SystemException(ErrorCode.SERVER_ERROR);
    }
  }

  public void resetPatientNotification(long patientId, StaffRole role) {
    if (Objects.isNull(role)) {
      return;
    }
    ResetPendingMessageRequest request = new ResetPendingMessageRequest();
    request.setPatientId(patientId);
    request.setRole(role);
    Response response = userServiceClient.resetPatientPendingMessage(request);
    if (!response.isSuccessful()) {
      logger.error("reset patient pending message fail ! {},response {}", patientId, response);
    }
  }

  /**
   * 查找省市信息，
   *
   * @param regionId 省id，若为null,则默认为0
   * @return 返回省市信息的list
   */
  public List<RegionInfo> getRegionList(Integer regionId) {
    if (regionId == null) {
      regionId = 0;
    }
    Response<RegionListResponse> response = userServiceClient.getPatientRegionInfo(regionId);
    if (!response.isSuccessful()) {
      throw new FailureException(UserErrorCode.REGION_NOT_FOUND);
    }
    RegionListResponse list = response.getDataAs(RegionListResponse.class);
    return list.getRegions();
  }

  public PatientVisualInfoResponse getIndexVisualInfo(UserRole role, long userId) {

    if (role == null || !(role == UserRole.DOCTOR || role == UserRole.ASSISTANT)) {
      logger.warn("user role is invalid ! role ->{} ,userId -> {}", role, userId);
      throw new FailureException(ErrorCode.INVALID_PARAMS, "user role is invalid !");
    }
    Response<PatientVisualInfoResponse> response = userServiceClient
        .getVisualCountInfo(userId, StaffRole.valueOf(role.name()));

    if (!response.isSuccessful()) {
      logger.warn("get index visual fail ! role -> {} ,userId -> {}", role, userId);
      throw new FailureException(ErrorCode.REQUEST_FAILED, "get index visual info fail !");
    }
    return response.getDataAs(PatientVisualInfoResponse.class);
  }

  private Iterator<Row> fileRows(InputStream inputStream) {
    Workbook wb;
    try {
      wb = WorkbookFactory.create(inputStream);
    } catch (IOException | InvalidFormatException e) {
      throw new FailureException(UserErrorCode.INVALID_UPLOAD_FILE);
    }
    return MoreObjects.firstNonNull(wb.getSheetAt(0), wb.createSheet()).rowIterator();
  }

  private boolean isHeaderInvalid(String[] header, Row row) {
    return row == null || IntStream.range(0, header.length)
        .anyMatch(i -> !Objects.equals(row.getCell(i).toString(), header[i]));
  }

  private List<StaffUploadBean> collectStaffUploadBean(Iterator<Row> rows) {
    List<StaffUploadBean> staffs = new ArrayList<>();

    while (rows.hasNext()) {
      Row row = rows.next();
      Cell cell0 = row.getCell(0);
      Cell cell1 = row.getCell(1);
      Cell cell2 = row.getCell(2);
      Cell cell3 = row.getCell(3);

      if (cell0 == null || cell1 == null || cell2 == null || cell3 == null) {
        continue;
      }

      double phone = cell2.getNumericCellValue();
      NumberFormat nf = NumberFormat.getInstance();
      nf.setGroupingUsed(false);
      StaffUploadBean sb = StaffUploadBean.builder().name(cell0.toString())
          .sex(cell1.toString())
          .tel(nf.format(phone)).title(cell3.toString()).build();
      if (sb.isEmpty()) {
        continue;
      }
      if (!sb.isValid()) {
        throw new FailureException(UserErrorCode.INVALID_UPLOAD_FILE);
      }
      staffs.add(sb);
    }
    return staffs;
  }

  private List<BatchInsertStaff> staffUploadBeans2BatchInsertStaffs(
      List<StaffUploadBean> staffUploadBeans) {
    return staffUploadBeans.stream().map(s -> {
      BatchInsertStaff staff = new BatchInsertStaff();
      staff.setName(s.getName());
      staff.setSex(Objects.equals(s.getSex(), "男") ? Gender.MALE : Gender.FEMALE);
      staff.setTel(s.getTel());
      staff.setTitle(Arrays.stream(StaffTitle.values()).filter(t -> Objects
          .equals(t.getDescription(), s.getTitle())).findFirst().orElse(StaffTitle.OTHER));
      return staff;
    }).collect(Collectors.toList());
  }

  public Response handleStaffFileUpload(InputStream inputStream, long adminUserId,
      long departmentId, long organizationId, UserRole role) {
    Iterator<Row> rows = fileRows(inputStream);
    String[] header = {"姓名", "性别", "手机", "职称"};
    if (isHeaderInvalid(header, rows.next())) {
      throw new FailureException(UserErrorCode.INVALID_UPLOAD_FILE_HEADERS);
    }

    StaffBatchInsertRequest request = new StaffBatchInsertRequest();
    request.setAdminUserId(adminUserId);
    request.setDepartmentId(departmentId);
    request.setOrganizationId(organizationId);
    request.setRole(StaffRole.valueOf(role.name()));
    List<StaffUploadBean> staffUploadBeans = collectStaffUploadBean(rows);
    List<String> invalidTels = staffUploadBeans.stream()
        .filter(t -> !t.getTel().matches(TEL_REG_EXP))
        .map(StaffUploadBean::getTel).collect(Collectors.toList());
    if (!invalidTels.isEmpty()) {
      return Response.instanceFail(UserErrorCode.INVALID_PHONE_NUMBER,
          StaffRole.valueOf(role.name()).getDescription() + "手机号不合法" + invalidTels.toString());
    }
    request.setStaffs(staffUploadBeans2BatchInsertStaffs(staffUploadBeans));
    Response response = userServiceClient.bulkInsertStaffs(request);
    if (response.isFailed()) {

      if (StaffErrorCode.STAFF_EXIST.getCode() == response.getFailureCode()) {
        logger.warn("doctor or assistant is existed ! response -> {}", response);
        return Response.instanceFail(UserErrorCode.TEL_ALREADY_EXIST, response.getFailureMessage());
      }
      logger.warn("batch insert staff fail ! response -> {}", response);
      throw new FailureException(ErrorCode.REQUEST_FAILED, "batch insert staff fail !");
    }

    if (response.isError()) {
      logger.warn("batch insert staff fail ! response -> {}", response);
      throw new FailureException(ErrorCode.REQUEST_FAILED, "batch insert staff fail !");
    }
    return Response.instanceSuccess();
  }

  private List<BatchInsertPatient> collectPatientBatchInsertRequest(Iterator<Row> rows) {
    ArrayList<BatchInsertPatient> requests = new ArrayList<>();
    NumberFormat nf = NumberFormat.getInstance();
    nf.setGroupingUsed(false);

    while (rows.hasNext()) {
      Row row = rows.next();
      Cell cell0 = row.getCell(0);
      Cell cell1 = row.getCell(1);
      Cell cell2 = row.getCell(2);
      Cell cell3 = row.getCell(3);
      Cell cell4 = row.getCell(4);

      if (cell0 == null || cell1 == null || cell3 == null || cell4 == null) {
        continue;
      }

      String patientPhone = nf.format(cell0.getNumericCellValue());
      String doctorPhone = nf.format(cell1.getNumericCellValue());
      String operatorPhone = nf.format(cell3.getNumericCellValue());

      String patientLevel = cell4.toString();
      if (Objects.equals(patientPhone, "0") || Objects.equals(doctorPhone, "0")
          || Objects.equals(operatorPhone, "0") || patientLevel.isEmpty()) {
        continue;
      }

      ServiceLevel patientEnumLevel = Arrays.stream(ServiceLevel.values()).filter(l -> Objects
          .equals(l.getDescription(), patientLevel))
          .findFirst().orElse(ServiceLevel.NORMAL);

      BatchInsertPatient patient = new BatchInsertPatient(patientPhone, doctorPhone,
          patientEnumLevel);
      patient.setOperatorTel(operatorPhone);
      //医生助理电话可为null
      patient.setAssistantTel(cell2 == null || StringUtils.isEmpty(cell2.toString()) ? null
          : nf.format(cell2.getNumericCellValue()));
      requests.add(patient);
    }
    return requests;
  }

  public Response handlePatientFileUpload(Long organizationId, InputStream inputStream) {
    Iterator<Row> rows = fileRows(inputStream);
    String[] header = {"患者手机", "医生手机", "医助手机", "运营手机", "患者级别"};
    if (isHeaderInvalid(header, rows.next())) {
      throw new FailureException(UserErrorCode.INVALID_UPLOAD_FILE_HEADERS);
    }
    List<BatchInsertPatient> batchInsertPatients = collectPatientBatchInsertRequest(rows);
    List<String> invalidTels = batchInsertPatients.stream()
        .filter(t -> !t.getPatientTel().matches(TEL_REG_EXP))
        .map(BatchInsertPatient::getPatientTel).collect(Collectors.toList());
    if (!invalidTels.isEmpty()) {
      return Response.instanceFail(UserErrorCode.INVALID_PHONE_NUMBER,
          "患者手机号不合法:" + invalidTels.toString());
    }

    PatientBatchInsertRequest request = new PatientBatchInsertRequest(batchInsertPatients);
    Response response = userServiceClient.bulkInsertPatients(organizationId, request);

    if (response.isFailed()) {

      if (cn.xinzhili.user.api.error.UserErrorCode.USER_PHONE_NUMBER_EXISTS.getCode()
          == response.getFailureCode()) {
        logger.warn("patient phone is existed ! response -> {}", response);
        return Response.instanceFail(UserErrorCode.TEL_ALREADY_EXIST, response.getFailureMessage());
      }

      if (StaffErrorCode.STAFF_NOT_EXIST.getCode() == response.getFailureCode()) {
        logger.warn("doctor or assistant is not existed ! response -> {}", response);
        return Response.instanceFail(UserErrorCode.NOT_FOUND_STAFF, response.getFailureMessage());
      }
      logger.warn("batch insert patients fail ! response -> {}", response);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    }

    if (response.isError()) {
      logger.warn("batch insert patients fail ! response -> {}", response);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    }
    return Response.instanceSuccess();
  }


  public AvatarUploadInfoResponse getAvatarUploadInfo(String filename) {

    GetImageUploadInfoRequest request = new GetImageUploadInfoRequest();
    request.setFilenames(Collections.singletonList(filename));
    request.setScope(ImageScope.STAFF_AVATAR);
    Response<ImagesUploadInfoResponse> response = userServiceClient
        .getImagesUploadInfo(request);
    if (response.isFailed()) {
      logger.warn("get staff avatar upload info fail ! filename -> {}", filename);
      throw new FailureException(UserErrorCode.GET_IMAGES_UPLOAD_FAIL);
    }
    if (response.isError()) {
      logger.error("get staff avatar upload info error !");
      throw new SystemException(ErrorCode.SERVER_ERROR);
    }
    ImagesUploadInfoResponse uploadInfo = response.getDataAs(ImagesUploadInfoResponse.class);
    UploadInfo urlInfo = uploadInfo.getUploadInfo().get(0);

    return new AvatarUploadInfoResponse(
        ImageUploadInfo.builder().signUrl(urlInfo.getOriginSignUrl())
            .rawUrl(urlInfo.getOriginRawUrl()).imageId(urlInfo.getOriginAwsId())
            .contentType(urlInfo.getContentType()).build());
  }


  public PatientRegionListResponse getStaffPatientRegion(UserRole role, long staffId) {

    Response<RegionListResponse> response = userServiceClient
        .getPatientRegionInfo(StaffRole.valueOf(role.name()), staffId);

    if (response.isFailed()) {
      logger.warn("get patient region info fail ! staffId {},role {}", staffId, role);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    }
    if (response.isError()) {
      logger.error("get patient region info error ! staffId {},role {}", staffId, role);
      throw new SystemException(ErrorCode.SERVER_ERROR);
    }
    List<RegionInfo> regions = response.getDataAs(RegionListResponse.class).getRegions();

    PatientRegionListResponse result = new PatientRegionListResponse();
    if (regions == null || regions.isEmpty()) {
      result.setRegions(new ArrayList<>());
      return result;
    }
    List<String> patientRegions = regions.stream()
        .map(RegionInfo::getRegionName).collect(Collectors.toList());
    result.setRegions(patientRegions);
    return result;
  }

  public CertificateUploadInfoResponse getCertificateUploadInfo(List<String> filenames) {
    GetImageUploadInfoRequest request = new GetImageUploadInfoRequest();
    request.setFilenames(filenames);
    request.setScope(ImageScope.STAFF_CERTIFICATE);
    Response<ImagesUploadInfoResponse> response = userServiceClient
        .getImagesUploadInfo(request);
    if (response.isFailed()) {
      logger.warn("get staff avatar upload info fail ! filenames -> {}", filenames);
      throw new FailureException(UserErrorCode.GET_IMAGES_UPLOAD_FAIL);
    }
    if (response.isError()) {
      logger.error("get staff avatar upload info error !");
      throw new SystemException(ErrorCode.SERVER_ERROR);
    }
    UploadInfo urlInfo = response.getDataAs(ImagesUploadInfoResponse.class).getUploadInfo().get(0);
    return new CertificateUploadInfoResponse(
        ImageUploadInfo.builder().signUrl(urlInfo.getOriginSignUrl())
            .rawUrl(urlInfo.getOriginRawUrl()).imageId(urlInfo.getOriginAwsId())
            .contentType(urlInfo.getContentType()).build());
  }

  public void patientRelativeNotify(List<Long> patientIds, PatientRelativeNotifyCategory category) {
    if (patientIds == null || patientIds.isEmpty()) {
      return;
    }
    Response response = userServiceClient
        .pushNotify(new PatientRelativeNotifyRequest(patientIds, category));
    if (!response.isSuccessful()) {
      logger
          .error("notify patient relative fail ! patientIds {},response {}", patientIds, response);
    }
  }

  public void reviewUser(UserReview userReview) {
    Response response = userServiceClient
        .reviewUser(UserInfoFactory.toUserReviewInfo(userReview));
    defaultDataCheck(response);
    if (!response.isSuccessful()) {
      logger.warn("call user-service fail request -> {},response -> {]", userReview, response);
      throw new FailureException(ErrorCode.REQUEST_FAILED, "call user-service fail!");
    }
  }

  public PatientDetailApiResponse getPatientFromAllById(long id) {
    Response<PatientDetailResponse> response = userServiceClient
        .getPatientFromAllById(id);
    if (!response.isSuccessful()) {
      logger.warn("get pending patient failed: {}", response);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    }
    return PatientFactory.api(response.getDataAs(PatientDetailResponse.class));
  }

  public void updatePendingMessage(long patientId) {

    UpdatePendingMessageRequest request = new UpdatePendingMessageRequest();
    request.setPatientId(patientId);
    request.setMessageToDoctor(true);
    Response response = userServiceClient.updatePatientMessageNotification(request);
    if (response.isFailed()) {
      logger.error("update patient pending message fail ,{}", response);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    } else if (response.isError()) {
      logger.error("update patient pending message error ,{}", response);
      throw new FailureException(ErrorCode.SERVER_ERROR);
    }

  }

  public ConsultationPatientListApiResponse getPatientByConsultationDoctor(Long doctorId,
      Integer pageAt, Integer pageSize) {
    Response<ConsultationPatientListResponse> response = userServiceClient
        .getPatientByConsultationDoctor(doctorId, pageAt, pageSize);
    if (response.isFailed()) {
      logger.error("get consultation patient fail ,{}", response);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    } else if (response.isError()) {
      logger.error("get consultation patient error ,{}", response);
      throw new FailureException(ErrorCode.SERVER_ERROR);
    }
    return ConsultationFactory.api(response.getDataAs(ConsultationPatientListResponse.class));

  }

  private void defaultDataCheck(Response response) {
    if (!response.isSuccessful()
        && response.getFailureCode() == StaffErrorCode.DEFAULT_DATA_CAN_NOT_BE_UPDATE.getCode()) {
      throw new FailureException(UserErrorCode.DEFAULT_DATA_CAN_NOT_BE_UPDATE);
    }
  }

  public InstitutionResponse getAllOperatorOrganization(long operatorId) {

    Response<StaffRelationshipListResponse> response = userServiceClient
        .getStaffRelationship(operatorId);

    if (response.isSuccessful()) {
      List<Institution> result = response.getDataAs(StaffRelationshipListResponse.class)
          .getRelationship().stream()
          .map(InstitutionFactory::api).collect(Collectors.toList());
      return new InstitutionResponse(result);
    } else if (response.isFailed()) {
      logger.warn("get operator organization fail ! {}", operatorId);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    } else {
      logger.error("get operator organization error ! {}", operatorId);
      throw new FailureException(ErrorCode.SERVER_ERROR);
    }
  }

  public void addFutureDoctor(Long uid, AddFutureDoctorApiRequest request) {
    AddFutureDoctorRequest target = new AddFutureDoctorRequest();
    BeanUtils.copyProperties(request, target);
    target.setDoctorId(uid);
    target.setSex(Gender.valueOf(request.getSex().name()));
    target.setTitle(StaffTitle.valueOf(request.getTitle().name()));
    target.setType(FutureDoctorType.valueOf(request.getType().name()));
    target.setHospital(request.getHospitalIntroduction());
    target.setAchievement(request.getExperience());
    Response response = userServiceClient.addFutureDoctor(target);
    if (response.isFailed()) {
      logger.warn("add future doctor fail ! request :{} ,response : {}", request, response);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    } else if (response.isError()) {
      logger.error("add future doctor error ! request {} ,response {}", request, response);
      throw new FailureException(ErrorCode.SERVER_ERROR);
    }
  }

  public FutureDoctorApiInfo updateFutureDoctor(Long uid, UpdateFutureDoctorApiRequest request) {
    UpdateFutureDoctorRequest target = new UpdateFutureDoctorRequest();
    BeanUtils.copyProperties(request, target);
    target.setDoctorId(uid);
    target.setSex(Gender.valueOf(request.getSex().name()));
    target.setTitle(StaffTitle.valueOf(request.getTitle().name()));
    target.setType(FutureDoctorType.valueOf(request.getType().name()));
    target.setHospital(request.getHospitalIntroduction());
    target.setAchievement(request.getExperience());
    target.setId(HashUtils.decode(request.getId()));
    Response<FutureDoctorInfo> response = userServiceClient
        .updateFutureDoctor(target);
    if (response.isFailed()) {
      logger.warn("update future doctor fail ! request :{} ,response : {}", request, response);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    } else if (response.isError()) {
      logger.error("update future doctor error ! request {}, response {}", request, response);
      throw new FailureException(ErrorCode.SERVER_ERROR);
    }
    FutureDoctorInfo futureDoctorInfo = response.getDataAs(FutureDoctorInfo.class);
    FutureDoctorApiInfo futureDoctorApiInfo = UserInfoFactory
        .getFutureDoctorApiInfo(futureDoctorInfo);
    return futureDoctorApiInfo;
  }

  public FutureDoctorApiInfo getFutureDoctorByDoctorId(long doctorId,
      FutureDoctorType type) {
    Response<FutureDoctorListResponse> response = userServiceClient
        .getFutureDoctorByDoctorId(doctorId, type);
    if (response.isFailed()) {
      logger.warn("get future doctor fail ! doctorId :{} ,type : {}", doctorId, type);
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    } else if (response.isError()) {
      logger.error("get future doctor error ! doctorId {}, type {}", doctorId, type);
      throw new FailureException(ErrorCode.SERVER_ERROR);
    }
    FutureDoctorListResponse futureDoctorListResponse = response
        .getDataAs(FutureDoctorListResponse.class);
    return UserInfoFactory
        .getFutureDoctorApiInfos(futureDoctorListResponse);
  }
}