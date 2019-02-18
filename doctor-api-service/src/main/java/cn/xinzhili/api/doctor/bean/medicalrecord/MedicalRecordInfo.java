package cn.xinzhili.api.doctor.bean.medicalrecord;

import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @author by Loki on 17/3/16.
 */
public class MedicalRecordInfo {

  //高血压
  private DiseaseApiStatus hypertension;
  @Max(100)
  @Min(0)
  private Integer hypertensionSince;
  private PharmacyApiStatus hypertensionPharmacy;

  //高血脂
  private DiseaseApiStatus hyperlipemia;
  @Max(100)
  @Min(0)
  private Integer hyperlipemiaSince;
  private PharmacyApiStatus hyperlipemiaPharmacy;

  //高血糖
  private DiseaseApiStatus hyperglycemia;
  @Max(100)
  @Min(0)
  private Integer hyperglycemiaSince;
  private PharmacyApiStatus hyperglycemiaPharmacy;

  //高尿酸
  private DiseaseApiStatus hyperuricemia;
  @Max(100)
  @Min(0)
  private Integer hyperuricemiaSince;
  private PharmacyApiStatus hyperuricemiaPharmacy;

  //十二指肠
  private DiseaseApiStatus duodenalUlcer;
  @Max(100)
  @Min(0)
  private Integer duodenalUlcerSince;
  private DiseaseApiCure duodenalUlcerCure;

  //呼吸睡眠暂停综合症
  private DiseaseApiStatus sleepApnea;
  @Max(100)
  @Min(0)
  private Integer sleepApneaSince;
  private DiseaseApiCure sleepApneaCure;

  //肾功能不全
  private DiseaseApiStatus nephroticSyndrome;
  @Max(100)
  @Min(0)
  private Integer nephroticSyndromeSince;
  private DiseaseApiCure nephroticSyndromeCure;

  //肾动脉狭窄
  private DiseaseApiStatus RenalArteryStenosis;
  @Max(100)
  @Min(0)
  private Integer RenalArteryStenosisSince;
  private DiseaseApiCure RenalArteryStenosisCure;

  //幽门杆菌
  private DiseaseApiStatus pylori;
  private DiseaseApiNature pyloriStatus;

  //其他添加疾病
  private List<OtherDiagnosisExtraDisease> extraDisease;

  //吸烟
  private DiseaseApiStatus smoking;
  @Max(100)
  @Min(0)
  private Integer smokingSince;
  private SmokingApiLevel smokingLevel;
  private ApiQuitStatus quitSmoking;
  private Integer quitSmokingSince;


  //饮酒
  private DiseaseApiStatus drinking;
  @Max(100)
  @Min(0)
  private Integer drinkingSince;
  private DrinkingApiLevel drinkingLevel;
  private ApiQuitStatus quitDrinking;
  private Integer quitDrinkingSince;

  //过敏史
  private DiseaseApiStatus allergy;
  private String allergyInfo;

  //家族史
  private DiseaseApiStatus familyHistory;
  //private boolean hasFatherFamilyHistory;
  private List<ApiFamilyHistoryDisease> fatherFamilyHistory;
  //private boolean hasMotherFamilyHistory;
  private List<ApiFamilyHistoryDisease> motherFamilyHistory;
  //private boolean hasBrotherFamilyHistory;
  private List<ApiFamilyHistoryDisease> brotherFamilyHistory;

  public DiseaseApiStatus getHypertension() {
    return hypertension;
  }

  public void setHypertension(DiseaseApiStatus hypertension) {
    this.hypertension = hypertension;
  }

  public Integer getHypertensionSince() {
    return hypertensionSince;
  }

  public void setHypertensionSince(Integer hypertensionSince) {
    this.hypertensionSince = hypertensionSince;
  }

  public PharmacyApiStatus getHypertensionPharmacy() {
    return hypertensionPharmacy;
  }

  public void setHypertensionPharmacy(
      PharmacyApiStatus hypertensionPharmacy) {
    this.hypertensionPharmacy = hypertensionPharmacy;
  }

  public DiseaseApiStatus getHyperlipemia() {
    return hyperlipemia;
  }

  public void setHyperlipemia(DiseaseApiStatus hyperlipemia) {
    this.hyperlipemia = hyperlipemia;
  }

  public Integer getHyperlipemiaSince() {
    return hyperlipemiaSince;
  }

  public void setHyperlipemiaSince(Integer hyperlipemiaSince) {
    this.hyperlipemiaSince = hyperlipemiaSince;
  }

  public PharmacyApiStatus getHyperlipemiaPharmacy() {
    return hyperlipemiaPharmacy;
  }

