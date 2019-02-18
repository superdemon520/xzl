package cn.xinzhili.api.doctor.util;

import cn.xinzhili.api.doctor.bean.medicalrecord.ApiFamilyHistoryDisease;
import cn.xinzhili.api.doctor.bean.medicalrecord.ApiQuitStatus;
import cn.xinzhili.api.doctor.bean.medicalrecord.DiseaseApiCure;
import cn.xinzhili.api.doctor.bean.medicalrecord.DiseaseApiNature;
import cn.xinzhili.api.doctor.bean.medicalrecord.DiseaseApiStatus;
import cn.xinzhili.api.doctor.bean.medicalrecord.DrinkingApiLevel;
import cn.xinzhili.api.doctor.bean.medicalrecord.MedicalRecordInfo;
import cn.xinzhili.api.doctor.bean.medicalrecord.OtherDiagnosisExtraDisease;
import cn.xinzhili.api.doctor.bean.medicalrecord.PharmacyApiStatus;
import cn.xinzhili.api.doctor.bean.medicalrecord.SmokingApiLevel;
import cn.xinzhili.api.doctor.bean.request.MedicalRecordApiRequest;
import cn.xinzhili.api.doctor.bean.response.MedicalRecordApiResponse;
import cn.xinzhili.medical.api.DiseaseNature;
import cn.xinzhili.medical.api.DiseaseStatus;
import cn.xinzhili.medical.api.DrinkLevel;
import cn.xinzhili.medical.api.ImageDetailInfo;
import cn.xinzhili.medical.api.PharmacyStatus;
import cn.xinzhili.medical.api.ReportInfo;
import cn.xinzhili.medical.api.ReportStatus;
import cn.xinzhili.medical.api.SmokingLevel;
import cn.xinzhili.medical.api.medicalRecord.DiseaseCure;
import cn.xinzhili.medical.api.medicalRecord.ExtraDisease;
import cn.xinzhili.medical.api.medicalRecord.FamilyHistoryDisease;
import cn.xinzhili.medical.api.medicalRecord.FourHighInfo;
import cn.xinzhili.medical.api.medicalRecord.IntegratedHistory;
import cn.xinzhili.medical.api.medicalRecord.OtherDiagnosis;
import cn.xinzhili.medical.api.medicalRecord.QuitStatus;
import cn.xinzhili.medical.api.request.MedicalRecordRequest;
import cn.xinzhili.medical.api.response.MedicalRecordsResponse;
import cn.xinzhili.xutils.core.ErrorCode;
import cn.xinzhili.xutils.core.FailureException;
import cn.xinzhili.xutils.core.util.HashUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;

/**
 * @author by Loki on 17/6/8.
 */
public class MedicalRecordFactory {


  public static MedicalRecordRequest of(MedicalRecordApiRequest request) {

    if (request == null) {
      throw new FailureException(ErrorCode.INVALID_PARAMS, "medical record api request is null !");
    }
    MedicalRecordRequest medicalRecordRequest = new MedicalRecordRequest();
    if (!StringUtils.isEmpty(request.getPatientId())) {
      medicalRecordRequest.setPatientId(HashUtils.decode(request.getPatientId()));
    }
    if (!StringUtils.isEmpty(request.getImageId())) {
      medicalRecordRequest.setImageId(HashUtils.decode(request.getImageId()));
    }
    ReportInfo reportInfo = new ReportInfo();
    if (request.getStatus() != null) {
      reportInfo.setStatus(ReportStatus.valueOf(request.getStatus().name()));
    }
    reportInfo.setReportAt(request.getReportAt());
    medicalRecordRequest.setReportInfo(reportInfo);
    MedicalRecordInfo medicalRecordInfo = request.getMedicalRecordInfo();
    medicalRecordRequest.setIntegratedHistory(ofIntegratedHistory(medicalRecordInfo));
    medicalRecordRequest.setFourHighInfo(ofFourHigh(medicalRecordInfo));
    medicalRecordRequest.setOtherDiagnosis(ofOtherDiagnosis(medicalRecordInfo));
    return medicalRecordRequest;
  }

