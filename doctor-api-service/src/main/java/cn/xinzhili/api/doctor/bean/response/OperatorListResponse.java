package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.Operator;
import java.util.List;

/**
 * @author by Loki on 18/04/04.
 */
public class OperatorListResponse {

  private List<Operator> operators;
  private Integer total;

  public OperatorListResponse() {
  }

  public OperatorListResponse(List<Operator> operators, Integer total) {
    this.operators = operators;
    this.total = total;
  }

  public List<Operator> getOperators() {
    return operators;
  }

  public void setOperators(List<Operator> operators) {
    this.operators = operators;
  }

  public Integer getTotal() {
    return total;
  }

  public void setTotal(Integer total) {
    this.total = total;
  }
}
