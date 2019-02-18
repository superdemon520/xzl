package cn.xinzhili.api.doctor.bean.request;

import cn.xinzhili.api.doctor.bean.DoctorTitle;
import cn.xinzhili.api.doctor.util.ValidationUtils;
import java.util.Objects;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author by MarlinL on 15/02/2017.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AddDoctorApiRequest extends AddStaffApiRequest {

  private String name;
  private DoctorTitle title;

  public boolean invalid() {
    return super.invalid() || Objects.isNull(getTitle()) || !ValidationUtils.isValidName(getName());
  }
}
