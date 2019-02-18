package cn.xinzhili.api.doctor.bean;

import cn.xinzhili.xutils.core.IntCodeEnum;

/**
 * @author by Loki on 17/9/1.
 */
public enum NoticeContentType implements IntCodeEnum {

  TEXT(0, "文本"),
  LINK(1, "链接");

  NoticeContentType(int code, String description) {
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
