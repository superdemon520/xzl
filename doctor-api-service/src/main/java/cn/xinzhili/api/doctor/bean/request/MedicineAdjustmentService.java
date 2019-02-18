package cn.xinzhili.api.doctor.bean.request;

import cn.xinzhili.api.doctor.bean.MedicineAdjustmentStatus;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;
import javax.validation.Valid;

/**
 * Created by @xin.
 */
@JsonDeserialize(builder = MedicineAdjustmentService.Builder.class)
public class MedicineAdjustmentService {

  private long id;
  private String name;
  private String markId;
  private String unit;
  private MedicineAdjustmentStatus status;
  @Valid
  private List<MedicationPlan> plan;


  public MedicineAdjustmentService() {
  }

  public MedicineAdjustmentService(long id, String name, String markId, String unit,
      MedicineAdjustmentStatus status,
      List<MedicationPlan> plan) {
    this.id = id;
    this.name = name;
    this.markId = markId;
    this.unit = unit;
    this.status = status;
    this.plan = plan;
  }

  private MedicineAdjustmentService(Builder builder) {
    id = builder.id;
    name = builder.name;
    markId = builder.markId;
    unit = builder.unit;
    status = builder.status;
    plan = builder.plan;
  }

  public static Builder builder() {
    return new Builder();
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getMarkId() {
    return markId;
  }

  public String getUnit() {
    return unit;
  }

  public MedicineAdjustmentStatus getStatus() {
    return status;
  }

  public List<MedicationPlan> getPlan() {
    return plan;
  }


  /**
   * {@code MedicineAdjustmentService} builder static inner class.
   */
  @JsonPOJOBuilder(withPrefix = "")
  public static final class Builder {

    private long id;
    private String name;
    private String markId;
    private String unit;
    private MedicineAdjustmentStatus status;
    private List<MedicationPlan> plan;

    private Builder() {
    }

    /**
     * Sets the {@code id} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code id} to set
     * @return a reference to this Builder
     */
    public Builder id(long val) {
      id = val;
      return this;
    }

    /**
     * Sets the {@code name} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code name} to set
     * @return a reference to this Builder
     */
    public Builder name(String val) {
      name = val;
      return this;
    }

    /**
     * Sets the {@code markId} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code markId} to set
     * @return a reference to this Builder
     */
    public Builder markId(String val) {
      markId = val;
      return this;
    }

    /**
     * Sets the {@code unit} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code unit} to set
     * @return a reference to this Builder
     */
    public Builder unit(String val) {
      unit = val;
      return this;
    }

    /**
     * Sets the {@code status} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code status} to set
     * @return a reference to this Builder
     */
    public Builder status(MedicineAdjustmentStatus val) {
      status = val;
      return this;
    }

    /**
     * Sets the {@code plan} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code plan} to set
     * @return a reference to this Builder
     */
    public Builder plan(List<MedicationPlan> val) {
      plan = val;
      return this;
    }

    /**
     * Returns a {@code MedicineAdjustmentService} built from the parameters previously set.
     *
     * @return a {@code MedicineAdjustmentService} built with parameters of this {@code
     * MedicineAdjustmentService.Builder}
     */
    public MedicineAdjustmentService build() {
      return new MedicineAdjustmentService(this);
    }
  }
}