package net.rc.lab.springboot.entities.common;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

import net.rc.lab.springboot.entities.UserVo;

import org.hibernate.envers.Audited;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper = true)
@Audited
public class BusinessEntity extends SystemEntity {

  private static final long serialVersionUID = 1L;
  public static final String DATE_FORMAT = "yyyy/mm/dd";

  public static interface Columns {
    String UPDATE_USER = "updateUser";
    String UPDATE_TIMESTAMP = "updateTimestamp";
  }

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "UpdateUserId")
  private UserVo updateUser;

}
