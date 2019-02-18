package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.NotifyContent;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;

/**
 * Created by @xin.
 */
@JsonDeserialize(builder = NotifyResponse.Builder.class)
public class NotifyResponse {

  private String title;
  private String alert;
  private List<String> receivers;
  private NotifyContent content;

  private NotifyResponse(Builder builder) {
    setTitle(builder.title);
    setAlert(builder.alert);
    setReceivers(builder.receivers);
    setContent(builder.content);
  }

  public static Builder builder() {
    return new Builder();
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAlert() {
    return alert;
  }

  public void setAlert(String alert) {
    this.alert = alert;
  }

  public List<String> getReceivers() {
    return receivers;
  }

  public void setReceivers(List<String> receivers) {
    this.receivers = receivers;
  }

  public NotifyContent getContent() {
    return content;
  }

  public void setContent(NotifyContent content) {
    this.content = content;
  }


  /**
   * {@code NotifyResponse} builder static inner class.
   */
  @JsonPOJOBuilder(withPrefix = "")
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static final class Builder {

    private String title;
    private String alert;
    private List<String> receivers;
    private NotifyContent content;

    private Builder() {
    }

    /**
     * Sets the {@code title} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code title} to set
     * @return a reference to this Builder
     */
    public Builder title(String val) {
      title = val;
      return this;
    }

    /**
     * Sets the {@code alert} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code alert} to set
     * @return a reference to this Builder
     */
    public Builder alert(String val) {
      alert = val;
      return this;
    }

    /**
     * Sets the {@code receivers} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code receivers} to set
     * @return a reference to this Builder
     */
    public Builder receivers(List<String> val) {
      receivers = val;
      return this;
    }

    /**
     * Sets the {@code content} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code content} to set
     * @return a reference to this Builder
     */
    public Builder content(NotifyContent val) {
      content = val;
      return this;
    }

    /**
     * Returns a {@code NotifyResponse} built from the parameters previously set.
     *
     * @return a {@code NotifyResponse} built with parameters of this {@code NotifyResponse.Builder}
     */
    public NotifyResponse build() {
      return new NotifyResponse(this);
    }
  }
}
