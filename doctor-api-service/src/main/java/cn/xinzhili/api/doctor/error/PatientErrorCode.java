package cn.xinzhili.api.doctor.error;

import cn.xinzhili.xutils.core.Code;

/**
 * Created by marlinl on 31/03/2017.
 */
public enum  PatientErrorCode implements Code{

  UPDATE_PATIENT_FAIL(2200,"更新患者信息失败"),
  PATIENT_REGION_INFO_ERROR(2201,"患者地理信息错误"),


  ;

  private final int code;
  private final String description;

  PatientErrorCode(int code, String description) {
    this.code = code;
    this.description = description;
  }

  @Override
  public int getCode() {
    return 0;
  }

  @Override
  public String getDescription() {
    return null;
  }
}
