package net.rc.lab.springboot.core.repository;

import net.rc.lab.springboot.entities.CompanyVo;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface CompanyRepository extends PagingAndSortingRepository<CompanyVo, Long> {
  
  Optional<CompanyVo> findByNameEn(String nameEn);
}
