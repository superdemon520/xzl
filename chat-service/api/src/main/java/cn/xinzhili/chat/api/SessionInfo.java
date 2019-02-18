package cn.xinzhili.chat.api;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/5/7 上午11:07
 */
@Data
public class SessionInfo {

  private Long id;
  @ApiModelProperty("发起人id")
  private Long initiatorId;
  @ApiModelProperty("发起人角色 PATIENT' 患者, 'OPERATOR' 运营, 'ASSISTANT'医助, 'DOCTOR'医生")
  private RoleType initiatorRoleType;
  @ApiModelProperty("机构id")
  private Long organizationId;
  @ApiModelProperty("群组类型 ALL 患者可见 ORG 患者不可见")
  private Type type;
  @ApiModelProperty("session类型 GROUP群组 SINGLE 单聊 SYSTEM_PUSH系统 ")
  private SessionType sessionType;
  @ApiModelProperty("当前会话人员信息")
  private List<MessageSessionInfo> messageSessionInfos;
}
