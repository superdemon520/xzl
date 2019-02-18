package cn.xinzhili.api.doctor.bean;

import cn.xinzhili.xutils.core.IntCodeEnum;

/**
 * @author by Loki on 17/9/1.
 */
public enum NoticeType implements IntCodeEnum {

  MESSAGE(0, "聊天"),
  ADVICE(1, "医嘱"),//2为患者端登陆推送
  INQUIRY(3, "生活采集表"),
  IMAGE_BLURRING(4, "图片审核不清晰");

  NoticeType(int code, String description) {
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

  public static String getNotifyTitle() {
    return NOTIFY_TITLE;
  }

  public static String getNotifyAdviceAlert() {
    return NOTIFY_ADVICE_ALERT;
  }

  public static String getNotifyMessageAlert() {
    return NOTIFY_MESSAGE_ALERT;
  }

  public static String getImageBlurringAlert() {
    return IMAGE_BLURRING_ALERT;
  }

  public static final String NOTIFY_TITLE = "心之力提醒";

  private static final String NOTIFY_ADVICE_ALERT = "医生给您发送了一条通知";
  private static final String NOTIFY_MESSAGE_ALERT = "医生给您发送了一条消息";
  private static final String IMAGE_BLURRING_ALERT = "心之力给您发送了一条消息";

  public static String getNoticeAlert(NoticeType type) {

    if (type == MESSAGE) {
      return NOTIFY_MESSAGE_ALERT;
    } else if (type == ADVICE) {
      return NOTIFY_ADVICE_ALERT;
    } else if (type == IMAGE_BLURRING) {
      return IMAGE_BLURRING_ALERT;
    } else {
      return "";
    }

  }
}
