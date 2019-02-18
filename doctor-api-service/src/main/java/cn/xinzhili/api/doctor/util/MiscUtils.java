package cn.xinzhili.api.doctor.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by marlinl on 29/03/2017.
 */
public class MiscUtils {

  private static final String NULL_BMI = "暂无";

  public static String bmiCalculate(Integer height, Integer weight) {
    if (height == null || weight == null) {
      return NULL_BMI;
    }

    if (height <= 0 || weight <= 0) {
      return NULL_BMI;
    }

    if (height > 250 || weight > 250) {
      return NULL_BMI;
    }
    int result = weight * 1000000 / (height * height);
    return result / 100 + "." + (result % 100 / 10) + "" + result % 10;
  }

  public static Integer birthday2Age(Long timestamp) {
    if (timestamp == null) {
      return null;
    }
    Period between = Period.between(timestamp2LocalDate(timestamp)
        , timestamp2LocalDate(new Date().getTime()));
    return between.getYears();
  }

  private static LocalDate timestamp2LocalDate(long timestamp) {
    return Instant.ofEpochMilli(timestamp).
        atZone(ZoneId.systemDefault()).toLocalDate();
  }


}
