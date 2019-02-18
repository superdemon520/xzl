package cn.xinzhili.chat.bean.notify;

import cn.xinzhili.xutils.core.IntCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author by Loki on 17/9/1.
 */
@Getter
@AllArgsConstructor
public enum NoticeContentType implements IntCodeEnum {

  TEXT(0, "文本"),
  LINK(1, "链接");

  private int code;

  private String description;

}