  public static MedicalRecordApiResponse api(MedicalRecordsResponse response) {

    if (response == null) {
      throw new FailureException(ErrorCode.INVALID_PARAMS, "medical record response is null !");
    }
    MedicalRecordApiResponse apiResponse = new MedicalRecordApiResponse();

    apiResponse.setMedicalRecordInfo(
        api(response.getIntegratedHistory(), response.getFourHighInfo(),
            response.getOtherDiagnosis()));

    ImageDetailInfo imageDetailInfo = new ImageDetailInfo();
    imageDetailInfo.setImage(response.getImageInfo());
    imageDetailInfo.setReport(response.getReportInfo());
    apiResponse.setImageInfo(ImageFactory.api(imageDetailInfo));
    apiResponse.setAllType(apiResponse.resolver(response.getTypes()));
    return apiResponse;
  }

  public static MedicalRecordInfo api(
      IntegratedHistory integratedHistory, FourHighInfo fourHighInfo,
      OtherDiagnosis otherDiagnosis) {

    MedicalRecordInfo apiInfo = new MedicalRecordInfo();

    if (fourHighInfo != null) {
      //高血压
      if (fourHighInfo.getHypertension() != null) {
        apiInfo.setHypertension(DiseaseApiStatus.valueOf(fourHighInfo.getHypertension().name()));
      }
      apiInfo.setHypertensionSince(fourHighInfo.getHypertensionSince());
      if (fourHighInfo.getHypertensionPharmacy() != null) {
        apiInfo.setHypertensionPharmacy(
            PharmacyApiStatus.valueOf(fourHighInfo.getHypertensionPharmacy().name()));
      }

      //高血脂
      if (fourHighInfo.getHyperlipemia() != null) {
        apiInfo.setHyperlipemia(
            DiseaseApiStatus.valueOf(fourHighInfo.getHyperlipemia().name()));
      }
      apiInfo.setHyperlipemiaSince(fourHighInfo.getHyperlipemiaSince());
      if (fourHighInfo.getHyperlipemiaPharmacy() != null) {
        apiInfo.setHyperlipemiaPharmacy(
            PharmacyApiStatus.valueOf(fourHighInfo.getHyperlipemiaPharmacy().name()));
      }

      //高血糖
      if (fourHighInfo.getHyperglycemia() != null) {
        apiInfo.setHyperglycemia(DiseaseApiStatus.valueOf(fourHighInfo.getHyperglycemia().name()));
      }
      apiInfo.setHyperglycemiaSince(fourHighInfo.getHyperglycemiaSince());
      if (fourHighInfo.getHyperglycemiaPharmacy() != null) {
        apiInfo.setHyperglycemiaPharmacy(
            PharmacyApiStatus.valueOf(fourHighInfo.getHyperglycemiaPharmacy().name()));
      }

      //高尿酸
      if (fourHighInfo.getHyperuricemia() != null) {
        apiInfo.setHyperuricemia(DiseaseApiStatus.valueOf(fourHighInfo.getHyperuricemia().name()));
      }
      apiInfo.setHyperuricemiaSince(fourHighInfo.getHyperuricemiaSince());
      if (fourHighInfo.getHyperuricemiaPharmacy() != null) {
        apiInfo.setHyperuricemiaPharmacy(
            PharmacyApiStatus.valueOf(fourHighInfo.getHyperuricemiaPharmacy().name()));
      }
    }

    if (otherDiagnosis != null) {

      //十二指肠
      if (otherDiagnosis.getDuodenalUlcer() != null) {
        apiInfo
            .setDuodenalUlcer(DiseaseApiStatus.valueOf(otherDiagnosis.getDuodenalUlcer().name()));
      }
      apiInfo.setDuodenalUlcerSince(otherDiagnosis.getDuodenalUlcerSince());
      if (otherDiagnosis.getDuodenalUlcerCure() != null) {
        apiInfo.setDuodenalUlcerCure(
            DiseaseApiCure.valueOf(otherDiagnosis.getDuodenalUlcerCure().name()));
      }

      //呼吸睡眠暂停综合症
      if (otherDiagnosis.getSleepApnea() != null) {
        apiInfo.setSleepApnea(DiseaseApiStatus.valueOf(otherDiagnosis.getSleepApnea().name()));
      }
      apiInfo.setSleepApneaSince(otherDiagnosis.getSleepApneaSince());
      if (otherDiagnosis.getSleepApneaCure() != null) {
        apiInfo
            .setSleepApneaCure(DiseaseApiCure.valueOf(otherDiagnosis.getSleepApneaCure().name()));
      }

      //肾功能不全
      if (otherDiagnosis.getNephroticSyndrome() != null) {
        apiInfo.setNephroticSyndrome(
            DiseaseApiStatus.valueOf(otherDiagnosis.getNephroticSyndrome().name()));
      }
      apiInfo.setNephroticSyndromeSince(otherDiagnosis.getNephroticSyndromeSince());
      if (otherDiagnosis.getNephroticSyndromeCure() != null) {
        apiInfo.setNephroticSyndromeCure(
            DiseaseApiCure.valueOf(otherDiagnosis.getNephroticSyndromeCure().name()));
      }

      //肾动脉狭窄
      if (otherDiagnosis.getRenalArteryStenosis() != null) {
        apiInfo.setRenalArteryStenosis(
            DiseaseApiStatus.valueOf(otherDiagnosis.getRenalArteryStenosis().name()));
      }
      apiInfo.setRenalArteryStenosisSince(otherDiagnosis.getRenalArteryStenosisSince());
      if (otherDiagnosis.getRenalArteryStenosisCure() != null) {
        apiInfo.setRenalArteryStenosisCure(
            DiseaseApiCure.valueOf(otherDiagnosis.getRenalArteryStenosisCure().name()));
      }

      //幽门杆菌
      if (otherDiagnosis.getPylori() != null) {
        apiInfo.setPylori(DiseaseApiStatus.valueOf(otherDiagnosis.getPylori().name()));
      }
      if (otherDiagnosis.getPyloriStatus() != null) {
        apiInfo.setPyloriStatus(DiseaseApiNature.valueOf(otherDiagnosis.getPyloriStatus().name()));
      }

      //额外添加疾病
      List<OtherDiagnosisExtraDisease> extraDiseaseList = new ArrayList<>();
      List<ExtraDisease> extraDisease = otherDiagnosis.getExtraDisease();
      if (extraDisease != null) {
        extraDisease.forEach(t -> extraDiseaseList.add(api(t)));
      }
      apiInfo.setExtraDisease(extraDiseaseList);
    }

    if (integratedHistory != null) {

      //吸烟
      if (integratedHistory.getSmoking() != null) {
        apiInfo.setSmoking(DiseaseApiStatus.valueOf(integratedHistory.getSmoking().name()));
      }
      apiInfo.setSmokingSince(integratedHistory.getSmokingSince());
      if (integratedHistory.getSmokingLevel() != null) {
        apiInfo
            .setSmokingLevel(SmokingApiLevel.valueOf(integratedHistory.getSmokingLevel().name()));
      }
      if (integratedHistory.getQuitSmoking() != null) {
        apiInfo.setQuitSmoking(ApiQuitStatus.valueOf(integratedHistory.getQuitSmoking().name()));
      }
      apiInfo.setQuitSmokingSince(integratedHistory.getQuitSmokingSince());

      //饮酒
      if (integratedHistory.getDrinking() != null) {
        apiInfo.setDrinking(DiseaseApiStatus.valueOf(integratedHistory.getDrinking().name()));
      }
      apiInfo.setDrinkingSince(integratedHistory.getDrinkingSince());
      if (integratedHistory.getDrinkingLevel() != null) {
        apiInfo
            .setDrinkingLevel(
                DrinkingApiLevel.valueOf(integratedHistory.getDrinkingLevel().name()));
      }
      if (integratedHistory.getQuitDrinking() != null) {
        apiInfo.setQuitDrinking(ApiQuitStatus.valueOf(integratedHistory.getQuitDrinking().name()));
      }
      apiInfo.setQuitDrinkingSince(integratedHistory.getQuitDrinkingSince());

      //过敏史
      if (integratedHistory.getAllergy() != null) {
        apiInfo.setAllergy(DiseaseApiStatus.valueOf(integratedHistory.getAllergy().name()));
      }
      //apiInfo.setAllergySince(integratedHistory.getAllergySince());
      apiInfo.setAllergyInfo(integratedHistory.getAllergyInfo());

      //家族史
      if (integratedHistory.getFamilyHistory() != null) {
        apiInfo.setFamilyHistory(
            DiseaseApiStatus.valueOf(integratedHistory.getFamilyHistory().name()));
      }
      if (integratedHistory.getFatherFamilyHistory() != null) {
        List<ApiFamilyHistoryDisease> fatherFamily = api(
            integratedHistory.getFatherFamilyHistory());
        //apiInfo.setHasFatherFamilyHistory(!fatherFamily.isEmpty());
        apiInfo.setFatherFamilyHistory(fatherFamily);
      }
      if (integratedHistory.getMotherFamilyHistory() != null) {
        List<ApiFamilyHistoryDisease> motherFamily = api(
            integratedHistory.getMotherFamilyHistory());
        //apiInfo.setHasMotherFamilyHistory(!motherFamily.isEmpty());
        apiInfo.setMotherFamilyHistory(motherFamily);
      }
      if (integratedHistory.getBrotherFamilyHistory() != null) {
        List<ApiFamilyHistoryDisease> brotherFamily = api(
            integratedHistory.getBrotherFamilyHistory());
        //apiInfo.setHasBrotherFamilyHistory(!brotherFamily.isEmpty());
        apiInfo.setBrotherFamilyHistory(brotherFamily);
      }

    }

    return apiInfo;
  }

