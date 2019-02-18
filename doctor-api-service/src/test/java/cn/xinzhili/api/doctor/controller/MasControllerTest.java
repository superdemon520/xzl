package cn.xinzhili.api.doctor.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cn.xinzhili.api.doctor.bean.PlanScope;
import cn.xinzhili.api.doctor.bean.response.PlanResponse;
import cn.xinzhili.api.doctor.bean.response.PlanResponse.OnePlan;
import cn.xinzhili.api.doctor.bean.response.RecordListApiResponse;
import cn.xinzhili.api.doctor.bean.response.RecordListApiResponse.TimeStatus;
import cn.xinzhili.api.doctor.common.ContextMock;
import cn.xinzhili.api.doctor.config.ConfigConsts;
import cn.xinzhili.api.doctor.service.MasService;
import cn.xinzhili.api.doctor.service.UserService;
import cn.xinzhili.mas.api.response.vis.VisRecordGroup;
import cn.xinzhili.mas.api.response.vis.VisRecordItem;
import cn.xinzhili.mas.api.response.vis.VisRecordLineResponse;
import cn.xinzhili.xutils.core.util.HashUtils;
import cn.xinzhili.xutils.spring.interceptor.ExceptionResolver;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.codehaus.jackson.map.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * @author by Loki on 17/3/8.
 */
@RunWith(MockitoJUnitRunner.class)
public class MasControllerTest {


  @Rule
  public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(
      "build/generated-snippets");

  private MockMvc mockMvc;

  @InjectMocks
  private MasController masController;

  @InjectMocks
  private ExceptionResolver exceptionResolver;

  @Mock
  private MasService masService;
  @Mock
  private UserService userService;

  private ObjectMapper mapper;

  private Long mockUserId = 66666L;

  private ContextMock contextMock = new ContextMock().withUid(mockUserId);

  @Before
  public void setUp() {
    this.mockMvc = MockMvcBuilders
        .standaloneSetup(this.masController)
        .setControllerAdvice(exceptionResolver)
        .setCustomArgumentResolvers(
            new AuthenticationPrincipalArgumentResolver(),
            contextMock.getMockUidResolver())
        .apply(MockMvcRestDocumentation
            .documentationConfiguration(restDocumentation)
            .uris()
            .withHost(ConfigConsts.API_HOST)
            .withPort(ConfigConsts.API_PORT)
        )
        .build();

    mapper = new ObjectMapper();
    given(userService.checkBindRelation(any(Long.class), any(Long.class)))
        .willReturn(true);
  }


  @Test
  public void testGetPlanByPatientId() throws Exception {

    PlanResponse response = new PlanResponse();
    List<PlanResponse.OnePlan> onePlans = new ArrayList<>();
    PlanResponse.OnePlan onePlan = new OnePlan();
    onePlan.setName("阿司匹林");
    onePlan.setUnit("mg");
    onePlan.setId("8VJwg8");
    onePlan.setCommodityName("阿司匹林");
    onePlan.setStrength("25mg");
    List<PlanResponse.PlanInfo> planInfos = new ArrayList<>();
    PlanResponse.PlanInfo planInfo = new PlanResponse().new PlanInfo();
    planInfo.setDosage("888");
    planInfo.setId(HashUtils.encode(2222));
    planInfo.setTakeTime(Arrays.asList(1, 2, 3));
    planInfos.add(planInfo);
    onePlan.setPlan(planInfos);
    onePlans.add(onePlan);
    onePlan.setMarkId(HashUtils.encode(10000));
    response.setPlans(onePlans);

    given(masService.getPlanByPatientId(any(Long.class), any(PlanScope.class)))
        .willReturn(response);
    given(userService.checkBindRelation(any(Long.class), any(Long.class))).willReturn(true);
    this.contextMock.withUid(mockUserId);

    this.mockMvc
        .perform(MockMvcRequestBuilders.get("/plan")
            .param("patientId", "5Z7g7r")
            .param("scope", "ADJUST_MEDICINE")
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("get_plan",
            requestParameters(
                parameterWithName("patientId").description("患者id"),
                parameterWithName("scope").description("PATIENT_INDEX/患者首页, ADJUST_MEDICINE/调药")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("结果数据"),
                fieldWithPath("data.plans[].markId").description("标示id"),
                fieldWithPath("data.plans[].name").description("药品名"),
                fieldWithPath("data.plans[].unit").description("单位"),
                fieldWithPath("data.plans[].id").description("药品id"),
                fieldWithPath("data.plans[].commodityName").description("商品名"),
                fieldWithPath("data.plans[].strength").description("单位规格"),
                fieldWithPath("data.plans[].plan[].dosage").description("服药计量"),
                fieldWithPath("data.plans[].plan[].id").description("标示id"),
                fieldWithPath("data.plans[].plan[].takeTime[]").description("服药的时间点(hour)")
            )))
        .andDo(print())
        .andReturn();
  }

