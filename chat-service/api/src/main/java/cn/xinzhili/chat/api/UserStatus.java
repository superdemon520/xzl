package cn.xinzhili.chat.api;

import cn.xinzhili.xutils.core.IntCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/5/3 下午2:30
 */
@Getter
@AllArgsConstructor
public enum UserStatus implements IntCodeEnum {
  NORMAL(0, "正常"),
  PROHIBITION_OF_SPEECH(1, "禁止发言"),
  INVALID(2, "失效");

  private int code;
  private String description;
}
