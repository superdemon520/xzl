package cn.xinzhili.api.doctor.bean.request;

import cn.xinzhili.api.doctor.bean.UserChatStatus;
import cn.xinzhili.chat.api.UserStatus;
import lombok.Data;

@Data
public class UpdateAssistantChatStatusRequest {

  private String assistantId;
  private UserChatStatus status;

}
