package cn.xinzhili.api.doctor.bean.request;

import cn.xinzhili.api.doctor.bean.ImageApiType;
import cn.xinzhili.user.api.util.StringUtils;
import java.util.List;
import java.util.Objects;

public class DeleteImageTypeApiRequest {

  private String imageId;
  private List<ImageApiType> type;

  public String getImageId() {
    return imageId;
  }

  public void setImageId(String imageId) {
    this.imageId = imageId;
  }

  public List<ImageApiType> getType() {
    return type;
  }

  public void setType(List<ImageApiType> type) {
    this.type = type;
  }

  public boolean invalid() {
    return Objects.isNull(getType()) || StringUtils.isEmpty(getImageId());
  }
}
