package io.oasp.application.mtsj.usermanagement.dataaccess.api.dao;

import io.oasp.application.mtsj.general.dataaccess.api.dao.ApplicationDao;
import io.oasp.application.mtsj.usermanagement.dataaccess.api.UserEntity;
import io.oasp.application.mtsj.usermanagement.logic.api.to.UserSearchCriteriaTo;
import io.oasp.module.jpa.common.api.to.PaginatedListTo;

/**
 * Data access interface for User entities
 */
public interface UserDao extends ApplicationDao<UserEntity> {

  /**
   * Finds the {@link UserEntity users} matching the given {@link UserSearchCriteriaTo}.
   *
   * @param criteria is the {@link UserSearchCriteriaTo}.
   * @return the {@link PaginatedListTo} with the matching {@link UserEntity} objects.
   */
  PaginatedListTo<UserEntity> findUsers(UserSearchCriteriaTo criteria);

  UserEntity findUserByUsername(String username);
}
