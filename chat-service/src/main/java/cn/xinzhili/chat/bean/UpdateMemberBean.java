package cn.xinzhili.chat.bean;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/5/12 下午2:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMemberBean {

  private List<PatientDetailBean> addList;
  private List<PatientDetailBean> removeList;

}
