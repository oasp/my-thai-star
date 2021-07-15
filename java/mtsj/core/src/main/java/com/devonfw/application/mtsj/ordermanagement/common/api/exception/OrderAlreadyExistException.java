package com.devonfw.application.mtsj.ordermanagement.common.api.exception;

import com.devonfw.application.mtsj.general.common.api.NlsBundleApplicationRoot;

import net.sf.mmm.util.exception.api.NlsRuntimeException;

/**
 * This exception is thrown if a {@link com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.BookingEntity} has
 * already {@link com.devonfw.application.mtsj.ordermanagement.dataaccess.api.OrderEntity} related.
 *
 */
public class OrderAlreadyExistException extends NlsRuntimeException {

	/** UID for serialization. */
	private static final long serialVersionUID = 1L;

	/**
	   * The constructor.
	   */
	public OrderAlreadyExistException() {

		this(null);
	}


	/**
	   * The constructor.
	   *
	   * @param cause The root cause of this exception.
	   */
	public OrderAlreadyExistException(Throwable cause) {

		super(cause, createBundle(NlsBundleApplicationRoot.class).erroOrderAlreadyExist());
	}

	@Override
	public boolean isTechnical() {

		return false;
	}
}
