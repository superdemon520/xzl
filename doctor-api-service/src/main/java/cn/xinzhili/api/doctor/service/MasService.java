package cn.xinzhili.api.doctor.service;

import cn.xinzhili.api.doctor.bean.PlanScope;
import cn.xinzhili.api.doctor.bean.PlanUnit;
import cn.xinzhili.api.doctor.bean.RecordInfo;
import cn.xinzhili.api.doctor.bean.MedicineInfo;
import cn.xinzhili.api.doctor.bean.response.PlanResponse;
import cn.xinzhili.api.doctor.bean.response.PlanResponse.OnePlan;
import cn.xinzhili.api.doctor.bean.response.RecordListApiResponse;
import cn.xinzhili.api.doctor.bean.response.RecordListApiResponse.OneDayRecord;
import cn.xinzhili.api.doctor.bean.response.RecordListApiResponse.OneDosageRecord;
import cn.xinzhili.api.doctor.bean.response.RecordListApiResponse.OneMedicineRecord;
import cn.xinzhili.api.doctor.bean.response.RecordListApiResponse.TimeStatus;
import cn.xinzhili.api.doctor.bean.response.RecordResponse;
import cn.xinzhili.api.doctor.client.MasServiceClient;
import cn.xinzhili.api.doctor.util.PlanComparator;
import cn.xinzhili.api.doctor.util.RecordInfoFactory;
import cn.xinzhili.mas.api.DosageUnit;
import cn.xinzhili.mas.api.PlanInfo;
import cn.xinzhili.mas.api.response.PlanListResponse;
import cn.xinzhili.mas.api.response.vis.VisRecordLineResponse;
import cn.xinzhili.xutils.core.ErrorCode;
import cn.xinzhili.xutils.core.FailureException;
import cn.xinzhili.xutils.core.http.Response;
import cn.xinzhili.xutils.core.util.HashUtils;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author by Loki on 17/3/6.
 */
@Service
public class MasService {

  private static final Logger logger = LoggerFactory.getLogger(MasService.class);
  @Autowired
  private MasServiceClient masServiceClient;
  @Autowired
  private MedicineService medicineService;

  private static final String YMD_FORMATTER = "YYYY/MM/dd";
  private static final String HOUR_FORMATTER = "H";
  private static final Integer PATIENT_RECORD_TIME_SIZE = 1;//月
  private static final DecimalFormat df = new DecimalFormat("#.##");


  public PlanResponse getPlanByPatientId(long patientId, PlanScope scope) {

    Response<PlanListResponse> response = masServiceClient
        .searchPatientPlans(patientId, null, null);
    if (!response.isSuccessful()) {
      throw new FailureException(ErrorCode.REQUEST_FAILED);
    }
    PlanListResponse planListResponse = response.getDataAs(PlanListResponse.class);
    List<PlanInfo> source = planListResponse.getPlans();

    PlanResponse result = new PlanResponse();
    if (source == null || source.isEmpty()) {
      result.setPlans(new ArrayList<>());
      return result;
    }
    source.sort(new PlanComparator());
    List<OnePlan> onePlans = sortPlans(source);

    //调药
    if (scope == PlanScope.ADJUST_MEDICINE) {

      onePlans = onePlans.stream().map(t -> {
        List<PlanResponse.PlanInfo> distinctList = new ArrayList<>();
        List<PlanResponse.PlanInfo> plansInfo = t.getPlan();
        plansInfo.forEach(info -> {
          distinctList.addAll(distinctPlanTakeTime(info));
        });
        t.setPlan(distinctList);
        return t;
      }).collect(Collectors.toList());
    }

    result.setPlans(onePlans);
    return result;
  }

  private List<PlanResponse.PlanInfo> distinctPlanTakeTime(PlanResponse.PlanInfo info) {

    List<Integer> takeTimes = info.getTakeTime();
    String dosage = info.getDosage();
    List<PlanResponse.PlanInfo> result = new ArrayList<>();

    List<Integer> distinctTimes = takeTimes.stream().distinct().collect(Collectors.toList());
    for (Integer item : distinctTimes) {
      int count = countElementInList(takeTimes, item);
      result.add(new PlanResponse().new PlanInfo(info.getId(),
          String.valueOf(Double.valueOf(dosage) * count),
          Collections.singletonList(item)));
    }
    return result;
  }

  private int countElementInList(List<Integer> source, int key) {

    int result = 0;
    for (Integer item : source) {
      if (key == item) {
        result++;
      }
    }
    return result;
  }

