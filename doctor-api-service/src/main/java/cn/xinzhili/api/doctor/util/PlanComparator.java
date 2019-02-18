package cn.xinzhili.api.doctor.util;

import cn.xinzhili.mas.api.PlanInfo;
import java.util.Comparator;

/**
 * @author by Loki on 17/7/14.
 */
public class PlanComparator implements Comparator<PlanInfo> {

  @Override
  public int compare(PlanInfo plan1, PlanInfo plan2) {

    String medicineName1 = plan1.getMedicineName();
    String medicineName2 = plan2.getMedicineName();
    Long dosage1 = plan1.getCount() * plan1.getDosage().longValue();
    Long dosage2 = plan2.getCount() * plan2.getDosage().longValue();

    int flag = medicineName1.compareTo(medicineName2);
    if (flag == 0) {
      return dosage1.compareTo(dosage2);
    } else {
      return flag;
    }
  }

}