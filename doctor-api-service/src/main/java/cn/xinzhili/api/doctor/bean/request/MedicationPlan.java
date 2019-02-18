package cn.xinzhili.api.doctor.bean.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;
import javax.validation.constraints.Pattern;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Created by @xin.
 */
@JsonDeserialize(builder = MedicationPlan.Builder.class)
public class MedicationPlan {

  private List<Integer> originTakeTime;
  private List<Integer> takeTime;
  @Pattern(regexp = "^[0-9]+(.[0-9]{1,2})?$", message = "原剂量数值不合法")
  private String originDosage;
  @Pattern(regexp = "^[0-9]+(.[0-9]{1,2})?$", message = "剂量数值不合法")
  private String dosage;
  private String id;
  private Status status;
  @JsonIgnore
  private Long medicineId = 0L;

  private MedicationPlan(Builder builder) {
    originTakeTime = builder.originTakeTime;
    takeTime = builder.takeTime;
    originDosage = builder.originDosage;
    dosage = builder.dosage;
    id = builder.id;
    status = builder.status;
    medicineId = builder.medicineId;
  }

  public static Builder builder() {
    return new Builder();
  }


  public enum Status {
    ADD,
    EDIT,
    DELETE,
    NONE
  }

  public MedicationPlan(List<Integer> originTakeTime, List<Integer> takeTime,
      String originDosage, String dosage, String id,
      Status status, Long medicineId) {
    this.originTakeTime = originTakeTime;
    this.takeTime = takeTime;
    this.originDosage = originDosage;
    this.dosage = dosage;
    this.id = id;
    this.status = status;
    this.medicineId = medicineId;
  }

  public List<Integer> getOriginTakeTime() {
    return originTakeTime;
  }

  public List<Integer> getTakeTime() {
    return takeTime;
  }

  public String getOriginDosage() {
    return originDosage;
  }

  public String getDosage() {
    return dosage;
  }

  public String getId() {
    return id;
  }

  public Status getStatus() {
    return status;
  }

  public Long getMedicineId() {
    return medicineId;
  }


  /**
   * {@code MedicationPlan} builder static inner class.
   */
  @JsonPOJOBuilder(withPrefix = "")
  public static final class Builder {

    private List<Integer> originTakeTime;
    private List<Integer> takeTime;
    private String originDosage;
    private String dosage;
    private String id;
    private Status status;
    private Long medicineId;

    private Builder() {
    }

    /**
     * Sets the {@code originTakeTime} and returns a reference to this Builder so that the methods
     * can be chained together.
     *
     * @param val the {@code originTakeTime} to set
     * @return a reference to this Builder
     */
    public Builder originTakeTime(List<Integer> val) {
      originTakeTime = val;
      return this;
    }

    /**
     * Sets the {@code takeTime} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code takeTime} to set
     * @return a reference to this Builder
     */
    public Builder takeTime(List<Integer> val) {
      takeTime = val;
      return this;
    }

    /**
     * Sets the {@code originDosage} and returns a reference to this Builder so that the methods can
     * be chained together.
     *
     * @param val the {@code originDosage} to set
     * @return a reference to this Builder
     */
    public Builder originDosage(String val) {
      originDosage = val;
      return this;
    }

    /**
     * Sets the {@code dosage} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code dosage} to set
     * @return a reference to this Builder
     */
    public Builder dosage(String val) {
      dosage = val;
      return this;
    }

    /**
     * Sets the {@code id} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code id} to set
     * @return a reference to this Builder
     */
    public Builder id(String val) {
      id = val;
      return this;
    }

    /**
     * Sets the {@code status} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code status} to set
     * @return a reference to this Builder
     */
    public Builder status(Status val) {
      status = val;
      return this;
    }

    /**
     * Sets the {@code medicineId} and returns a reference to this Builder so that the methods can
     * be chained together.
     *
     * @param val the {@code medicineId} to set
     * @return a reference to this Builder
     */
    public Builder medicineId(Long val) {
      medicineId = val;
      return this;
    }

    /**
     * Returns a {@code MedicationPlan} built from the parameters previously set.
     *
     * @return a {@code MedicationPlan} built with parameters of this {@code MedicationPlan.Builder}
     */
    public MedicationPlan build() {
      return new MedicationPlan(this);
    }
  }
}
