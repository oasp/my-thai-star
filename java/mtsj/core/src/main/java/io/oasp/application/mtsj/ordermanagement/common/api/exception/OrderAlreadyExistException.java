package io.oasp.application.mtsj.ordermanagement.common.api.exception;

import net.sf.mmm.util.exception.api.BusinessErrorUserException;

import io.oasp.application.mtsj.bookingmanagement.dataaccess.api.BookingEntity;
import io.oasp.application.mtsj.ordermanagement.dataaccess.api.OrderEntity;

/**
 * This exception is thrown if a {@link BookingEntity} has already {@link OrderEntity} related.
 *
 */
public class OrderAlreadyExistException extends BusinessErrorUserException {

  public OrderAlreadyExistException() {

    super(null, "The order for this booking already exist. Please cancel the order before create a new one.");
  }
}