  public void setHyperlipemiaPharmacy(
      PharmacyApiStatus hyperlipemiaPharmacy) {
    this.hyperlipemiaPharmacy = hyperlipemiaPharmacy;
  }

  public DiseaseApiStatus getHyperglycemia() {
    return hyperglycemia;
  }

  public void setHyperglycemia(DiseaseApiStatus hyperglycemia) {
    this.hyperglycemia = hyperglycemia;
  }

  public Integer getHyperglycemiaSince() {
    return hyperglycemiaSince;
  }

  public void setHyperglycemiaSince(Integer hyperglycemiaSince) {
    this.hyperglycemiaSince = hyperglycemiaSince;
  }

  public PharmacyApiStatus getHyperglycemiaPharmacy() {
    return hyperglycemiaPharmacy;
  }

  public void setHyperglycemiaPharmacy(
      PharmacyApiStatus hyperglycemiaPharmacy) {
    this.hyperglycemiaPharmacy = hyperglycemiaPharmacy;
  }

  public DiseaseApiStatus getHyperuricemia() {
    return hyperuricemia;
  }

  public void setHyperuricemia(DiseaseApiStatus hyperuricemia) {
    this.hyperuricemia = hyperuricemia;
  }

  public Integer getHyperuricemiaSince() {
    return hyperuricemiaSince;
  }

  public void setHyperuricemiaSince(Integer hyperuricemiaSince) {
    this.hyperuricemiaSince = hyperuricemiaSince;
  }

  public PharmacyApiStatus getHyperuricemiaPharmacy() {
    return hyperuricemiaPharmacy;
  }

  public void setHyperuricemiaPharmacy(
      PharmacyApiStatus hyperuricemiaPharmacy) {
    this.hyperuricemiaPharmacy = hyperuricemiaPharmacy;
  }

  public DiseaseApiStatus getDuodenalUlcer() {
    return duodenalUlcer;
  }

  public void setDuodenalUlcer(DiseaseApiStatus duodenalUlcer) {
    this.duodenalUlcer = duodenalUlcer;
  }

  public Integer getDuodenalUlcerSince() {
    return duodenalUlcerSince;
  }

  public void setDuodenalUlcerSince(Integer duodenalUlcerSince) {
    this.duodenalUlcerSince = duodenalUlcerSince;
  }

  public DiseaseApiCure getDuodenalUlcerCure() {
    return duodenalUlcerCure;
  }

  public void setDuodenalUlcerCure(
      DiseaseApiCure duodenalUlcerCure) {
    this.duodenalUlcerCure = duodenalUlcerCure;
  }

  public DiseaseApiStatus getSleepApnea() {
    return sleepApnea;
  }

  public void setSleepApnea(DiseaseApiStatus sleepApnea) {
    this.sleepApnea = sleepApnea;
  }

  public Integer getSleepApneaSince() {
    return sleepApneaSince;
  }

  public void setSleepApneaSince(Integer sleepApneaSince) {
    this.sleepApneaSince = sleepApneaSince;
  }

  public DiseaseApiCure getSleepApneaCure() {
    return sleepApneaCure;
  }

  public void setSleepApneaCure(DiseaseApiCure sleepApneaCure) {
    this.sleepApneaCure = sleepApneaCure;
  }

  public DiseaseApiStatus getNephroticSyndrome() {
    return nephroticSyndrome;
  }

  public void setNephroticSyndrome(
      DiseaseApiStatus nephroticSyndrome) {
    this.nephroticSyndrome = nephroticSyndrome;
  }

  public Integer getNephroticSyndromeSince() {
    return nephroticSyndromeSince;
  }

  public void setNephroticSyndromeSince(Integer nephroticSyndromeSince) {
    this.nephroticSyndromeSince = nephroticSyndromeSince;
  }

  public DiseaseApiCure getNephroticSyndromeCure() {
    return nephroticSyndromeCure;
  }

  public void setNephroticSyndromeCure(
      DiseaseApiCure nephroticSyndromeCure) {
    this.nephroticSyndromeCure = nephroticSyndromeCure;
  }

  public DiseaseApiStatus getRenalArteryStenosis() {
    return RenalArteryStenosis;
  }

  public void setRenalArteryStenosis(
      DiseaseApiStatus renalArteryStenosis) {
    RenalArteryStenosis = renalArteryStenosis;
  }

  public Integer getRenalArteryStenosisSince() {
    return RenalArteryStenosisSince;
  }

  public void setRenalArteryStenosisSince(Integer renalArteryStenosisSince) {
    RenalArteryStenosisSince = renalArteryStenosisSince;
  }

