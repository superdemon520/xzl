package cn.xinzhili.api.doctor.bean.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

/**
 * Created by @xin.
 */
@JsonDeserialize(builder = ConsumeMessageRequest.Builder.class)
public class ConsumeMessageRequest {

  private List<String> senders;

  private List<String> assistants;

  private ConsumeMessageRequest(Builder builder) {
    setSenders(builder.senders);
    setAssistants(builder.assistants);
  }

  public static Builder builder() {
    return new Builder();
  }

  public List<String> getSenders() {
    return senders;
  }

  public void setSenders(List<String> senders) {
    this.senders = senders;
  }

  public List<String> getAssistants() {
    return assistants;
  }

  public void setAssistants(List<String> assistants) {
    this.assistants = assistants;
  }


  /**
   * {@code ConsumeMessageRequest} builder static inner class.
   */
  public static final class Builder {

    private List<String> senders;
    private List<String> assistants;

    private Builder() {
    }

    /**
     * Sets the {@code senders} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code senders} to set
     * @return a reference to this Builder
     */
    public Builder senders(List<String> val) {
      senders = val;
      return this;
    }

    /**
     * Sets the {@code assistants} and returns a reference to this Builder so that the methods can
     * be chained together.
     *
     * @param val the {@code assistants} to set
     * @return a reference to this Builder
     */
    public Builder assistants(List<String> val) {
      assistants = val;
      return this;
    }

    /**
     * Returns a {@code ConsumeMessageRequest} built from the parameters previously set.
     *
     * @return a {@code ConsumeMessageRequest} built with parameters of this {@code
     * ConsumeMessageRequest.Builder}
     */
    public ConsumeMessageRequest build() {
      return new ConsumeMessageRequest(this);
    }
  }
}
