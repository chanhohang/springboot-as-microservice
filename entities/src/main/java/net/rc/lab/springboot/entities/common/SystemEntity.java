package net.rc.lab.springboot.entities.common;

import lombok.Data;

import org.hibernate.envers.Audited;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

@MappedSuperclass
@Data
@Audited
public class SystemEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  public static interface Columns {
    String VERSION = "version";
  }

  @Version
  private Integer version;

  @Column(name = "UpdateTimestamp")
  @NotNull
  private Long updateTimestamp;

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  @PrePersist
  public void setLastUpdateTimestamp() {
    setUpdateTimestamp(System.currentTimeMillis());
  }
}
