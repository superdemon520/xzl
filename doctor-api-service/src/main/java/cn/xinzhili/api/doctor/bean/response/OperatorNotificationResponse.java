package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.OperatorNotification;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperatorNotificationResponse {

  private List<OperatorNotification> notifications;
  private int total;
  private boolean isHasUnRead;
}
