package cn.xinzhili.chat.util;

import cn.xinzhili.chat.api.RoleType;
import cn.xinzhili.chat.api.Type;
import cn.xinzhili.chat.bean.PatientDetailBean;
import cn.xinzhili.chat.bean.UserBean;
import cn.xinzhili.user.api.PatientInfo;
import cn.xinzhili.user.api.StaffInfo;
import cn.xinzhili.user.api.response.PatientDetailResponse;
import cn.xinzhili.user.api.response.PatientRelationResponse;
import cn.xinzhili.user.api.response.StaffDetailResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/5/10 下午1:58
 */
public class UserFactory {

  public static UserBean getNameAndAvatar(RoleType roleType, PatientDetailResponse response) {
    UserBean user = new UserBean();
    switch (roleType) {
      case DOCTOR:
        StaffInfo boundDoctor = response.getBoundDoctor();
        user.setName(boundDoctor.getName());
        user.setAvatar(boundDoctor.getAvatar());
        break;
      case ASSISTANT:
        StaffInfo boundAssistant = response.getBoundAssistant();
        user.setName(boundAssistant.getName());
        user.setAvatar(boundAssistant.getAvatar());
        break;
      case OPERATOR:
        StaffInfo boundOperator = response.getBoundOperator();
        user.setName(boundOperator.getName());
        user.setAvatar(boundOperator.getAvatar());
        break;
      case PATIENT:
        PatientInfo patient = response.getPatient();
        user.setName(patient.getName());
        user.setAvatar(patient.getAvatar());
        break;
    }
    return user;
  }


  public static List<PatientDetailBean> getDetail(PatientRelationResponse response, Type type) {
    List<PatientDetailBean> patientDetailBeans = new ArrayList<>();
    if (type.equals(Type.GROUP_ALL)) {
      patientDetailBeans.add(new PatientDetailBean(response.getPatientId(), RoleType.PATIENT));
    }
    patientDetailBeans.add(new PatientDetailBean(response.getOperatorId(), RoleType.OPERATOR));
    if (response.getAssistantId() != null) {
      patientDetailBeans.add(new PatientDetailBean(response.getAssistantId(), RoleType.ASSISTANT));
    }
    patientDetailBeans.add(new PatientDetailBean(response.getDoctorId(), RoleType.DOCTOR));
    return patientDetailBeans;
  }

  public static UserBean of(StaffDetailResponse response) {
    UserBean userBean = new UserBean();
    userBean.setName(response.getUser().getName());
    userBean.setAvatar(response.getUser().getAvatar());
    return userBean;
  }

  public static UserBean of(PatientDetailResponse response) {
    UserBean userBean = new UserBean();
    userBean.setName(response.getPatient().getName());
    userBean.setAvatar(response.getPatient().getAvatar());
    return userBean;
  }
}