  /**
   * 对患者的最近一条服药计划按 服药名称，服药计量  进行分类
   */
  private List<OnePlan> sortPlans(List<PlanInfo> plans) {

    //init
    List<OnePlan> result = new ArrayList<>();
    OnePlan onePlan;
    PlanResponse.PlanInfo planInfo = new PlanResponse().new PlanInfo();
    List<PlanResponse.PlanInfo> planInfoList = new ArrayList<>();

    String originName = RandomStringUtils.random(5);
    String originDosage = String.valueOf(RandomStringUtils.randomNumeric(5));// 随机五位负数
    String itemName;
    String itemDosage;

    //handle data
    for (PlanInfo plan : plans) {

      if (invalidPlan(plan)) {
        throw new FailureException(ErrorCode.INVALID_PARAMS, "plan invalid !" + plan);
      }

      int takeTime = plan.getTakeAt() / 60;// get hour
      itemName = plan.getMedicineName();
      itemDosage = downScale(plan.getDosage().longValue() * plan.getCount());

      if (!itemName.equals(originName)) {
        originName = itemName;
        originDosage = itemDosage;
        List<Integer> takeTimes = new ArrayList<>();
        takeTimes.add(takeTime);
        Integer markId = Integer.valueOf(RandomStringUtils.randomNumeric(3));
        planInfo = new PlanResponse().new PlanInfo(HashUtils.encode(markId), itemDosage, takeTimes);
        planInfoList = new ArrayList<>();
        planInfoList.add(planInfo);
        MedicineInfo medicineInfo = medicineService
            .getMedicineById(plan.getMedicineId());
        onePlan = new OnePlan(HashUtils.encode(plan.getId()),
            HashUtils.encode(plan.getMedicineId()), itemName,
            medicineInfo.getCommodityName(), medicineInfo.getStrength(),
            PlanUnit.getUnit(plan.getDosageUnit()), planInfoList);
        result.add(onePlan);
      } else {
        if (!Objects.equals(originDosage, itemDosage)) {
          originDosage = downScale(Long.valueOf(plan.getDosage()) * plan.getCount());
          List<Integer> takeTimes = new ArrayList<>();
          takeTimes.add(takeTime);
          Integer markId = Integer.valueOf(RandomStringUtils.randomNumeric(3));
          planInfo = new PlanResponse().new PlanInfo(HashUtils.encode(markId), itemDosage,
              takeTimes);
          planInfoList.add(planInfo);
        } else {
          List<Integer> takeTimes = planInfo.getTakeTime();
          takeTimes.add(takeTime);
        }
      }
    }
    return result;

  }

  private static String downScale(Long value) {
    Double v = Double.parseDouble(df.format(value / 1000000.0));
    if (v % 1 == 0) {
      return String.valueOf(v.intValue());
    } else {
      return v.toString();
    }
  }

  private boolean invalidPlan(PlanInfo plan) {
    return plan.getMedicineName() == null || plan.getMedicineId() == null
        || plan.getDosage() == null || plan.getTakeAt() == null
        || plan.getDosageUnit() == null;
  }

  /**
   * 患者最近一个 月的服药纪录
   */
  public RecordListApiResponse getRecordsByPatientId(long patientId, Long end) {
    if (null == end) {
      end = System.currentTimeMillis();
    }
    Instant instant = Instant.ofEpochMilli(end);
    long timeOneMonthAgo = Timestamp
        .valueOf(LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
            .minusMonths(PATIENT_RECORD_TIME_SIZE).toLocalDate().atStartOfDay()).getTime();
    Response<RecordResponse> recordResponse =
        masServiceClient.getPatientMRecords(
            patientId, timeOneMonthAgo, end);
    Response<PlanListResponse> planResponse = masServiceClient
        .searchPatientPlans(patientId, timeOneMonthAgo, end);
    if (!recordResponse.isSuccessful()) {
      throw new FailureException(ErrorCode.REQUEST_FAILED,
          "查询患者最近一个月服药纪录失败！");
    }
    if (recordResponse.getData().isEmpty()
        || recordResponse.getData() == null) {
      throw new FailureException(ErrorCode.REQUEST_FAILED,
          "服药纪录数据有误！");
    }
    if (!planResponse.isSuccessful()) {
      throw new FailureException(ErrorCode.REQUEST_FAILED,
          "查询患者最近一个月服药计划失败！");
    }
    if (planResponse.getData().isEmpty()
        || planResponse.getData() == null) {
      throw new FailureException(ErrorCode.REQUEST_FAILED,
          "服药计划数据有误！");
    }
    RecordResponse records = recordResponse.getDataAs(RecordResponse.class);
    PlanListResponse plans = planResponse.getDataAs(PlanListResponse.class);
    List<OneDayRecord> oneDayRecords =
        divideRecordsByTime(records.getRecords(), plans.getPlans());
    RecordListApiResponse recordListApiResponse =
        new RecordListApiResponse();
    recordListApiResponse.setDayRecords(oneDayRecords);
    return recordListApiResponse;
  }

