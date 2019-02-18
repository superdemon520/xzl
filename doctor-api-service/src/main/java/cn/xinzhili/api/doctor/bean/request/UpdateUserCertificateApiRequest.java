package cn.xinzhili.api.doctor.bean.request;

import cn.xinzhili.user.api.CertificateApi;
import java.util.List;
import java.util.Objects;

/**
 * @author by Loki on 17/8/15.
 */
public class UpdateUserCertificateApiRequest {

  private List<CertificateApi> certificates;

  public boolean invalid() {
    return Objects.isNull(getCertificates()) || getCertificates().isEmpty();
  }

  public List<CertificateApi> getCertificates() {
    return certificates;
  }

  public void setCertificates(List<CertificateApi> certificates) {
    this.certificates = certificates;
  }
}
