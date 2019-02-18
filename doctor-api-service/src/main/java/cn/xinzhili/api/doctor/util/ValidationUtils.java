package cn.xinzhili.api.doctor.util;

import cn.xinzhili.xutils.core.ErrorCode;
import cn.xinzhili.xutils.core.FailureException;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;

/**
 * @author by Loki on 03/02/2017.
 */
public class ValidationUtils {

  //中文,大小写字母和数字,下划线 ,{2，10}字符的长度为2-10
  private static final String namePatternStr = "^[\\u4E00-\\u9FA5\\uF900-\\uFA2D\\w]{2,10}$";
  private static Pattern pattern = Pattern.compile(namePatternStr);


  public static void validatePaginationParams(Integer pageAt,
      Integer pageSize) {

    if (pageSize != null && (pageSize > 100 || pageSize < 2)) {
      throw new FailureException(ErrorCode.INVALID_PARAMS,
          "pageSize can only between 2 and 100");
    }
    if (pageAt != null && pageAt < 0) {
      throw new FailureException(ErrorCode.INVALID_PARAMS,
          "pageAt cannot be negative");
    }
  }

  public static boolean isSameHour(Date date1, Date date2) {
    if (date1 == null || date2 == null) {
      throw new IllegalArgumentException("The date must not be null");
    }
    Calendar cal1 = Calendar.getInstance();
    cal1.setTime(date1);
    Calendar cal2 = Calendar.getInstance();
    cal2.setTime(date2);

    return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
        cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
        cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
        cal1.get(Calendar.HOUR_OF_DAY) == cal2.get(Calendar.HOUR_OF_DAY));
  }

  /**
   * a simple helper to test if a string is empty or not
   *
   * @param s the String
   * @return true if it is <code>null</code>, or empty string
   */
  public static boolean isEmpty(String s) {
    return s == null || s.isEmpty();
  }


  /**
   * check is user's name vaild
   *
   * @param name value
   * @return is valid
   */
  public static boolean isValidName(String name) {

    return !StringUtils.isEmpty(name)
        && pattern.matcher(name).matches();
  }

}
