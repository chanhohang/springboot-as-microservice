package net.rc.lab.springboot.importer.mapper;

import net.rc.lab.springboot.entities.CountryVo;

import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

@Component
public class CountryFieldSetMapper extends AbstractFieldSetMapper<CountryVo> {

  @Override
  public CountryVo mapFieldSet(FieldSet fieldSet) throws BindException {
    CountryVo country = new CountryVo();
    country.setNameEn(fieldSet.readString("Name English"));
    country.setNameTc(fieldSet.readString("Name Traditional Chinese"));
    country.setNameSc(fieldSet.readString("Name Simplified Chinese"));
    country.setFlagUri(fieldSet.readString("Flag URI"));
    return country;
  }

}
