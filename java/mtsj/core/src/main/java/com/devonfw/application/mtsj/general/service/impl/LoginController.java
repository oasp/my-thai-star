package com.devonfw.application.mtsj.general.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for Login-Page.
 */
@Controller
public class LoginController {

  /**
   * Default URL to redirect to after successfully login.
   */
  public static final String DEFAULT_TARGET_URL = "/";

  private static final String ERROR = "error";

  /**
   * Builds the model for the login page---mainly focusing on the error message handling.
   *
   * @param authenticationFailed flag for authentication failed
   * @param invalidSession flag for invalid session
   * @param accessDenied flag for access denied
   * @param logout flag for successful logout
   * @return the view model
   */
  @RequestMapping(value = "/login**", method = { RequestMethod.GET, RequestMethod.POST })
  public ModelAndView login(
      @RequestParam(value = "authentication_failed", required = false) boolean authenticationFailed,
      @RequestParam(value = "invalid_session", required = false) boolean invalidSession,
      @RequestParam(value = "access_denied", required = false) boolean accessDenied,
      @RequestParam(value = "logout", required = false) boolean logout) {

    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (!authentication.getPrincipal().equals("anonymousUser")) {
      return new ModelAndView("redirect:" + DEFAULT_TARGET_URL);
    }

    ModelAndView model = new ModelAndView();
    if (authenticationFailed) {
      model.addObject(ERROR, "Authentication failed!");
    } else if (invalidSession) {
      model.addObject(ERROR, "You are currently not logged in!");
    } else if (accessDenied) {
      model.addObject(ERROR, "You have insufficient permissions to access this page!");
    } else if (logout) {
      model.addObject("msg", "Logout successful!");
    }

    return model;
  }

}
