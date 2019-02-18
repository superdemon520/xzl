package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.response.AssayReport.Builder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;

/**
 * Created by @xin.
 */
@JsonDeserialize(builder = Builder.class)
public class AssayReport {

  private String name;
  private List<String> link;

  private AssayReport(Builder builder) {
    setName(builder.name);
    setLink(builder.link);
  }

  public static Builder builder() {
    return new Builder();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<String> getLink() {
    return link;
  }

  public void setLink(List<String> link) {
    this.link = link;
  }


  /**
   * {@code AssayReport} builder static inner class.
   */
  @JsonPOJOBuilder(withPrefix = "")
  public static final class Builder {

    private String name;
    private List<String> link;

    private Builder() {
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
     * Sets the {@code link} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code link} to set
     * @return a reference to this Builder
     */
    public Builder link(List<String> val) {
      link = val;
      return this;
    }

    /**
     * Returns a {@code AssayReport} built from the parameters previously set.
     *
     * @return a {@code AssayReport} built with parameters of this {@code AssayReport.Builder}
     */
    public AssayReport build() {
      return new AssayReport(this);
    }
  }
}
