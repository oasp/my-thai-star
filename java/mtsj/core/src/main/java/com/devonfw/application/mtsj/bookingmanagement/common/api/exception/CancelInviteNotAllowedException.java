package com.devonfw.application.mtsj.bookingmanagement.common.api.exception;

import com.devonfw.application.mtsj.general.common.api.NlsBundleApplicationRoot;

import net.sf.mmm.util.exception.api.NlsRuntimeException;

/**
 * Exception thrown when the user tries to cancel a booking in the previous hour
 *
 */
public class CancelInviteNotAllowedException extends NlsRuntimeException {

	/** UID for serialization. */
	private static final long serialVersionUID = 1L;

	/**
	   * The constructor.
	   */
	public CancelInviteNotAllowedException() {

		this(null);
	}


	/**
	   * The constructor.
	   *
	   * @param cause The root cause of this exception.
	   */
	public CancelInviteNotAllowedException(Throwable cause) {

		super(cause, createBundle(NlsBundleApplicationRoot.class).errorCancelInviteNotAllowed());
	}

	@Override
	public boolean isTechnical() {

		return false;
	}
}
