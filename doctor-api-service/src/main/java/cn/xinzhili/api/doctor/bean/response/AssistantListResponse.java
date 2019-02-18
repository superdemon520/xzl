package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.Assistant;

import java.util.List;

/**
 * @author by Loki on 17/2/24.
 */
public class AssistantListResponse {

  private List<Assistant> assistants;
  private Integer total;

  public List<Assistant> getAssistants() {
    return assistants;
  }

  public void setAssistants(List<Assistant> assistants) {
    this.assistants = assistants;
  }

  public Integer getTotal() {
    return total;
  }

  public void setTotal(Integer total) {
    this.total = total;
  }
}
