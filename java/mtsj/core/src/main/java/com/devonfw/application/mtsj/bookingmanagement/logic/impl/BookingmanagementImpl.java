package com.devonfw.application.mtsj.bookingmanagement.logic.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.devonfw.application.mtsj.bookingmanagement.common.api.exception.CancelInviteNotAllowedException;
import com.devonfw.application.mtsj.bookingmanagement.common.api.to.BookingCto;
import com.devonfw.application.mtsj.bookingmanagement.common.api.to.BookingEto;
import com.devonfw.application.mtsj.bookingmanagement.common.api.to.BookingSearchCriteriaTo;
import com.devonfw.application.mtsj.bookingmanagement.common.api.to.InvitedGuestEto;
import com.devonfw.application.mtsj.bookingmanagement.common.api.to.InvitedGuestSearchCriteriaTo;
import com.devonfw.application.mtsj.bookingmanagement.common.api.to.TableEto;
import com.devonfw.application.mtsj.bookingmanagement.common.api.to.TableSearchCriteriaTo;
import com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.BookingEntity;
import com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.InvitedGuestEntity;
import com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.TableEntity;
import com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.repo.BookingRepository;
import com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.repo.InvitedGuestRepository;
import com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.repo.TableRepository;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.Bookingmanagement;
import com.devonfw.application.mtsj.general.common.impl.security.ApplicationAccessControlConfig;
import com.devonfw.application.mtsj.general.logic.base.AbstractComponentFacade;
import com.devonfw.application.mtsj.mailservice.logic.api.Mail;
import com.devonfw.application.mtsj.ordermanagement.common.api.to.OrderCto;
import com.devonfw.application.mtsj.ordermanagement.common.api.to.OrderEto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.Ordermanagement;
import com.devonfw.application.mtsj.usermanagement.common.api.to.UserEto;

/**
 * Implementation of component interface of bookingmanagement
 */

@Named
@Transactional
public class BookingmanagementImpl extends AbstractComponentFacade implements Bookingmanagement {

  /**
   * Logger instance.
   */
  private static final Logger LOG = LoggerFactory.getLogger(BookingmanagementImpl.class);

  @Value("${client.port}")
  private int clientPort;

  @Value("${server.servlet.context-path}")
  private String serverContextPath;

  @Value("${mythaistar.hourslimitcancellation}")
  private int hoursLimit;

  /**
   * @see #getBookingDao()
   */
  @Inject
  private BookingRepository bookingDao;

  /**
   * @see #getInvitedGuestDao()
   */
  @Inject
  private InvitedGuestRepository invitedGuestDao;

  /**
   * @see #getTableDao()
   */
  @Inject
  private TableRepository tableDao;

  @Inject
  private Ordermanagement orderManagement;

  @Inject
  private Mail mailService;

  /**
   * The constructor.
   */
  public BookingmanagementImpl() {

    super();
  }

  @Override
  public BookingCto findBooking(Long id) {

    LOG.debug("Get Booking with id {} from database.", id);
    BookingEntity entity = getBookingDao().find(id);
    BookingCto cto = new BookingCto();
    cto.setBooking(getBeanMapper().map(entity, BookingEto.class));
    cto.setTable(getBeanMapper().map(entity.getTable(), TableEto.class));
    cto.setOrder(getBeanMapper().map(entity.getOrder(), OrderEto.class));
    cto.setInvitedGuests(getBeanMapper().mapList(entity.getInvitedGuests(), InvitedGuestEto.class));
    cto.setOrders(getBeanMapper().mapList(entity.getOrders(), OrderEto.class));
    return cto;
  }

  @Override
  public BookingCto findBookingByToken(String token) {

    BookingEntity entity = getBookingDao().findBookingByToken(token);
    BookingCto cto = null;
    if (entity != null) {
      cto = new BookingCto();
      cto.setBooking(getBeanMapper().map(entity, BookingEto.class));
      cto.setTable(getBeanMapper().map(entity.getTable(), TableEto.class));
      cto.setOrder(getBeanMapper().map(entity.getOrder(), OrderEto.class));
      cto.setInvitedGuests(getBeanMapper().mapList(entity.getInvitedGuests(), InvitedGuestEto.class));
      cto.setOrders(getBeanMapper().mapList(entity.getOrders(), OrderEto.class));
    }
    return cto;
  }

  @Override
  public InvitedGuestEto findInvitedGuestByToken(String token) {

    return getBeanMapper().map(getInvitedGuestDao().findInvitedGuestByToken(token), InvitedGuestEto.class);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_FIND_BOOKING)
  public Page<BookingCto> findBookingsByPost(BookingSearchCriteriaTo criteria) {

    return findBookingCtos(criteria);
  }

