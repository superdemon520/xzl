package cn.xinzhili.api.doctor.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * Created by @xin.
 */
@JsonDeserialize(builder = NotifyContent.Builder.class)
public class NotifyContent {

  private int type;
  private NotifyContentData data;

  private NotifyContent(Builder builder) {
    setType(builder.type);
    setData(builder.data);
  }

  public static Builder builder() {
    return new Builder();
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public NotifyContentData getData() {
    return data;
  }

  public void setData(NotifyContentData data) {
    this.data = data;
  }


  /**
   * {@code NotifyContent} builder static inner class.
   */
  @JsonPOJOBuilder(withPrefix = "")
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static final class Builder {

    private int type;
    private NotifyContentData data;

    private Builder() {
    }

    /**
     * Sets the {@code type} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code type} to set
     * @return a reference to this Builder
     */
    public Builder type(int val) {
      type = val;
      return this;
    }

    /**
     * Sets the {@code data} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code data} to set
     * @return a reference to this Builder
     */
    public Builder data(NotifyContentData val) {
      data = val;
      return this;
    }

    /**
     * Returns a {@code NotifyContent} built from the parameters previously set.
     *
     * @return a {@code NotifyContent} built with parameters of this {@code NotifyContent.Builder}
     */
    public NotifyContent build() {
      return new NotifyContent(this);
    }
  }
}
