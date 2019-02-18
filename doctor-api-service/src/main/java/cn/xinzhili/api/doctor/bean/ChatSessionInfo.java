package cn.xinzhili.api.doctor.bean;

import cn.xinzhili.chat.api.SessionType;
import cn.xinzhili.chat.api.Type;
import java.util.List;
import lombok.Data;

@Data
public class ChatSessionInfo {

  private String sessionId;
  private String organizationId;
  private SessionType sessionType;
  private Type groupType;
  private String initiator;
  private List<GroupMember> members;

}
