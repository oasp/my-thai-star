package io.oasp.application.mtsj.ordermanagement.dataaccess.api;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import io.oasp.application.mtsj.dishmanagement.dataaccess.api.DishEntity;
import io.oasp.application.mtsj.general.dataaccess.api.ApplicationPersistenceEntity;
import io.oasp.application.mtsj.ordermanagement.common.api.OrderedDishes;

/**
 * {@link ApplicationPersistenceEntity Entity} that represents a single
 * {@link OrderedDishesPerMonth} of an {@link OrderedDishesPerMonth}.
 */
@Entity
@Immutable
@Table(name = "OrderedDishesPerMonth")
public class OrderedDishesPerMonthEntity extends ApplicationPersistenceEntity implements OrderedDishes {

  private static final long serialVersionUID = 1L;

  private DishEntity dish;

  // the number of ordered dishes per month
  private Integer amount;

  private Double temperature;

  private Timestamp bookingdate;

  /**
   * @return dish
   */
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "idDish")
  public DishEntity getDish() {

    return this.dish;
  }

  /**
   * @param dish new value of {@link #getDish}.
   */
  public void setDish(DishEntity dish) {

    this.dish = dish;
  }

  @Override
  public Integer getAmount() {

    return this.amount;
  }

  @Override
  public void setAmount(Integer amount) {

    this.amount = amount;
  }

  @Override
  public Double getTemperature() {

    return this.temperature;
  }

  @Override
  public void setTemperature(Double temperature) {

    this.temperature = temperature;
  }

  @Override
  public Timestamp getBookingdate() {

    return this.bookingdate;
  }

  @Override
  public void setBookingdate(Timestamp bookingdate) {

    this.bookingdate = bookingdate;
  }
}