  @Override
  public Page<BookingCto> findBookingCtos(BookingSearchCriteriaTo criteria) {

    Page<BookingCto> pagListTo = null;
    Page<BookingEntity> bookings = getBookingDao().findBookings(criteria);
    List<BookingCto> ctos = new ArrayList<>();
    for (BookingEntity entity : bookings.getContent()) {
      BookingCto cto = new BookingCto();
      cto.setBooking(getBeanMapper().map(entity, BookingEto.class));
      cto.setInvitedGuests(getBeanMapper().mapList(entity.getInvitedGuests(), InvitedGuestEto.class));
      cto.setOrder(getBeanMapper().map(entity.getOrder(), OrderEto.class));
      cto.setTable(getBeanMapper().map(entity.getTable(), TableEto.class));
      cto.setUser(getBeanMapper().map(entity.getUser(), UserEto.class));
      cto.setOrders(getBeanMapper().mapList(entity.getOrders(), OrderEto.class));
      ctos.add(cto);
    }
    if (ctos.size() > 0) {
      Pageable pagResultTo = PageRequest.of(criteria.getPageable().getPageNumber(), ctos.size());
      pagListTo = new PageImpl<>(ctos, pagResultTo, bookings.getTotalElements());
    }
    return pagListTo;
  }

  @Override
  public boolean deleteBooking(Long bookingId) {

    List<OrderCto> bookingOrders = this.orderManagement.findOrders(bookingId);
    for (OrderCto orderCto : bookingOrders) {
      boolean deleteOrderResult = this.orderManagement.deleteOrder(orderCto.getOrder().getId());
      if (deleteOrderResult) {
        LOG.debug("The order with id '{}' has been deleted.", orderCto.getOrder().getId());
      }
    }

    BookingEntity booking = getBookingDao().find(bookingId);
    getBookingDao().delete(booking);
    LOG.debug("The booking with id '{}' has been deleted.", bookingId);
    return true;
  }

  @Override
  public BookingEto saveBooking(BookingCto booking) {

    Objects.requireNonNull(booking, "booking");
    BookingEntity bookingEntity = getBeanMapper().map(booking.getBooking(), BookingEntity.class);
    bookingEntity.setStatus("order taken");
    List<InvitedGuestEntity> invited = getBeanMapper().mapList(booking.getInvitedGuests(), InvitedGuestEntity.class);

    for (InvitedGuestEntity invite : invited) {
      try {
        invite.setGuestToken(buildToken(invite.getEmail(), "GB_"));
      } catch (NoSuchAlgorithmException e) {
        LOG.debug("MD5 Algorithm not available at the enviroment");
      }
      invite.setAccepted(false);
    }

    bookingEntity.setInvitedGuests(invited);
    try {
      bookingEntity.setBookingToken(buildToken(bookingEntity.getEmail(), "CB_"));
    } catch (NoSuchAlgorithmException e) {
      LOG.debug("MD5 Algorithm not available at the enviroment");
    }

    bookingEntity.setCreationDate(Instant.now());
    bookingEntity.setExpirationDate(bookingEntity.getBookingDate().minus(Duration.ofHours(1)));

    bookingEntity.setInvitedGuests(getBeanMapper().mapList(invited, InvitedGuestEntity.class));

    BookingEntity resultEntity = getBookingDao().save(bookingEntity);
    LOG.debug("Booking with id '{}' has been created.", resultEntity.getId());
    for (InvitedGuestEntity invitedGuest : resultEntity.getInvitedGuests()) {
      invitedGuest.setBookingId(resultEntity.getId());
      InvitedGuestEntity resultInvitedGuest = getInvitedGuestDao().save(invitedGuest);
      LOG.info("OrderLine with id '{}' has been created.", resultInvitedGuest.getId());
    }
    LOG.debug("Booking with id '{}' has been created.", resultEntity.getId());

    sendConfirmationEmails(resultEntity);

    return getBeanMapper().map(resultEntity, BookingEto.class);
  }

  @Override
  public String buildToken(String email, String type) throws NoSuchAlgorithmException {

    Instant now = Instant.now();
    LocalDateTime ldt1 = LocalDateTime.ofInstant(now, ZoneId.systemDefault());
    String date = String.format("%04d", ldt1.getYear()) + String.format("%02d", ldt1.getMonthValue())
        + String.format("%02d", ldt1.getDayOfMonth()) + "_";

    String time = String.format("%02d", ldt1.getHour()) + String.format("%02d", ldt1.getMinute())
        + String.format("%02d", ldt1.getSecond());

    MessageDigest md = MessageDigest.getInstance("MD5");
    md.update((email + date + time).getBytes());
    byte[] digest = md.digest();
    StringBuilder sb = new StringBuilder();
    for (byte b : digest) {
      sb.append(String.format("%02x", b & 0xff));
    }
    return type + date + sb;
  }

