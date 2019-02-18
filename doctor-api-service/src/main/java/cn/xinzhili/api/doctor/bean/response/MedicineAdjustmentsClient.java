package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.UserRole;
import cn.xinzhili.api.doctor.bean.request.MedicineAdjustmentService;
import cn.xinzhili.api.doctor.bean.request.MedicineAdjustmentsService;
import cn.xinzhili.api.doctor.bean.request.MedicineAdjustmentsService.MedicineAdjustmentData;
import cn.xinzhili.xutils.core.util.HashUtils;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by @xin.
 */

@JsonDeserialize(builder = MedicineAdjustmentsClient.Builder.class)
public class MedicineAdjustmentsClient {

  @Valid
  private List<MedicineAdjustment> medicineSupplement;

  private MedicineAdjustmentsClient(Builder builder) {
    medicineSupplement = builder.medicineSupplement;
  }

  public static Builder builder() {
    return new Builder();
  }

  @JsonDeserialize(builder = MedicineAdjustment.Builder.class)
  public static class MedicineAdjustment {

    private UserRole role;
    @Valid
    private List<MedicineAdjustmentClient> medicineAdjustment;
    @NotNull
    private String note;

    public MedicineAdjustment() {
    }

    public MedicineAdjustment(UserRole role,
        List<MedicineAdjustmentClient> medicineAdjustment, String note) {
      this.role = role;
      this.medicineAdjustment = medicineAdjustment;
      this.note = note;
    }

    private MedicineAdjustment(Builder builder) {
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

    public List<MedicineAdjustmentClient> getMedicineAdjustment() {
      return medicineAdjustment;
    }

    public String getNote() {
      return note;
    }


    /**
     * {@code MedicineAdjustment} builder static inner class.
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static final class Builder {

      private UserRole role;
      private List<MedicineAdjustmentClient> medicineAdjustment;
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
      public Builder medicineAdjustment(List<MedicineAdjustmentClient> val) {
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
       * Returns a {@code MedicineAdjustment} built from the parameters previously set.
       *
       * @return a {@code MedicineAdjustment} built with parameters of this {@code
       * MedicineAdjustment.Builder}
       */
      public MedicineAdjustment build() {
        return new MedicineAdjustment(this);
      }
    }
  }

  public MedicineAdjustmentsClient(
      List<MedicineAdjustment> medicineSupplement) {
    this.medicineSupplement = medicineSupplement;
  }

  public MedicineAdjustmentsClient() {
  }


  public List<MedicineAdjustment> getMedicineSupplement() {
    return medicineSupplement;
  }

  public MedicineAdjustmentsService toMedicineAdjustmentsService() {
    List<MedicineAdjustmentData> medicineAdjustmentData =
        Objects.nonNull(medicineSupplement) ? medicineSupplement.stream().map(
            ma -> MedicineAdjustmentData.builder().role(ma.getRole()).note(ma.getNote())
                .medicineAdjustment(ma.getMedicineAdjustment().stream()
                    .map(ad -> MedicineAdjustmentService.builder().id(HashUtils.decode(ad.getId()))
                        .markId(ad.getMarkId()).name(ad.getName()).plan(ad.getPlan())
                        .status(ad.getStatus())
                        .unit(ad.getUnit()).build()).collect(Collectors.toList())).build()
        ).collect(Collectors.toList()) : new ArrayList<>();
    return MedicineAdjustmentsService.builder().medicineSupplement(medicineAdjustmentData).build();
  }

  /**
   * {@code MedicineAdjustmentsClient} builder static inner class.
   */
  @JsonPOJOBuilder(withPrefix = "")
  public static final class Builder {

    private List<MedicineAdjustment> medicineSupplement;

    private Builder() {
    }

    /**
     * Sets the {@code medicineSupplement} and returns a reference to this Builder so that the
     * methods can be chained together.
     *
     * @param val the {@code medicineSupplement} to set
     * @return a reference to this Builder
     */
    public Builder medicineSupplement(List<MedicineAdjustment> val) {
      medicineSupplement = val;
      return this;
    }

    /**
     * Returns a {@code MedicineAdjustmentsClient} built from the parameters previously set.
     *
     * @return a {@code MedicineAdjustmentsClient} built with parameters of this {@code
     * MedicineAdjustmentsClient.Builder}
     */
    public MedicineAdjustmentsClient build() {
      return new MedicineAdjustmentsClient(this);
    }
  }
}