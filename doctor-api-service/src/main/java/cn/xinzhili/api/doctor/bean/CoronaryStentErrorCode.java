package cn.xinzhili.api.doctor.bean;

import cn.xinzhili.xutils.core.Code;

public enum CoronaryStentErrorCode implements Code {

  OUT_OF_MAX_SIZE(100, "支架数量不能超过10个");

  CoronaryStentErrorCode(int code, String description) {
    this.code = code;
    this.description = description;
  }

  private int code;
  private String description;

  public int getCode() {
    return code;
  }

  public String getDescription() {
    return description;
  }

}
