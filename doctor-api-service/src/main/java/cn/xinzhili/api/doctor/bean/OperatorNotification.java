package cn.xinzhili.api.doctor.bean;

import lombok.Data;

@Data
public class OperatorNotification {

  private String id;
  private OperatorNotificationCategory category;
  private String content;
  private OperatorNotificationStatus status;
  private Long updatedAt;
  private Long readAt;
}
