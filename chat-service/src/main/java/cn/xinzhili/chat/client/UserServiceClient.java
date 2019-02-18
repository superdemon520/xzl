package cn.xinzhili.chat.client;


import cn.xinzhili.user.api.UserRole;
import cn.xinzhili.user.api.response.PatientDetailResponse;
import cn.xinzhili.user.api.response.PatientListResponse;
import cn.xinzhili.user.api.response.PatientRelationResponse;
import cn.xinzhili.user.api.response.StaffDetailResponse;
import cn.xinzhili.xutils.core.http.Response;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * 对用户信息请求 Created by MarlinL on 9/19/16.
 *
 * @author MarlinL
 */
@FeignClient(value = "user-service")
public interface UserServiceClient {

  @GetMapping(value = "/patient/user/v1/{id}")
  Response<PatientDetailResponse> getPatientBy(@PathVariable("id") long id);

  @GetMapping("/doctor/user/{id}")
  Response<StaffDetailResponse> findStaffByUserId(
      @PathVariable("id") long id,
      @RequestParam(value = "withDepartment", defaultValue = "false") boolean withDepartment,
      @RequestParam(value = "organizationId", required = false) Long organizationId);

  @GetMapping(value = "/patient/staff/relation")
  Response<PatientRelationResponse> getPatientStaffRelationship(@RequestParam("id") Long id,
      @RequestParam("role") UserRole userRole,
      @RequestParam(value = "orgId", required = false) Long organizationId);

  @RequestMapping(value = "/patient/users", method = RequestMethod.GET)
  Response<PatientListResponse> searchList(
      @RequestParam(value = "pageAt", defaultValue = "0") Long pageAt
      , @RequestParam(value = "pageSize", defaultValue = "1000") Integer pageSize);
}
