package cn.xinzhili.api.doctor.bean;

/**
 * @author by Loki on 17/6/28.
 */
public class ImageUploadInfo {

  private String signUrl;
  private String rawUrl;
  private String imageId;
  private String contentType;
  private String fileName;

  public ImageUploadInfo() {
  }

  public ImageUploadInfo(String signUrl, String rawUrl, String imageId, String contentType,
      String fileName) {
    this.signUrl = signUrl;
    this.rawUrl = rawUrl;
    this.imageId = imageId;
    this.contentType = contentType;
    this.fileName = fileName;
  }

  private ImageUploadInfo(Builder builder) {
    setSignUrl(builder.signUrl);
    setRawUrl(builder.rawUrl);
    setImageId(builder.imageId);
    setContentType(builder.contentType);
    setFileName(builder.fileName);
  }

  public static Builder builder() {
    return new Builder();
  }

  public String getSignUrl() {
    return signUrl;
  }

  public void setSignUrl(String signUrl) {
    this.signUrl = signUrl;
  }

  public String getRawUrl() {
    return rawUrl;
  }

  public void setRawUrl(String rawUrl) {
    this.rawUrl = rawUrl;
  }

  public String getImageId() {
    return imageId;
  }

  public void setImageId(String imageId) {
    this.imageId = imageId;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }


  /**
   * {@code ImageUploadInfo} builder static inner class.
   */
  public static final class Builder {

    private String signUrl;
    private String rawUrl;
    private String imageId;
    private String contentType;
    private String fileName;

    private Builder() {
    }

    /**
     * Sets the {@code signUrl} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code signUrl} to set
     * @return a reference to this Builder
     */
    public Builder signUrl(String val) {
      signUrl = val;
      return this;
    }

    /**
     * Sets the {@code rawUrl} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code rawUrl} to set
     * @return a reference to this Builder
     */
    public Builder rawUrl(String val) {
      rawUrl = val;
      return this;
    }

    /**
     * Sets the {@code imageId} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code imageId} to set
     * @return a reference to this Builder
     */
    public Builder imageId(String val) {
      imageId = val;
      return this;
    }

    /**
     * Sets the {@code contentType} and returns a reference to this Builder so that the methods can
     * be chained together.
     *
     * @param val the {@code contentType} to set
     * @return a reference to this Builder
     */
    public Builder contentType(String val) {
      contentType = val;
      return this;
    }

    /**
     * Sets the {@code fileName} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code fileName} to set
     * @return a reference to this Builder
     */
    public Builder fileName(String val) {
      fileName = val;
      return this;
    }

    /**
     * Returns a {@code ImageUploadInfo} built from the parameters previously set.
     *
     * @return a {@code ImageUploadInfo} built with parameters of this {@code
     * ImageUploadInfo.Builder}
     */
    public ImageUploadInfo build() {
      return new ImageUploadInfo(this);
    }
  }
}
