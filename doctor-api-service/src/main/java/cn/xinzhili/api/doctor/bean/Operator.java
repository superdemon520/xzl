package cn.xinzhili.api.doctor.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Operator extends User {

  private String id;
}
