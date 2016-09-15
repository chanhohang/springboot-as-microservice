package net.rc.lab.springboot.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import net.rc.lab.springboot.entities.CompanyVo;
import net.rc.lab.springboot.entities.RoleVo;
import net.rc.lab.springboot.entities.UserVo;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@ToString
@EqualsAndHashCode
public class AuthenticateUserModel implements Serializable, Principal, UserDetails {

  private static final long serialVersionUID = 1L;

  private UserVo user = null;

  private final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

  @Override
  public Collection<GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  @JsonIgnore
  public String getPassword() {
    return user != null ? user.getPasswordHash() : null;
  }

  @Override
  @JsonIgnore
  public String getUsername() {
    return user != null ? user.getLoginId() : null;
  }

  @Override
  public boolean isAccountNonExpired() {
    // TODO stored in DB.
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    // TODO stored in DB.
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    // TODO stored in DB.
    return true;
  }

  @Override
  public boolean isEnabled() {
    // TODO stored in DB.
    return true;
  }

  @JsonIgnore
  public UserVo getUserVo() {
    return user;
  }
  
  public CompanyVo getCompanyVo() {
    return user.getCompany();
  }

  /**
   * Set UserVo and populate the User Authorities at the same time.
   * 
   * @param user
   *          UserVo Object.
   */
  public void setUserVo(UserVo user) {
    this.user = user;
    setUserAuthorities();
  }

  private void setUserAuthorities() {
    if (getUserVo() != null && getUserVo().getRole() != null) {
      RoleVo role = getUserVo().getRole();
      getAuthorities().add(new SimpleGrantedAuthority(role.getName()));
    }
  }

  public void revoke() {
    setUserVo(null);
    getAuthorities().clear();
  }

  @Override
  public String getName() {
    return this.getUsername();
  }
  
}
