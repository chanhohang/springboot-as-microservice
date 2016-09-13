package net.rc.lab.springboot.entities.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import net.rc.lab.springboot.entities.CompanyVo;
import net.rc.lab.springboot.entities.UserVo;

import java.io.Serializable;
import java.security.Principal;

@Data
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable, Principal {

  private static final long serialVersionUID = 1L;

  private CompanyVo company;
  
  private UserVo user;
  
  private Long userId;
  
  private String loginId;

  @Override
  public String getName() {
    return user != null ? user.getLoginId() : null;
  }
  
  @JsonIgnore
  public UserVo getUser() {
    return user;
  }
  
}

