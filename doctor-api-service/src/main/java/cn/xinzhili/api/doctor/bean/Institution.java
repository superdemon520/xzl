package cn.xinzhili.api.doctor.bean;

/**
 * 机构基本信息（医生助手运营人员都可见的基本信息）
 *
 * @author Gan Dong
 */
public class Institution {

  private String id;

  private String name;

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