  /**
   * Returns the field 'bookingDao'.
   *
   * @return the {@link BookingDao} instance.
   */
  public BookingRepository getBookingDao() {

    return this.bookingDao;
  }

  @Override
  public InvitedGuestEto findInvitedGuest(Long id) {

    LOG.debug("Get InvitedGuest with id {} from database.", id);
    return getBeanMapper().map(getInvitedGuestDao().find(id), InvitedGuestEto.class);
  }

  public List<InvitedGuestEto> findInvitedGuestByBooking(Long bookingId) {

    List<InvitedGuestEntity> invitedGuestList = getInvitedGuestDao().findInvitedGuestByBooking(bookingId);
    List<InvitedGuestEto> invitedGuestEtoList = new ArrayList<InvitedGuestEto>();
    for (InvitedGuestEntity invitedGuestEntity : invitedGuestList) {
      invitedGuestEtoList.add(getBeanMapper().map(invitedGuestEntity, InvitedGuestEto.class));
    }
    return invitedGuestEtoList;
  }

  @Override
  public Page<InvitedGuestEto> findInvitedGuestEtos(InvitedGuestSearchCriteriaTo criteria) {

    Page<InvitedGuestEntity> invitedguests = getInvitedGuestDao().findInvitedGuests(criteria);
    return mapPaginatedEntityList(invitedguests, InvitedGuestEto.class);
  }

  @Override
  public boolean deleteInvitedGuest(Long invitedGuestId) {

    InvitedGuestEntity invitedGuest = getInvitedGuestDao().find(invitedGuestId);
    List<OrderCto> guestOrdersCto = this.orderManagement
        .findOrdersByBookingToken(invitedGuest.getBooking().getBookingToken());
    for (OrderCto orderCto : guestOrdersCto) {
      this.orderManagement.deleteOrder(orderCto.getOrder().getId());
    }
    getInvitedGuestDao().delete(invitedGuest);
    LOG.debug("The invitedGuest with id '{}' has been deleted.", invitedGuestId);
    return true;
  }

  @Override
  public InvitedGuestEto saveInvitedGuest(InvitedGuestEto invitedGuest) {

    Objects.requireNonNull(invitedGuest, "invitedGuest");
    InvitedGuestEntity invitedGuestEntity = getBeanMapper().map(invitedGuest, InvitedGuestEntity.class);

    // initialize, validate invitedGuestEntity here if necessary
    InvitedGuestEntity resultEntity = getInvitedGuestDao().save(invitedGuestEntity);
    LOG.debug("InvitedGuest with id '{}' has been created.", resultEntity.getId());

    return getBeanMapper().map(resultEntity, InvitedGuestEto.class);
  }

  /**
   * Returns the field 'invitedGuestDao'.
   *
   * @return the {@link InvitedGuestDao} instance.
   */
  public InvitedGuestRepository getInvitedGuestDao() {

    return this.invitedGuestDao;
  }

  @Override
  public TableEto findTable(Long id) {

    LOG.debug("Get Table with id {} from database.", id);
    return getBeanMapper().map(getTableDao().find(id), TableEto.class);
  }

  @Override
  public Page<TableEto> findTableEtos(TableSearchCriteriaTo criteria) {

    Page<TableEntity> tables = getTableDao().findTables(criteria);
    return mapPaginatedEntityList(tables, TableEto.class);
  }

  @Override
  public boolean deleteTable(Long tableId) {

    TableEntity table = getTableDao().find(tableId);
    getTableDao().delete(table);
    LOG.debug("The table with id '{}' has been deleted.", tableId);
    return true;
  }

  @Override
  public TableEto saveTable(TableEto table) {

    Objects.requireNonNull(table, "table");
    TableEntity tableEntity = getBeanMapper().map(table, TableEntity.class);

    // initialize, validate tableEntity here if necessary
    TableEntity resultEntity = getTableDao().save(tableEntity);
    LOG.debug("Table with id '{}' has been created.", resultEntity.getId());

    return getBeanMapper().map(resultEntity, TableEto.class);
  }

  public InvitedGuestEto acceptInvite(String guestToken) {

    Objects.requireNonNull(guestToken);
    InvitedGuestEto invited = findInvitedGuestByToken(guestToken);
    invited.setAccepted(true);
    BookingCto booking = findBooking(invited.getBookingId());
    sendConfirmationAcceptedInviteToGuest(booking, invited);
    sendConfirmationActionToHost(booking, invited, "accepted");
    return saveInvitedGuest(invited);
  }

