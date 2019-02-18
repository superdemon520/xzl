package cn.xinzhili.api.doctor.bean;

import cn.xinzhili.xutils.core.IntCodeEnum;

/**
 * @author by Loki on 17/9/1.
 */
public enum StatisticAdviceDateType implements IntCodeEnum {

  ONE_MONTH(1, "一个月"),
  THREE_MONTH(3, "三个月"),
  HALF_YEAR(6, "半年"),
  ONE_YEAR(12, "一年");

  StatisticAdviceDateType(int code, String description) {
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
