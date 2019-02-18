package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.ChatSessionInfo;
import java.util.List;
import lombok.Data;

@Data
public class ChatSessionApiResponse {

  List<ChatSessionInfo> sessions;

}
