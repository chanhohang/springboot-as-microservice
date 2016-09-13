package net.rc.lab.springboot.core.repository;

import net.rc.lab.springboot.entities.RoleVo;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface RoleRepository extends PagingAndSortingRepository<RoleVo, Long> {

  Optional<RoleVo> findByName(String name);
}
