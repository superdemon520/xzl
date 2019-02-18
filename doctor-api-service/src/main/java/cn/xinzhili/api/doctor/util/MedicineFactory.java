package cn.xinzhili.api.doctor.util;

import cn.xinzhili.api.doctor.bean.MedicineInfo;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/4/24 下午2:52
 */
public class MedicineFactory {

  private static final Logger logger = LoggerFactory
      .getLogger(MedicineFactory.class);

  public static MedicineInfo map2Medicine(Map<String, Object> map) {
    MedicineInfo medicineInfo = new MedicineInfo();
    try {
      BeanInfo beanInfo = Introspector.getBeanInfo(MedicineInfo.class);
      PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
      if (map != null) {
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
          String key = propertyDescriptor.getName();
          Object value = map.get(key);
          if (key.equals("class")) {
            continue;
          }
          Method writeMethod = propertyDescriptor.getWriteMethod();
          writeMethod.invoke(medicineInfo, value);
        }
      }
    } catch (Exception e) {
      logger.error("map2medicine error", e);
    }
    return medicineInfo;
  }
}