  @Override
  public InvitedGuestEto declineInvite(String guestToken) {

    Objects.requireNonNull(guestToken);
    InvitedGuestEto invited = findInvitedGuestByToken(guestToken);
    InvitedGuestEntity invitedEntity = getInvitedGuestDao().find(invited.getId());
    invited.setAccepted(false);

    List<OrderCto> guestOrdersCto = this.orderManagement.findOrdersByInvitedGuest(invitedEntity.getId());
    for (OrderCto orderCto : guestOrdersCto) {
      this.orderManagement.deleteOrder(orderCto.getOrder().getId());
    }
    BookingCto booking = findBooking(invited.getBookingId());
    sendConfirmationActionToHost(booking, invited, "declined");
    sendDeclineConfirmationToGuest(booking, invited);
    return saveInvitedGuest(invited);
  }

  @Override
  public void cancelInvite(String bookingToken) {

    Objects.requireNonNull(bookingToken, "bookingToken");
    BookingCto bookingCto = findBookingByToken(bookingToken);

    if (bookingCto != null) {
      if (!cancelInviteAllowed(bookingCto.getBooking())) {
        throw new CancelInviteNotAllowedException();
      }
      List<InvitedGuestEto> guestsEto = findInvitedGuestByBooking(bookingCto.getBooking().getId());
      if (guestsEto != null) {
        for (InvitedGuestEto guestEto : guestsEto) {
          deleteInvitedGuest(guestEto.getId());
          sendCancellationEmailToGuest(bookingCto.getBooking(), guestEto);
        }
      }
      // delete booking and related orders
      deleteBooking(bookingCto.getBooking().getId());
      sendCancellationEmailToHost(bookingCto.getBooking());
    }
  }

  private void sendConfirmationEmails(BookingEntity booking) {

    if (!booking.getInvitedGuests().isEmpty()) {
      for (InvitedGuestEntity guest : booking.getInvitedGuests()) {
        sendInviteEmailToGuest(guest, booking);
      }
    }

    sendConfirmationEmailToHost(booking);
  }

  private void sendInviteEmailToGuest(InvitedGuestEntity guest, BookingEntity booking) {

    try {
      StringBuilder invitedMailContent = new StringBuilder();
      invitedMailContent.append("MY THAI STAR").append("\n");
      invitedMailContent.append("Hi ").append(guest.getEmail()).append("\n");
      invitedMailContent.append(booking.getEmail()).append(" has invited you to an event on My Thai Star restaurant")
          .append("\n");
      invitedMailContent.append("Booking Date: ").append(booking.getBookingDate()).append("\n");

      String linkAccept = getClientUrl() + "/booking/acceptInvite/" + guest.getGuestToken();

      String linkDecline = getClientUrl() + "/booking/rejectInvite/" + guest.getGuestToken();

      invitedMailContent.append("To accept: ").append(linkAccept).append("\n");
      invitedMailContent.append("To decline: ").append(linkDecline).append("\n");

      this.mailService.sendMail(guest.getEmail(), "Event invite", invitedMailContent.toString());
    } catch (Exception e) {
      LOG.error("Email not sent. {}", e.getMessage());
    }

  }

  private void sendConfirmationEmailToHost(BookingEntity booking) {

    try {
      StringBuilder hostMailContent = new StringBuilder();
      hostMailContent.append("MY THAI STAR").append("\n");
      hostMailContent.append("Hi ").append(booking.getEmail()).append("\n");
      hostMailContent.append("Your booking has been confirmed.").append("\n");
      hostMailContent.append("Host: ").append(booking.getName()).append("<").append(booking.getEmail()).append(">")
          .append("\n");
      hostMailContent.append("Booking CODE: ").append(booking.getBookingToken()).append("\n");
      hostMailContent.append("Booking Date: ").append(booking.getBookingDate()).append("\n");
      if (!booking.getInvitedGuests().isEmpty()) {
        hostMailContent.append("Guest list:").append("\n");
        for (InvitedGuestEntity guest : booking.getInvitedGuests()) {
          hostMailContent.append("-").append(guest.getEmail()).append("\n");
        }
      }
      String cancellationLink = getClientUrl() + "/booking/cancel/" + booking.getBookingToken();
      hostMailContent.append(cancellationLink).append("\n");
      this.mailService.sendMail(booking.getEmail(), "Booking confirmation", hostMailContent.toString());
    } catch (Exception e) {
      LOG.error("Email not sent. {}", e.getMessage());
    }
  }

