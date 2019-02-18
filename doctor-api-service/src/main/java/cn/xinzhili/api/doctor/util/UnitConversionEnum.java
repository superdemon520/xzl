package cn.xinzhili.api.doctor.util;

import java.util.List;

/**
 * 单位转换比例枚举类
 */
public enum UnitConversionEnum {

  BLOOD_SUGAR("GLU", 18D), //血糖
  TOTAL_CHOLESTEROL("TC", 38.67),//总胆固醇
  GLYCEROL_THREE_FAT("TG", 88.545),//甘油三脂
  POTASSIUM("K", 3.91),//钾
  SODIUM("Na", 2.299),//钠
  CALCIUM("Ca", 4.008),//钙
  BLOOD_URIC_ACID("UA", 0.01681),//血尿酸
  CREATININE("CREA", 0.01131),//肌酐
  CREATINE_KINASE("CK", 0.01131),//肌酸激酶
  TOTAL_BILIRUBIN("STB", 0.05847);//总胆红素

  private static final List<String> NEED_FORMATTING_UNIT = List.of("mg/dl");

  UnitConversionEnum(String code, Double proportion) {
    this.code = code;
    this.proportion = proportion;
  }

  protected String code;

  private Double proportion;

  public String getCode() {
    return code;
  }

  public Double getProportion() {
    return proportion;
  }

  public static Double getProportion(String code, String unit) {
    if (unit != null && NEED_FORMATTING_UNIT.contains(unit)) {
      for (UnitConversionEnum unitConversion : UnitConversionEnum.values()) {
        if (unitConversion.getCode().equals(code)) {
          return unitConversion.proportion;
        }
      }
    }
    return 1D;
  }

}
