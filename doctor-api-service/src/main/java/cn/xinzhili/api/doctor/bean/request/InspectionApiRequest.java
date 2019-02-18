package cn.xinzhili.api.doctor.bean.request;

import cn.xinzhili.api.doctor.bean.ImageApiType;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.apache.commons.lang.StringUtils;

/**
 * @author by Loki on 17/4/17.
 */
public class InspectionApiRequest extends BaseMedicalDataApiRequest {

  private ImageApiType imageType;
  @Valid
  private List<ApiInspection> inspections;

  public static class ApiInspection {

    private String referenceId;
    @Max(10000)
    @Min(-10)
    private Double value;
    /**
     * 单位
     */
    private String unit;
    /**
     * 缩写
     */
    private String abbreviation;

    public String getAbbreviation() {
      return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
      this.abbreviation = abbreviation;
    }

    public String getUnit() {
      return unit;
    }

    public void setUnit(String unit) {
      this.unit = unit;
    }

    public String getReferenceId() {
      return referenceId;
    }

    public void setReferenceId(String referenceId) {
      this.referenceId = referenceId;
    }

    public Double getValue() {
      return value;
    }

    public void setValue(Double value) {
      this.value = value;
    }
  }

  public boolean apiInspectionInvalid() {
    return StringUtils.isEmpty(super.getImageId())
        || StringUtils.isEmpty(super.getPatientId()) || imageType == null || inspections == null;
  }

  public ImageApiType getImageType() {
    return imageType;
  }

  public void setImageType(ImageApiType imageType) {
    this.imageType = imageType;
  }

  public List<ApiInspection> getInspections() {
    return inspections;
  }

  public void setInspections(
      List<ApiInspection> inspections) {
    this.inspections = inspections;
  }


}