  private static OtherDiagnosisExtraDisease api(ExtraDisease extraDisease) {
    OtherDiagnosisExtraDisease otherDiagnosisExtraDisease = new OtherDiagnosisExtraDisease();
    otherDiagnosisExtraDisease.setDiseaseId(HashUtils.encode(extraDisease.getDiseaseId()));
    otherDiagnosisExtraDisease.setDiseaseName(extraDisease.getDiseaseName());
    otherDiagnosisExtraDisease.setSince(extraDisease.getSince());
    if (extraDisease.getDiseaseCure() != null) {
      otherDiagnosisExtraDisease
          .setDiseaseCure(DiseaseApiCure.valueOf(extraDisease.getDiseaseCure().name()));
    }
    return otherDiagnosisExtraDisease;
  }

  private static FourHighInfo ofFourHigh(MedicalRecordInfo info) {

    if (info == null) {
      throw new FailureException(ErrorCode.INVALID_PARAMS, "medical record info is null !");
    }

    FourHighInfo fourHighInfo = new FourHighInfo();
    //高血压
    if (info.getHypertension() != null) {
      fourHighInfo.setHypertension(
          DiseaseStatus.valueOf(info.getHypertension().name()));
    }
    fourHighInfo.setHypertensionSince(info.getHypertensionSince());
    if (info.getHypertensionPharmacy() != null) {
      fourHighInfo
          .setHypertensionPharmacy(PharmacyStatus.valueOf(info.getHypertensionPharmacy().name()));
    }

    //高血脂
    if (info.getHyperlipemia() != null) {
      fourHighInfo.setHyperlipemia(DiseaseStatus.valueOf(info.getHyperlipemia().name()));
    }
    fourHighInfo.setHyperlipemiaSince(info.getHyperlipemiaSince());
    if (info.getHyperlipemiaPharmacy() != null) {
      fourHighInfo
          .setHyperlipemiaPharmacy(PharmacyStatus.valueOf(info.getHyperlipemiaPharmacy().name()));
    }

    //高血糖
    if (info.getHyperglycemia() != null) {
      fourHighInfo.setHyperglycemia(DiseaseStatus.valueOf(info.getHyperglycemia().name()));
    }
    fourHighInfo.setHyperglycemiaSince(info.getHyperglycemiaSince());
    if (info.getHyperglycemiaPharmacy() != null) {
      fourHighInfo
          .setHyperglycemiaPharmacy(PharmacyStatus.valueOf(info.getHyperglycemiaPharmacy().name()));
    }

    //高尿酸
    if (info.getHyperuricemia() != null) {
      fourHighInfo.setHyperuricemia(DiseaseStatus.valueOf(info.getHyperuricemia().name()));
    }
    fourHighInfo.setHyperuricemiaSince(info.getHyperuricemiaSince());
    if (info.getHyperuricemiaPharmacy() != null) {
      fourHighInfo
          .setHyperuricemiaPharmacy(PharmacyStatus.valueOf(info.getHyperuricemiaPharmacy().name()));
    }
    return fourHighInfo;
  }

