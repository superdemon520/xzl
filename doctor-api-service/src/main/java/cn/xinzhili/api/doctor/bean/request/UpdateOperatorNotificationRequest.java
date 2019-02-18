package cn.xinzhili.api.doctor.bean.request;

import cn.xinzhili.api.doctor.bean.OperatorNotificationCategory;
import cn.xinzhili.api.doctor.bean.OperatorNotificationStatus;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang.StringUtils;

public class UpdateOperatorNotificationRequest {

  private String id;
  private OperatorNotificationCategory category;
  private OperatorNotificationStatus status;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public OperatorNotificationCategory getCategory() {
    return category;
  }

  public void setCategory(OperatorNotificationCategory category) {
    this.category = category;
  }

  public OperatorNotificationStatus getStatus() {
    return status;
  }

  public void setStatus(OperatorNotificationStatus status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return "UpdateOperatorNotificationRequest{" +
        "id='" + id + '\'' +
        ", category=" + category +
        ", status=" + status +
        '}';
  }

  public boolean invalid() {
    return StringUtils.isEmpty(getId()) || Objects.isNull(getCategory()) || Objects
        .isNull(getStatus()) || statusInvalid() || categoryInvalid();
  }

  private boolean statusInvalid() {
    return Objects.nonNull(getStatus()) &&
        !List.of(OperatorNotificationStatus.ACCEPT, OperatorNotificationStatus.REFUSED)
            .contains(getStatus());
  }

  private boolean categoryInvalid() {
    return Objects.nonNull(getCategory())
        && OperatorNotificationCategory.INVITE_FROM_ORGANIZATION != getCategory();
  }
}
