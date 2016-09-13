package net.rc.lab.springboot.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import net.rc.lab.springboot.entities.common.SystemEntity;

import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "role")
@Data
@EqualsAndHashCode(callSuper = true)
@Audited
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleVo extends SystemEntity {

  private static final long serialVersionUID = 9189979467979637577L;

  public enum RoleType {
    Buyer, Seller
  }

  public static interface Columns {
    String NAME = "name";
    String DESCRIPTION_EN = "descriptionEn";
    String DESCRIPTION_TC = "descriptionTc";
    String DESCRIPTION_SC = "descriptionSc";
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "RoleId")
  private Long roleId;

  @Column(name = "Name", length = 255, nullable = false)
  @NotBlank
  private String name;

  @Column(name = "Description_en", length = 255)
  private String descriptionEn;

  @Column(name = "Description_tc", length = 255)
  private String descriptionTc;

  @Column(name = "Description_sc", length = 255)
  private String descriptionSc;

  public Long getRoleId() {
    return roleId;
  }

  public void setRoleId(Long roleId) {
    this.roleId = roleId;
  }

  public String getDescriptionEn() {
    return descriptionEn;
  }

  public void setDescriptionEn(String descriptionEn) {
    this.descriptionEn = descriptionEn;
  }

  public String getDescriptionTc() {
    return descriptionTc;
  }

  public void setDescriptionTc(String descriptionTc) {
    this.descriptionTc = descriptionTc;
  }

  public String getDescriptionSc() {
    return descriptionSc;
  }

  public void setDescriptionSc(String descriptionSc) {
    this.descriptionSc = descriptionSc;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
