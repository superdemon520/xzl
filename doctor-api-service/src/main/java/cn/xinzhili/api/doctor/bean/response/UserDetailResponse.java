package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.Department;
import cn.xinzhili.api.doctor.bean.Institution;
import cn.xinzhili.api.doctor.bean.User;
import lombok.Data;

/**
 * 登录用户详细信息
 */
@Data
public class UserDetailResponse {

  private User user;

  private Institution institution;

  private Department department;

  private String reviewOrgId;
}
