package cn.xinzhili.api.doctor.bean.request;

import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UpdateFutureDoctorApiRequest extends AddFutureDoctorApiRequest {

  @NotNull
  private String id;
}
