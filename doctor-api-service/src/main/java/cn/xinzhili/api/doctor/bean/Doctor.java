package cn.xinzhili.api.doctor.bean;


/**
 * @author by Loki on 17/2/22.
 */
public class Doctor extends User {

  private String id;
  private DoctorTitle title;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public DoctorTitle getTitle() {
    return title;
  }

  public void setTitle(DoctorTitle title) {
    this.title = title;
  }
}
