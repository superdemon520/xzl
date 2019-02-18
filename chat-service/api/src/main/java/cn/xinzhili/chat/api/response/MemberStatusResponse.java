package cn.xinzhili.chat.api.response;

import cn.xinzhili.chat.api.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/5/15 下午12:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberStatusResponse {

  private UserStatus assistantStatus;
}