  private void sendConfirmationAcceptedInviteToGuest(BookingCto booking, InvitedGuestEto guest) {

    try {
      StringBuilder guestMailContent = new StringBuilder();
      guestMailContent.append("MY THAI STAR").append("\n");
      guestMailContent.append("Hi ").append(guest.getEmail()).append("\n");
      guestMailContent.append("You have accepted the invite to an event in our restaurant.").append("\n");
      guestMailContent.append("Host: ").append(booking.getBooking().getName()).append("<")
          .append(booking.getBooking().getEmail()).append(">").append("\n");
      guestMailContent.append("Guest CODE: ").append(guest.getGuestToken()).append("\n");
      guestMailContent.append("Booking Date: ").append(booking.getBooking().getBookingDate()).append("\n");

      String cancellationLink = getClientUrl() + "/booking/rejectInvite/" + guest.getGuestToken();

      guestMailContent.append("To cancel invite: ").append(cancellationLink).append("\n");
      this.mailService.sendMail(guest.getEmail(), "Invite accepted", guestMailContent.toString());
    } catch (Exception e) {
      LOG.error("Email not sent. {}", e.getMessage());
    }
  }

  private void sendConfirmationActionToHost(BookingCto booking, InvitedGuestEto guest, String action) {

    try {
      StringBuilder mailContent = new StringBuilder();
      mailContent.append("MY THAI STAR").append("\n");
      mailContent.append("Hi ").append(booking.getBooking().getEmail()).append("\n");
      mailContent.append(guest.getEmail()).append(" has ").append(action).append(" your invitation for the event on ")
          .append(booking.getBooking().getBookingDate()).append("\n");

      this.mailService.sendMail(booking.getBooking().getEmail(), "Invite " + action, mailContent.toString());
    } catch (Exception e) {
      LOG.error("Email not sent. {}", e.getMessage());
    }
  }

  private void sendDeclineConfirmationToGuest(BookingCto booking, InvitedGuestEto guest) {

    try {
      StringBuilder guestMailContent = new StringBuilder();
      guestMailContent.append("MY THAI STAR").append("\n");
      guestMailContent.append("Hi ").append(guest.getEmail()).append("\n");
      guestMailContent.append("You have declined the invitation from ").append(booking.getBooking().getName())
          .append("<").append(booking.getBooking().getEmail()).append(">").append(" for the event on ")
          .append(booking.getBooking().getBookingDate()).append("\n");

      this.mailService.sendMail(guest.getEmail(), "Invite declined", guestMailContent.toString());
    } catch (Exception e) {
      LOG.error("Email not sent. {}", e.getMessage());
    }
  }

  private void sendCancellationEmailToGuest(BookingEto booking, InvitedGuestEto guest) {

    try {
      StringBuilder mailContent = new StringBuilder();
      mailContent.append("MY THAI STAR").append("\n");
      mailContent.append("Hi ").append(guest.getEmail()).append("\n");
      mailContent.append("The invitation from ").append(booking.getEmail()).append(" for the event on ")
          .append(booking.getBookingDate()).append(" has been cancelled.").append("\n");
      this.mailService.sendMail(guest.getEmail(), "Event cancellation", mailContent.toString());
    } catch (Exception e) {
      LOG.error("Email not sent. {}", e.getMessage());
    }
  }

  private void sendCancellationEmailToHost(BookingEto booking) {

    try {
      StringBuilder mailContent = new StringBuilder();
      mailContent.append("MY THAI STAR").append("\n");
      mailContent.append("Hi ").append(booking.getEmail()).append("\n");
      mailContent.append("The invitation for the event on ").append(booking.getBookingDate())
          .append(" has been cancelled.").append("\n");
      this.mailService.sendMail(booking.getEmail(), "Event cancellation", mailContent.toString());
    } catch (Exception e) {
      LOG.error("Email not sent. {}", e.getMessage());
    }
  }

  /**
   * Returns the field 'tableDao'.
   *
   * @return the {@link TableDao} instance.
   */
  public TableRepository getTableDao() {

    return this.tableDao;
  }

  private boolean cancelInviteAllowed(BookingEto booking) {

    Long bookingTimeMillis = booking.getBookingDate().toEpochMilli();
    Long cancellationLimit = bookingTimeMillis - (3600000 * this.hoursLimit);
    Long now = Instant.now().toEpochMilli();

    return (now > cancellationLimit) ? false : true;
  }

  private String getClientUrl() {

    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    String clientUrl = request.getHeader("origin");
    if (clientUrl == null) {
      return "http://localhost:" + this.clientPort;
    }
    return clientUrl;
  }

}
