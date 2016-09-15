package net.rc.lab.springboot.auth.filter;

import net.rc.lab.springboot.auth.security.CustomInMemoryAuthorizationCodeServices;
import net.rc.lab.springboot.auth.security.CustomOAuthClientDetailService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component("OAuthTokenFilter")
@Lazy
public class OAuthTokenFilter extends OncePerRequestFilter {

  private Logger log = LoggerFactory.getLogger(OAuthTokenFilter.class);

  private TokenStore tokenStore;

  @Autowired
  private CustomOAuthClientDetailService clientDetailsService;

  @Autowired
  private CustomInMemoryAuthorizationCodeServices authorizationCodeService;

  private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource =
      new WebAuthenticationDetailsSource();

  private AuthenticationManager authenticationManager;

  @Autowired
  public void setTokenStore(TokenStore tokenStore) {
    this.tokenStore = tokenStore;
  }

  @Autowired
  public void setAuthenticationManager(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String grantType = request.getParameter("grant_type");
    Authentication authentication = null;
    if (grantType != null) {
      switch (grantType) {
        case "authorization_code": {
          String clientId = request.getParameter("client_id");

          if (clientId != null) {
            ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
            if (clientDetails != null) {
              String clientSecret = request.getParameter("client_secret");
              if (clientDetails.getClientSecret().equals(clientSecret)) {
                String code = request.getParameter("code");
                if (code != null) {
                  authentication = authorizationCodeService.get(code);
                  if (authentication != null) {
                    log.info("Obtain user {} from client_id {}.", authentication.getName(),
                        clientId);
                  }
                }

              } else {
                log.info("Incorrect client-id {} and client-secret {}", clientId, clientSecret);
              }
            }
          }
        }
          break;
        case "password": {
          String username = request.getParameter("username");
          String password = request.getParameter("password");
          if ((username != null) && (password != null)) {
            UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(username, password);
            authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
            Authentication authResult = authenticationManager.authenticate(authRequest);
            if (authResult != null) {
              SecurityContextHolder.getContext().setAuthentication(authResult);
            }
          }

        }
          break;
        case "refresh_token": {
          String accessToken = request.getParameter("access_token");
          if (authentication == null && accessToken != null) {
            authentication = tokenStore.readAuthentication(accessToken);
            if (authentication != null) {
              log.info("Obtain user {} from access_token {}.", authentication.getName(),
                  accessToken);
            } else {
              log.info("Unknown refresh_token {}", accessToken);
            }
          }
        }
          break;
        default: {
          log.info("Unsupported grantType {}", grantType);
        }
      }
    }

    String accessToken = request.getParameter("access_token");
    if (authentication == null && accessToken != null) {
      authentication = tokenStore.readAuthentication(accessToken);
      if (authentication != null) {
        log.info("Obtain user {} from access_token {}.", authentication.getName(), accessToken);
      } else {
        log.info("Unknown access_token {}", accessToken);
      }
    }
    if (authentication != null) {
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    filterChain.doFilter(request, response);
  }

}
