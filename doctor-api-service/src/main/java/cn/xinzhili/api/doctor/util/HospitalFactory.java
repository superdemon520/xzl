package cn.xinzhili.api.doctor.util;

import cn.xinzhili.api.doctor.bean.Department;
import cn.xinzhili.api.doctor.bean.DepartmentDetail;
import cn.xinzhili.api.doctor.bean.InstitutionDetail;
import cn.xinzhili.api.doctor.bean.response.InstitutionDetailResponse;
import cn.xinzhili.user.api.DepartmentInfo;
import cn.xinzhili.user.api.response.OrganizationDetailResponse;
import cn.xinzhili.xutils.core.util.HashUtils;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by MarlinL on 15/02/2017.
 */
public class HospitalFactory {

  public static Department departmentOf(DepartmentInfo info) {
    Department result = new Department();
    result.setName(info.getName());
    result.setId(HashUtils.encode(info.getId()));
    return result;
  }

  public static InstitutionDetailResponse of(
      OrganizationDetailResponse detailResponse) {
    InstitutionDetail institutionDetail = new InstitutionDetail();
    institutionDetail.setId(HashUtils.encode(detailResponse.getId()));
    institutionDetail.setName(detailResponse.getName());
    institutionDetail.setAddress(detailResponse.getAddress());
    institutionDetail.setIntroduction(detailResponse.getIntroduction());
    institutionDetail.setLogoUrl(detailResponse.getLogoUrl());

    List<DepartmentDetail> departmentDetails = detailResponse.getDepartments().stream()
        .map(HospitalFactory::of).collect(Collectors.toList());
    InstitutionDetailResponse result = new InstitutionDetailResponse();
    result.setInstitution(institutionDetail);
    result.setDepartments(departmentDetails);
    return result;
  }

  public static DepartmentDetail of(DepartmentInfo departmentInfo) {
    DepartmentDetail departmentDetail = new DepartmentDetail();
    departmentDetail.setName(departmentInfo.getName());
    departmentDetail.setId(HashUtils.encode(departmentInfo.getId()));
    departmentDetail.setAssistantCount(departmentInfo.getAssistantCount());
    departmentDetail.setDoctorCount(departmentInfo.getDoctorCount());
    departmentDetail.setPatientCount(departmentInfo.getPatientCount());
    departmentDetail.setOperatorCount(departmentInfo.getOperatorCount());
    return departmentDetail;
  }
}
