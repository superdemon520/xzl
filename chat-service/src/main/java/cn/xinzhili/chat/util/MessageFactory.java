package cn.xinzhili.chat.util;

import cn.xinzhili.chat.api.MessageInfo;
import cn.xinzhili.chat.api.request.AddMessageRequest;
import cn.xinzhili.chat.bean.UserBean;
import cn.xinzhili.chat.model.Message;
import cn.xinzhili.user.api.response.PatientDetailResponse;
import java.sql.Timestamp;
import org.springframework.beans.BeanUtils;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/5/3 下午3:48
 */
public class MessageFactory {

  public static Message of(AddMessageRequest request) {
    Message message = new Message();
    BeanUtils.copyProperties(request, message);
    return message;
  }

  public static MessageInfo api(Message message, PatientDetailResponse response) {
    MessageInfo messageInfo = new MessageInfo();
    BeanUtils.copyProperties(message, messageInfo);
    messageInfo.setCreatedAt(Timestamp.valueOf(message.getCreatedAt()).getTime());
    UserBean user = UserFactory.getNameAndAvatar(messageInfo.getSenderRoleType(), response);
    messageInfo.setAvatar(user.getAvatar());
    messageInfo.setName(user.getName());
    return messageInfo;
  }

  public static MessageInfo api(Message message, UserBean user) {
    MessageInfo messageInfo = new MessageInfo();
    BeanUtils.copyProperties(message, messageInfo);
    messageInfo.setCreatedAt(Timestamp.valueOf(message.getCreatedAt()).getTime());
    messageInfo.setAvatar(user.getAvatar());
    messageInfo.setName(user.getName());
    return messageInfo;
  }
}
