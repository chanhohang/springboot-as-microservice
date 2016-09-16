package net.rc.lab.springboot.importer.writer;

import net.rc.lab.springboot.core.repository.UserRepository;
import net.rc.lab.springboot.entities.UserVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserItemWriter extends AbstractItemWriter<UserVo> {

  @Autowired
  private UserRepository userRepository;

  @Override
  protected UserVo findExisting(UserVo item) {
    return userRepository.findByLoginId(item.getLoginId()).orElse(null);
  }

  @Override
  protected void update(UserVo existing, UserVo item) {
    item.setUserId(existing.getUserId());
    userRepository.save(item);
  }

  @Override
  protected void insert(UserVo item) {
    userRepository.save(item);
  }

}