package cn.xinzhili.api.doctor.bean.response;

import java.util.List;

/**
 * @author by Loki on 17/3/7.
 */
public class PlanResponse {

  private List<OnePlan> plans;

  public List<OnePlan> getPlans() {
    return plans;
  }

  public void setPlans(List<OnePlan> plans) {
    this.plans = plans;
  }

  public static class OnePlan {

    private String markId;
    private String id;
    private String name;
    private String commodityName;
    private String strength;
    private String unit;
    private List<PlanInfo> plan;

    public OnePlan() {
    }

    public OnePlan(String markId, String id, String name, String commodityName,
        String strength, String unit,
        List<PlanInfo> plan) {
      this.markId = markId;
      this.id = id;
      this.name = name;
      this.commodityName = commodityName;
      this.strength = strength;
      this.unit = unit;
      this.plan = plan;
    }

    public String getMarkId() {
      return markId;
    }

    public void setMarkId(String markId) {
      this.markId = markId;
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getUnit() {
      return unit;
    }

    public void setUnit(String unit) {
      this.unit = unit;
    }

    public List<PlanInfo> getPlan() {
      return plan;
    }

    public void setPlan(List<PlanInfo> plan) {
      this.plan = plan;
    }

    public String getCommodityName() {
      return commodityName;
    }

    public void setCommodityName(String commodityName) {
      this.commodityName = commodityName;
    }

    public String getStrength() {
      return strength;
    }

    public void setStrength(String strength) {
      this.strength = strength;
    }
  }

  public class PlanInfo {

    private String id;
    private String dosage;
    private List<Integer> takeTime;

    public PlanInfo() {
    }

    public PlanInfo(String id, String dosage, List<Integer> takeTime) {
      this.id = id;
      this.dosage = dosage;
      this.takeTime = takeTime;
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getDosage() {
      return dosage;
    }

    public void setDosage(String dosage) {
      this.dosage = dosage;
    }

    public List<Integer> getTakeTime() {
      return takeTime;
    }

    public void setTakeTime(List<Integer> takeTime) {
      this.takeTime = takeTime;
    }
  }

}
