package net.rc.lab.springboot.importer.mapper;

import net.rc.lab.springboot.entities.RoleVo;

import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

@Component
public class RoleFieldSetMapper extends AbstractFieldSetMapper<RoleVo> {

  @Override
  public RoleVo mapFieldSet(FieldSet fieldSet) throws BindException {
    RoleVo role = new RoleVo();
    role.setDescriptionEn(fieldSet.readString("Description English"));
    role.setDescriptionTc(fieldSet.readString("Description Traditional Chinese"));
    role.setDescriptionSc(fieldSet.readString("Description Simplified Chinese"));
    role.setName(fieldSet.readString("Name"));
    return role;
  }

}
