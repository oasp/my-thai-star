package com.devonfw.application.mtsj.general.service.impl.rest;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.devonfw.application.mtsj.general.common.api.to.UserDetailsClientTo;
import com.devonfw.application.mtsj.general.common.base.TokenAuthenticationService;
import com.devonfw.application.mtsj.general.service.api.rest.SecurityRestService;

/**
 *
 */
@Named("SecurityRestService")
public class SecurityRestServiceImpl implements SecurityRestService {

  /** Logger instance. */
  private static final Logger LOG = LoggerFactory.getLogger(SecurityRestServiceImpl.class);

  @Inject
  private TokenAuthenticationService tokenAuthenticationService;

  @Override
  public UserDetailsClientTo getCurrentUserDetails(HttpServletRequest request) {

    return this.tokenAuthenticationService.getUserdetailsFromToken(request.getHeader("Authorization"));
  }

}