  private static OtherDiagnosis ofOtherDiagnosis(MedicalRecordInfo info) {

    OtherDiagnosis otherDiagnosis = new OtherDiagnosis();
    //十二指肠
    if (info.getDuodenalUlcer() != null) {
      otherDiagnosis.setDuodenalUlcer(DiseaseStatus.valueOf(info.getDuodenalUlcer().name()));
    }
    otherDiagnosis.setDuodenalUlcerSince(info.getDuodenalUlcerSince());
    if (info.getDuodenalUlcerCure() != null) {
      otherDiagnosis.setDuodenalUlcerCure(DiseaseCure.valueOf(info.getDuodenalUlcerCure().name()));
    }

    //呼吸睡眠暂停综合症
    if (info.getSleepApnea() != null) {
      otherDiagnosis.setSleepApnea(DiseaseStatus.valueOf(info.getSleepApnea().name()));
    }
    otherDiagnosis.setSleepApneaSince(info.getSleepApneaSince());
    if (info.getSleepApneaCure() != null) {
      otherDiagnosis.setSleepApneaCure(DiseaseCure.valueOf(info.getSleepApneaCure().name()));
    }

    //肾功能不全
    if (info.getNephroticSyndrome() != null) {
      otherDiagnosis
          .setNephroticSyndrome(DiseaseStatus.valueOf(info.getNephroticSyndrome().name()));
    }
    otherDiagnosis.setNephroticSyndromeSince(info.getNephroticSyndromeSince());
    if (info.getNephroticSyndromeCure() != null) {
      otherDiagnosis
          .setNephroticSyndromeCure(DiseaseCure.valueOf(info.getNephroticSyndromeCure().name()));
    }

    //肾动脉狭窄
    if (info.getRenalArteryStenosis() != null) {
      otherDiagnosis
          .setRenalArteryStenosis(DiseaseStatus.valueOf(info.getRenalArteryStenosis().name()));
    }
    otherDiagnosis.setRenalArteryStenosisSince(info.getRenalArteryStenosisSince());
    if (info.getRenalArteryStenosisCure() != null) {
      otherDiagnosis
          .setRenalArteryStenosisCure(
              DiseaseCure.valueOf(info.getRenalArteryStenosisCure().name()));
    }

    //幽门杆菌
    if (info.getPylori() != null) {
      otherDiagnosis.setPylori(DiseaseStatus.valueOf(info.getPylori().name()));
    }
    if (info.getPyloriStatus() != null) {
      otherDiagnosis.setPyloriStatus(DiseaseNature.valueOf(info.getPyloriStatus().name()));
    }

    //其他诊断
    List<OtherDiagnosisExtraDisease> extraDisease = info.getExtraDisease();
    if (extraDisease != null) {
      otherDiagnosis.setExtraDisease(
          extraDisease.stream().map(MedicalRecordFactory::of).collect(Collectors.toList()));
    }
    return otherDiagnosis;
  }

