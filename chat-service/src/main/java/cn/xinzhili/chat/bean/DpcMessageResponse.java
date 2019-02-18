package cn.xinzhili.chat.bean;

import java.util.List;
import lombok.Data;
import org.joda.time.DateTime;

@Data
public class DpcMessageResponse {

  private List<DpcMessage> messages;

  @Data
  public static class DpcMessage {

    private Long id;
    private DateTime createdAt;
    private String sender;
    private String receiver;
    private DpcContent content;
    private DateTime readAt;
    private Integer doctorRead;
    private Integer assistantRead;
    private Long commitTimes;
  }

  @Data
  public static class DpcContent {

    private ContentData data;
    private Integer type;
  }

  @Data
  public static class ContentData {

    private Integer type;
    private String content;
  }
}
