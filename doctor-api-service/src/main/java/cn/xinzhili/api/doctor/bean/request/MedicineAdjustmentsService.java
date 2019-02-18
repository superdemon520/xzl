package cn.xinzhili.api.doctor.bean.request;

import cn.xinzhili.api.doctor.bean.UserRole;
import cn.xinzhili.api.doctor.bean.request.MedicineAdjustmentsService.Builder;
import cn.xinzhili.api.doctor.bean.response.MedicineAdjustmentClient;
import cn.xinzhili.api.doctor.bean.response.MedicineAdjustmentsClient;
import cn.xinzhili.api.doctor.bean.response.MedicineAdjustmentsClient.MedicineAdjustment;
import cn.xinzhili.xutils.core.util.HashUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

@JsonDeserialize(builder = Builder.class)
public class MedicineAdjustmentsService {

  private List<MedicineAdjustmentData> medicineSupplement;

  private MedicineAdjustmentsService(Builder builder) {
    medicineSupplement = builder.medicineSupplement;
  }

  public static Builder builder() {
    return new Builder();
  }

  @JsonDeserialize(builder = MedicineAdjustmentData.Builder.class)
  public static class MedicineAdjustmentData {

    private UserRole role;
    @Valid
    private List<MedicineAdjustmentService> medicineAdjustment;
    private String note;

    public MedicineAdjustmentData(UserRole role,
        List<MedicineAdjustmentService> medicineAdjustment, String note) {
      this.role = role;
      this.medicineAdjustment = medicineAdjustment;
      this.note = note;
    }

    public MedicineAdjustmentData() {
    }

    private MedicineAdjustmentData(Builder builder) {
      role = builder.role;
      medicineAdjustment = builder.medicineAdjustment;
      note = builder.note;
    }

    public static Builder builder() {
      return new Builder();
    }

    public UserRole getRole() {
      return role;
    }

    public List<MedicineAdjustmentService> getMedicineAdjustment() {
      return medicineAdjustment;
    }

    public String getNote() {
      return note;
    }


    /**
     * {@code MedicineAdjustmentData} builder static inner class.
     */
    @JsonPOJOBuilder(withPrefix = "")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Builder {

      private UserRole role;
      private List<MedicineAdjustmentService> medicineAdjustment;
      private String note;

      private Builder() {
      }

      /**
       * Sets the {@code role} and returns a reference to this Builder so that the methods can be
       * chained together.
       *
       * @param val the {@code role} to set
       * @return a reference to this Builder
       */
      public Builder role(UserRole val) {
        role = val;
        return this;
      }

      /**
       * Sets the {@code medicineAdjustment} and returns a reference to this Builder so that the
       * methods can be chained together.
       *
       * @param val the {@code medicineAdjustment} to set
       * @return a reference to this Builder
       */
      public Builder medicineAdjustment(List<MedicineAdjustmentService> val) {
        medicineAdjustment = val;
        return this;
      }

      /**
       * Sets the {@code note} and returns a reference to this Builder so that the methods can be
       * chained together.
       *
       * @param val the {@code note} to set
       * @return a reference to this Builder
       */
      public Builder note(String val) {
        note = val;
        return this;
      }

      /**
       * Returns a {@code MedicineAdjustmentData} built from the parameters previously set.
       *
       * @return a {@code MedicineAdjustmentData} built with parameters of this {@code
       * MedicineAdjustmentData.Builder}
       */
      public MedicineAdjustmentData build() {
        return new MedicineAdjustmentData(this);
      }
    }
  }

  public MedicineAdjustmentsClient toMedicineAdjustmentsClient() {
    return MedicineAdjustmentsClient.builder()
        .medicineSupplement(this.medicineSupplement.stream().map(
            ma -> MedicineAdjustment.builder().role(ma.getRole()).note(ma.getNote())
                .medicineAdjustment(ma.getMedicineAdjustment().stream()
                    .map(ad -> MedicineAdjustmentClient.builder().id(HashUtils.encode(ad.getId()))
                        .markId(ad.getMarkId()).name(ad.getName()).plan(ad.getPlan())
                        .status(ad.getStatus())
                        .unit(ad.getUnit()).build()).collect(Collectors.toList())).build()
        ).collect(Collectors.toList())).build();
  }

  public MedicineAdjustmentsService(
      List<MedicineAdjustmentData> medicineSupplement) {
    this.medicineSupplement = medicineSupplement;
  }

  public MedicineAdjustmentsService() {
  }

  public List<MedicineAdjustmentData> getMedicineSupplement() {
    return medicineSupplement;
  }


  /**
   * {@code MedicineAdjustmentsService} builder static inner class.
   */
  @JsonPOJOBuilder(withPrefix = "")
  public static final class Builder {

    private List<MedicineAdjustmentData> medicineSupplement;

    private Builder() {
    }

    /**
     * Sets the {@code medicineSupplement} and returns a reference to this Builder so that the
     * methods can be chained together.
     *
     * @param val the {@code medicineSupplement} to set
     * @return a reference to this Builder
     */
    public Builder medicineSupplement(List<MedicineAdjustmentData> val) {
      medicineSupplement = val;
      return this;
    }

    /**
     * Returns a {@code MedicineAdjustmentsService} built from the parameters previously set.
     *
     * @return a {@code MedicineAdjustmentsService} built with parameters of this {@code
     * MedicineAdjustmentsService.Builder}
     */
    public MedicineAdjustmentsService build() {
      return new MedicineAdjustmentsService(this);
    }
  }
}