package cn.xinzhili.api.doctor.bean;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/4/24 下午2:43
 */
public class MedicineInfo {

  private String name;
  private String commodityName;
  private String strength;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCommodityName() {
    return commodityName;
  }

  public void setCommodityName(String commodityName) {
    this.commodityName = commodityName;
  }

  public String getStrength() {
    return strength;
  }

  public void setStrength(String strength) {
    this.strength = strength;
  }
}
