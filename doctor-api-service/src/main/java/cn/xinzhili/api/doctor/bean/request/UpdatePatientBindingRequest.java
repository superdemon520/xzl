package cn.xinzhili.api.doctor.bean.request;

import java.util.List;
import java.util.Objects;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

/**
 * @author by Loki on 17/3/22.
 */
@Data
public class UpdatePatientBindingRequest {

  private List<String> patientIds;
  private String doctorId;
  private String assistantId;
  private String operatorId;
  private String organizationId;

  public boolean invalid() {
    return Objects.isNull(getPatientIds())
        || StringUtils.isEmpty(getOrganizationId())
        || (StringUtils.isEmpty(getDoctorId()) && StringUtils.isEmpty(getAssistantId())
        && StringUtils.isEmpty(getOperatorId()));
  }
}