  public DiseaseApiCure getRenalArteryStenosisCure() {
    return RenalArteryStenosisCure;
  }

  public void setRenalArteryStenosisCure(
      DiseaseApiCure renalArteryStenosisCure) {
    RenalArteryStenosisCure = renalArteryStenosisCure;
  }

  public DiseaseApiStatus getPylori() {
    return pylori;
  }

  public void setPylori(DiseaseApiStatus pylori) {
    this.pylori = pylori;
  }

  public DiseaseApiNature getPyloriStatus() {
    return pyloriStatus;
  }

  public void setPyloriStatus(DiseaseApiNature pyloriStatus) {
    this.pyloriStatus = pyloriStatus;
  }

  public List<OtherDiagnosisExtraDisease> getExtraDisease() {
    return extraDisease;
  }

  public void setExtraDisease(
      List<OtherDiagnosisExtraDisease> extraDisease) {
    this.extraDisease = extraDisease;
  }

  public DiseaseApiStatus getSmoking() {
    return smoking;
  }

  public void setSmoking(DiseaseApiStatus smoking) {
    this.smoking = smoking;
  }

  public Integer getSmokingSince() {
    return smokingSince;
  }

  public void setSmokingSince(Integer smokingSince) {
    this.smokingSince = smokingSince;
  }

  public SmokingApiLevel getSmokingLevel() {
    return smokingLevel;
  }

  public void setSmokingLevel(SmokingApiLevel smokingLevel) {
    this.smokingLevel = smokingLevel;
  }

  public ApiQuitStatus getQuitSmoking() {
    return quitSmoking;
  }

  public void setQuitSmoking(ApiQuitStatus quitSmoking) {
    this.quitSmoking = quitSmoking;
  }

  public Integer getQuitSmokingSince() {
    return quitSmokingSince;
  }

  public void setQuitSmokingSince(Integer quitSmokingSince) {
    this.quitSmokingSince = quitSmokingSince;
  }

  public DiseaseApiStatus getDrinking() {
    return drinking;
  }

  public void setDrinking(DiseaseApiStatus drinking) {
    this.drinking = drinking;
  }

  public Integer getDrinkingSince() {
    return drinkingSince;
  }

  public void setDrinkingSince(Integer drinkingSince) {
    this.drinkingSince = drinkingSince;
  }

  public DrinkingApiLevel getDrinkingLevel() {
    return drinkingLevel;
  }

  public void setDrinkingLevel(DrinkingApiLevel drinkingLevel) {
    this.drinkingLevel = drinkingLevel;
  }

  public ApiQuitStatus getQuitDrinking() {
    return quitDrinking;
  }

  public void setQuitDrinking(ApiQuitStatus quitDrinking) {
    this.quitDrinking = quitDrinking;
  }

  public Integer getQuitDrinkingSince() {
    return quitDrinkingSince;
  }

  public void setQuitDrinkingSince(Integer quitDrinkingSince) {
    this.quitDrinkingSince = quitDrinkingSince;
  }

  public DiseaseApiStatus getAllergy() {
    return allergy;
  }

  public void setAllergy(DiseaseApiStatus allergy) {
    this.allergy = allergy;
  }

  public String getAllergyInfo() {
    return allergyInfo;
  }

  public void setAllergyInfo(String allergyInfo) {
    this.allergyInfo = allergyInfo;
  }

  public DiseaseApiStatus getFamilyHistory() {
    return familyHistory;
  }

  public void setFamilyHistory(DiseaseApiStatus familyHistory) {
    this.familyHistory = familyHistory;
  }

  public List<ApiFamilyHistoryDisease> getFatherFamilyHistory() {
    return fatherFamilyHistory;
  }

  public void setFatherFamilyHistory(
      List<ApiFamilyHistoryDisease> fatherFamilyHistory) {
    this.fatherFamilyHistory = fatherFamilyHistory;
  }

  public List<ApiFamilyHistoryDisease> getMotherFamilyHistory() {
    return motherFamilyHistory;
  }

  public void setMotherFamilyHistory(
      List<ApiFamilyHistoryDisease> motherFamilyHistory) {
    this.motherFamilyHistory = motherFamilyHistory;
  }

  public List<ApiFamilyHistoryDisease> getBrotherFamilyHistory() {
    return brotherFamilyHistory;
  }

  public void setBrotherFamilyHistory(
      List<ApiFamilyHistoryDisease> brotherFamilyHistory) {
    this.brotherFamilyHistory = brotherFamilyHistory;
  }
}
