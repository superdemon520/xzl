package cn.xinzhili.api.doctor.bean.request;

import cn.xinzhili.api.doctor.bean.ImageApiType;

/**
 * @author by Loki on 17/3/22.
 */
public class ImageReviewApiRequest
    extends BaseMedicalDataApiRequest {

  private ImageApiType type;

  public boolean reviewInfoInvalid() {
    return super.getImageId() == null || type == null;
  }

  public ImageApiType getType() {
    return type;
  }

  public void setType(ImageApiType type) {
    this.type = type;
  }
}
