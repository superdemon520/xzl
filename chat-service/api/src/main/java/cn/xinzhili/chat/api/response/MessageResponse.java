package cn.xinzhili.chat.api.response;

import cn.xinzhili.chat.api.MessageInfo;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/5/4 下午3:11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {

  private List<MessageInfo> messageInfos;
  private int total;

}
