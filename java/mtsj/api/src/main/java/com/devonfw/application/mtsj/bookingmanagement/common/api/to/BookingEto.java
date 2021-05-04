package com.devonfw.application.mtsj.bookingmanagement.common.api.to;

import java.time.Instant;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

import com.devonfw.application.mtsj.bookingmanagement.common.api.Booking;
import com.devonfw.application.mtsj.bookingmanagement.common.api.datatype.BookingType;
import com.devonfw.application.mtsj.general.common.api.validation.EmailExtended;
import com.devonfw.module.basic.common.api.to.AbstractEto;

/**
 * Entity transport object of Booking
 */
public class BookingEto extends AbstractEto implements Booking {

  private static final long serialVersionUID = 1L;

  @NotNull
  private String name;

  private String bookingToken;

  private String comment;

  @NotNull
  @Future
  private Instant bookingDate;

  private Instant expirationDate;

  private Instant creationDate;

  @NotNull
  @EmailExtended
  private String email;

  private String statusString;

  private BookingType bookingType;

  private Long tableId;

  private Long orderId;

  private Integer assistants;

  private Long userId;

  @Override
  public String getName() {

    return this.name;
  }

  @Override
  public void setName(String name) {

    this.name = name;
  }

  @Override
  public String getBookingToken() {

    return this.bookingToken;
  }

  @Override
  public void setBookingToken(String bookingToken) {

    this.bookingToken = bookingToken;
  }

  @Override
  public String getComment() {

    return this.comment;
  }

  @Override
  public void setComment(String comment) {

    this.comment = comment;
  }

  @Override
  public Instant getBookingDate() {

    return this.bookingDate;
  }

  @Override
  public void setBookingDate(Instant bookingDate) {

    this.bookingDate = bookingDate;
  }

  @Override
  public Instant getExpirationDate() {

    return this.expirationDate;
  }

  @Override
  public void setExpirationDate(Instant expirationDate) {

    this.expirationDate = expirationDate;
  }

  @Override
  public Instant getCreationDate() {

    return this.creationDate;
  }

  @Override
  public void setCreationDate(Instant creationDate) {

    this.creationDate = creationDate;
  }

  @Override
  public String getEmail() {

    return this.email;
  }

  @Override
  public void setEmail(String email) {

    this.email = email;
  }

  @Override
  public String getStatus() {

    return this.statusString;
  }

  @Override
  public void setStatus(final String statusString) {

    this.statusString = statusString;
  }

  @Override
  public BookingType getBookingType() {

    return this.bookingType;
  }

  @Override
  public void setBookingType(BookingType bookingType) {

    this.bookingType = bookingType;
  }

  @Override
  public Long getTableId() {

    return this.tableId;
  }

  @Override
  public void setTableId(Long tableId) {

    this.tableId = tableId;
  }

  @Override
  public int hashCode() {

    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
    result = prime * result + ((this.bookingToken == null) ? 0 : this.bookingToken.hashCode());
    result = prime * result + ((this.comment == null) ? 0 : this.comment.hashCode());
    result = prime * result + ((this.bookingDate == null) ? 0 : this.bookingDate.hashCode());
    result = prime * result + ((this.expirationDate == null) ? 0 : this.expirationDate.hashCode());
    result = prime * result + ((this.creationDate == null) ? 0 : this.creationDate.hashCode());
    result = prime * result + ((this.email == null) ? 0 : this.email.hashCode());
    result = prime * result + ((this.statusString == null) ? 0 : this.statusString.hashCode());
    result = prime * result + ((this.bookingType == null) ? 0 : this.bookingType.hashCode());

    result = prime * result + ((this.tableId == null) ? 0 : this.tableId.hashCode());

    return result;
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    // class check will be done by super type EntityTo!
    if (!super.equals(obj)) {
      return false;
    }
    BookingEto other = (BookingEto) obj;
    if (this.name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!this.name.equals(other.name)) {
      return false;
    }
    if (this.bookingToken == null) {
      if (other.bookingToken != null) {
        return false;
      }
    } else if (!this.bookingToken.equals(other.bookingToken)) {
      return false;
    }
    if (this.comment == null) {
      if (other.comment != null) {
        return false;
      }
    } else if (!this.comment.equals(other.comment)) {
      return false;
    }
    if (this.bookingDate == null) {
      if (other.bookingDate != null) {
        return false;
      }
    } else if (!this.bookingDate.equals(other.bookingDate)) {
      return false;
    }
    if (this.expirationDate == null) {
      if (other.expirationDate != null) {
        return false;
      }
    } else if (!this.expirationDate.equals(other.expirationDate)) {
      return false;
    }
    if (this.creationDate == null) {
      if (other.creationDate != null) {
        return false;
      }
    } else if (!this.creationDate.equals(other.creationDate)) {
      return false;
    }
    if (this.email == null) {
      if (other.email != null) {
        return false;
      }
    } else if (!this.email.equals(other.email)) {
      return false;
    }
    if (this.statusString == null) {
      if (other.statusString != null) {
        return false;
      }
    } else if (!this.statusString.equals(other.statusString)) {
      return false;
    }
    if (this.bookingType == null) {
      if (other.bookingType != null) {
        return false;
      }
    } else if (!this.bookingType.equals(other.bookingType)) {
      return false;
    }

    if (this.tableId == null) {
      if (other.tableId != null) {
        return false;
      }
    } else if (!this.tableId.equals(other.tableId)) {
      return false;
    }

    return true;
  }

  @Override
  public Long getOrderId() {

    return this.orderId;
  }

  @Override
  public void setOrderId(Long orderId) {

    this.orderId = orderId;
  }

  @Override
  public Integer getAssistants() {

    return this.assistants;
  }

  @Override
  public void setAssistants(Integer assistants) {

    this.assistants = assistants;
  }

  @Override
  public Long getUserId() {

    return this.userId;
  }

  @Override
  public void setUserId(Long userId) {

    this.userId = userId;
  }

}
