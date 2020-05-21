package com.devonfw.application.mtsj.general.common.base;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/*
 * {@link HttpServletResponse} wrapper
 *
 * */
public class TwoFactorHttpResponseHeaderWrapper extends HttpServletResponseWrapper {

  /**
   * The constructor.
   *
   * @param response
   */
  public TwoFactorHttpResponseHeaderWrapper(HttpServletResponse response) {

    super(response);
  }

}
