package cn.xinzhili.chat.service;

import cn.xinzhili.chat.api.MessageType;
import cn.xinzhili.chat.api.RoleType;
import cn.xinzhili.chat.api.SessionInfo;
import cn.xinzhili.chat.api.Type;
import cn.xinzhili.chat.api.request.AddMessageRequest;
import cn.xinzhili.chat.api.response.SessionResponse;
import cn.xinzhili.chat.bean.DpcMessageResponse;
import cn.xinzhili.chat.client.DpcServiceClient;
import cn.xinzhili.chat.client.UserServiceClient;
import cn.xinzhili.dpc.api.DpcMessageType;
import cn.xinzhili.user.api.PatientInfo;
import cn.xinzhili.user.api.response.PatientListResponse;
import cn.xinzhili.user.api.response.StaffDetailResponse;
import cn.xinzhili.xutils.core.FailureException;
import cn.xinzhili.xutils.core.http.Response;
import java.util.List;
import java.util.Objects;
import java.util.stream.LongStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InitService {

  @Autowired
  private UserServiceClient userServiceClient;
  @Autowired
  private DpcServiceClient dpcServiceClient;
  @Autowired
  private SessionService sessionService;
  @Autowired
  private MessageService messageService;

  public void initData() {
    int pageSize = 10;
    long pageTotal = getPageTotal(pageSize);
    LongStream.range(0, pageTotal)
        .forEach(p -> userServiceClient.searchList(p, pageSize).getDataAs(PatientListResponse.class)
            .getPatients().stream().map(PatientInfo::getId).forEach(this::initGroupData)
        );
  }

  private void initGroupData(Long id) {
    SessionResponse sessionByInitiator = sessionService
        .getSessionByInitiator(id, RoleType.PATIENT, null);
    sessionByInitiator.getSession().stream()
        .filter(sessionInfo -> sessionInfo.getType().equals(Type.GROUP_ALL))
        .forEach(this::insertDataToMessage);
  }

  private void insertDataToMessage(SessionInfo sessionInfo) {
    for (int i = 0; ; i++) {
      Response<DpcMessageResponse> dpcResponse = dpcServiceClient
          .getChatList(null, "p_" + sessionInfo.getInitiatorId(), List.of(DpcMessageType.CHAT), i,
              25);
      if (dpcResponse.isSuccessful()) {
        DpcMessageResponse data = dpcResponse.getDataAs(DpcMessageResponse.class);
        if (!data.getMessages().isEmpty()) {
          data.getMessages().forEach(ms -> {
            AddMessageRequest request = new AddMessageRequest();
            request.setContent(ms.getContent().getData().getContent());
            request.setMessageType(MessageType.TEXT);
            request.setReceiverId(null);
            request.setSenderId(Long.valueOf(ms.getSender().split("_")[1]));
            request.setSessionId(sessionInfo.getId());
            if (ms.getSender().startsWith("p")) {
              request.setSenderRoleType(RoleType.PATIENT);
            } else if (ms.getSender().startsWith("d")) {
              Response<StaffDetailResponse> staffByUserId = userServiceClient
                  .findStaffByUserId(Long.valueOf(ms.getSender().split("_")[1]), false,
                      sessionInfo.getOrganizationId());
              if (staffByUserId.isSuccessful()) {
                StaffDetailResponse dataAs = staffByUserId.getDataAs(StaffDetailResponse.class);
                String code = dataAs.getUser().getRoles().isEmpty() ? null
                    : dataAs.getUser().getRoles().get(0).getCode();
                request.setSenderRoleType(
                    Objects.isNull(code) ? RoleType.DOCTOR : RoleType.valueOf(code));
              } else {
                request.setSenderRoleType(RoleType.OPERATOR);
              }
            }
            messageService.addMessage(request);
          });
          if (data.getMessages().size() < 25) {
            return;
          }
        } else {
          return;
        }
      }
    }

  }

  /**
   * 获取用户总页数
   */
  private long getPageTotal(int pageSize) {
    PatientListResponse response = userServiceClient.searchList(0L, pageSize)
        .getDataAs(PatientListResponse.class);
    if (response.getPatients().isEmpty()) {
      throw new FailureException("获取用户列表失败");
    }
    return (long) Math.ceil(response.getTotal() / (double) pageSize);
  }
}
