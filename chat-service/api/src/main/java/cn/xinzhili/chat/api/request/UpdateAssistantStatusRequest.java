package cn.xinzhili.chat.api.request;

import cn.xinzhili.chat.api.UserStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/5/14 上午10:11
 */
@Data
public class UpdateAssistantStatusRequest {

  @ApiModelProperty("大医生id")
  private Long doctorId;

  @ApiModelProperty("医助id")
  private Long assistantId;

  @ApiModelProperty("用户状态 NORMAL|正常  PROHIBITION_OF_SPEECH|禁止发言 INVALID|失效")
  private UserStatus userStatus;

}
