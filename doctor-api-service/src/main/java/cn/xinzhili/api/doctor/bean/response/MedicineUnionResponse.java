package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.response.MedicineUnionResponse.Builder;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * Created by @xin.
 */
@JsonDeserialize(builder = Builder.class)
public class MedicineUnionResponse {

  private long id;
  private String name;
  private String company;
  private String commodityCode;
  private String commodityName;
  private String dosageForm;
  private String strength;
  private String approvalNo;
  private String pkgStrength;
  private Long updatedAt;
  private String constitute;
  private boolean isPrescription;
  private String saveMode;
  private boolean isEmergency;
  private String category;
  private long price;

  private MedicineUnionResponse(Builder builder) {
    id = builder.id;
    name = builder.name;
    company = builder.company;
    commodityCode = builder.commodityCode;
    commodityName = builder.commodityName;
    dosageForm = builder.dosageForm;
    strength = builder.strength;
    approvalNo = builder.approvalNo;
    pkgStrength = builder.pkgStrength;
    updatedAt = builder.updatedAt;
    constitute = builder.constitute;
    isPrescription = builder.isPrescription;
    saveMode = builder.saveMode;
    isEmergency = builder.isEmergency;
    category = builder.category;
    price = builder.price;
  }

  public static Builder builder() {
    return new Builder();
  }

  public MedicineUnionResponse() {
  }

  public MedicineUnionResponse(long id, String name, String company, String commodityCode,
      String commodityName, String dosageForm, String strength, String approvalNo,
      String pkgStrength, Long updatedAt, String constitute, boolean isPrescription,
      String saveMode, boolean isEmergency, String category, long price) {
    this.id = id;
    this.name = name;
    this.company = company;
    this.commodityCode = commodityCode;
    this.commodityName = commodityName;
    this.dosageForm = dosageForm;
    this.strength = strength;
    this.approvalNo = approvalNo;
    this.pkgStrength = pkgStrength;
    this.updatedAt = updatedAt;
    this.constitute = constitute;
    this.isPrescription = isPrescription;
    this.saveMode = saveMode;
    this.isEmergency = isEmergency;
    this.category = category;
    this.price = price;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getCompany() {
    return company;
  }

  public String getCommodityCode() {
    return commodityCode;
  }

  public String getCommodityName() {
    return commodityName;
  }

  public String getDosageForm() {
    return dosageForm;
  }

  public String getStrength() {
    return strength;
  }

  public String getApprovalNo() {
    return approvalNo;
  }

  public String getPkgStrength() {
    return pkgStrength;
  }

  public Long getUpdatedAt() {
    return updatedAt;
  }

  public String getConstitute() {
    return constitute;
  }

  public boolean isPrescription() {
    return isPrescription;
  }

  public String getSaveMode() {
    return saveMode;
  }

  public boolean isEmergency() {
    return isEmergency;
  }

  public String getCategory() {
    return category;
  }

  public long getPrice() {
    return price;
  }


  /**
   * {@code MedicineUnionResponse} builder static inner class.
   */
  @JsonPOJOBuilder(withPrefix = "")
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static final class Builder {

    private long id;
    private String name;
    private String company;
    private String commodityCode;
    private String commodityName;
    private String dosageForm;
    private String strength;
    private String approvalNo;
    private String pkgStrength;
    private Long updatedAt;
    private String constitute;
    private boolean isPrescription;
    private String saveMode;
    private boolean isEmergency;
    private String category;
    private long price;

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
     * Sets the {@code company} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code company} to set
     * @return a reference to this Builder
     */
    public Builder company(String val) {
      company = val;
      return this;
    }

    /**
     * Sets the {@code commodityCode} and returns a reference to this Builder so that the methods
     * can be chained together.
     *
     * @param val the {@code commodityCode} to set
     * @return a reference to this Builder
     */
    public Builder commodityCode(String val) {
      commodityCode = val;
      return this;
    }

    /**
     * Sets the {@code commodityName} and returns a reference to this Builder so that the methods
     * can be chained together.
     *
     * @param val the {@code commodityName} to set
     * @return a reference to this Builder
     */
    public Builder commodityName(String val) {
      commodityName = val;
      return this;
    }

    /**
     * Sets the {@code dosageForm} and returns a reference to this Builder so that the methods can
     * be chained together.
     *
     * @param val the {@code dosageForm} to set
     * @return a reference to this Builder
     */
    public Builder dosageForm(String val) {
      dosageForm = val;
      return this;
    }

    /**
     * Sets the {@code strength} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code strength} to set
     * @return a reference to this Builder
     */
    public Builder strength(String val) {
      strength = val;
      return this;
    }

    /**
     * Sets the {@code approvalNo} and returns a reference to this Builder so that the methods can
     * be chained together.
     *
     * @param val the {@code approvalNo} to set
     * @return a reference to this Builder
     */
    public Builder approvalNo(String val) {
      approvalNo = val;
      return this;
    }

    /**
     * Sets the {@code pkgStrength} and returns a reference to this Builder so that the methods can
     * be chained together.
     *
     * @param val the {@code pkgStrength} to set
     * @return a reference to this Builder
     */
    public Builder pkgStrength(String val) {
      pkgStrength = val;
      return this;
    }

    /**
     * Sets the {@code updatedAt} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code updatedAt} to set
     * @return a reference to this Builder
     */
    public Builder updatedAt(Long val) {
      updatedAt = val;
      return this;
    }

    /**
     * Sets the {@code constitute} and returns a reference to this Builder so that the methods can
     * be chained together.
     *
     * @param val the {@code constitute} to set
     * @return a reference to this Builder
     */
    public Builder constitute(String val) {
      constitute = val;
      return this;
    }

    /**
     * Sets the {@code isPrescription} and returns a reference to this Builder so that the methods
     * can be chained together.
     *
     * @param val the {@code isPrescription} to set
     * @return a reference to this Builder
     */
    public Builder isPrescription(boolean val) {
      isPrescription = val;
      return this;
    }

    /**
     * Sets the {@code saveMode} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code saveMode} to set
     * @return a reference to this Builder
     */
    public Builder saveMode(String val) {
      saveMode = val;
      return this;
    }

    /**
     * Sets the {@code isEmergency} and returns a reference to this Builder so that the methods can
     * be chained together.
     *
     * @param val the {@code isEmergency} to set
     * @return a reference to this Builder
     */
    public Builder isEmergency(boolean val) {
      isEmergency = val;
      return this;
    }

    /**
     * Sets the {@code category} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code category} to set
     * @return a reference to this Builder
     */
    public Builder category(String val) {
      category = val;
      return this;
    }

    /**
     * Sets the {@code price} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code price} to set
     * @return a reference to this Builder
     */
    public Builder price(long val) {
      price = val;
      return this;
    }

    /**
     * Returns a {@code MedicineUnionResponse} built from the parameters previously set.
     *
     * @return a {@code MedicineUnionResponse} built with parameters of this {@code
     * MedicineUnionResponse.Builder}
     */
    public MedicineUnionResponse build() {
      return new MedicineUnionResponse(this);
    }
  }
}
