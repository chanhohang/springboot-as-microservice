package net.rc.lab.springboot.importer.writer;

import net.rc.lab.springboot.core.repository.RoleRepository;
import net.rc.lab.springboot.entities.RoleVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleItemWriter extends AbstractItemWriter<RoleVo> {

  @Autowired
  private RoleRepository roleRepository;

  @Override
  protected RoleVo findExisting(RoleVo item) {
    return roleRepository.findByName(item.getName()).orElse(null);
  }

  @Override
  protected void update(RoleVo existing, RoleVo item) {
    item.setRoleId(existing.getRoleId());
    roleRepository.save(item);
  }

  @Override
  protected void insert(RoleVo item) {
    roleRepository.save(item);
  }

}
