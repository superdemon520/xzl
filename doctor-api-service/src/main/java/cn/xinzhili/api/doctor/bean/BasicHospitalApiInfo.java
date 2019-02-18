package cn.xinzhili.api.doctor.bean;

/**
 * Created by dkw on 7/12/2017.
 */
public class BasicHospitalApiInfo {

  private String id;
  /**
   * 医院名称
   */
  private String name;
  /**
   * 医院简称
   */
  private String abbreviation;
  /**
   * 省份
   */
  private String province;
  /**
   * 城市
   */
  private String city;
  /**
   * 区县
   */
  private String county;
  /**
   * 乡镇
   */
  private String township;

  public String getAbbreviation() {
    return abbreviation;
  }

  public void setAbbreviation(String abbreviation) {
    this.abbreviation = abbreviation;
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCounty() {
    return county;
  }

  public void setCounty(String county) {
    this.county = county;
  }

  public String getTownship() {
    return township;
  }

  public void setTownship(String township) {
    this.township = township;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