  /**
   * 对患者的服药纪录按day,medicine进行分组
   */
  private List<OneDayRecord> divideRecordsByTime(List<RecordInfo> records,
      List<PlanInfo> plans) {
    Comparator<OneDayRecord> comparator = Comparator.comparing(OneDayRecord::getDay);
    return records.stream()
        .collect(Collectors.groupingBy(RecordInfo::getDayNum)).entrySet().stream()
        .map(byDayListEntry -> {
              OneDayRecord oneDayRecord = new RecordListApiResponse().new OneDayRecord();
              oneDayRecord.setDay(byDayListEntry.getKey());
              Set<Integer> statusSet = new HashSet<>();
              List<OneMedicineRecord> oneMedicineRecords = getOneMedicineRecords(plans,
                  byDayListEntry, statusSet);
              RecordListApiResponse.setOneDayRecordStatus(statusSet, oneDayRecord);
              oneDayRecord.setOneMedicineRecords(oneMedicineRecords);
              return oneDayRecord;
            }
        ).filter(oneDayRecord ->
            !oneDayRecord.getDay()
                .equals(RecordInfoFactory.getFormatedTime(LocalDateTime.now()))
                && !oneDayRecord.getDay()
                .equals(RecordInfoFactory.getFormatedTime(LocalDateTime.now().minusDays(1)))
        )
        .sorted(comparator.reversed()).collect(Collectors.toList());
  }

  private List<OneMedicineRecord> getOneMedicineRecords(List<PlanInfo> plans,
      Entry<String, List<RecordInfo>> byDayListEntry, Set<Integer> statusSet) {
    return byDayListEntry.getValue().stream()
        .collect(Collectors.groupingBy(RecordInfo::getMedicineName)).entrySet().stream()
        .map(byNameListEntry -> {
          OneMedicineRecord oneMedicineRecord = new RecordListApiResponse().new OneMedicineRecord();
          oneMedicineRecord.setMedicineName(byNameListEntry.getKey());
          List<OneDosageRecord> oneDosageRecords = genOneDosageRecords(byNameListEntry,
              statusSet, oneMedicineRecord, plans);
          oneMedicineRecord.setOneDosageRecords(oneDosageRecords);
          return oneMedicineRecord;
        }).collect(Collectors.toList());
  }

  private List<OneDosageRecord> genOneDosageRecords(Entry<String, List<RecordInfo>> byNameListEntry,
      Set<Integer> statusSet, OneMedicineRecord oneMedicineRecord, List<PlanInfo> planInfos) {
    return byNameListEntry.getValue().stream()
        .collect(Collectors.groupingBy(RecordInfo::getCount)).entrySet().stream()
        .map(byDosageEntry -> {
          OneDosageRecord oneDosageRecord = new RecordListApiResponse().new OneDosageRecord();
          byDosageEntry.getValue().forEach(data -> {
            RecordInfo.validateRecord(data);
            oneDosageRecord.setDosage(data.getFinalDosage());
            statusSet.add(data.getStatus());
            oneMedicineRecord.setDosageUnit(getDosageUnit(planInfos, data));
          });
          oneDosageRecord.setTimeStatus(genTimeStatus(byDosageEntry.getValue()));
          return oneDosageRecord;
        }).collect(Collectors.toList());
  }

  private List<TimeStatus> genTimeStatus(List<RecordInfo> recordInfos) {
    return recordInfos.stream().collect(Collectors.groupingBy(RecordInfo::getTakeTime))
        .entrySet().stream().map(entry -> getOnlyTimeStatus(entry.getKey(), entry.getValue()))
        .collect(Collectors.toList());
  }

  private TimeStatus getOnlyTimeStatus(Date takeTime, List<RecordInfo> recordInfos) {
    TimeStatus timeStatus = new RecordListApiResponse().new TimeStatus();
    timeStatus.setTime(new SimpleDateFormat("H").format(takeTime));
    Optional<RecordInfo> recordInfoOption = recordInfos.stream()
        .filter(r -> r.getConfirmedAt() != null || r.getStatus() != 0).findFirst();
    timeStatus.setStatus(recordInfoOption.isPresent() ? recordInfoOption.get().getStatus() : 0);
    return timeStatus;
  }

  private static String getDosageUnit(List<PlanInfo> plans, RecordInfo info) {
    Optional<PlanInfo> planData = plans.stream()
        .filter(planInfo -> info.getPlanId().equals(planInfo.getId())).findFirst();
    return !planData.isPresent() ? DosageUnit.switchDescription((short) 1)
        : DosageUnit.switchDescription(planData.get().getDosageUnit().shortValue());
  }

  /**
   * 获取患者服药计划的时间轴
   *
   * @param patientId 患者的id
   * @param start 时间轴开始时间
   * @param end 时间轴结束时间
   * @return 返回时间轴bean
   */
  public VisRecordLineResponse lineDataForFontEnd(
      long patientId, long start, long end) {
    Response<VisRecordLineResponse> response = masServiceClient
        .getPatientRecordLine(patientId, start, end);
    if (!response.isSuccessful()) {
      logger.warn("请求获取服药时间轴出错,patientId{},response:{}", patientId, response);
      throw new FailureException();
    }
    return response.getDataAs(VisRecordLineResponse.class);
  }


}
