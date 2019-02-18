package cn.xinzhili.infra;

import cn.xinzhili.xutils.auth.UidToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Date: 16/7/19 Time: AM11:59
 *
 * @author gan
 */
public class DoctorUsernamePasswordAuthenticationToken extends
    UsernamePasswordAuthenticationToken implements UidToken {

  private long uid;

  public DoctorUsernamePasswordAuthenticationToken(Object principal,
      Object credentials) {
    super(principal, credentials);
  }

  public DoctorUsernamePasswordAuthenticationToken(Object principal,
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
