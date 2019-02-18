package cn.xinzhili.chat.api;

import cn.xinzhili.xutils.core.IntCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/5/8 下午4:51
 */
@Getter
@AllArgsConstructor
public enum SessionType implements IntCodeEnum {

  GROUP(0, "群组"),
  SINGLE(1, "单聊"),
  SYSTEM_PUSH(2, "系统");

  private int code;
  private String description;
}
