package com.devonfw.application.mtsj.general.common.api.exception;

import com.devonfw.application.mtsj.general.common.api.NlsBundleApplicationRoot;

/**
 * Thrown when an operation is requested that requires a user to be logged in, but no such user exists.
 */
public class NoActiveUserException extends ApplicationBusinessException {

  /** UID for serialization. */
  private static final long serialVersionUID = 1L;

  /**
   * The constructor.
   */
  public NoActiveUserException() {

    this(null);
  }

  /**
   * The constructor.
   *
   * @param cause The root cause of this exception.
   */
  public NoActiveUserException(Throwable cause) {

    super(cause, createBundle(NlsBundleApplicationRoot.class).errorNoActiveUser());
  }

  @Override
	public boolean isTechnical() {

		return false;
	}
}
