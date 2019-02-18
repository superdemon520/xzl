package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.response.AssaySupplement.Builder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;

/**
 * Created by @xin.
 */
@JsonDeserialize(builder = Builder.class)
public class AssaySupplement {

  private List<AssayReport> report;

  private AssaySupplement(Builder builder) {
    setReport(builder.report);
  }

  public static Builder builder() {
    return new Builder();
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static final class AssaySupplementBuilder {

  }

  public List<AssayReport> getReport() {
    return report;
  }

  public void setReport(List<AssayReport> report) {
    this.report = report;
  }


  /**
   * {@code AssaySupplement} builder static inner class.
   */
  @JsonPOJOBuilder(withPrefix = "")
  public static final class Builder {

    private List<AssayReport> report;

    private Builder() {
    }

    /**
     * Sets the {@code report} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code report} to set
     * @return a reference to this Builder
     */
    public Builder report(List<AssayReport> val) {
      report = val;
      return this;
    }

    /**
     * Returns a {@code AssaySupplement} built from the parameters previously set.
     *
     * @return a {@code AssaySupplement} built with parameters of this {@code
     * AssaySupplement.Builder}
     */
    public AssaySupplement build() {
      return new AssaySupplement(this);
    }
  }
}
