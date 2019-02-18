package cn.xinzhili.chat.bean.notify;

import lombok.Data;

@Data
public class NotifyContentData {

  private int type;
  private String content;
  private long createdAt;
}
