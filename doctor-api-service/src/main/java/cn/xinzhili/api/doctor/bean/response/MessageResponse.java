package cn.xinzhili.api.doctor.bean.response;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/3/23 下午3:59
 */
public class MessageResponse {
  private Integer number;

  public Integer getNumber() {
    return number;
  }

  public void setNumber(Integer number) {
    this.number = number;
  }

  public MessageResponse(Integer number) {
    this.number = number;
  }

  public MessageResponse() {
  }
}
