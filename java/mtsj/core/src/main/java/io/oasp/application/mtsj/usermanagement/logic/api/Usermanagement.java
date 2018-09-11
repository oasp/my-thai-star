package io.oasp.application.mtsj.usermanagement.logic.api;

import io.oasp.application.mtsj.usermanagement.logic.api.to.UserEto;
import io.oasp.application.mtsj.usermanagement.logic.api.to.UserRoleEto;
import io.oasp.application.mtsj.usermanagement.logic.api.to.UserRoleSearchCriteriaTo;
import io.oasp.application.mtsj.usermanagement.logic.api.to.UserSearchCriteriaTo;
import io.oasp.module.jpa.common.api.to.PaginatedListTo;
import io.oasp.application.mtsj.usermanagement.dataaccess.api.UserEntity;
import io.oasp.application.mtsj.usermanagement.dataaccess.api.dao.UserDao;
import io.oasp.application.mtsj.general.common.impl.security.BaseUserDetailsService;

/**
 * Interface for Usermanagement component.
 */
public interface Usermanagement {

  /**
   * Returns a User by its id 'id'.
   *
   * @param id The id 'id' of the User.
   * @return The {@link UserEto} with id 'id'
   */
  UserEto findUser(Long id);

  /**
   * Returns a paginated list of Users matching the search criteria.
   *
   * @param criteria the {@link UserSearchCriteriaTo}.
   * @return the {@link List} of matching {@link UserEto}s.
   */
  PaginatedListTo<UserEto> findUserEtos(UserSearchCriteriaTo criteria);

  /**
   * Deletes a user from the database by its id 'userId'.
   *
   * @param userId Id of the user to delete
   * @return boolean <code>true</code> if the user can be deleted, <code>false</code> otherwise
   */
  boolean deleteUser(Long userId);

  /**
   * Saves a user and store it in the database.
   *
   * @param user the {@link UserEto} to create.
   * @return the new {@link UserEto} that has been saved with ID and version.
   */
  UserEto saveUser(UserEto user);

  /**
   * Returns a UserRole by its id 'id'.
   *
   * @param id The id 'id' of the UserRole.
   * @return The {@link UserRoleEto} with id 'id'
   */
  UserRoleEto findUserRole(Long id);

  /**
   * Returns a paginated list of UserRoles matching the search criteria.
   *
   * @param criteria the {@link UserRoleSearchCriteriaTo}.
   * @return the {@link List} of matching {@link UserRoleEto}s.
   */
  PaginatedListTo<UserRoleEto> findUserRoleEtos(UserRoleSearchCriteriaTo criteria);

  /**
   * Deletes a userRole from the database by its id 'userRoleId'.
   *
   * @param userRoleId Id of the userRole to delete
   * @return boolean <code>true</code> if the userRole can be deleted, <code>false</code> otherwise
   */
  boolean deleteUserRole(Long userRoleId);

  /**
   * Saves a userRole and store it in the database.
   *
   * @param userRole the {@link UserRoleEto} to create.
   * @return the new {@link UserRoleEto} that has been saved with ID and version.
   */
  UserRoleEto saveUserRole(UserRoleEto userRole);

  /**
   * Finds a UserEntity in the db and returns a UserEto
   *
   * @param username the username from {@link BaseUserDetailsService}
   * @return the {@link UserRoleEto} generated from the {@link UserEntity returned from {@link UserDao}}
   */
  UserEto findUserByUsername(String username);

}
