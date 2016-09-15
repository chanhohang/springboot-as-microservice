package net.rc.lab.springboot.auth.security;

import net.rc.lab.springboot.auth.model.AuthenticateUserModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CustomLogoutHandler implements LogoutHandler {

  @Autowired
  private AuthenticateUserModel loggedInUser;

  private Logger log = LoggerFactory.getLogger(CustomLogoutHandler.class);

  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {
    log.info("{} is logout.", loggedInUser != null ? loggedInUser.getUsername() : "null");
    if (loggedInUser != null) {
      loggedInUser.revoke();
    }
  }

}
