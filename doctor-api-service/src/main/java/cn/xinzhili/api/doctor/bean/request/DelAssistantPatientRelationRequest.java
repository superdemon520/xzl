package cn.xinzhili.api.doctor.bean.request;

import java.util.List;

/**
 * Created by ywb on 28/3/2017.
 */
public class DelAssistantPatientRelationRequest {

  private List<String> assistantIds;

  public boolean invalid() {
    return assistantIds == null || assistantIds.isEmpty();
  }

  public List<String> getAssistantIds() {
    return assistantIds;
  }

  public void setAssistantIds(List<String> assistantIds) {
    this.assistantIds = assistantIds;
  }
}
