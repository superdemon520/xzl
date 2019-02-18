package cn.xinzhili.api.doctor.bean.request;

import cn.xinzhili.api.doctor.bean.UserRole;
import cn.xinzhili.user.api.util.StringUtils;
import java.util.List;
import java.util.Objects;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

/**
 * Created by @xin.
 */
@Data
public class StaffDepartmentRequest {

  private List<String> StaffIds;
  private String departmentId;
  private UserRole userRole;

  public boolean invalid() {
    return CollectionUtils.isEmpty(getStaffIds()) || StringUtils.isEmpty(getDepartmentId())
        || Objects.isNull(getUserRole());
  }
}
