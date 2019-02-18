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
public enum UnreadStatus implements IntCodeEnum {
  BLOCK(0, "消息数目正常显示"),
  NONE(1, "显示小红点");

  private int code;
  private String description;
}
