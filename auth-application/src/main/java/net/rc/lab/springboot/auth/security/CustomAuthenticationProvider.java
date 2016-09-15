package net.rc.lab.springboot.auth.security;

import net.rc.lab.springboot.auth.model.AuthenticateUserModel;
import net.rc.lab.springboot.core.repository.UserRepository;
import net.rc.lab.springboot.entities.UserVo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

import javax.transaction.Transactional;

@Service("CustomAuthenticationProvider")
public class CustomAuthenticationProvider implements AuthenticationProvider {

  private static Logger logger = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

  @Autowired
  private UserRepository userService;

  @Autowired
  private AuthenticateUserModel loggerInUser;

  @Override
  @Transactional(dontRollbackOn = BadCredentialsException.class)
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    AbstractAuthenticationToken auth = (AbstractAuthenticationToken) authentication;
    String username = String.valueOf(auth.getPrincipal());
    String password = String.valueOf(auth.getCredentials());
    logger.info("authenticate user {} from ip-address {}", username, auth.getDetails());

    Optional<UserVo> user = userService.findByLoginId(username);

    if (!user.isPresent()) {
      logger.info("user {} does not exist.", username);
      throw new BadCredentialsException("login_username_error");
    } else {
      String validatePassword = BCrypt.hashpw(password, user.get().getPasswordSalt());
      if (!validatePassword.equals(user.get().getPasswordHash())) {
        logger.info("user {} is having incorrect password input.", username);
        throw new BadCredentialsException("login_password_error");
      }
    }

    loggerInUser.setUserVo(user.get());

    AuthenticateUserModel newUser = new AuthenticateUserModel();
    newUser.setUserVo(user.get());
    return new UsernamePasswordAuthenticationToken(newUser, null, newUser.getAuthorities());

  }

  @Override
  public boolean supports(Class<?> authentication) {
    return (AbstractAuthenticationToken.class.isAssignableFrom(authentication));
  }

}
