package cn.xinzhili.infra;

import cn.xinzhili.xutils.auth.UidToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Date: 16/7/19 Time: AM11:52
 *
 * @author gan
 */
public class PatientUsernamePasswordAuthenticationToken extends
    UsernamePasswordAuthenticationToken implements UidToken {

  private long uid;

  public PatientUsernamePasswordAuthenticationToken(Object principal,
      Object credentials) {
    super(principal, credentials);
  }

  public PatientUsernamePasswordAuthenticationToken(Object principal,
      Object credentials,
      Collection<? extends GrantedAuthority> authorities) {
    super(principal, credentials, authorities);
  }

  @Override
  public long getUid() {
    return uid;
  }

  public void setUid(long uid) {
    this.uid = uid;
  }
}
