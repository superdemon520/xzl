package cn.xinzhili.chat.api.response;

import cn.xinzhili.chat.api.SessionInfo;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/5/7 上午11:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionResponse {

  @ApiModelProperty(value = "群组详情")
  private List<SessionInfo> session;
}
