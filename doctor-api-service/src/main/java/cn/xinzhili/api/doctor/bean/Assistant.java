package cn.xinzhili.api.doctor.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author by Loki on 17/2/23.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Assistant extends User {

  private String id;
  private UserChatStatus chatStatus;

}
