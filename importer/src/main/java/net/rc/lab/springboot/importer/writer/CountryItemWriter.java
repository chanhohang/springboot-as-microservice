package net.rc.lab.springboot.importer.writer;

import net.rc.lab.springboot.core.repository.CountryRepository;
import net.rc.lab.springboot.entities.CountryVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CountryItemWriter extends AbstractItemWriter<CountryVo> {

  @Autowired
  private CountryRepository countryRepository;

  @Override
  protected CountryVo findExisting(CountryVo item) {
    return countryRepository.findByNameEn(item.getNameEn()).orElse(null);
  }

  @Override
  protected void update(CountryVo existing, CountryVo item) {
    item.setCountryId(existing.getCountryId());
    countryRepository.save(item);
  }

  @Override
  protected void insert(CountryVo item) {
    countryRepository.save(item);
  }

}
