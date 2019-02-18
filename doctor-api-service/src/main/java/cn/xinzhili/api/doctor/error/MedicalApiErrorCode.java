package cn.xinzhili.api.doctor.error;

import cn.xinzhili.xutils.core.Code;

/**
 * Time : 17/04/17
 *
 * @author Loki
 */
public enum MedicalApiErrorCode implements Code {

  DIAGNOSIS_OVER_LIMIT(2201, "诊断数量超过上限"),
  DIAGNOSIS_STAGE1_EXIST(2202, "一级诊断已经存在，不能重复添加"),
  DIAGNOSIS_STAGE2_EXIST(2203, "一级诊断已经存在，不能重复添加"),
  DIAGNOSIS_STAGE3_EXIST(2204, "一级诊断已经存在，不能重复添加"),

  GET_MEDICINE_TABOO_FAILED(2211, "获取药品关联禁忌症失败"),

  GET_TREATMENT_FAILED(2221,"获取处理方式失败"),
  GET_STENT_FAILED(2222,"获取冠状支架失败");

  MedicalApiErrorCode(int code, String description) {
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
