package com.devonfw.application.mtsj.ordermanagement.common.api.exception;

import com.devonfw.application.mtsj.general.common.api.NlsBundleApplicationRoot;

import net.sf.mmm.util.exception.api.NlsRuntimeException;

/**
 * Exception thrown when the user tries to cancel an order in the previous hour of a booking
 *
 */
public class CancelNotAllowedException extends NlsRuntimeException {

	/** UID for serialization. */
	private static final long serialVersionUID = 1L;

	/**
	   * The constructor.
	   */
	public CancelNotAllowedException() {

		this(null);
	}


	/**
	   * The constructor.
	   *
	   * @param cause The root cause of this exception.
	   */
	public CancelNotAllowedException(Throwable cause) {

		super(cause, createBundle(NlsBundleApplicationRoot.class).errorCancelInviteNotAllowed());
	}

	@Override
	public boolean isTechnical() {

		return false;
	}
}
