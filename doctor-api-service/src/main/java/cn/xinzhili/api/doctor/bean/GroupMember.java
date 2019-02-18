package cn.xinzhili.api.doctor.bean;

import cn.xinzhili.chat.api.RoleType;
import cn.xinzhili.chat.api.UnreadStatus;
import cn.xinzhili.chat.api.UserStatus;
import lombok.Data;

@Data
public class GroupMember {

  private String name;
  private String avatar;
  private String userId;
  private RoleType role;
  private UserStatus userStatus;
  private UnreadStatus countShow;

}
