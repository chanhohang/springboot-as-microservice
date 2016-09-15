package net.rc.lab.springboot.auth.security;

import net.rc.lab.springboot.auth.model.AuthenticateUserModel;
import net.rc.lab.springboot.core.repository.UserRepository;
import net.rc.lab.springboot.entities.UserVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component("CustomUserDetailService")
public class CustomUserDetailService implements UserDetailsService {

  @Autowired
  private UserRepository userService;

  private InMemoryUserDetailsManager memoryManager =
      new InMemoryUserDetailsManager(Collections.emptyList());

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    UserDetails result = null;
    if (memoryManager.userExists(username)) {
      result = memoryManager.loadUserByUsername(username);
    } else if (result == null) {
      Optional<UserVo> user = userService.findByLoginId(username);
      if (user.isPresent()) {
        AuthenticateUserModel newUser = new AuthenticateUserModel();
        newUser.setUserVo(user.get());
        result = newUser;
        memoryManager.createUser(result);
      }
    }
    return result;
  }

}
