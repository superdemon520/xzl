package cn.xinzhili.api.doctor.bean;

import cn.xinzhili.xutils.core.IntCodeEnum;

/**
 * @author by Loki on 17/3/28.
 */
public enum LatestInspectionType implements IntCodeEnum {

  BLOOD_PRESSURE(0, "血压"),
  HEART_RATE(1, "心率"),
  BLOOD_SUGAR(2, "血糖"),;

  LatestInspectionType(int code, String description) {
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
