package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.ImageApiInfo;
import java.util.List;

/**
 * @author by Loki on 17/3/10.
 */
public class ImageApiResponse {

  private List<ImageApiInfo> images;
  private Integer total;

  public ImageApiResponse() {
  }

  public ImageApiResponse(List<ImageApiInfo> images, Integer total) {
    this.images = images;
    this.total = total;
  }

  private ImageApiResponse(Builder builder) {
    setImages(builder.images);
    setTotal(builder.total);
  }

  public static Builder builder() {
    return new Builder();
  }

  public List<ImageApiInfo> getImages() {
    return images;
  }

  public void setImages(List<ImageApiInfo> images) {
    this.images = images;
  }

  public Integer getTotal() {
    return total;
  }

  public void setTotal(Integer total) {
    this.total = total;
  }


  /**
   * {@code ImageApiResponse} builder static inner class.
   */
  public static final class Builder {

    private List<ImageApiInfo> images;
    private Integer total;

    private Builder() {
    }

    /**
     * Sets the {@code images} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code images} to set
     * @return a reference to this Builder
     */
    public Builder images(List<ImageApiInfo> val) {
      images = val;
      return this;
    }

    /**
     * Sets the {@code total} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code total} to set
     * @return a reference to this Builder
     */
    public Builder total(Integer val) {
      total = val;
      return this;
    }

    /**
     * Returns a {@code ImageApiResponse} built from the parameters previously set.
     *
     * @return a {@code ImageApiResponse} built with parameters of this {@code
     * ImageApiResponse.Builder}
     */
    public ImageApiResponse build() {
      return new ImageApiResponse(this);
    }
  }
}
