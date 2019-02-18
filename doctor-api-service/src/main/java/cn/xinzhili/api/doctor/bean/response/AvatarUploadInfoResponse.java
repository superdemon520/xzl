package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.ImageUploadInfo;

/**
 * @author by Loki on 17/6/27.
 */
public class AvatarUploadInfoResponse {

  private ImageUploadInfo uploadInfo;

  public AvatarUploadInfoResponse() {
  }

  public AvatarUploadInfoResponse(ImageUploadInfo uploadInfo) {
    this.uploadInfo = uploadInfo;
  }

  public ImageUploadInfo getUploadInfo() {
    return uploadInfo;
  }

  public void setUploadInfo(ImageUploadInfo uploadInfo) {
    this.uploadInfo = uploadInfo;
  }
}