  private static IntegratedHistory ofIntegratedHistory(MedicalRecordInfo info) {

    IntegratedHistory integratedHistory = new IntegratedHistory();
    //吸烟
    if (info.getSmoking() != null) {
      integratedHistory.setSmoking(DiseaseStatus.valueOf(info.getSmoking().name()));
    }
    integratedHistory.setSmokingSince(info.getSmokingSince());
    if (info.getSmokingLevel() != null) {
      integratedHistory.setSmokingLevel(SmokingLevel.valueOf(info.getSmokingLevel().name()));
    }
    if (info.getQuitSmoking() != null) {
      integratedHistory.setQuitSmoking(QuitStatus.valueOf(info.getQuitSmoking().name()));
    }
    integratedHistory.setQuitSmokingSince(info.getQuitSmokingSince());

    //饮酒
    if (info.getDrinking() != null) {
      integratedHistory.setDrinking(DiseaseStatus.valueOf(info.getDrinking().name()));
    }
    integratedHistory.setDrinkingSince(info.getDrinkingSince());
    if (info.getDrinkingLevel() != null) {
      integratedHistory.setDrinkingLevel(DrinkLevel.valueOf(info.getDrinkingLevel().name()));
    }
    if (info.getQuitDrinking() != null) {
      integratedHistory.setQuitDrinking(QuitStatus.valueOf(info.getQuitDrinking().name()));
    }
    integratedHistory.setQuitDrinkingSince(info.getQuitDrinkingSince());

    //过敏史
    if (info.getAllergy() != null) {
      integratedHistory.setAllergy(DiseaseStatus.valueOf(info.getAllergy().name()));
    }
    integratedHistory.setAllergyInfo(info.getAllergyInfo());

    //家族史
    if (info.getFamilyHistory() != null) {
      integratedHistory.setFamilyHistory(DiseaseStatus.valueOf(info.getFamilyHistory().name()));
    }
    if (info.getFatherFamilyHistory() != null) {
      integratedHistory.setFatherFamilyHistory(of(info.getFatherFamilyHistory()));
    }
    if (info.getMotherFamilyHistory() != null) {
      integratedHistory.setMotherFamilyHistory(of(info.getMotherFamilyHistory()));
    }
    if (info.getBrotherFamilyHistory() != null) {
      integratedHistory.setBrotherFamilyHistory(of(info.getBrotherFamilyHistory()));
    }
    return integratedHistory;
  }

