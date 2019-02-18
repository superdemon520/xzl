package cn.xinzhili.api.doctor.bean;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/4/16 下午5:55
 */
public class CountAdvicesApiInfo {

  private String advicesTypeName;
  private int countAdvices;

  public String getAdvicesTypeName() {
    return advicesTypeName;
  }

  public void setAdvicesTypeName(String advicesTypeName) {
    this.advicesTypeName = advicesTypeName;
  }

  public int getCountAdvices() {
    return countAdvices;
  }

  public void setCountAdvices(int countAdvices) {
    this.countAdvices = countAdvices;
  }
}
