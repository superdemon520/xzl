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
public enum Type implements IntCodeEnum {

  GROUP_ALL(0, "患者可见的聊天群"),
  GROUP_ORG(1, "患者不可见的聊天群");

  private int code;
  private String description;
}
