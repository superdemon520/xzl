package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.api.doctor.bean.DepartmentDetail;
import cn.xinzhili.api.doctor.bean.InstitutionDetail;
import java.util.List;

/**
 * 机构详情，包括其所包含的部门 Created by MarlinL on 15/02/2017.
 */
public class InstitutionDetailResponse {

  private InstitutionDetail institution;

  private List<DepartmentDetail> departments;

  public InstitutionDetail getInstitution() {
    return institution;
  }

  public void setInstitution(InstitutionDetail institution) {
    this.institution = institution;
  }

  public List<DepartmentDetail> getDepartments() {
    return departments;
  }

  public void setDepartments(List<DepartmentDetail> departments) {
    this.departments = departments;
  }
}
