package net.rc.lab.springboot.test.helper;

import net.rc.lab.springboot.entities.RoleVo;
import net.rc.lab.springboot.entities.dto.UserDto;

import org.assertj.core.util.Lists;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class TestUserDetails extends UserDto implements UserDetails {

  private static final long serialVersionUID = 1L;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    if (getUser() != null && getUser().getRole() != null) {
      RoleVo role = getUser().getRole();
      return Lists.newArrayList(new SimpleGrantedAuthority(role.getName()));
    }
    return Lists.emptyList();
  }

  @Override
  public String getPassword() {
    return "N/A";
  }

  @Override
  public String getUsername() {
    return getName();
  }

  @Override
  public boolean isAccountNonExpired() {
    return false;
  }

  @Override
  public boolean isAccountNonLocked() {
    return false;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return false;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

}
