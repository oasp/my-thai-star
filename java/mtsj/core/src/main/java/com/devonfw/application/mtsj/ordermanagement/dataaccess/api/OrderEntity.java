package com.devonfw.application.mtsj.ordermanagement.dataaccess.api;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.BookingEntity;
import com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.InvitedGuestEntity;
import com.devonfw.application.mtsj.general.dataaccess.api.ApplicationPersistenceEntity;
import com.devonfw.application.mtsj.ordermanagement.common.api.Order;

/**
 * The {@link com.devonfw.application.mtsj.general.dataaccess.api.ApplicationPersistenceEntity persistent entity} for
 * {@link Order}.
 */
@Entity
@Table(name = "Orders")
public class OrderEntity extends ApplicationPersistenceEntity implements Order {

  private static final long serialVersionUID = 1L;
//-------------------------------------------------------------------------------------------------------------------------------------------
  private String status;
//-------------------------------------------------------------------------------------------------------------------------------------------
  private BookingEntity booking;

  private InvitedGuestEntity invitedGuest;

  private BookingEntity host;

  private List<OrderLineEntity> orderLines;


//-------------------------------------------------------------------------------------------------------------------------------------------
  /**
   * 
   * @return Status der Bestellung
   */
  @Override
  public String getStatus() {
    return this.status;
  }
  
  /**
  * 
  * @param statusString
  */  
  @Override
  public void setStatus(String statusString) {
    this.status = statusString;
  }
//-------------------------------------------------------------------------------------------------------------------------------------------
  /**
   * @return booking
   */
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "idBooking")
  public BookingEntity getBooking() {

    return this.booking;
  }

  /**
   * @param booking new value of {@link #getbooking}.
   */
  public void setBooking(BookingEntity booking) {

    this.booking = booking;
  }

  /**
   * @return invitedGuest
   */
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "idInvitedGuest")
  public InvitedGuestEntity getInvitedGuest() {

    return this.invitedGuest;
  }

  /**
   * @param invitedGuest new value of {@link #getinvitedGuest}.
   */
  public void setInvitedGuest(InvitedGuestEntity invitedGuest) {

    this.invitedGuest = invitedGuest;
  }

  /**
   * @return orderLines
   */
  @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
  public List<OrderLineEntity> getOrderLines() {

    return this.orderLines;
  }

  /**
   * @param orderLines new value of {@link #getorderLines}.
   */
  public void setOrderLines(List<OrderLineEntity> orderLines) {

    this.orderLines = orderLines;
  }

  @Override
  @Transient
  public Long getBookingId() {

    if (this.booking == null) {
      return null;
    }
    return this.booking.getId();
  }

  @Override
  public void setBookingId(Long bookingId) {

    if (bookingId == null) {
      this.booking = null;
    } else {
      BookingEntity bookingEntity = new BookingEntity();
      bookingEntity.setId(bookingId);
      this.booking = bookingEntity;
    }
  }

  @Override
  @Transient
  public Long getInvitedGuestId() {

    if (this.invitedGuest == null) {
      return null;
    }
    return this.invitedGuest.getId();
  }

  @Override
  public void setInvitedGuestId(Long invitedGuestId) {

    if (invitedGuestId == null) {
      this.invitedGuest = null;
    } else {
      InvitedGuestEntity invitedGuestEntity = new InvitedGuestEntity();
      invitedGuestEntity.setId(invitedGuestId);
      this.invitedGuest = invitedGuestEntity;
    }
  }

  /**
   * @return host
   */
  @OneToOne
  @JoinColumn(name = "idHost")
  public BookingEntity getHost() {

    return this.host;
  }

  /**
   * @param host new value of {@link #gethost}.
   */
  public void setHost(BookingEntity host) {

    this.host = host;
  }

  @Override
  @Transient
  public Long getHostId() {

    if (this.host == null) {
      return null;
    }
    return this.host.getId();
  }

  @Override
  public void setHostId(Long hostId) {

    if (hostId == null) {
      this.host = null;
    } else {
      BookingEntity bookingEntity = new BookingEntity();
      bookingEntity.setId(hostId);
      this.host = bookingEntity;
    }
  }

}
