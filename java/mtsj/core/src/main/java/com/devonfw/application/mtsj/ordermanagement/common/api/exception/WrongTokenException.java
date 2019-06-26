package com.devonfw.application.mtsj.ordermanagement.common.api.exception;

import com.devonfw.application.mtsj.general.common.api.NlsBundleApplicationRoot;

import net.sf.mmm.util.exception.api.NlsRuntimeException;
import net.sf.mmm.util.nls.api.NlsMessage;

/**
 * This exception is thrown if the token of an
 * {@link com.devonfw.application.mtsj.ordermanagement.common.api.to.OrderEto} is not well formed (wrong prefix).
 *
 */
public class WrongTokenException extends NlsRuntimeException {

	/** UID for serialization. */
	private static final long serialVersionUID = 1L;

	/**
	   * The constructor.
	   */
	public WrongTokenException() {

		this(null);
	}


	/**
	   * The constructor.
	   *
	   * @param cause The root cause of this exception.
	   */
	public WrongTokenException(Throwable cause) {

		super(cause, createBundle(NlsBundleApplicationRoot.class).errorWrongToken());
	}

	@Override
	public boolean isTechnical() {

		return false;
	}

}
