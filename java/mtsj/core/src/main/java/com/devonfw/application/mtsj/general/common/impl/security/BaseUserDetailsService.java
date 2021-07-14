package com.devonfw.application.mtsj.general.common.impl.security;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.devonfw.application.mtsj.general.common.api.UserProfile;
import com.devonfw.application.mtsj.general.common.api.security.UserData;
import com.devonfw.application.mtsj.usermanagement.dataaccess.api.UserEntity;
import com.devonfw.application.mtsj.usermanagement.dataaccess.api.repo.UserRepository;
import com.devonfw.application.mtsj.usermanagement.logic.api.Usermanagement;
import com.devonfw.module.security.common.api.accesscontrol.AccessControl;
import com.devonfw.module.security.common.api.accesscontrol.AccessControlProvider;
import com.devonfw.module.security.common.api.accesscontrol.PrincipalAccessControlProvider;
import com.devonfw.module.security.common.base.accesscontrol.AccessControlGrantedAuthority;

/**
 * This class represents a customized implementation of the {@link UserDetailsService} interface.<br/>
 * <br/>
 * It should be used in custom subclasses of {@link WebSecurityConfigurerAdapter} in the following way:
 * <ul>
 * <li>Inject a fully configured instance of {@link BaseUserDetailsService} into the subclass of
 * {@link WebSecurityConfigurerAdapter}</li>
 * <li>Override method {@code configure(HttpSecurity)} of {@link WebSecurityConfigurerAdapter}</li>
 * <li>Add the {@link BaseUserDetailsService} to the {@code HttpSecurity} object.</li>
 * </ul>
 * The following code snippet shows the above steps:<br/>
 *
 * <pre>
 * &#64;Configuration
 * &#64;EnableWebSecurity
 * public class MyWebSecurityConfig extends WebSecurityConfigurerAdapter {
 *   // ...
 *   &#64;Inject
 *   private UserDetailsService userDetailsService;
 *   // ...
 *   &#64;Override
 *   public void configure(HttpSecurity http) throws Exception {
 *      http.userDetailsService(this.userDetailsService)... //add matchers and other stuff
 *   }
 * }
 * </pre>
 *
 * <br/>
 * For another example, have a look at
 * {@link com.devonfw.application.mtsj.general.service.impl.config.BaseWebSecurityConfig}.
 */
@Named
public class BaseUserDetailsService implements UserDetailsService {

  /** Logger instance. */
  private static final Logger LOG = LoggerFactory.getLogger(BaseUserDetailsService.class);

  private Usermanagement usermanagement;

  private AuthenticationManagerBuilder amBuilder;

  private AccessControlProvider accessControlProvider;

  private PrincipalAccessControlProvider<UserProfile> principalAccessControlProvider;

  private UserEntity user;

  @Inject
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    try {
      UserProfile principal = this.usermanagement.findUserProfileByLogin(username);
      this.user = this.userRepository.findByUsername(username);
      UserData userData = new UserData(this.user.getUsername(), this.user.getPassword(), getAuthorities(principal));
      userData.setUserProfile(principal);
      return userData;
    } catch (Exception e) {
      throw new UsernameNotFoundException("Authentication failed, for user:" + username, e);
    }
  }

  /**
   * Returns the {@link GrantedAuthority}s of the user associated with the provided {@link UserProfile}.
   *
   * @param principal the {@link UserProfile} of the user
   * @return the associated {@link GrantedAuthority}s
   * @throws AuthenticationException if no principal is retrievable for the given {@code username}
   */
  protected Set<GrantedAuthority> getAuthorities(UserProfile principal) throws AuthenticationException {

    Set<AccessControl> accessControlSet = new HashSet<>();
    Set<String> undefinedIds = this.principalAccessControlProvider.getAccessControlIds(principal).stream()
        .filter(id -> !this.accessControlProvider.collectAccessControls(id, accessControlSet))
        .collect(Collectors.toUnmodifiableSet());

    undefinedIds.forEach(id -> LOG.warn("Undefined access control {}.", id));

    return accessControlSet.stream().map(accessControl -> new AccessControlGrantedAuthority(accessControl))
        .collect(Collectors.toUnmodifiableSet());
  }

  /**
   * @return usermanagement
   */
  public Usermanagement getUsermanagement() {

    return this.usermanagement;
  }

  /**
   * @param usermanagement new value of {@link #getUsermanagement}.
   */
  @Inject
  public void setUsermanagement(Usermanagement usermanagement) {

    this.usermanagement = usermanagement;
  }

  /**
   * @return amBuilder
   */
  public AuthenticationManagerBuilder getAmBuilder() {

    return this.amBuilder;
  }

  /**
   * @param amBuilder new value of {@link #getAmBuilder}.
   */
  @Inject
  public void setAmBuilder(AuthenticationManagerBuilder amBuilder) {

    this.amBuilder = amBuilder;
  }

  /**
   * @return accessControlProvider
   */
  public AccessControlProvider getAccessControlProvider() {

    return this.accessControlProvider;
  }

  /**
   * @param accessControlProvider new value of {@link #getAccessControlProvider}.
   */
  @Inject
  public void setAccessControlProvider(AccessControlProvider accessControlProvider) {

    this.accessControlProvider = accessControlProvider;
  }

  /**
   * @return principalAccessControlProvider
   */
  public PrincipalAccessControlProvider<UserProfile> getPrincipalAccessControlProvider() {

    return this.principalAccessControlProvider;
  }

  /**
   * @param principalAccessControlProvider new value of {@link #getPrincipalAccessControlProvider}.
   */
  @Inject
  public void setPrincipalAccessControlProvider(
      PrincipalAccessControlProvider<UserProfile> principalAccessControlProvider) {

    this.principalAccessControlProvider = principalAccessControlProvider;
  }
}
