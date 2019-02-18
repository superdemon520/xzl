package cn.xinzhili.chat.api;

import cn.xinzhili.xutils.core.IntCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/5/3 下午2:30
 */
@Getter
@AllArgsConstructor
public enum MessageType implements IntCodeEnum {
  TEXT(0, "文本消息"),
  IMAGE(1, "图片消息"),
  TIPS(2,"提示消息");

  private int code;
  private String description;

}
