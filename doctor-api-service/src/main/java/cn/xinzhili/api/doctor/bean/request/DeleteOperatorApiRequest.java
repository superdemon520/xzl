package cn.xinzhili.api.doctor.bean.request;

import java.util.List;
import java.util.Objects;

public class DeleteOperatorApiRequest {

  private List<String> operatorId;
  private String organizationId;

  public List<String> getOperatorId() {
    return operatorId;
  }

  public void setOperatorId(List<String> operatorId) {
    this.operatorId = operatorId;
  }

  public String getOrganizationId() {
    return organizationId;
  }

  public void setOrganizationId(String organizationId) {
    this.organizationId = organizationId;
  }

  @Override
  public String toString() {
    return "DeleteOperatorApiRequest{" +
        "operatorId=" + operatorId +
        ", organizationId='" + organizationId + '\'' +
        '}';
  }

  public boolean invalid() {
    return Objects.isNull(operatorId) || Objects.isNull(organizationId);
  }
}
