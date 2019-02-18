package cn.xinzhili.api.doctor.bean;

/**
 * 机构的详细信息，包括管理员可见的属性
 *
 * @author Gan Dong
 */
public class InstitutionDetail extends Institution {

  private String logoUrl;

  private String introduction;

  private String address;

  public String getLogoUrl() {
    return logoUrl;
  }

  public void setLogoUrl(String logoUrl) {
    this.logoUrl = logoUrl;
  }

  public String getIntroduction() {
    return introduction;
  }

  public void setIntroduction(String introduction) {
    this.introduction = introduction;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }
}
