package cn.xinzhili.api.doctor.bean;

import cn.xinzhili.api.doctor.bean.NotifyContentData.Builder;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * Created by @xin.
 */
@JsonDeserialize(builder = Builder.class)
public class NotifyContentData {

  private int type;
  private String content;
  private long createdAt;

  private NotifyContentData(Builder builder) {
    setType(builder.type);
    setContent(builder.content);
    setCreatedAt(builder.createdAt);
  }

  public static Builder builder() {
    return new Builder();
  }

  public NotifyContentData() {
  }

  public NotifyContentData(int type, String content, long createdAt) {
    this.type = type;
    this.content = content;
    this.createdAt = createdAt;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public long getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(long createdAt) {
    this.createdAt = createdAt;
  }


  /**
   * {@code NotifyContentData} builder static inner class.
   */
  @JsonPOJOBuilder(withPrefix = "")
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static final class Builder {

    private int type;
    private String content;
    private long createdAt;

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
     * Sets the {@code content} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code content} to set
     * @return a reference to this Builder
     */
    public Builder content(String val) {
      content = val;
      return this;
    }

    /**
     * Sets the {@code createdAt} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code createdAt} to set
     * @return a reference to this Builder
     */
    public Builder createdAt(long val) {
      createdAt = val;
      return this;
    }

    /**
     * Returns a {@code NotifyContentData} built from the parameters previously set.
     *
     * @return a {@code NotifyContentData} built with parameters of this {@code
     * NotifyContentData.Builder}
     */
    public NotifyContentData build() {
      return new NotifyContentData(this);
    }
  }
}
