package cn.xinzhili.api.doctor.bean.response;

import java.util.List;

/**
 * @author by Loki on 17/3/22.
 */
public class ImageTypeResponse {

  private List<TypeInfo> types;

  public class TypeInfo {

    private String key;
    private String name;

    public String getKey() {
      return key;
    }

    public void setKey(String key) {
      this.key = key;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }

  public List<TypeInfo> getTypes() {
    return types;
  }

  public void setTypes(
      List<TypeInfo> types) {
    this.types = types;
  }
}
