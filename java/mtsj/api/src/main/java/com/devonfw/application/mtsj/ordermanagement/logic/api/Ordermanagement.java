package com.devonfw.application.mtsj.ordermanagement.logic.api;

import java.util.List;

import org.springframework.data.domain.Page;

import com.devonfw.application.mtsj.ordermanagement.common.api.to.OrderCto;
import com.devonfw.application.mtsj.ordermanagement.common.api.to.OrderEto;
import com.devonfw.application.mtsj.ordermanagement.common.api.to.OrderLineCto;
import com.devonfw.application.mtsj.ordermanagement.common.api.to.OrderLineEto;
import com.devonfw.application.mtsj.ordermanagement.common.api.to.OrderLineSearchCriteriaTo;
import com.devonfw.application.mtsj.ordermanagement.common.api.to.OrderSearchCriteriaTo;
import com.devonfw.application.mtsj.ordermanagement.common.api.to.OrderedDishesCto;
import com.devonfw.application.mtsj.ordermanagement.common.api.to.OrderedDishesSearchCriteriaTo;

/**
 * Interface for Ordermanagement component.
 */
public interface Ordermanagement {

  /**
   * Returns a Order by its id 'id'.
   *
   * @param id The id 'id' of the Order.
   * @return The {@link OrderEto} with id 'id'
   */
  OrderCto findOrder(Long id);

  /**
   * Returns a paginated list of Orders matching the search criteria. Needs Authorization.
   *
   * @param criteria the {@link OrderSearchCriteriaTo}.
   * @return the {@link List} of matching {@link OrderCto}s.
   */
  Page<OrderCto> findOrdersByPost(OrderSearchCriteriaTo criteria);

  /**
   * Returns a paginated list of Orders matching the search criteria.
   *
   * @param criteria the {@link OrderSearchCriteriaTo}.
   * @return the {@link List} of matching {@link OrderCto}s.
   */
  Page<OrderCto> findOrderCtos(OrderSearchCriteriaTo criteria);

  /**
   * Returns the list of OrderCto
   *
   * @param invitedGuestId
   * @return the list {@link OrderCto}
   */
  List<OrderCto> findOrdersByInvitedGuest(Long invitedGuestId);

  /**
   * Returns the list of OrderCtos
   *
   * @param bookingToken
   * @return the list {@link OrderCto}
   */
  List<OrderCto> findOrdersByBookingToken(String bookingToken);

  /**
   * Returns the list of OrderCtos
   *
   * @param idBooking
   * @return the list {@link OrderCto}
   */
  List<OrderCto> findOrders(Long idBooking);

  /**
   * Deletes a order from the database by its id 'orderId'.
   *
   * @param orderId Id of the order to delete
   * @return boolean <code>true</code> if the order can be deleted, <code>false</code> otherwise
   */
  boolean deleteOrder(Long orderId);

 //------------------------------------------------------------------------------------------------------------------------------------------- 
  /**
   * 
   * @param orderId Id of order thats status is to be set
   * @param statusString new status as String
   * @return boolean <code>true</code> if the order can be deleted, else <code>false</code> 
   */
  boolean setOrderStatus(long orderId, String statusString);
//-------------------------------------------------------------------------------------------------------------------------------------------

  /**
   * Saves a order and store it in the database.
   *
   * @param order the {@link OrderEto} to create.
   * @return the new {@link OrderEto} that has been saved with ID and version.
   */
  OrderEto saveOrder(OrderCto order);

  /**
   * Returns a OrderLine by its id 'id'.
   *
   * @param id The id 'id' of the OrderLine.
   * @return The {@link OrderLineEto} with id 'id'
   */
  OrderLineEto findOrderLine(Long id);

  /**
   * Returns a paginated list of OrderLines matching the search criteria.
   *
   * @param criteria the {@link OrderLineSearchCriteriaTo}.
   * @return the {@link List} of matching {@link OrderLineEto}s.
   */
  // PaginatedListTo<OrderLineEto> findOrderLineEtos(OrderLineSearchCriteriaTo criteria);
  Page<OrderLineCto> findOrderLineCtos(OrderLineSearchCriteriaTo criteria);

  /**
   * Deletes a orderLine from the database by its id 'orderLineId'.
   *
   * @param orderLineId Id of the orderLine to delete
   * @return boolean <code>true</code> if the orderLine can be deleted, <code>false</code> otherwise
   */
  boolean deleteOrderLine(Long orderLineId);

  /**
   * Saves a orderLine and store it in the database.
   *
   * @param orderLine the {@link OrderLineEto} to create.
   * @return the new {@link OrderLineEto} that has been saved with ID and version.
   */
  OrderLineEto saveOrderLine(OrderLineEto orderLine);

  Page<OrderedDishesCto> findOrderedDishes(OrderedDishesSearchCriteriaTo criteria);

}
