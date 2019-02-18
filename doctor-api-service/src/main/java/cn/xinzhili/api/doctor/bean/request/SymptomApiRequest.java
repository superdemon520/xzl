package cn.xinzhili.api.doctor.bean.request;

import cn.xinzhili.medical.api.SymptomContent.PatientSymptom;
import java.util.List;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author by Loki on 17/4/20.
 */
public class SymptomApiRequest {

  @NotEmpty
  private String patientId;
  private List<PatientSymptom> content;

  private String note;


  public String getPatientId() {
    return patientId;
  }

  public void setPatientId(String patientId) {
    this.patientId = patientId;
  }

  public List<PatientSymptom> getContent() {
    return content;
  }

  public void setContent(List<PatientSymptom> content) {
    this.content = content;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }
}
