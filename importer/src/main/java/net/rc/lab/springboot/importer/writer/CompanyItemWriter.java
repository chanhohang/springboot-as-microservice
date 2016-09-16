package net.rc.lab.springboot.importer.writer;

import net.rc.lab.springboot.core.repository.CompanyRepository;
import net.rc.lab.springboot.entities.CompanyVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CompanyItemWriter extends AbstractItemWriter<CompanyVo> {

  @Autowired
  private CompanyRepository companyRepository;

  @Override
  protected CompanyVo findExisting(CompanyVo item) {
    return companyRepository.findByNameEn(item.getNameEn()).orElse(null);
  }

  @Override
  protected void update(CompanyVo existing, CompanyVo item) {
    item.setCompanyId(existing.getCompanyId());
    companyRepository.save(item);
  }

  @Override
  protected void insert(CompanyVo item) {
    companyRepository.save(item);
  }

}
