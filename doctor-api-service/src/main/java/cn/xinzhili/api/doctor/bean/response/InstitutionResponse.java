package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.Institution;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author by MarlinL on 13/02/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstitutionResponse {

  private List<Institution> institutions;
}
