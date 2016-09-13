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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "company")
@Data
@EqualsAndHashCode(callSuper = true)
@Audited
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyVo extends SystemEntity {

  private static final long serialVersionUID = 6458578790969002485L;

  public static interface Columns {
    String NAME_EN = "nameEn";
    String NAME_TC = "nameTc";
    String NAME_SC = "nameSc";
    String COUNTRY = "country";
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "CompanyId")
  private Long companyId;

  @Column(name = "Name_en", length = 255, nullable = false)
  @NotBlank
  private String nameEn;

  @Column(name = "Name_tc", length = 255, nullable = false)
  private String nameTc;

  @Column(name = "Name_sc", length = 255, nullable = false)
  private String nameSc;

  @ManyToOne
  @JoinColumn(name = "CountryId", nullable = false)
  private CountryVo country;

}
