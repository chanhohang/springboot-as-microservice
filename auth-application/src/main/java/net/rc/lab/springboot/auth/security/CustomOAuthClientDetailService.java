package net.rc.lab.springboot.auth.security;

import com.google.common.collect.Sets;

import net.rc.lab.springboot.core.repository.UserRepository;
import net.rc.lab.springboot.entities.UserVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomOAuthClientDetailService implements ClientDetailsService {

  @Autowired
  private UserRepository userService;

  @Value("${security.oauth2.accesstoken.validity.second:60}")
  private int accesstokenValiditySecond;

  @Value("${security.oauth2.expiretoken.validity.second:3600}")
  private int expiretokenValiditySecond;

  @Override
  public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {

    // reuse user table for the moment.
    Optional<UserVo> user = null;
    if (!"rc-lab".equals(clientId)) {
      user = userService.findByLoginId(clientId);
      if (!user.isPresent()) {
        throw new UsernameNotFoundException(
            String.format("ClientDetails %s does not exist!", clientId));
      }
    }
    BaseClientDetails clientDetails = new BaseClientDetails();

    clientDetails.setClientId(clientId);
    clientDetails.setScope(Sets.newHashSet("read", "write"));
    clientDetails.setAuthorizedGrantTypes(Sets.newHashSet("authorization_code", "refresh_token",
        "implicit", "password", "client_credentials"));
    if (user != null) {
      clientDetails
          .setAuthorities(AuthorityUtils.createAuthorityList(user.get().getRole().getName()));
    } else {
      clientDetails.setAuthorities(AuthorityUtils.createAuthorityList("ROLE_CLIENT"));
    }

    clientDetails.setAccessTokenValiditySeconds(accesstokenValiditySecond);
    clientDetails.setRefreshTokenValiditySeconds(expiretokenValiditySecond);
    clientDetails.setClientSecret("rc-lab-secret");
    clientDetails.setAutoApproveScopes(Sets.newHashSet("true"));
    return clientDetails;
  }

}
