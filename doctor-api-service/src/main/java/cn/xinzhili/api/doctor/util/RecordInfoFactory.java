package cn.xinzhili.api.doctor.util;

import cn.xinzhili.mas.api.RecordInfo;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class RecordInfoFactory {

  public static int getDay(RecordInfo recordInfo) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(recordInfo.getTakeTime());
    return cal.get(Calendar.DAY_OF_YEAR);
  }

  public static String getFormatedTime(LocalDateTime localDateTime) {
    return localDateTime.format(DateTimeFormatter.ofPattern("YYYY/MM/dd"));
  }

}
