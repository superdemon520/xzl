package cn.xinzhili.chat.api;

import cn.xinzhili.xutils.core.IntCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/5/3 下午2:30
 */
@Getter
@AllArgsConstructor
public enum RoleType implements IntCodeEnum {
  PATIENT(0, "患者"),
  OPERATOR(1, "运营"),
  ASSISTANT(2, "医助"),
  DOCTOR(3, "医生");

  private int code;
  private String description;
}
