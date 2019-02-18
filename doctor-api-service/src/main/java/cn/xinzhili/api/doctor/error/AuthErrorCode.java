package cn.xinzhili.api.doctor.error;

import cn.xinzhili.xutils.core.Code;

/**
 * Created by liuhao on 23/08/2017.
 */
public enum AuthErrorCode implements Code {

  DOCTOR_UNAUTH_MODIFY(2300, "医生不能修改医嘱"),
  DOCTOR_NO_NEED_MODIFY(2301, "医生不需修改该医嘱"),;

  AuthErrorCode(int code, String description) {
    this.code = code;
    this.description = description;
  }

  private int code;

  private String description;

  @Override
  public int getCode() {
    return code;
  }

  @Override
  public String getDescription() {
    return description;
  }
}
