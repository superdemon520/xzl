package cn.xinzhili.api.doctor.bean.response;

import cn.xinzhili.medical.api.SymptomContent.PatientSymptom;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Date;
import java.util.List;

/**
 * @author  by Loki on 17/4/20.
 */
public class SymptomApiResponse {

  private List<PatientSymptom> content;
  private String note;
  @JsonInclude(Include.NON_NULL)
  private Date recordedDay;



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

  public Date getRecordedDay() {
    return recordedDay;
  }

  public void setRecordedDay(Date recordedDay) {
    this.recordedDay = recordedDay;
  }
}
