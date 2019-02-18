package cn.xinzhili.api.doctor.bean.request;

import cn.xinzhili.user.api.ServiceLevel;
import java.util.Objects;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

/**
 * @author by Loki on 17/3/1.
 */
@Data
public class AddPatientApiRequest {

  private String tel;
  private String doctorId;
  private String assistantId;
  private String operatorId;
  private ServiceLevel serviceLevel;
  private String organizationId;

  public boolean invalid() {
    return StringUtils.isEmpty(getOrganizationId())
        || StringUtils.isEmpty(getTel()) || StringUtils.isEmpty(getDoctorId())
        || StringUtils.isEmpty(getOperatorId()) || Objects.isNull(getServiceLevel());
  }
}
