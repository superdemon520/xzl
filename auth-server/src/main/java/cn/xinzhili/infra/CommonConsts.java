package cn.xinzhili.infra;

/**
 * Date: 16/7/19 Time: PM12:07
 *
 * @author gan
 */
public interface CommonConsts {

  /**
   * 患者客户端
   */
  String CLIENT_PATIENT = "patient_app";

  /**
   * 医生客户端
   */
  String CLIENT_DOCTOR = "doctor_web";

  String SCOPE_PATIENT = "PATIENT";

  String SCOPE_DOCTOR = "DOCTOR";

  // authorities

  String PATIENT_READ = "PATIENT_READ";

  String PATIENT_WRITE = "PATIENT_WRITE";

  String DOCTOR_READ = "DOCTOR_READ";

  String DOCTOR_WRITE = "DOCTOR_WRITE";

  String DEVICE_TOKEN_KEY = "device_token";

  String DEVICE_TYPE_KEY = "device_type";
}
