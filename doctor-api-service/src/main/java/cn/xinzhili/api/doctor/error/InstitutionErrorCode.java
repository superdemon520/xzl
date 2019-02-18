package cn.xinzhili.api.doctor.error;

import cn.xinzhili.xutils.core.Code;

/**
 * Created by MarlinL on 15/02/2017.
 */
public enum InstitutionErrorCode implements Code {

  NOT_FOUND_HOSPITAL(2101, "没有找到医院"),

  ADD_DEPARTMENT_FAIL(2102, "无法添加部门"),

  DEPARTMENT_ALREADY_EXISTS(2103, "部门名称已经存在"),

  ;

  private final int code;
  private final String description;

  InstitutionErrorCode(int code, String description) {
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
