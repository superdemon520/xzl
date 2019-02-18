package cn.xinzhili.api.doctor.bean.request;

import cn.xinzhili.api.doctor.bean.MessageApiInfo;
import cn.xinzhili.chat.api.RoleType;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/5/9 下午2:00
 */
@Data
public class SendMessageRequest {

  private String avatar;
  @NotEmpty
  private String name;
  @NotEmpty
  private String sender;
  @NotNull
  private RoleType senderRoleType;
  private String receiver;
  @NotEmpty
  private String sessionId;
  @NotNull
  private MessageApiInfo message;
  @NotEmpty
  private String organizationId;

}
