package net.rc.lab.springboot.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import net.rc.lab.springboot.entities.RoleVo.RoleType;
import net.rc.lab.springboot.entities.common.SystemEntity;

import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Email;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "user")
@Data
@EqualsAndHashCode(callSuper = true)
@Audited
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVo extends SystemEntity {
  private static final long serialVersionUID = -2879554431458604240L;

  public static interface Columns {
    String USER_ID = "userId";
    String LOGIN_ID = "loginId";
    String PASSWORD_HASH = "password_hash";
    String PASSWORD_SALT = "password_salt";
    String ROLE = "role";
    String COMPANY = "company";
    String EMAIL = "email";
    String PHONE_NO = "phoneNo";
    String MOBILE_NO = "mobileNo";
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "UserId")
  private Long userId;

  @Column(name = "LoginId", length = 255, nullable = false)
  private String loginId;

  @JsonIgnore
  @Column(name = "Password_hash", length = 255, nullable = false)
  private String passwordHash;

  @JsonIgnore
  @Column(name = "Password_salt", length = 255, nullable = false)
  private String passwordSalt;

  @ManyToOne
  @JoinColumn(name = "RoleId", nullable = false)
  private RoleVo role;

  @ManyToOne
  @JoinColumn(name = "CompanyId", nullable = false)
  private CompanyVo company;

  @Column(name = "Email", length = 255)
  @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$")
  private String email;

  @Column(name = "PhoneNo", length = 255)
  @Pattern(regexp = "^\\d*$")
  private String phoneNo;

  @Column(name = "MobileNo", length = 255)
  @Pattern(regexp = "^\\d*$")
  private String mobileNo;

  @JsonIgnore
  public boolean isBuyer() {
    return getRole() != null && RoleType.Buyer.toString().equals(getRole().getName());
  }

  @JsonIgnore
  public boolean isSeller() {
    return getRole() != null && RoleType.Seller.toString().equals(getRole().getName());
  }

}
