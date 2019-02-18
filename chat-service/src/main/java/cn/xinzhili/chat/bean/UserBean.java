package cn.xinzhili.chat.bean;

import java.io.Serializable;
import lombok.Data;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/5/10 上午11:47
 */
@Data
public class UserBean implements Serializable {

  private String avatar;
  private String name;
}
