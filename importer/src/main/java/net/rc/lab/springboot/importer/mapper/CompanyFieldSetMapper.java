package net.rc.lab.springboot.importer.mapper;

import net.rc.lab.springboot.core.repository.CountryRepository;
import net.rc.lab.springboot.entities.CompanyVo;
import net.rc.lab.springboot.entities.CountryVo;

import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

import java.util.Optional;

@Component
public class CompanyFieldSetMapper extends AbstractFieldSetMapper<CompanyVo> {

  @Autowired
  private CountryRepository countryRepository;

  @Override
  public CompanyVo mapFieldSet(FieldSet fieldSet) throws BindException {
    CompanyVo company = new CompanyVo();
    company.setNameEn(fieldSet.readString("Name English"));
    company.setNameTc(fieldSet.readString("Name Traditional Chinese"));
    company.setNameSc(fieldSet.readString("Name Simplified Chinese"));
    Optional<CountryVo> country = countryRepository.findByNameEn(fieldSet.readString("Country"));
    if (country.isPresent()) {
      company.setCountry(country.get());
    }
    return company;
  }

}
