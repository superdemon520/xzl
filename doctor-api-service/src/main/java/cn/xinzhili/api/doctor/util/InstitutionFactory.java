package cn.xinzhili.api.doctor.util;

import cn.xinzhili.api.doctor.bean.DepartmentCountPatientApiInfo;
import cn.xinzhili.api.doctor.bean.DepartmentDetail;
import cn.xinzhili.api.doctor.bean.Institution;
import cn.xinzhili.api.doctor.bean.response.StatisticsPatientsApiResponse;
import cn.xinzhili.user.api.DepartmentInfo;
import cn.xinzhili.user.api.OrganizationInfo;
import cn.xinzhili.user.api.StaffRelationshipInfo;
import cn.xinzhili.user.api.response.OrganizationDetailResponse;
import cn.xinzhili.xutils.core.FailureException;
import cn.xinzhili.xutils.core.util.HashUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Date: 16/02/2017 Time: 10:29 PM
 *
 * @author Gan Dong
 */
public class InstitutionFactory {

  /**
   * 根据user-service 返回的organization info 返回适合API的bean
   *
   * @param organization 数据来源
   * @return 机构信息bean
   */
  public static Institution valueOf(OrganizationInfo organization) {
    if (organization == null) {
      throw new IllegalArgumentException("organization cannot be null");
    }
    Institution institution = new Institution();
    institution.setId(HashUtils.encode(organization.getId()));
    institution.setName(organization.getName());
    return institution;
  }

  public static List<Institution> valueOf(List<OrganizationInfo> organizations) {

    if (organizations == null) {
      throw new IllegalArgumentException("organizations is null !");
    }

    List<Institution> institutions = new ArrayList<>();
    for (OrganizationInfo org : organizations) {
      institutions.add(valueOf(org));
    }
    return institutions;
  }

  public static Institution api(StaffRelationshipInfo relationship) {
    Institution institution = new Institution();
    institution.setId(HashUtils.encode(relationship.getOrganizationId()));
    institution.setName(relationship.getOrganizationName());
    return institution;
  }

  public static StatisticsPatientsApiResponse api(
      OrganizationDetailResponse detailResponse) {
    if (Objects.isNull(detailResponse.getDepartments())
        || detailResponse.getDepartments().size() <= 0) {
      return new StatisticsPatientsApiResponse(List.of());
    }
    List<DepartmentCountPatientApiInfo> departmentCountPatientApiInfos = detailResponse
        .getDepartments().stream()
        .map(InstitutionFactory::api).collect(Collectors.toList());
    return new StatisticsPatientsApiResponse(departmentCountPatientApiInfos);
  }

  public static DepartmentCountPatientApiInfo api(DepartmentInfo departmentInfo) {
    DepartmentCountPatientApiInfo info = new DepartmentCountPatientApiInfo();
    info.setName(departmentInfo.getName());
    info.setPatientCount(departmentInfo.getPatientCount());
    return info;
  }


}
