package cn.xinzhili.api.doctor.bean;

import cn.xinzhili.user.api.AdviceLevel;
import cn.xinzhili.user.api.ConsultationLevel;
import cn.xinzhili.user.api.MedicationStatus;
import cn.xinzhili.user.api.MetricsStatus;
import cn.xinzhili.user.api.RiskFactor;
import cn.xinzhili.user.api.ServiceLevel;
import java.util.List;
import lombok.Data;

/**
 * Date: 28/12/2016 Time: 11:25 PM
 *
 * @author Gan Dong
 */
@Data
public class PatientApiInfo {

  private String id;

  private String name;

  private UserGender sex;

  private Integer age;

  private String avatar;

  private String tel;

  private String doctorName;

  private String assistantName;

  private String operatorName;

  private ServiceLevel serviceLevel;

  private MedicationStatus medicationStatus;

  private MetricsStatus metricsStatus;

  private Integer pendingDoctorMessage;

  private Integer pendingAssistantMessage;

  private Integer pendingOperatorMessage;

  private AdviceLevel pendingDoctorAdviceLevel;

  private Integer pendingDoctorAdviceCount;

  private AdviceLevel pendingAssistantAdviceLevel;

  private Integer pendingAssistantAdviceCount;

  private ConsultationLevel pendingConsultationLevel;

  private Integer pendingDoctorConsultation;

  private List<RiskFactor> riskFactor;

  private String remark;

  private String ethnicity;

  private String area;

  private Integer weight;
  private Integer height;
  private Integer waistline;
  private String bmi;

  private boolean hasAssistant;

  private PatientApiProgress progress;
  private UserStatus status;

  private Integer province;
  private Integer city;
  private Integer town;

  private Integer unauditedImageCount;

  private UnreadApiStatus unreadStatus;

  private String address;

  private Long birthday;
}
