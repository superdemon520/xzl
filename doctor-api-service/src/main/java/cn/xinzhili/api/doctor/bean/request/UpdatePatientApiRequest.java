package cn.xinzhili.api.doctor.bean.request;

import cn.xinzhili.api.doctor.bean.PatientApiProgress;
import cn.xinzhili.api.doctor.bean.UserGender;
import cn.xinzhili.user.api.PatientMarriage;
import cn.xinzhili.user.api.ServiceLevel;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

/**
 * @author by Loki on 17/3/6.
 */
@Data
public class UpdatePatientApiRequest {

  private String id;
  private String name;
  private UserGender sex;
  private Integer height;
  private Integer waistline;
  private Integer weight;
  private String ethnicity;
  private PatientMarriage marriage;//TODO api marriage
  private String address;
  private String doctorId;
  private String assistantId;
  private String operatorId;
  private Integer province;
  private Integer city;
  private Integer town;
  private String remark;
  private PatientApiProgress progress;
  private ServiceLevel serviceLevel;
  private String organizationId;
  private Long birthday;

  public boolean invalid() {
    return StringUtils.isEmpty(getId()) || StringUtils.isEmpty(getOrganizationId());
  }
}
