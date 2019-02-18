package cn.xinzhili.api.doctor.util;

import cn.xinzhili.api.doctor.bean.Department;
import cn.xinzhili.api.doctor.bean.DepartmentDetail;
import cn.xinzhili.user.api.DepartmentInfo;
import cn.xinzhili.xutils.core.util.HashUtils;

/**
 * Date: 07/03/2017
 * Time: 8:00 PM
 *
 * @author Gan Dong
 */
public class DepartmentFactory {

  /**
   * 根据user-service 返回的department info 返回适合API的bean
   *
   * @param departmentInfo 数据来源
   * @return 部门信息bean
   */
  public static Department valueOf(DepartmentInfo departmentInfo) {
    if (departmentInfo == null) {
      throw new IllegalArgumentException("department cannot be null");
    }
    Department department;
    if (departmentInfo.getAssistantCount() != null) {
      department = new DepartmentDetail();
      ((DepartmentDetail) department)
          .setAssistantCount(departmentInfo.getAssistantCount());
    } else {
      department = new Department();
    }
    department.setName(departmentInfo.getName());
    department.setId(HashUtils.encode(departmentInfo.getId()));
    return department;
  }

}
