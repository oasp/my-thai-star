package com.devonfw.application.mtsj.bookingmanagement.dataaccess.api;

import javax.persistence.Entity;

import com.devonfw.application.mtsj.bookingmanagement.common.api.Table;
import com.devonfw.application.mtsj.general.dataaccess.api.ApplicationPersistenceEntity;

@Entity
@javax.persistence.Table(name = "\"TABLE\"")
public class TableEntity extends ApplicationPersistenceEntity implements Table {

  private Integer seatsNumber;

  private static final long serialVersionUID = 1L;

  /**
   * @return seatsNumber
   */
  public Integer getSeatsNumber() {

    return this.seatsNumber;
  }

  /**
   * @param seatsNumber new value of {@link #getseatsNumber}.
   */
  public void setSeatsNumber(Integer seatsNumber) {

    this.seatsNumber = seatsNumber;
  }

}
