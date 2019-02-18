package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.RecordStatus;
import cn.xinzhili.api.doctor.bean.TakeMedicineStatus;
import java.util.List;
import java.util.Set;

/**
 * @author by Loki on 17/3/7.
 */
public class RecordListApiResponse {

  private List<OneDayRecord> dayRecords;

  public List<OneDayRecord> getDayRecords() {
    return dayRecords;
  }

  public void setDayRecords(
      List<OneDayRecord> dayRecords) {
    this.dayRecords = dayRecords;
  }

  public class OneDayRecord {

    private String day;
    private int takeMedicineStatus;
    private List<OneMedicineRecord> OneMedicineRecords;

    public String getDay() {
      return day;
    }

    public void setDay(String day) {
      this.day = day;
    }

    public int getTakeMedicineStatus() {
      return takeMedicineStatus;
    }

    public void setTakeMedicineStatus(int takeMedicineStatus) {
      this.takeMedicineStatus = takeMedicineStatus;
    }

    public List<OneMedicineRecord> getOneMedicineRecords() {
      return OneMedicineRecords;
    }

    public void setOneMedicineRecords(
        List<OneMedicineRecord> oneMedicineRecords) {
      OneMedicineRecords = oneMedicineRecords;
    }
  }

  public class OneMedicineRecord {

    private String medicineName;
    private String dosageUnit;
    private List<OneDosageRecord> oneDosageRecords;

    public String getMedicineName() {
      return medicineName;
    }

    public void setMedicineName(String medicineName) {
      this.medicineName = medicineName;
    }

    public String getDosageUnit() {
      return dosageUnit;
    }

    public void setDosageUnit(String dosageUnit) {
      this.dosageUnit = dosageUnit;
    }

    public List<OneDosageRecord> getOneDosageRecords() {
      return oneDosageRecords;
    }

    public void setOneDosageRecords(
        List<OneDosageRecord> oneDosageRecords) {
      this.oneDosageRecords = oneDosageRecords;
    }
  }

  public class OneDosageRecord {

    private String dosage;
    private List<TimeStatus> timeStatus;

    public String getDosage() {
      return dosage;
    }

    public void setDosage(String dosage) {
      this.dosage = dosage;
    }

    public List<TimeStatus> getTimeStatus() {
      return timeStatus;
    }

    public void setTimeStatus(
        List<TimeStatus> timeStatus) {
      this.timeStatus = timeStatus;
    }
  }

  public class TimeStatus {

    private String time;
    private Integer status;

    public String getTime() {
      return time;
    }

    public void setTime(String time) {
      this.time = time;
    }

    public Integer getStatus() {
      return status;
    }

    public void setStatus(Integer status) {
      this.status = status;
    }
  }

  public static void setOneDayRecordStatus(Set<Integer> statusSet, OneDayRecord oneDayRecord) {
    if (!statusSet.isEmpty()) {
      if (statusSet.contains(RecordStatus.PATIENT_TAKE.getCode())) {
        if (statusSet.contains(RecordStatus.AUTO_GENERATE.getCode()) || statusSet
            .contains(RecordStatus.NOT_TAKE.getCode())) {
          oneDayRecord.setTakeMedicineStatus(TakeMedicineStatus.TAKE_PART.getCode());
        } else {
          oneDayRecord.setTakeMedicineStatus(TakeMedicineStatus.TAKE_ALL.getCode());
        }
      } else {
        oneDayRecord.setTakeMedicineStatus(TakeMedicineStatus.NOT_TAKE.getCode());
      }
    }
  }

}
