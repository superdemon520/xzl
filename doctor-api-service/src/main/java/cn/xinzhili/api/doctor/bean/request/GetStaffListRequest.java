package cn.xinzhili.api.doctor.bean.request;

import cn.xinzhili.api.doctor.bean.UserInviteStatus;
import cn.xinzhili.api.doctor.bean.UserStatus;
import java.util.List;
import java.util.Objects;
import lombok.Data;

@Data
public class GetStaffListRequest {

  private String organizationId;
  private String departmentId;
  private String doctorId;
  private String keyword;
  private Boolean isBindingPatient;
  private String title;
  private List<UserInviteStatus> inviteStatuses;
  private List<UserStatus> status;
  private Integer pageAt;
  private Integer pageSize;

  public boolean invalid() {
    return Objects.isNull(organizationId) || Objects.isNull(departmentId);
  }
}
