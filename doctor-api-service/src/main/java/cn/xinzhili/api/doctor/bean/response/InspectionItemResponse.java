package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.InspectionItem;
import java.util.List;

/**
 * @author by Loki on 17/4/17.
 */
public class InspectionItemResponse {

  private List<InspectionItem> items;

  public List<InspectionItem> getItems() {
    return items;
  }

  public void setItems(List<InspectionItem> items) {
    this.items = items;
  }
}
