package cn.xinzhili.chat.api;

import cn.xinzhili.xutils.core.Code;
import cn.xinzhili.xutils.core.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/5/9 上午9:48
 */
@Getter
@AllArgsConstructor
public enum ChatErrorCode implements Code {
  MISMATCHING_ERROR(450, "用户不在群组中"),;

  private final int code;
  private final String description;


  @Override
  public String toString() {
    return code + ": " + description;
  }
}
