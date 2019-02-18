package cn.xinzhili.api.doctor.bean;

import cn.xinzhili.user.api.StaffTitle;
import java.util.Arrays;
import java.util.Objects;

/**
 * Created by @xin.
 */
public class StaffUploadBean {

  private String name;
  private String sex;
  private String tel;
  private String title;

  private StaffUploadBean(Builder builder) {
    setName(builder.name);
    setSex(builder.sex);
    setTel(builder.tel);
    setTitle(builder.title);
  }

  public static Builder builder() {
    return new Builder();
  }

  public boolean isEmpty() {
    return name.isEmpty() && sex.isEmpty() && (tel.isEmpty() || Objects.equals(tel, "0")) && title
        .isEmpty();
  }

  public boolean isValid() {
    boolean hasEmpty = name.isEmpty() || sex.isEmpty() || tel.isEmpty() || title.isEmpty();
    boolean isSexValid = Objects.equals(sex, "男") || Objects.equals(sex, "女");
    boolean isTitleValid = Arrays.stream(StaffTitle.values())
        .anyMatch(t -> Objects.equals(t.getDescription(), title));
    return !hasEmpty && isSexValid && isTitleValid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }

  public String getTel() {
    return tel;
  }

  public void setTel(String tel) {
    this.tel = tel;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }


  /**
   * {@code StaffUploadBean} builder static inner class.
   */
  public static final class Builder {

    private String name;
    private String sex;
    private String tel;
    private String title;

    private Builder() {
    }

    /**
     * Sets the {@code name} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code name} to set
     * @return a reference to this Builder
     */
    public Builder name(String val) {
      name = val;
      return this;
    }

    /**
     * Sets the {@code sex} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code sex} to set
     * @return a reference to this Builder
     */
    public Builder sex(String val) {
      sex = val;
      return this;
    }

    /**
     * Sets the {@code tel} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code tel} to set
     * @return a reference to this Builder
     */
    public Builder tel(String val) {
      tel = val;
      return this;
    }

    /**
     * Sets the {@code title} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param val the {@code title} to set
     * @return a reference to this Builder
     */
    public Builder title(String val) {
      title = val;
      return this;
    }

    /**
     * Returns a {@code StaffUploadBean} built from the parameters previously set.
     *
     * @return a {@code StaffUploadBean} built with parameters of this {@code
     * StaffUploadBean.Builder}
     */
    public StaffUploadBean build() {
      return new StaffUploadBean(this);
    }
  }
}
