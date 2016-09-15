package net.rc.lab.springboot.auth.security;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;

public class CustomInMemoryAuthorizationCodeServices extends InMemoryAuthorizationCodeServices {

  public OAuth2Authentication get(String code) {
    OAuth2Authentication auth = this.authorizationCodeStore.get(code);
    return auth;
  }
}
