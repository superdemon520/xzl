package cn.xinzhili.api.doctor.util;

import cn.xinzhili.api.doctor.bean.OperatorNotification;
import cn.xinzhili.api.doctor.bean.OperatorNotificationCategory;
import cn.xinzhili.api.doctor.bean.OperatorNotificationStatus;
import cn.xinzhili.api.doctor.bean.request.UpdateOperatorNotificationRequest;
import cn.xinzhili.dpc.api.StaffNotificationCategory;
import cn.xinzhili.dpc.api.StaffNotificationInfo;
import cn.xinzhili.dpc.api.StaffNotificationStatus;
import cn.xinzhili.dpc.api.request.UpdateNotificationStatusRequest;
import cn.xinzhili.xutils.core.util.HashUtils;

public class OperatorNotificationFactory {

  public static OperatorNotification api(StaffNotificationInfo notification) {

    OperatorNotification operatorNotification = new OperatorNotification();
    operatorNotification.setId(HashUtils.encode(notification.getId()));
    operatorNotification.setContent(notification.getContent());
    operatorNotification
        .setCategory(OperatorNotificationCategory.valueOf(notification.getCategory().name()));
    operatorNotification
        .setStatus(OperatorNotificationStatus.valueOf(notification.getStatus().name()));
    operatorNotification.setUpdatedAt(notification.getUpdatedAt());
    operatorNotification.setReadAt(notification.getReadAt());
    return operatorNotification;
  }

  public static UpdateNotificationStatusRequest of(UpdateOperatorNotificationRequest request) {

    UpdateNotificationStatusRequest req = new UpdateNotificationStatusRequest();
    req.setId(HashUtils.decode(request.getId()));
    req.setCategory(StaffNotificationCategory.valueOf(request.getCategory().name()));
    req.setStatus(StaffNotificationStatus.valueOf(request.getStatus().name()));
    return req;
  }

}
