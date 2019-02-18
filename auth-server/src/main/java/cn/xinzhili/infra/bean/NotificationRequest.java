package cn.xinzhili.infra.bean;

import java.util.List;
import java.util.Map;

/**
 * Date: 06/03/2017
 * Time: 4:00 PM
 *
 * @author Gan Dong
 */
public class NotificationRequest {
  private String title;

  private String alert;

  private Map<String, Object> content;

  private List<String> receivers;

  private String deviceToken;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAlert() {
    return alert;
  }

  public void setAlert(String alert) {
    this.alert = alert;
  }

  public Map<String, Object> getContent() {
    return content;
  }

  public void setContent(Map<String, Object> content) {
    this.content = content;
  }

  public List<String> getReceivers() {
    return receivers;
  }

  public void setReceivers(List<String> receivers) {
    this.receivers = receivers;
  }

  public String getDeviceToken() {
    return deviceToken;
  }

  public void setDeviceToken(String deviceToken) {
    this.deviceToken = deviceToken;
  }
}
