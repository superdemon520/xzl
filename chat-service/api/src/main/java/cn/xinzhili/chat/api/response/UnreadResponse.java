package cn.xinzhili.chat.api.response;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author dkw[dongkewei@xinzhili.cn]
 * @data 2018/5/7 下午4:34
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnreadResponse {

  @ApiModelProperty("患者所在聊天组未读数")
  private int patientUnreadCount;
  @ApiModelProperty("医生或医助或运营的其他两人聊天未读数")
  private int otherUnreadCount;
  @ApiModelProperty("未读数列表")
  private List<MedicalUnreadCount> medicalUnreadCounts;

  @Data
  public static class MedicalUnreadCount {

    @ApiModelProperty("收信人id")
    private Long receiverId;
    @ApiModelProperty("发送人id")
    private Long senderId;
    @ApiModelProperty("未读数")
    private Integer unreadCount;
  }
}
