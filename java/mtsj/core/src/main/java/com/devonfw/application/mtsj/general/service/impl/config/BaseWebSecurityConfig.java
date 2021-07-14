package com.devonfw.application.mtsj.general.service.impl.config;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.devonfw.application.mtsj.general.common.base.AdvancedDaoAuthenticationProvider;
import com.devonfw.application.mtsj.general.common.base.JWTAuthenticationFilter;
import com.devonfw.application.mtsj.general.common.base.JWTLoginFilter;
import com.devonfw.application.mtsj.general.common.base.TwoFactorFilter;
import com.devonfw.application.mtsj.general.common.impl.security.BaseUserDetailsService;
import com.devonfw.application.mtsj.general.common.impl.security.twofactor.TwoFactorAuthenticationProvider;
import com.devonfw.module.security.common.api.config.WebSecurityConfigurer;

/**
 * This type serves as a base class for extensions of the {@code WebSecurityConfigurerAdapter} and provides a default
 * configuration. <br/>
 * Security configuration is based on {@link WebSecurityConfigurerAdapter}. This configuration is by purpose designed
 * most simple for authentication based on Json Web Token.
 */
public abstract class BaseWebSecurityConfig extends WebSecurityConfigurerAdapter {


  @Inject
  private BaseUserDetailsService userDetailsService;

  @Inject
  private PasswordEncoder passwordEncoder;

  @Bean
  public AdvancedDaoAuthenticationProvider advancedDaoAuthenticationProvider() {

    AdvancedDaoAuthenticationProvider authProvider = new AdvancedDaoAuthenticationProvider();
    authProvider.setPasswordEncoder(this.passwordEncoder);
    authProvider.setUserDetailsService(this.userDetailsService);
    return authProvider;
  }

  @Bean
  public TwoFactorAuthenticationProvider twoFactorAuthenticationProvider() {

    TwoFactorAuthenticationProvider authProvider = new TwoFactorAuthenticationProvider();
    authProvider.setPasswordEncoder(this.passwordEncoder);
    return authProvider;
  }

  @Inject
  private AdvancedDaoAuthenticationProvider advancedDaoAuthenticationProvider;

  @Inject
  private TwoFactorAuthenticationProvider twoFactorAuthenticationProvider;

  @Inject
  private WebSecurityConfigurer webSecurityConfigurer;

  /**
   * Configure spring security to enable login with JWT.
   */
  @Override
  public void configure(HttpSecurity http) throws Exception {

    String[] unsecuredResources = new String[] { "/login", "/security/**", "/services/rest/login",
    "/services/rest/logout", "/services/rest/dishmanagement/**", "/services/rest/imagemanagement/**",
    "/services/rest/ordermanagement/v1/order", "/services/rest/bookingmanagement/v1/booking",
    "/services/rest/bookingmanagement/v1/booking/cancel/**",
    "/services/rest/bookingmanagement/v1/invitedguest/accept/**",
    "/services/rest/bookingmanagement/v1/invitedguest/decline/**",
    "/services/rest/ordermanagement/v1/order/cancelorder/**" };

    // disable CSRF protection by default, use csrf starter to override.
    http = http.csrf().disable();
    // load starters as pluggins.
    http = this.webSecurityConfigurer.configure(http);

    http.userDetailsService(this.userDetailsService).csrf().disable().exceptionHandling().and().sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
        .antMatchers(unsecuredResources).permitAll().antMatchers(HttpMethod.POST, "/login").permitAll().anyRequest()
        .authenticated().and()
        // verification with OTP are filtered with the TwoFactorFilter
        .addFilterBefore(new TwoFactorFilter("/verify", authenticationManager(), this.userDetailsService),
            UsernamePasswordAuthenticationFilter.class)
        // the api/login requests are filtered with the JWTLoginFilter
        .addFilterBefore(new JWTLoginFilter("/login", authenticationManager(), this.userDetailsService),
            UsernamePasswordAuthenticationFilter.class)
        // other requests are filtered to check the presence of JWT in header
        .addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

  }

  @Override
  @SuppressWarnings("javadoc")
  public void configure(AuthenticationManagerBuilder auth) throws Exception {

    auth.authenticationProvider(this.advancedDaoAuthenticationProvider)
        .authenticationProvider(this.twoFactorAuthenticationProvider);
  }
}