  private static ExtraDisease of(OtherDiagnosisExtraDisease otherDiagnosisExtraDisease) {
    ExtraDisease extraDisease = new ExtraDisease();
    extraDisease.setDiseaseName(otherDiagnosisExtraDisease.getDiseaseName());
    if (!StringUtils.isEmpty(otherDiagnosisExtraDisease.getDiseaseId())) {
      extraDisease.setDiseaseId(HashUtils.decode(otherDiagnosisExtraDisease.getDiseaseId()));
    }
    extraDisease.setSince(otherDiagnosisExtraDisease.getSince());
    if (otherDiagnosisExtraDisease.getDiseaseCure() != null) {
      extraDisease
          .setDiseaseCure(DiseaseCure.valueOf(otherDiagnosisExtraDisease.getDiseaseCure().name()));
    }
    return extraDisease;
  }

  private static List<ApiFamilyHistoryDisease> api(FamilyHistoryDisease familyHistoryDisease) {

    return new ArrayList<ApiFamilyHistoryDisease>() {
      {
        if (familyHistoryDisease.isCoronary()) {
          add(ApiFamilyHistoryDisease.CORONARY);
        }
        if (familyHistoryDisease.isHyperglycemia()) {
          add(ApiFamilyHistoryDisease.HYPERGLYCEMIA);
        }
        if (familyHistoryDisease.isHypertension()) {
          add(ApiFamilyHistoryDisease.HYPERTENSION);
        }
        if (familyHistoryDisease.isHyperlipemia()) {
          add(ApiFamilyHistoryDisease.HYPERLIPEMIA);
        }
        if (familyHistoryDisease.isPeripheralVascular()) {
          add(ApiFamilyHistoryDisease.PERIPHERALVASCULAR);
        }
        if (familyHistoryDisease.isGout()) {
          add(ApiFamilyHistoryDisease.GOUT);
        }
        if (familyHistoryDisease.isTumour()) {
          add(ApiFamilyHistoryDisease.TUMOUR);
        }
      }

    };
  }

  private static FamilyHistoryDisease of(List<ApiFamilyHistoryDisease> diseases) {

    FamilyHistoryDisease familyHistoryDisease = new FamilyHistoryDisease();
    for (ApiFamilyHistoryDisease f : diseases) {

      if (f == ApiFamilyHistoryDisease.CORONARY) {
        familyHistoryDisease.setCoronary(true);
      } else if (f == ApiFamilyHistoryDisease.HYPERGLYCEMIA) {
        familyHistoryDisease.setHyperglycemia(true);
      } else if (f == ApiFamilyHistoryDisease.HYPERLIPEMIA) {
        familyHistoryDisease.setHyperlipemia(true);
      } else if (f == ApiFamilyHistoryDisease.HYPERTENSION) {
        familyHistoryDisease.setHypertension(true);
      } else if (f == ApiFamilyHistoryDisease.PERIPHERALVASCULAR) {
        familyHistoryDisease.setPeripheralVascular(true);
      } else if (f == ApiFamilyHistoryDisease.GOUT) {
        familyHistoryDisease.setGout(true);
      } else if (f == ApiFamilyHistoryDisease.TUMOUR) {
        familyHistoryDisease.setTumour(true);
      }
    }
    return familyHistoryDisease;
  }

}
