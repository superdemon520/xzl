package cn.xinzhili.api.doctor.bean;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;

@JsonDeserialize(builder = InspectionStandard.Builder.class)
public class InspectionStandard {

  private String advice;
  private UserRole userRole;
  List<InspectionStandardData> editIns;

  private InspectionStandard(Builder builder) {
    setAdvice(builder.advice);
    setUserRole(builder.userRole);
    setEditIns(builder.editIns);
  }

  public static Builder builder() {
    return new Builder();
  }


  public String getAdvice() {
    return advice;
  }

  public void setAdvice(String advice) {
    this.advice = advice;
  }

  public UserRole getUserRole() {
    return userRole;
  }

  public void setUserRole(UserRole userRole) {
    this.userRole = userRole;
  }

  public List<InspectionStandardData> getEditIns() {
    return editIns;
  }

  public void setEditIns(List<InspectionStandardData> editIns) {
    this.editIns = editIns;
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static final class Builder {

    private String advice;
    private UserRole userRole;
    private List<InspectionStandardData> editIns;

    private Builder() {
    }

    public Builder advice(String val) {
      advice = val;
      return this;
    }

    public Builder userRole(UserRole val) {
      userRole = val;
      return this;
    }

    public Builder editIns(List<InspectionStandardData> val) {
      editIns = val;
      return this;
    }

    public InspectionStandard build() {
      return new InspectionStandard(this);
    }
  }
}
