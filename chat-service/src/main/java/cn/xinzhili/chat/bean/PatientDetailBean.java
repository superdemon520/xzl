package cn.xinzhili.chat.bean;

import cn.xinzhili.chat.api.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/5/12 下午3:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDetailBean {

  private Long id;
  private RoleType roleType;

}
