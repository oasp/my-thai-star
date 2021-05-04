package com.devonfw.application.mtsj.bookingmanagement.common.api;

import java.time.Instant;

import com.devonfw.application.mtsj.bookingmanagement.common.api.datatype.BookingType;
import com.devonfw.application.mtsj.general.common.api.ApplicationEntity;

public interface Booking extends ApplicationEntity {

  public String getName();

  public void setName(String name);

  public String getBookingToken();

  public void setBookingToken(String bookingToken);

  public String getComment();

  public void setComment(String comment);

  public Instant getBookingDate();

  public void setBookingDate(Instant bookingDate);

  public Instant getExpirationDate();

  public void setExpirationDate(Instant expirationDate);

  public Instant getCreationDate();

  public void setCreationDate(Instant creationDate);

  public String getEmail();

  public void setEmail(String email);

  public boolean getCanceled();

  public void setCanceled(boolean canceled);

  public BookingType getBookingType();

  public void setBookingType(BookingType bookingType);

  public Long getTableId();

  public void setTableId(Long tableId);

  public Long getOrderId();

  public void setOrderId(Long orderId);

  public Integer getAssistants();

  public void setAssistants(Integer assistants);

  public Long getUserId();

  public void setUserId(Long userId);

}
