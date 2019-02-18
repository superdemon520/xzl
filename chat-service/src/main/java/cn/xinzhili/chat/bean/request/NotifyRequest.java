package cn.xinzhili.chat.bean.request;

import cn.xinzhili.chat.bean.notify.NotifyContent;
import java.util.List;
import lombok.Data;

@Data
public class NotifyRequest {

  private String title;
  private String alert;
  private List<String> receivers;
  private NotifyContent content;

}