  @Test
  public void testGetRecordByPatientId() throws Exception {

    RecordListApiResponse response = new RecordListApiResponse();
    List<TimeStatus> timeStatuses = new ArrayList<>();
    TimeStatus timeStatus = new RecordListApiResponse().new TimeStatus();
    timeStatus.setStatus(1);
    timeStatus.setTime("12");
    timeStatuses.add(timeStatus);

    List<RecordListApiResponse.OneDosageRecord> oneDosageRecords = new ArrayList<>();
    RecordListApiResponse.OneDosageRecord oneDosageRecord =
        new RecordListApiResponse().new OneDosageRecord();
    oneDosageRecord.setTimeStatus(timeStatuses);
    oneDosageRecord.setDosage("20");
    oneDosageRecords.add(oneDosageRecord);

    List<RecordListApiResponse.OneMedicineRecord> oneMedicineRecords = new ArrayList<>();
    RecordListApiResponse.OneMedicineRecord oneMedicineRecord =
        new RecordListApiResponse().new OneMedicineRecord();
    oneMedicineRecord.setDosageUnit("mg");
    oneMedicineRecord.setMedicineName("阿司匹林");
    oneMedicineRecord.setOneDosageRecords(oneDosageRecords);
    oneMedicineRecords.add(oneMedicineRecord);

    List<RecordListApiResponse.OneDayRecord> oneDayRecords = new ArrayList<>();
    RecordListApiResponse.OneDayRecord oneDayRecord =
        new RecordListApiResponse().new OneDayRecord();
    oneDayRecord.setOneMedicineRecords(oneMedicineRecords);
    oneDayRecord.setDay("2017/08/15");
    oneDayRecord.setTakeMedicineStatus(2);
    oneDayRecords.add(oneDayRecord);
    response.setDayRecords(oneDayRecords);

    given(masService.getRecordsByPatientId(any(Long.class), any(Long.class))).willReturn(response);
    given(userService.checkBindRelation(any(Long.class), any(Long.class))).willReturn(true);

    this.contextMock.withUid(mockUserId);

    this.mockMvc
        .perform(MockMvcRequestBuilders.get("/record")
            .param("patientId", "5Z7g7r")
            .param("end", "1500630581595")
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.status", Matchers.equalTo("success")))
        .andDo(document("get_record",
            requestParameters(
                parameterWithName("patientId").description("患者id"),
                parameterWithName("end").description("所需服药记录截止日期时间戳").optional()),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("结果数据"),
                fieldWithPath("data.dayRecords[].day").description("服药日期"),
                fieldWithPath("data.dayRecords[].takeMedicineStatus")
                    .description("本日服药状态：0为未服药，1为已全服，2为已服部分"),
                fieldWithPath("data.dayRecords[].oneMedicineRecords[].medicineName").
                    description("药品名称"),
                fieldWithPath("data.dayRecords[].oneMedicineRecords[].dosageUnit").
                    description("服药计量单位"),
                fieldWithPath("data.dayRecords[].oneMedicineRecords[].oneDosageRecords[].dosage").
                    description("药品剂量"),
                fieldWithPath(
                    "data.dayRecords[].oneMedicineRecords[].oneDosageRecords[].timeStatus[].status")
                    .
                        description("服药状态：0为未操作,1为服药,2为未服药品"),
                fieldWithPath(
                    "data.dayRecords[].oneMedicineRecords[].oneDosageRecords[].timeStatus[].time").
                    description("服药时间(hour)")

            )))
        .andDo(print())
        .andReturn();
  }

  @Test
  public void testGetPatientTimeLine() throws Exception {
    VisRecordLineResponse mockResponse = new VisRecordLineResponse();
    List<VisRecordGroup> groups = new ArrayList<>();
    List<VisRecordItem> items = new ArrayList<>();
    int index = 0;
    while (index < 2) {
      VisRecordGroup group = new VisRecordGroup();
      group.setContent("123");
      group.setGid(String.valueOf(index));
      groups.add(group);
      index++;
    }
    while (index < 5) {
      VisRecordItem item = new VisRecordItem();
      item.setContent("#12D212");
      item.setDosage("12mg");
      item.setReminder(Arrays.asList(1, index));
      item.setGid("12");
      item.setStart(System.currentTimeMillis());
      item.setEnd(System.currentTimeMillis());
      items.add(item);
      index++;
    }
    mockResponse.setGroups(groups);
    mockResponse.setItems(items);
    given(masService.lineDataForFontEnd(any(Long.class), any(Long.class), any(Long.class)))
        .willReturn(mockResponse);

    this.mockMvc.perform(MockMvcRequestBuilders.get("/plan/timeline")
        .contentType(MediaType.APPLICATION_JSON)
        .param("patientId", "5Z7g7r")
        .param("start", String.valueOf(System.currentTimeMillis()))
        .param("end", String.valueOf(System.currentTimeMillis())))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("success")))
        .andDo(document("get_patient_record_timeline",
            requestParameters(
                parameterWithName("patientId").description("患者的id"),
                parameterWithName("start").description("时间轴需要的起始时间"),
                parameterWithName("end").description("时间轴需要的结束时间")
            ),
            responseFields(
                fieldWithPath("status").description("请求是否成功"),
                fieldWithPath("data").description("结果数据"),
                fieldWithPath("data.groups[]").description("时间轴组"),
                fieldWithPath("data.groups[].gid").description("组id"),
                fieldWithPath("data.groups[].content").description("药名"),
                fieldWithPath("data.items[]").description("时间轴item"),
                fieldWithPath("data.items[].gid").description("组id"),
                fieldWithPath("data.items[].content").description("目前方的是颜色"),
                fieldWithPath("data.items[].start").description("开始时间"),
                fieldWithPath("data.items[].end").description("结束时间"),
                fieldWithPath("data.items[].dosage").description("计量"),
                fieldWithPath("data.items[].reminder[]").description("提醒的时间段")
            )));

  }

}
