package cn.xinzhili.chat.bean.notify;

import cn.xinzhili.xutils.core.IntCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author by Loki on 17/9/1.
 */
@Getter
@AllArgsConstructor
public enum NoticeType implements IntCodeEnum {

  MESSAGE(0, "聊天");
  private int code;

  private String description;

  public static final String NOTIFY_TITLE = "心之力提醒";

  private static final String NOTIFY_MESSAGE_ALERT = "医生给您发送了一条消息";

  public static String getNoticeAlert(NoticeType type) {

    if (type == MESSAGE) {
      return NOTIFY_MESSAGE_ALERT;
    } else {
      return "";
    }

  }
}
