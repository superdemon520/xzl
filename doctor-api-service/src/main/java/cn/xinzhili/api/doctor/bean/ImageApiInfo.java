package cn.xinzhili.api.doctor.bean;

import java.util.Date;

/**
 * @author by Loki on 17/3/10.
 */
public class ImageApiInfo {

  private String id;
  private ImageCategory category;//大类
  private ImageApiType type;
  private String typeName;
  private ImageApiStatus status;
  private Date reportAt;
  private BasicHospitalApiInfo hospital;
  private Integer count;
  private String url;
  private String thumbUrl;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public ImageCategory getCategory() {
    return category;
  }

  public void setCategory(ImageCategory category) {
    this.category = category;
  }

  public ImageApiType getType() {
    return type;
  }

  public void setType(ImageApiType type) {
    this.type = type;
  }

  public String getTypeName() {
    return typeName;
  }

  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  public ImageApiStatus getStatus() {
    return status;
  }

  public void setStatus(ImageApiStatus status) {
    this.status = status;
  }

  public Date getReportAt() {
    return reportAt;
  }

  public void setReportAt(Date reportAt) {
    this.reportAt = reportAt;
  }

  public BasicHospitalApiInfo getHospital() {
    return hospital;
  }

  public void setHospital(BasicHospitalApiInfo hospital) {
    this.hospital = hospital;
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getThumbUrl() {
    return thumbUrl;
  }

  public void setThumbUrl(String thumbUrl) {
    this.thumbUrl = thumbUrl;
  }
}
