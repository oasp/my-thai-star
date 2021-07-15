package com.devonfw.application.mtsj.general.common.base;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.devonfw.application.mtsj.usermanagement.dataaccess.api.UserEntity;
import com.devonfw.application.mtsj.usermanagement.dataaccess.api.repo.UserRepository;
import com.devonfw.module.security.jwt.common.api.JwtCreator;
import com.devonfw.module.security.jwt.common.base.JwtConstants;
import com.devonfw.module.security.jwt.common.base.JwtLoginFilter;

/**
 * Filter is used to intercept JwtLoginFilter and set 2FA related headers
 *
 */
public class PreLoginFilter extends JwtLoginFilter {

  /**
   * Logger instance.
   */
  private static final Logger LOG = LoggerFactory.getLogger(AdvancedDaoAuthenticationProvider.class);

  private JwtCreator jwtCreator;

  @Inject
  private UserRepository userRepository;

  /**
   * The constructor.
   *
   * @param urlPattern
   */
  public PreLoginFilter(String urlPattern) {

    super(urlPattern);
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
      Authentication authentication) throws IOException {

    String token = this.jwtCreator.create(authentication);
    response.addHeader(JwtConstants.EXPOSE_HEADERS, JwtConstants.HEADER_AUTHORIZATION);
    response.addHeader(JwtConstants.HEADER_AUTHORIZATION, JwtConstants.TOKEN_PREFIX + " " + token);

    HttpServletResponse httpSerResp = response;

    TwoFactorHttpResponseHeaderWrapper wrapper = new TwoFactorHttpResponseHeaderWrapper(httpSerResp);

    UserEntity user = this.userRepository.findByUsername(authentication.getName());
    if (user.getTwoFactorStatus()) {
      wrapper.setHeader(JwtConstants.EXPOSE_HEADERS, TokenAuthenticationService.HEADER_OTP);
      wrapper.setHeader(TokenAuthenticationService.HEADER_OTP, TokenAuthenticationService.OTP);
    }

    try {
      chain.doFilter(request, response);
    } catch (ServletException e) {
      LOG.error("Error in filter chaining :{}", e.getMessage());
    }
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException failed) {

    super.unsuccessfulAuthentication(request, response, failed);
  }

  /**
   * @return the instance of {@link JwtCreator}.
   */
  @Override
  public JwtCreator getJwtCreator() {

    return this.jwtCreator;
  }

  /**
   * @param jwtCreator new value of {@link #getJwtCreator()}.
   */
  @Override
  @Inject
  public void setJwtCreator(JwtCreator jwtCreator) {

    this.jwtCreator = jwtCreator;
  }

}
