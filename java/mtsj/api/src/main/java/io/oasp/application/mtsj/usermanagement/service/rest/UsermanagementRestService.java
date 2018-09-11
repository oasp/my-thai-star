package io.oasp.application.mtsj.usermanagement.service.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.oasp.application.mtsj.general.common.api.Usermanagement;
import io.oasp.application.mtsj.usermanagement.logic.api.to.UserEto;
import io.oasp.application.mtsj.usermanagement.logic.api.to.UserRoleEto;
import io.oasp.application.mtsj.usermanagement.logic.api.to.UserRoleSearchCriteriaTo;
import io.oasp.application.mtsj.usermanagement.logic.api.to.UserSearchCriteriaTo;
import io.oasp.module.jpa.common.api.to.PaginatedListTo;

/**
 * The service interface for REST calls in order to execute the logic of component {@link Usermanagement}.
 */
@Path("/usermanagement/v1")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface UsermanagementRestService {

  /**
   * Delegates to {@link Usermanagement#findUser}.
   *
   * @param id the ID of the {@link UserEto}
   * @return the {@link UserEto}
   */
  @GET
  @Path("/user/{id}/")
  public UserEto getUser(@PathParam("id") long id);

  /**
   * Delegates to {@link Usermanagement#saveUser}.
   *
   * @param user the {@link UserEto} to be saved
   * @return the recently created {@link UserEto}
   */
  @POST
  @Path("/user/")
  public UserEto saveUser(UserEto user);

  /**
   * Delegates to {@link Usermanagement#saveUser}.
   *
   * @param user the {@link UserEto} to be saved
   * @return the recently created {@link UserEto}
   */
  @POST
  @Path("/user/register/")
  public UserEto registerUser(UserEto user);

  /**
   * Delegates to {@link Usermanagement#deleteUser}.
   *
   * @param id ID of the {@link UserEto} to be deleted
   */
  @DELETE
  @Path("/user/{id}/")
  public void deleteUser(@PathParam("id") long id);

  /**
   * Delegates to {@link Usermanagement#findUserEtos}.
   *
   * @param searchCriteriaTo the pagination and search criteria to be used for finding users.
   * @return the {@link PaginatedListTo list} of matching {@link UserEto}s.
   */
  @Path("/user/search")
  @POST
  public PaginatedListTo<UserEto> findUsersByPost(UserSearchCriteriaTo searchCriteriaTo);

  /**
   * Delegates to {@link Usermanagement#findUserRole}.
   *
   * @param id the ID of the {@link UserRoleEto}
   * @return the {@link UserRoleEto}
   */
  @GET
  @Path("/userrole/{id}/")
  public UserRoleEto getUserRole(@PathParam("id") long id);

  /**
   * Delegates to {@link Usermanagement#saveUserRole}.
   *
   * @param userrole the {@link UserRoleEto} to be saved
   * @return the recently created {@link UserRoleEto}
   */
  @POST
  @Path("/userrole/")
  public UserRoleEto saveUserRole(UserRoleEto userrole);

  /**
   * Delegates to {@link Usermanagement#deleteUserRole}.
   *
   * @param id ID of the {@link UserRoleEto} to be deleted
   */
  @DELETE
  @Path("/userrole/{id}/")
  public void deleteUserRole(@PathParam("id") long id);

  /**
   * Delegates to {@link Usermanagement#findUserRoleEtos}.
   *
   * @param searchCriteriaTo the pagination and search criteria to be used for finding userroles.
   * @return the {@link PaginatedListTo list} of matching {@link UserRoleEto}s.
   */
  @Path("/userrole/search")
  @POST
  public PaginatedListTo<UserRoleEto> findUserRolesByPost(UserRoleSearchCriteriaTo searchCriteriaTo);

}
