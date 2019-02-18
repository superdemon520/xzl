package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.ImageApiInfo;
import cn.xinzhili.api.doctor.bean.ImageApiType;
import cn.xinzhili.medical.api.ImageType;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author by Loki on 17/3/17.
 */
class BaseMedicalDataApiResponse {

  private ImageApiInfo imageInfo;
  private List<ImageApiType> allType;

  public ImageApiInfo getImageInfo() {
    return imageInfo;
  }

  public void setImageInfo(ImageApiInfo imageInfo) {
    this.imageInfo = imageInfo;
  }

  public List<ImageApiType> getAllType() {
    return allType;
  }

  public void setAllType(List<ImageApiType> allType) {
    this.allType = allType;
  }

  public List<ImageApiType> resolver(List<ImageType> source) {
    if (Objects.isNull(source) || source.isEmpty()) {
      return List.of();
    }
    return source.stream().map(t -> ImageApiType.valueOf(t.name()))
        .collect(Collectors.toList());
  }
}
