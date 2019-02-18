package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.MessageDetailInfo;
import java.util.List;
import lombok.Data;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/5/9 下午2:32
 */
@Data
public class MessageListResponse {

  private List<MessageDetailInfo> messageDetail;
  private Integer total;
}
