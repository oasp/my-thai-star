package com.devonfw.application.mtsj.dishmanagement.dataaccess.api;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.devonfw.application.mtsj.dishmanagement.common.api.Category;
import com.devonfw.application.mtsj.general.dataaccess.api.ApplicationPersistenceEntity;
import org.hibernate.envers.Audited;

/**
 * The {@link com.devonfw.application.mtsj.general.dataaccess.api.ApplicationPersistenceEntity persistent entity} for
 * {@link CategoryEntity}.
 */
@Entity
@Table(name = "Category")
@Audited
public class CategoryEntity extends ApplicationPersistenceEntity implements Category {

  private static final long serialVersionUID = 1L;

  private String name;

  private String description;

  private int showOrder;

  /**
   * @return name
   */
  public String getName() {

    return this.name;
  }

  /**
   * @param name new value of {@link #getName}.
   */
  public void setName(String name) {

    this.name = name;
  }

  /**
   * @return description
   */
  public String getDescription() {

    return this.description;
  }

  /**
   * @param description new value of {@link #getDescription}.
   */
  public void setDescription(String description) {

    this.description = description;
  }

  /**
   * @return showOrder
   */
  public int getShowOrder() {

    return this.showOrder;
  }

  /**
   * @param showOrder new value of {@link #getShowOrder}.
   */
  public void setShowOrder(int showOrder) {

    this.showOrder = showOrder;
  }

}
