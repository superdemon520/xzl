package cn.xinzhili.chat.api.request;

import cn.xinzhili.chat.api.MessageType;
import cn.xinzhili.chat.api.RoleType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/5/3 下午3:47
 */
@Data
public class AddMessageRequest {

  @ApiModelProperty(value = "发送人id")
  @NotNull(message = "发送人不能为空")
  private Long senderId;

  @ApiModelProperty(value = "角色")
  @NotNull(message = "发送人角色不能为空")
  private RoleType senderRoleType;

  @ApiModelProperty(value = "群组id")
  @NotNull(message = "接受群组id不能为空")
  private Long sessionId;

  @ApiModelProperty(value = "消息内容")
  @NotEmpty(message = "消息内容")
  private String content;

  @ApiModelProperty(value = "消息类型")
  @NotNull(message = "消息类型不能为空")
  private MessageType messageType;

  @ApiModelProperty(value = "接收人id")
  private Long receiverId;

  @ApiModelProperty(value = "机构id")
  private Long organizationId;

  @ApiModelProperty(value = "头像url")
  private String avatar;

  @ApiModelProperty(value = "发送人名")
  private String name;

}
