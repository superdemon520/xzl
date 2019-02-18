package cn.xinzhili.api.doctor.bean.request;

import cn.xinzhili.api.doctor.bean.UserGender;
import cn.xinzhili.user.api.util.StringUtils;
import lombok.Data;

@Data
public class AddStaffApiRequest {

  private String tel;
  private UserGender sex;
  private String organizationId;
  private String departmentId;

  public boolean invalid() {
    return StringUtils.isEmpty(getTel())
        || StringUtils.isEmpty(getOrganizationId())
        || StringUtils.isEmpty(getDepartmentId());
  }
}
