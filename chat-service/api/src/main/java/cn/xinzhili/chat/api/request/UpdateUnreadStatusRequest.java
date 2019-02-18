package cn.xinzhili.chat.api.request;

import cn.xinzhili.chat.api.RoleType;
import cn.xinzhili.chat.api.UnreadStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/5/8 上午10:00
 */
@Data
public class UpdateUnreadStatusRequest {

  @ApiModelProperty("当前用户id")
  private Long userId;

  @ApiModelProperty("当前用户角色")
  private RoleType roleType;

  @ApiModelProperty("会话id")
  private Long sessionId;

  @ApiModelProperty("未读消息状态 BLOCK 消息数目正常显示 NONE显示小红点")
  private UnreadStatus unreadStatus;
}
