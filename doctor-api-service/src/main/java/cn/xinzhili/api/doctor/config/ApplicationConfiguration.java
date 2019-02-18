package cn.xinzhili.api.doctor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * Application Scope specific configurations <p> Date: 08/10/2016 Time: 11:39 AM
 *
 * @author Gan Dong
 */
@Configuration
@RefreshScope
public class ApplicationConfiguration {

  @Value("${dpc.message.perpage}")
  private Long messagePerPage;

  @Value("${dpc.message.image.review.content}")
  private String imageReviewSystemMessageContent;

  @Value("${inquiry.title}")
  private String inquiryTitle;

  @Value("${inquiry.alert}")
  private String inquiryAlert;

  @Value("${inquiry.description}")
  private String inquiryDescription;

  @Value("${inquiry.page.url}")
  private String inquiryPageUrl;

  @Value("${inquiry.page.title}")
  private String inquiryPageTitle;

  @Value("${inquiry.banner.url}")
  private String inquiryBannerUrl;

  public Long getMessagePerPage() {
    return messagePerPage;
  }

  public String getImageReviewSystemMessageContent() {
    return imageReviewSystemMessageContent;
  }

  public String getInquiryTitle() {
    return inquiryTitle;
  }

  public String getInquiryAlert() {
    return inquiryAlert;
  }

  public String getInquiryDescription() {
    return inquiryDescription;
  }

  public String getInquiryPageUrl() {
    return inquiryPageUrl;
  }

  public String getInquiryPageTitle() {
    return inquiryPageTitle;
  }

  public String getInquiryBannerUrl() {
    return inquiryBannerUrl;
  }
}
