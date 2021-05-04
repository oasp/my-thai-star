package com.devonfw.application.mtsj.ordermanagement.service.api.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.data.domain.Page;

import com.devonfw.application.mtsj.ordermanagement.common.api.to.OrderCto;
import com.devonfw.application.mtsj.ordermanagement.common.api.to.OrderEto;
import com.devonfw.application.mtsj.ordermanagement.common.api.to.OrderLineCto;
import com.devonfw.application.mtsj.ordermanagement.common.api.to.OrderLineEto;
import com.devonfw.application.mtsj.ordermanagement.common.api.to.OrderLineSearchCriteriaTo;
import com.devonfw.application.mtsj.ordermanagement.common.api.to.OrderSearchCriteriaTo;
import com.devonfw.application.mtsj.ordermanagement.common.api.to.OrderedDishesCto;
import com.devonfw.application.mtsj.ordermanagement.common.api.to.OrderedDishesSearchCriteriaTo;
import com.devonfw.application.mtsj.ordermanagement.logic.api.Ordermanagement;

/**
 * The service interface for REST calls in order to execute the logic of component {@link Ordermanagement}.
 */
@Path("/ordermanagement/v1")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface OrdermanagementRestService {

  /**
   * Delegates to {@link Ordermanagement#findOrder}.
   *
   * @param id the ID of the {@link OrderEto}
   * @return the {@link OrderEto}
   */
  @GET
  @Path("/order/{id}/")
  public OrderCto getOrder(@PathParam("id") long id);

  /**
   * Delegates to {@link Ordermanagement#saveOrder}.
   *
   * @param order the {@link OrderEto} to be saved
   * @return the recently created {@link OrderEto}
   */
  @POST
  @Path("/order/")
  public OrderEto saveOrder(OrderCto order);

  /**
   * Delegates to {@link Ordermanagement#deleteOrder}.
   *
   * @param id ID of the {@link OrderEto} to be deleted
   */
  @DELETE
  @Path("/order/{id}/")
  public boolean deleteOrder(@PathParam("id") long id);

  @GET
  @Path("/order/cancelorder/{id}/")
  public void cancelOrder(@PathParam("id") long id);

//----------------------------------------------------------------------------------------------------------------------

  @POST
  @Path("/order/orderstatus/{id}/")
  public void setOrderStatus(@PathParam("id") long id, String status);

//----------------------------------------------------------------------------------------------------------------------

  /**
   * Delegates to {@link Ordermanagement#findOrderCtos}.
   *
   * @param searchCriteriaTo the pagination and search criteria to be used for finding orders.
   * @return the {@link Page list} of matching {@link OrderCto}s.
   */
  @Path("/order/search")
  @POST
  public Page<OrderCto> findOrdersByPost(OrderSearchCriteriaTo searchCriteriaTo);

  /**
   * Delegates to {@link Ordermanagement#findOrderLine}.
   *
   * @param id the ID of the {@link OrderLineEto}
   * @return the {@link OrderLineEto}
   */
  @GET
  @Path("/orderline/{id}/")
  public OrderLineEto getOrderLine(@PathParam("id") long id);

  /**
   * Delegates to {@link Ordermanagement#saveOrderLine}.
   *
   * @param orderline the {@link OrderLineEto} to be saved
   * @return the recently created {@link OrderLineEto}
   */
  @POST
  @Path("/orderline/")
  public OrderLineEto saveOrderLine(OrderLineEto orderline);

  /**
   * Delegates to {@link Ordermanagement#deleteOrderLine}.
   *
   * @param id ID of the {@link OrderLineEto} to be deleted
   */
  @DELETE
  @Path("/orderline/{id}/")
  public void deleteOrderLine(@PathParam("id") long id);

  /**
   * Delegates to {@link Ordermanagement#findOrderLineEtos}.
   *
   * @param searchCriteriaTo the pagination and search criteria to be used for finding orderlines.
   * @return the {@link Page list} of matching {@link OrderLineEto}s.
   */
  @Path("/orderline/search")
  @POST
  public Page<OrderLineCto> findOrderLinesByPost(OrderLineSearchCriteriaTo searchCriteriaTo);

  @Path("/ordereddishes/history")
  @POST
  public Page<OrderedDishesCto> findOrderedDishes(OrderedDishesSearchCriteriaTo searchCriteriaTo);

}
