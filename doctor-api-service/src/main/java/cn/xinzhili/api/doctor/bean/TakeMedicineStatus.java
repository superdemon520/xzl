package cn.xinzhili.api.doctor.bean;

public enum TakeMedicineStatus {
  NOT_TAKE(0, "全部未吃"),
  TAKE_ALL(1, "已吃全部"),
  TAKE_PART(2, "已吃部分");

  TakeMedicineStatus(int code, String description) {
    this.code = code;
    this.description = description;
  }

  private int code;

  private String description;

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
