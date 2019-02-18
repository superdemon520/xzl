package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.UnreadCountInfo;
import java.util.List;
import lombok.Data;

@Data
public class UnreadCountApiResponse {

  private int patientUnreadCount;
  private int otherUnreadCount;
  private List<UnreadCountInfo> medicalUnreadCounts;
}
