package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.ImageUploadInfo;

/**
 * Created by ywb on 9/8/2017.
 */
public class CertificateUploadInfoResponse {

  private ImageUploadInfo uploadInfo;

  public CertificateUploadInfoResponse() {
  }

  public CertificateUploadInfoResponse(ImageUploadInfo uploadInfo) {
    this.uploadInfo = uploadInfo;
  }

  public ImageUploadInfo getUploadInfo() {
    return uploadInfo;
  }

  public void setUploadInfo(ImageUploadInfo uploadInfo) {
    this.uploadInfo = uploadInfo;
  }
}
