package cn.xinzhili.chat.api.request;

import cn.xinzhili.chat.api.RoleType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/5/9 下午3:23
 */
@Data
public class CleanUnreadRequest {


  @ApiModelProperty("当前用户id")
  private Long userId;

  @ApiModelProperty("当前用户角色")
  private RoleType roleType;

  @ApiModelProperty("会话id")
  private Long sessionId;

  @ApiModelProperty("接受人id")
  private Long receiverId;

}
