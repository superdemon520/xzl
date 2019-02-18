package cn.xinzhili.api.doctor.bean;

import cn.xinzhili.xutils.core.IntCodeEnum;

/**
 * @author by Loki on 17/8/11.
 */
public enum AdviceCategory implements IntCodeEnum {

  NORMAL(0, "普通医嘱"),
  MEDICINE_ADJUSTMENT(1, "调药医嘱"),
  RE_EXAMINATION(2, "复查"),
  SIDE_EFFECT_MEDICINE_ADJUSTMENT(3, "副作用调药"),
  CONTRAINDICATION_MEDICINE_ADJUSTMENT(4, "禁忌症调药"),
  LIVING_STANDARD_MEDICINE_ADJUSTMENT(5, "生活达标调药"),
  INSPECTION_STANDARD(6, "用药达标值调整");

  AdviceCategory(int code, String description) {
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
