package net.rc.lab.springboot.core.repository;

import net.rc.lab.springboot.entities.CompanyVo;
import net.rc.lab.springboot.entities.UserVo;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<UserVo, Long> {

  Optional<UserVo> findByLoginId(String loginId);
  
  Optional<UserVo> findByCompanyAndUserId(CompanyVo company, Long userId);
}
