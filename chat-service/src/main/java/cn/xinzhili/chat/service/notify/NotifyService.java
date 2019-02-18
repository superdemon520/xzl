package cn.xinzhili.chat.service.notify;

import cn.xinzhili.chat.api.request.AddMessageRequest;
import cn.xinzhili.chat.bean.notify.NoticeContentType;
import cn.xinzhili.chat.bean.notify.NoticeType;
import cn.xinzhili.chat.bean.notify.NotifyContent;
import cn.xinzhili.chat.bean.notify.NotifyContentData;
import cn.xinzhili.chat.bean.request.NotifyRequest;
import cn.xinzhili.chat.client.NotifyServiceClient;
import cn.xinzhili.chat.service.RedisBasicService;
import cn.xinzhili.chat.service.RedisService;
import cn.xinzhili.chat.util.RedisKeyFactory;
import cn.xinzhili.xutils.core.http.Response;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotifyService {

  @Autowired
  private NotifyServiceClient notifyServiceClient;
  @Autowired
  private RedisService redisService;

  @Autowired
  private RedisBasicService redisBasicService;

  public void sendNotifyMessage(AddMessageRequest request) {
    NotifyContentData data = new NotifyContentData();
    data.setContent(request.getContent());
    data.setCreatedAt(System.currentTimeMillis());
    data.setType(NoticeContentType.TEXT.getCode());
    NotifyContent content = new NotifyContent();
    content.setData(data);
    content.setType(NoticeType.MESSAGE.getCode());
    NotifyRequest notifyRequest = new NotifyRequest();
    notifyRequest.setAlert(NoticeType.getNoticeAlert(NoticeType.MESSAGE));
    notifyRequest.setContent(content);
    notifyRequest.setTitle(NoticeType.NOTIFY_TITLE);
    String initiatorId = "p" + redisBasicService
        .getString(RedisKeyFactory.getInitiatorKey(request.getSessionId()));
    notifyRequest.setReceivers(List.of(initiatorId));
    Response response = notifyServiceClient.pushNotify(notifyRequest);
    if (response.isSuccessful()) {
      log.info("message has been notified to {}", initiatorId);
    } else {
      log.warn("message notified failed");
    }
  }

}
