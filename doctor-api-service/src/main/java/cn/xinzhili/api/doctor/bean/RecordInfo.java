package cn.xinzhili.api.doctor.bean;

import cn.xinzhili.xutils.core.ErrorCode;
import cn.xinzhili.xutils.core.FailureException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecordInfo {

  private Long id;
  private Long userId;
  private Long medicineId;
  private Long planId;
  private String medicineName;
  private Date takeTime;
  private Integer count;
  private Integer status;
  private Integer source;
  private Date confirmedAt;
  private Integer dosage;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Long getMedicineId() {
    return medicineId;
  }

  public void setMedicineId(Long medicineId) {
    this.medicineId = medicineId;
  }

  public Long getPlanId() {
    return planId;
  }

  public void setPlanId(Long planId) {
    this.planId = planId;
  }

  public String getMedicineName() {
    return medicineName;
  }

  public void setMedicineName(String medicineName) {
    this.medicineName = medicineName;
  }

  public Date getTakeTime() {
    return takeTime;
  }

  public void setTakeTime(Date takeTime) {
    this.takeTime = takeTime;
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Integer getSource() {
    return source;
  }

  public void setSource(Integer source) {
    this.source = source;
  }

  public Date getConfirmedAt() {
    return confirmedAt;
  }

  public void setConfirmedAt(Date confirmedAt) {
    this.confirmedAt = confirmedAt;
  }

  public Integer getDosage() {
    return dosage;
  }

  public void setDosage(Integer dosage) {
    this.dosage = dosage;
  }

  public String getDayNum() {
    return new SimpleDateFormat("YYYY/MM/dd").format(this.getTakeTime());
  }

  public static void validateRecord(RecordInfo recordInfo) {
    if (recordInfo.getTakeTime() == null
        || recordInfo.getMedicineName() == null
        || recordInfo.getDosage() == null
        || recordInfo.getStatus() == null) {
      throw new FailureException(ErrorCode.REQUEST_FAILED, "服药纪录数据有误！");
    }
  }

  public String getFinalDosage() {
    BigDecimal unit = new BigDecimal(1000);
    BigDecimal bigCount = new BigDecimal(count);
    BigDecimal bigDosage = new BigDecimal(dosage);
    return bigCount.divide(unit).multiply(bigDosage).divide(unit).toString();

  }
}
