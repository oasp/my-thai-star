package com.devonfw.application.mtsj.ordermanagement.common.api.exception;

import com.devonfw.application.mtsj.general.common.api.NlsBundleApplicationRoot;

import net.sf.mmm.util.exception.api.NlsRuntimeException;

/**
 * This exception is thrown if the guest token of an
 * {@link com.devonfw.application.mtsj.ordermanagement.common.api.to.OrderEto}
 * has no
 * {@link com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.BookingEntity}
 * related.
 *
 */
public class NoInviteException extends NlsRuntimeException {

	/** UID for serialization. */
	private static final long serialVersionUID = 1L;

	/**
	 * The constructor.
	 */
	public NoInviteException() {

		this(null);
	}

	/**
	 * The constructor.
	 *
	 * @param cause The root cause of this exception.
	 */
	public NoInviteException(Throwable cause) {

		super(cause, createBundle(NlsBundleApplicationRoot.class).errorNoInvite());
	}

	@Override
	public boolean isTechnical() {

		return false;
	}
}
