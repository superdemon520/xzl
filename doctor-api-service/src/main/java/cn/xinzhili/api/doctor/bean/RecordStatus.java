package cn.xinzhili.api.doctor.bean;


public enum RecordStatus {

  AUTO_GENERATE(0, "自动补全"),
  PATIENT_TAKE(1, "患者手动添加"),
  NOT_TAKE(2, "患者未服药");

  RecordStatus(int code, String description) {
    this.code = code;
    this.description = description;
  }

  private int code;

  private String description;

  public int getCode() {
    return code;
  }

  public String getDescription() {
    return description;
  }
}
