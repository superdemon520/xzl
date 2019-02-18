package cn.xinzhili.api.doctor.client;


import cn.xinzhili.api.doctor.bean.request.SendDoctorAdviceRequest;
import cn.xinzhili.api.doctor.bean.request.UpdateDoctorAdviceServiceRequest;
import cn.xinzhili.api.doctor.bean.request.UpdateMessageRequest;
import cn.xinzhili.api.doctor.bean.request.UpdateReadMessageRequest;
import cn.xinzhili.api.doctor.bean.response.AdviceLevelResponse;
import cn.xinzhili.api.doctor.bean.response.DoctorAdviceDataResponse;
import cn.xinzhili.api.doctor.bean.response.DoctorAdviceListResponse;
import cn.xinzhili.dpc.api.AddConsultationRequest;
import cn.xinzhili.dpc.api.UpdateConsultationRequest;
import cn.xinzhili.dpc.api.request.UpdateNotificationReadAtRequest;
import cn.xinzhili.dpc.api.request.UpdateNotificationStatusRequest;
import cn.xinzhili.dpc.api.response.StaffNotificationListResponse;
import cn.xinzhili.xutils.core.http.Response;
import java.util.List;
import java.util.Map;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Loki on 16/9/28.
 */
@FeignClient("dpc-service")
public interface DpcServiceClient {

  /**
   * get messages by sender and receive
   *
   * @return Response
   */
  @RequestMapping(method = RequestMethod.GET, value = "/messages")
  Response searchDoctorMessages(@RequestParam("sender") String sender,
      @RequestParam("receiver") String receiver, @RequestParam("page") long page,
      @RequestParam("perPage") long perPage);


  /**
   * add doctor message by sender,receiver and content
   */
  @RequestMapping(method = RequestMethod.POST, value = "/message", consumes = MediaType.APPLICATION_JSON_VALUE)
  Response addDoctorMessages(@RequestBody Map<String, Object> body);

  @RequestMapping(value = "/chat/senders", method = RequestMethod.GET)
  Response unreadChatSenders(@RequestParam(value = "receiver") String receiver);

  @RequestMapping(value = "/chat/senders", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  Response consumeChatSenders(@RequestBody Map<String, Object> body);

  @RequestMapping(value = "/advice", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  Response<DoctorAdviceDataResponse> sendDoctorAdvice(@RequestBody SendDoctorAdviceRequest body);

  @RequestMapping(value = "/advices", method = RequestMethod.GET)
  Response<DoctorAdviceListResponse> advices(@RequestParam(value = "patientId") long patientId,
      @RequestParam(value = "doctorId") long doctorId,
      @RequestParam(value = "assistantId") Long assistantId,
      @RequestParam(value = "status") List<Integer> status,
      @RequestParam(value = "category") List<Integer> category,
      @RequestParam(value = "orderByUpdateTime") boolean orderByUpdateTime,
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "perPage", defaultValue = "25") int perPage);

  @RequestMapping(value = "v1/advices", method = RequestMethod.GET)
  Response<DoctorAdviceListResponse> advicesByCategoryAndStatus(
      @RequestParam(value = "patientId") long patientId,
      @RequestParam(value = "doctorId") long doctorId,
      @RequestParam(value = "assistantId") Long assistantId,
      @RequestParam(value = "categoryStatus") List<String> categoryStatus,
      @RequestParam(value = "orderByUpdateTime") boolean orderByUpdateTime,
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "perPage", defaultValue = "25") int perPage);

  @RequestMapping(value = "/advice/{id}", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
  Response updateDoctorAdvice(@PathVariable(value = "id") long id,
      @RequestBody UpdateDoctorAdviceServiceRequest body);

  @RequestMapping(value = "/advice/assistant", method = RequestMethod.GET)
  Response unreadAdviceAssistant(@RequestParam(value = "doctorId") long doctorId);

  @RequestMapping(value = "/advice/assistant", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  Response consumeAdviceAssistant(@RequestBody Map<String, Object> body);

  @RequestMapping(value = "/advice/level", method = RequestMethod.GET)
  Response<AdviceLevelResponse> adviceLevel(@RequestParam("doctorId") long doctorId,
      @RequestParam("assistantId") long assistantId, @RequestParam("patientId") long patientId);


  @RequestMapping(value = "/chat/to/doctor", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
  Response commitChatToDoctor(@RequestBody UpdateMessageRequest request);

  @RequestMapping(value = "/doctor/read/chat", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
  Response changeChatToDoctorRead(@RequestBody UpdateReadMessageRequest request);

  @RequestMapping(method = RequestMethod.GET, value = "/doctor/chat")
  Response messageByDoctor(@RequestParam("sender") String sender,
      @RequestParam("receiver") String receiver, @RequestParam("page") long page,
      @RequestParam("perPage") long perPage);


  @GetMapping(value = "/consultation")
  Response getConsultation(@RequestParam("patientId") Long patientId,
      @RequestParam(value = "doctorId") Long doctorId,
      @RequestParam(value = "consultationDoctorId") Long consultationDoctorId,
      @RequestParam(value = "status") Integer status,
      @RequestParam(value = "page", defaultValue = "1") Integer page,
      @RequestParam(value = "perPage", defaultValue = "10") Integer perPage);

  @RequestMapping(value = "/consultation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  Response sendConsultation(@RequestBody AddConsultationRequest request);

  @RequestMapping(value = "/consultation", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
  Response updateConsultation(@RequestBody UpdateConsultationRequest request);

  @GetMapping(value = "/consultation/binding")
  Response getBindingByConsultationAndPatient(
      @RequestParam("consultationDoctorId") Long consultationDoctorId,
      @RequestParam("patientId") Long patientId);

  @GetMapping(value = "/staff/notification")
  Response<StaffNotificationListResponse> getNotificationList(
      @RequestParam("receiver") Long receiver,
      @RequestParam(value = "pageAt", defaultValue = "0") Integer pageAt,
      @RequestParam(value = "pageSize", defaultValue = "15") Integer pageSize);

  @RequestMapping(value = "/staff/notification/status",
      method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
  Response updateNotificationStatus(@RequestBody UpdateNotificationStatusRequest request);

  @RequestMapping(value = "/staff/notification/read",
      method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
  Response updateNotificationReadAt(@RequestBody UpdateNotificationReadAtRequest request);

  @GetMapping(value = "/statistics/department/advices")
  Response getDepartmentAdvices(@RequestParam("organizationId") Long organizationId,
      @RequestParam(value = "months", defaultValue = "1") int months);

}