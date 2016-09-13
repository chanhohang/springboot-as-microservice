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
@Table(name = "country")
@Data
@EqualsAndHashCode(callSuper = true)
@Audited
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountryVo extends SystemEntity {

  private static final long serialVersionUID = 658053079184216964L;

  /**
   * Hibernate Column names to be used in Criteria Searching.
   */
  public static interface Columns {
    String NAME_EN = "nameEn";
    String NAME_TC = "nameTc";
    String NAME_SC = "nameSc";
    String FLAG_URI = "flagURI";
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "CountryId")
  private Long countryId;

  @Column(name = "Name_en", length = 255, unique = true, nullable = false)
  @NotBlank
  private String nameEn;

  @Column(name = "Name_tc", length = 255, nullable = false)
  private String nameTc;

  @Column(name = "Name_sc", length = 255, nullable = false)
  private String nameSc;

  @Column(name = "Flag_uri", length = 255)
  private String flagUri;

}
