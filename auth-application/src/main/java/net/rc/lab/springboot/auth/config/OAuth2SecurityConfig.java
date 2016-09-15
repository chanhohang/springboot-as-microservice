package net.rc.lab.springboot.auth.config;

import net.rc.lab.springboot.auth.filter.OAuthTokenFilter;
import net.rc.lab.springboot.auth.security.CustomLogoutHandler;
import net.rc.lab.springboot.auth.security.CustomOAuthClientDetailService;
import net.rc.lab.springboot.auth.security.CustomUserDetailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class OAuth2SecurityConfig {

  @Configuration
  @Order(-20)
  protected static class LoginConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("CustomAuthenticationProvider")
    private AuthenticationProvider authenticationProvider;

    @Autowired
    @Qualifier("CustomUserDetailService")
    private CustomUserDetailService userDetailsService;

    @Autowired
    private CustomLogoutHandler logoutHandler;

    @Autowired
    private OAuthTokenFilter oauthRefreshTokenAuthorization;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      // @formatter:off
      http.authorizeRequests()
          .antMatchers("/login", "/uaa/oauth/token")
          .permitAll()
        .and()
          .addFilterBefore(oauthRefreshTokenAuthorization, FilterSecurityInterceptor.class).csrf()
          .csrfTokenRepository(csrfTokenRepository()).disable().authorizeRequests().and()
          .formLogin().permitAll()
        .and()
          .logout()
          .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).invalidateHttpSession(true)
          .addLogoutHandler(logoutHandler).deleteCookies("JSESSIONID").logoutSuccessUrl("/")
          .permitAll()
        .and()
          .userDetailsService(userDetailsService);
           // @formatter:on
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
      // @formatter:off
      auth.authenticationProvider(authenticationProvider);
      // @formatter:on
    }

    @Override
    public UserDetailsService userDetailsServiceBean() {
      return userDetailsService;
    }

    private CsrfTokenRepository csrfTokenRepository() {
      HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
      repository.setHeaderName("X-XSRF-TOKEN");
      return repository;
    }

  }

  @Configuration
  protected static class AuthorizationServerConfiguration
      extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomOAuthClientDetailService clientDetailsService;

    @Autowired
    private ApprovalStoreUserApprovalHandler userApprovalHandler;

    @Autowired
    @Qualifier("CustomUserDetailService")
    private CustomUserDetailService userDetailsService;

    @Bean
    public ResourceServerTokenServices resourceServerTokenServices(
        CustomOAuthClientDetailService clientDetailsService, TokenStore tokenStore) {
      DefaultTokenServices tokenService = new DefaultTokenServices();
      tokenService.setAuthenticationManager(authenticationManager);
      tokenService.setClientDetailsService(clientDetailsService);
      tokenService.setTokenStore(tokenStore);
      return tokenService;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
      clients.withClientDetails(clientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
      endpoints
        // @formatter:off
        .tokenStore(tokenStore).userDetailsService(userDetailsService)
        .authorizationCodeServices(authorizationCodeServices)
        .authenticationManager(authenticationManager).userApprovalHandler(userApprovalHandler)
        .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
         // @formatter:on
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
      // @formatter:off
      oauthServer.tokenKeyAccess("isAnonymous() || hasAuthority('ROLE_TRUSTED_CLIENT')")
          .checkTokenAccess("hasAuthority('ROLE_TRUSTED_CLIENT')");
      ;
      // @formatter:on
      // oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()")

    }

  }

}
