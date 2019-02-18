package cn.xinzhili.api.doctor.bean.request;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

@Data
public class SendInquiryRequest {

  private String organizationId;

  public boolean invalid() {
    return StringUtils.isEmpty(getOrganizationId());
  }

}
