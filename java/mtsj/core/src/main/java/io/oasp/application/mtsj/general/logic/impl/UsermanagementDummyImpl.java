package io.oasp.application.mtsj.general.logic.impl;

import javax.inject.Inject;
import javax.inject.Named;

import io.oasp.application.mtsj.usermanagement.logic.api.to.UserEto;
import org.springframework.stereotype.Component;

import io.oasp.application.mtsj.general.common.api.UserProfile;
import io.oasp.application.mtsj.general.common.api.Usermanagement;
import io.oasp.application.mtsj.general.common.api.datatype.Role;
import io.oasp.application.mtsj.general.common.api.to.UserDetailsClientTo;
import io.oasp.application.mtsj.general.common.base.AbstractBeanMapperSupport;

/**
 * Implementation of {@link Usermanagement}.
 */
@Named
@Component
public class UsermanagementDummyImpl extends AbstractBeanMapperSupport implements Usermanagement {

  private io.oasp.application.mtsj.usermanagement.logic.api.Usermanagement usermanagementCore;

  @Override
  public UserProfile findUserProfileByLogin(String login) {
    UserEto userEto = this.usermanagementCore.findUserByUsername(login);
    // this is only a dummy - please replace with a real implementation
    // 30/08/2018 first attempt to implement
    UserDetailsClientTo profile = new UserDetailsClientTo();
    profile.setId(userEto.getId());
    profile.setName(login);
    profile.setRole(setRole(userEto.getUserRoleId()));
    return profile;
  }

  /**
   * Returns a Role instance from its id
   *
   * @param roleId received from the {@link UserEto constructed in findUserProfileByLogin}
   * @return an instance from {@link Role}
   */
  public Role setRole(long roleId) {
    switch (String.valueOf(roleId).trim()) {
      case "1":
        return Role.WAITER;
      case "0":
        return Role.CUSTOMER;
      default:
        return Role.CUSTOMER;
    }
  }

  /**
   * @return instance from the UserManagement interface that is in the core package
   * we need this particular one to return us a UserEto
   */
  public io.oasp.application.mtsj.usermanagement.logic.api.Usermanagement getUsermanagementCore() {

    return this.usermanagementCore;
  }

  @Inject
  public void setUsermanagementCore(io.oasp.application.mtsj.usermanagement.logic.api.Usermanagement usermanagementCore) {

    this.usermanagementCore = usermanagementCore;
  }

}
