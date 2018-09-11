package io.oasp.application.mtsj.usermanagement.service.rest;

import javax.inject.Inject;
import javax.inject.Named;

import io.oasp.application.mtsj.usermanagement.common.api.User;
import io.oasp.application.mtsj.usermanagement.dataaccess.api.UserEntity;
import io.oasp.application.mtsj.usermanagement.logic.api.Usermanagement;
import io.oasp.application.mtsj.usermanagement.logic.api.to.UserEto;
import io.oasp.application.mtsj.usermanagement.logic.api.to.UserRoleEto;
import io.oasp.application.mtsj.usermanagement.logic.api.to.UserRoleSearchCriteriaTo;
import io.oasp.application.mtsj.usermanagement.logic.api.to.UserSearchCriteriaTo;
import io.oasp.module.jpa.common.api.to.PaginatedListTo;

import java.util.List;

/**
 * The service implementation for REST calls in order to execute the logic of component {@link Usermanagement}.
 */
@Named("UsermanagementRestService")
public class UsermanagementRestServiceImpl implements UsermanagementRestService {

  @Inject
  private Usermanagement usermanagement;

  @Override
  public UserEto getUser(long id) {

    return this.usermanagement.findUser(id);
  }

  @Override
  public UserEto saveUser(UserEto user) {

    return this.usermanagement.saveUser(user);
  }

  @Override
  public void deleteUser(long id) {

    this.usermanagement.deleteUser(id);
  }

  @Override
  public PaginatedListTo<UserEto> findUsersByPost(UserSearchCriteriaTo searchCriteriaTo) {

    return this.usermanagement.findUserEtos(searchCriteriaTo);
  }

  @Override
  public UserRoleEto getUserRole(long id) {

    return this.usermanagement.findUserRole(id);
  }

  @Override
  public UserRoleEto saveUserRole(UserRoleEto userrole) {

    return this.usermanagement.saveUserRole(userrole);
  }

  @Override
  public void deleteUserRole(long id) {

    this.usermanagement.deleteUserRole(id);
  }

  @Override
  public PaginatedListTo<UserRoleEto> findUserRolesByPost(UserRoleSearchCriteriaTo searchCriteriaTo) {

    return this.usermanagement.findUserRoleEtos(searchCriteriaTo);
  }

  @Override
  public UserEto registerUser(UserEto userEto){
    return this.usermanagement.saveUser(userEto);
  }

}
