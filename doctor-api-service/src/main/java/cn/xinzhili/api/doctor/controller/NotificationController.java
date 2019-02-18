package cn.xinzhili.api.doctor.controller;

import cn.xinzhili.api.doctor.bean.DepartmentDetail;
import cn.xinzhili.api.doctor.bean.UserRole;
import cn.xinzhili.api.doctor.bean.request.SendInquiryRequest;
import cn.xinzhili.api.doctor.bean.response.UserDetailResponse;
import cn.xinzhili.api.doctor.error.UserErrorCode;
import cn.xinzhili.api.doctor.service.DpcService;
import cn.xinzhili.api.doctor.service.NotifyService;
import cn.xinzhili.api.doctor.service.UserService;
import cn.xinzhili.api.doctor.util.AuthUtils;
import cn.xinzhili.xutils.auth.CurrentUserId;
import cn.xinzhili.xutils.core.ErrorCode;
import cn.xinzhili.xutils.core.FailureException;
import cn.xinzhili.xutils.core.http.Response;
import cn.xinzhili.xutils.core.util.HashUtils;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Date: 07/03/2017 Time: 1:25 PM
 *
 * @author Gan Dong
 */
@RestController
public class NotificationController {

  @Autowired
  private DpcService dpcService;
  @Autowired
  private NotifyService notifyService;
  @Autowired
  private UserService userService;

  @PreAuthorize("hasAnyRole('DOCTOR','ASSISTANT')")
  @PostMapping("/inquiry")
  public Response sendInquiries(@CurrentUserId Long id, @RequestBody SendInquiryRequest request) {

    if (request.invalid()) {
      throw new FailureException(ErrorCode.INVALID_PARAMS);
    }

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserRole role;
    if (AuthUtils.isUserOfRole(UserRole.DOCTOR, authentication)) {
      role = UserRole.DOCTOR;
      final Long orgId = HashUtils.decode(request.getOrganizationId());
      UserDetailResponse userDetails = userService.getUserDetails(id, orgId, true);
      // 如果是医生的话需要验证是否医生有没有助手，没有才允许发送
      if (((DepartmentDetail) userDetails.getDepartment()).getAssistantCount() > 0) {
        throw new FailureException(UserErrorCode.NOT_PERMITTED_TO_SEND_INQUIRY);
      }
    } else {
      role = UserRole.ASSISTANT;
    }

    List<Long> patientIds = userService.getManagedPatientIds(role, id);
    if (patientIds == null || patientIds.isEmpty()) {
      return Response.instanceSuccess();
    }
    dpcService.sendInquiriesSystemMessage(id, patientIds);
    notifyService.pushInquiries(patientIds);
    return Response.instanceSuccess();
  }
}

