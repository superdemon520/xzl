package cn.xinzhili.api.doctor.controller;

import cn.xinzhili.api.doctor.bean.Department;
import cn.xinzhili.api.doctor.bean.request.HospitalDepartmentRequest;
import cn.xinzhili.api.doctor.bean.response.InstitutionDetailResponse;
import cn.xinzhili.api.doctor.bean.response.InstitutionResponse;
import cn.xinzhili.api.doctor.error.InstitutionErrorCode;
import cn.xinzhili.api.doctor.service.UserService;
import cn.xinzhili.xutils.auth.CurrentUserId;
import cn.xinzhili.xutils.core.ErrorCode;
import cn.xinzhili.xutils.core.FailureException;
import cn.xinzhili.xutils.core.http.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 机构Controller 目前包括医院
 * Created by MarlinL on 13/02/2017.
 */
@RestController
@RequestMapping(value = "/institution")
public class InstitutionController {

  private static final Logger logger = LoggerFactory
      .getLogger(InstitutionController.class);

  @Autowired
  private UserService userService;

  /**
   * 获取医院及科室所有信息
   *
   * @param instId 医院id
   * @return 返回医院及科室详细信息
   */
  @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
  @GetMapping(value = "/{id}")
  public Response<InstitutionDetailResponse> getHospitalInfo(
      @PathVariable(value = "id") String instId, @CurrentUserId Long userId) {
    return Response.instanceSuccess(userService.getOrgInfoOfUser(instId, userId));
  }

  /**
   * 添加科室信息 目前是单条科室添加
   *
   * @param id 医院id
   * @param body 科室信息
   * @return 结果
   */
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping(value = "/{id}/department")
  public Response addHospitalDepartment(
      @PathVariable String id, @RequestBody HospitalDepartmentRequest body,
      @CurrentUserId Long userId) {
    if (StringUtils.isEmpty(body.getName())) {
      logger.info("添加机构部门失败，参数错误，机构id-{},body={}", id, body);
      throw new FailureException(InstitutionErrorCode.ADD_DEPARTMENT_FAIL);
    }
    Department result = userService
        .addOrgDepartment(body.getName(), id, userId);
    return Response.instanceSuccess(result);
  }

  /**
   * 修改科室名称
   *
   * @param id 科室id
   * @param body 科室信息
   * @return 结果
   */
  @PreAuthorize("hasRole('ADMIN')")
  @PatchMapping(value = "/department/{id}")
  public Response renameHospitalDepartment(
      @PathVariable String id, @RequestBody HospitalDepartmentRequest body,
      @CurrentUserId Long adminUserId) {
    if (StringUtils.isEmpty(body.getName())) {
      logger.info("添加机构部门失败，参数错误，机构id-{}", id);
      throw new FailureException(InstitutionErrorCode.ADD_DEPARTMENT_FAIL);
    }
    boolean result = userService
        .renameOrgDepartment(body.getName(), id, adminUserId);
    if (!result) {
      logger.info("修改科室名称失败 id={} | request={}"
          , id, body);
      throw new FailureException();
    }
    return Response.instanceSuccess();
  }


  /**
   * Delete a department.
   *
   * @param id department id
   * @return Success response if ok otherwise fail response
   */
  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping(value = "/department/{id}")
  public Response deleteDepartment(@CurrentUserId long userId,
      @PathVariable(value = "id") String id) {
    return userService.deleteDepartment(userId, id);
  }

  /**
   * get hospital info
   *
   * @return hospital info
   */
  @PreAuthorize("hasRole('OPERATOR')")
  @GetMapping
  public Response getHospital(
      @RequestParam(value = "pageAt", required = false) Integer pageAt,
      @RequestParam(value = "pageSize", required = false) Integer pageSize) {

    InstitutionResponse organization =
        userService.getOrganization(pageAt, pageSize);

    if (organization == null) {
      throw new FailureException(
          ErrorCode.REQUEST_FAILED, "hospital info is null !");
    }
    return Response.instanceSuccess(organization);
  }
}