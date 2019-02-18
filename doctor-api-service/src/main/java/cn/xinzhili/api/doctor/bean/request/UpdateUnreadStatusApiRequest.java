package cn.xinzhili.api.doctor.bean.request;

import cn.xinzhili.chat.api.RoleType;
import cn.xinzhili.chat.api.UnreadStatus;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/5/9 下午3:19
 */
@Data
public class UpdateUnreadStatusApiRequest {

  @NotEmpty
  private String sessionId;
  @NotNull(message = "unreadStatus can not be null")
  private UnreadStatus unreadStatus;
  @NotNull(message = "roleType can not be null")
  private RoleType roleType;

}
