package cn.xinzhili.api.doctor.bean;

/**
 * 用户状态
 *
 * @author Gan Dong
 */
public enum UserStatus {

  /**
   * 正常状态
   */
  NORMAL,

  /**
   * 未通过首次登录重置密码流程
   */
  PENDING,

  /**
   * 待审核
   */
  TO_REVIEW,

  /**
   * 审核失败
   */
  FAILURE
  ;

}
