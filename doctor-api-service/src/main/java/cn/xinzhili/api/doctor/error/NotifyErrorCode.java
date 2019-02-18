package cn.xinzhili.api.doctor.error;

import cn.xinzhili.xutils.core.Code;

/**
 * Created by marlinl on 22/03/2017.
 */
public enum  NotifyErrorCode implements Code {

  SMS_EXCEED_RATE_LIMIT(18001, "发送短信过于频繁"),

  SMS_VERIFICATION_CODE_INCORRECT(18002, "验证码不正确"),

  SMS_SERVER_ERROR(18003,"发送验证码失败");

  private final int code;
  private final String description;

  NotifyErrorCode(int code, String description) {
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
