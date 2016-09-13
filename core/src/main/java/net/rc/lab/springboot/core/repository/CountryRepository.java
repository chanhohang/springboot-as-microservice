package net.rc.lab.springboot.core.repository;

import net.rc.lab.springboot.entities.CountryVo;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface CountryRepository extends PagingAndSortingRepository<CountryVo, Long> {

  Optional<CountryVo> findByNameEn(String nameEn);
}
