package cn.xinzhili.api.doctor.error;

import cn.xinzhili.xutils.core.Code;

/**
 * Created by MarlinL on 15/02/2017.
 */
public enum UserErrorCode implements Code {

  NOT_FOUND_STAFF(2001, "没找到用户"),
  TEL_ALREADY_EXIST(2002, "该手机号已被注册"),
  DOCTOR_HAS_PATIENT(2003, "医生存在绑定患者，不能被删除"),
  BINDING_RELATION_INVALID(2004, "不合法的绑定关系"),
  NOT_PERMITTED_TO_SEND_INQUIRY(2005, "当前用户无权发送采集表"),
  PASSWORD_UPDATE_FAILED(2006, "重置用户密码失败"),
  NEW_PASSWORD_MUST_BE_DIFFERENT(2007, "新密码必须和原密码不同"),
  PASSWORD_INCORRECT(2008, "密码错误"),
  PASSWORD_FORMAT_INVALID(2009, "密码格式错误"),
  USER_STATUS_NOT_AVAILABLE(2010, "用户状态异常"),
  USER_UPDATE_FAILED(2011, "更新用户信息失败"),
  REGION_NOT_FOUND(2012, "没有找到省市县信息"),
  ASSISTANT_HAS_PATIENT(2013, "医生助理存在绑定患者，不能被删除"),
  INVALID_USER_NAME(2014, "填写的姓名格式错误"),
  NO_PATIENTS_ASSIGNED(2015, "当前医助没有关联的患者"),
  INVALID_UPLOAD_FILE(2016, "非法的文件格式"),
  INVALID_UPLOAD_FILE_HEADERS(2017, "EXCEL 文件头不正确"),
  INVALID_PHONE_NUMBER(2018, "非法的手机号"),

  GET_IMAGES_UPLOAD_FAIL(2019, "获取图片上传信息失败"),
  INVALID_STAFF_STATUS(2020, "不合法的用户,无权进行该操作"),
  OPERATOR_HAS_PATIENT(2021, "运营存在绑定患者，无法进行该操作"),
  DEFAULT_DATA_CAN_NOT_BE_UPDATE(2022, "该数据不可更新"),
  NO_OPERATOR_IN_DEPARTMENT(2023, "该机构科室下没有运营人员"),;

  private final int code;
  private final String description;

  UserErrorCode(int code, String description) {
    this.code = code;
    this.description = description;
  }

  @Override
  public int getCode() {
    return code;
  }

  @Override
  public String getDescription() {
    return description;
  }
}
