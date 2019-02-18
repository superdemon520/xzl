package cn.xinzhili.api.doctor.bean;

/**
 * @author by Loki on 17/3/8.
 */
public enum PlanUnit {

  //(1.mg 2.g 3.ml 4.IU 5.other)
  MG("mg", 1), G("g", 2), ML("ml", 3), IU("IU", 4), OTHER("other", 5);

  private String unit;
  private int index;

  PlanUnit(String unit, int index) {
    this.unit = unit;
    this.index = index;
  }

  public static String getUnit(int index) {
    for (PlanUnit c : PlanUnit.values()) {
      if (c.getIndex() == index) {
        return c.unit;
      }
    }
    return null;
  }

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

}
