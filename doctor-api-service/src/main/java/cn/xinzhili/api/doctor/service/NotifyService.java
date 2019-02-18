package cn.xinzhili.api.doctor.service;

import cn.xinzhili.api.doctor.bean.InquiryNotifyContentData;
import cn.xinzhili.api.doctor.bean.NoticeContentType;
import cn.xinzhili.api.doctor.bean.NoticeType;
import cn.xinzhili.api.doctor.bean.NotifyContent;
import cn.xinzhili.api.doctor.bean.NotifyContentData;
import cn.xinzhili.api.doctor.bean.response.NotifyResponse;
import cn.xinzhili.api.doctor.client.NotifyServiceClient;
import cn.xinzhili.api.doctor.config.ApplicationConfiguration;
import cn.xinzhili.api.doctor.error.NotifyErrorCode;
import cn.xinzhili.xutils.core.ErrorCode;
import cn.xinzhili.xutils.core.FailureException;
import cn.xinzhili.xutils.core.SystemException;
import cn.xinzhili.xutils.core.http.Response;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by @xin.
 */
@Service
public class NotifyService {

  @Autowired
  private NotifyServiceClient notifyServiceClient;
  @Autowired
  private ApplicationConfiguration acf;

  private static final String NOTIFY_RECEIVER_PREFIX = "p";

  private Response pushNotify(String chat, String alert, int chatType, Long... receivers) {
    NotifyContentData data = NotifyContentData.builder().type(NoticeContentType.TEXT.getCode())
        .content(chat)
        .createdAt(new Date().getTime()).build();
    NotifyContent content = NotifyContent.builder().data(data).type(chatType)
        .build();
    NotifyResponse body = NotifyResponse.builder().title(NoticeType.NOTIFY_TITLE).alert(alert)
        .receivers(
            Arrays.stream(receivers).map(s -> NOTIFY_RECEIVER_PREFIX + s)
                .collect(Collectors.toList()))
        .content(content).build();

    return notifyServiceClient.pushNotify(body);
  }

  public void pushImageBlurringNotify(String chat, Long... receivers) {
    pushNotify(chat, NoticeType.getNoticeAlert(NoticeType.IMAGE_BLURRING),
        NoticeType.IMAGE_BLURRING.getCode(), receivers);
  }

  public Response pushAdviceNotify(String chat, Long... receivers) {
    return pushNotify(chat, NoticeType.getNoticeAlert(NoticeType.ADVICE),
        NoticeType.ADVICE.getCode(), receivers);
  }

  public Response pushMessageNotify(String chat, Long... receivers) {
    return pushNotify(chat, NoticeType.getNoticeAlert(NoticeType.MESSAGE),
        NoticeType.MESSAGE.getCode(), receivers);
  }

  public void pushInquiries(List<Long> receivers) {

    InquiryNotifyContentData contentData = new InquiryNotifyContentData();
    contentData.setType(NoticeContentType.LINK.getCode());
    contentData.setContent(acf.getInquiryPageUrl());
    contentData.setCreatedAt(new Date().getTime());
    contentData.setDescription(acf.getInquiryDescription());
    contentData.setTitle(acf.getInquiryTitle());
    contentData.setBannerUrl(acf.getInquiryBannerUrl());
    NotifyContent content = NotifyContent.builder().data(contentData)
        .type(NoticeType.INQUIRY.getCode())
        .build();
    NotifyResponse body = NotifyResponse.builder().title(acf.getInquiryTitle())
        .alert(acf.getInquiryAlert())
        .receivers(
            receivers.stream().map(s -> NOTIFY_RECEIVER_PREFIX + s).collect(Collectors.toList()))
        .content(content).build();
    notifyServiceClient.pushNotify(body);
  }

  /**
   * 发送验证码
   *
   * @param phone 手机号
   */
  public void sendVcode(String phone) {
    Response response = notifyServiceClient.requestVcode(phone);
    if (response.isFailed()) {
      Integer failCode = response.getFailureCode();
      if (2 == failCode) {
        throw new FailureException(NotifyErrorCode.SMS_EXCEED_RATE_LIMIT);
      } else {
        throw new FailureException(NotifyErrorCode.SMS_SERVER_ERROR);
      }
    }
    if (response.isError()) {
      throw new SystemException(ErrorCode.SERVER_ERROR);
    }
  }
}
