package cn.xinzhili.api.doctor.bean.request;

import lombok.Data;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/5/9 下午3:19
 */
@Data
public class CleanUnreadApiRequest {

  private String sessionId;
  private String senderId;
}
