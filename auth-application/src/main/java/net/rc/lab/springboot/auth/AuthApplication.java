package net.rc.lab.springboot.auth;

import net.rc.lab.springboot.auth.security.CustomInMemoryAuthorizationCodeServices;
import net.rc.lab.springboot.auth.security.CustomOAuthClientDetailService;
import net.rc.lab.springboot.core.repository.UserRepository;
import net.rc.lab.springboot.entities.UserVo;
import net.rc.lab.springboot.entities.dto.UserDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.ApprovalStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.approval.InMemoryApprovalStore;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.security.Principal;

/**
 * The Auth service for springboot.
 */
@ComponentScan(basePackages = "net.rc.lab.springboot")
@SpringBootApplication
@SessionAttributes("authorizationRequest")
@EnableJpaRepositories(basePackages = "net.rc.lab.springboot.core.repository")
@RestController
@EnableAuthorizationServer
@EnableOAuth2Client
public class AuthApplication {

  public static void main(String[] args) {
    SpringApplication.run(AuthApplication.class, args);
  }

  @Autowired
  private UserRepository userRepository;
  
  /**
   * Return a specific User object
   * @param user autowired the existing User from SecurityContext.
   * @return see {@link UserDto}.
   */
  @RequestMapping("/user")
  @ResponseBody
  public Principal user(Principal user) {
    if (! (user instanceof UserDto)) {
      String username = user.getName();
      UserDto systemUser = new UserDto();
      UserVo userVo = userRepository.findByLoginId(username).get();
      systemUser.setUser(userVo);
      systemUser.setCompany(userVo.getCompany());
      systemUser.setUserId(userVo.getUserId());
      systemUser.setLoginId(userVo.getLoginId());
      user = systemUser;
    }
    return user;
  }

  @Bean
  public TokenStore tokenStore() {
    return new InMemoryTokenStore();
  }

  @Bean
  public CustomInMemoryAuthorizationCodeServices authorizationCodeServices() {
    return new CustomInMemoryAuthorizationCodeServices();
  }

  /**
   * Create ApprovalStoreHandler.
   * @param store see {@link ApprovalStore}
   * @param clientDetailsService see {@link ClientDetailsService}
   * @return {@link ApprovalStoreUserApprovalHandler}
   */
  @Bean
  public ApprovalStoreUserApprovalHandler userApprovalHandler(ApprovalStore store,
      CustomOAuthClientDetailService clientDetailsService) {
    ApprovalStoreUserApprovalHandler handler = new ApprovalStoreUserApprovalHandler();
    handler.setApprovalStore(store);
    handler.setRequestFactory(new DefaultOAuth2RequestFactory(clientDetailsService));
    handler.setClientDetailsService(clientDetailsService);
    return handler;
  }

  @Bean
  public ApprovalStore approvalStore() {
    ApprovalStore store = new InMemoryApprovalStore();
    return store;
  }

}
