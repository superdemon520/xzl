package cn.xinzhili.api.doctor.bean.request;


import cn.xinzhili.api.doctor.util.ValidationUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author by MarlinL on 15/02/2017.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AddAssistantApiRequest extends AddStaffApiRequest {

  private String name;

  public boolean nameInvalid() {
    return !ValidationUtils.isValidName(getName());
  }

}
