package com.devonfw.application.mtsj.general.service.impl.rest;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.devonfw.application.mtsj.general.common.api.to.UserDetailsClientTo;
import com.devonfw.application.mtsj.general.common.base.TokenAuthenticationService;

/**
 * The security REST service provides access to the csrf token, the authenticated user's meta-data. Furthermore, it
 * provides functionality to check permissions and roles of the authenticated user.
 *
 */
@Path("/security/v1")
@Named("SecurityRestService")
@Transactional
public class SecurityRestServiceImpl {

  /** Logger instance. */
  private static final Logger LOG = LoggerFactory.getLogger(SecurityRestServiceImpl.class);

  @Inject
  private TokenAuthenticationService tokenAuthenticationService;

  /**
   * Returns the user details from the jwt token included in the 'Authorization' header
   *
   * @param request {@link HttpServletRequest} to retrieve the token
   * @return the user details {@link UserDetailsClientTo}
   */
  @Produces(MediaType.APPLICATION_JSON)
  @GET
  @Path("/currentuser/")
  public UserDetailsClientTo getCurrentUserDetails(@Context HttpServletRequest request) {

    return this.tokenAuthenticationService.getUserdetailsFromToken(request.getHeader("Authorization"));
  }

}
