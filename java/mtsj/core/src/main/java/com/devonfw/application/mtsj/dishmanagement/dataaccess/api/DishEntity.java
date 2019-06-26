package com.devonfw.application.mtsj.dishmanagement.dataaccess.api;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.devonfw.application.mtsj.dishmanagement.common.api.Dish;
import com.devonfw.application.mtsj.general.dataaccess.api.ApplicationPersistenceEntity;
import com.devonfw.application.mtsj.imagemanagement.dataaccess.api.ImageEntity;
import org.hibernate.envers.Audited;

/**
 * The {@link com.devonfw.application.mtsj.general.dataaccess.api.ApplicationPersistenceEntity persistent entity} for
 * {@link Dish}.
 */
@Entity
@Table(name = "Dish")
@Audited
public class DishEntity extends ApplicationPersistenceEntity implements Dish {

  private static final long serialVersionUID = 1L;

  private String name;

  private String description;

  private BigDecimal price;

  private ImageEntity image;

  private List<IngredientEntity> extras;

  private List<CategoryEntity> categories;

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
   * @return price
   */
  public BigDecimal getPrice() {

    return this.price;
  }

  /**
   * @param price new value of {@link #getPrice}.
   */
  public void setPrice(BigDecimal price) {

    this.price = price;
  }

  /**
   * @return image
   */
  @OneToOne
  @JoinColumn(name = "idImage")
  public ImageEntity getImage() {

    return this.image;
  }

  /**
   * @param image new value of {@link #getImage}.
   */
  public void setImage(ImageEntity image) {

    this.image = image;
  }

  /**
   * @return extras
   */
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "DishIngredient", joinColumns = {
  @javax.persistence.JoinColumn(name = "idDish") }, inverseJoinColumns = @javax.persistence.JoinColumn(name = "idIngredient"))
  public List<IngredientEntity> getExtras() {

    return this.extras;
  }

  /**
   * @param extras new value of {@link #getExtras}.
   */
  public void setExtras(List<IngredientEntity> extras) {

    this.extras = extras;
  }

  /**
   * @return categories
   */
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "DishCategory", joinColumns = {
  @javax.persistence.JoinColumn(name = "idDish") }, inverseJoinColumns = @javax.persistence.JoinColumn(name = "idCategory"))
  public List<CategoryEntity> getCategories() {

    return this.categories;
  }

  /**
   * @param categories new value of {@link #getCategories}.
   */
  public void setCategories(List<CategoryEntity> categories) {

    this.categories = categories;
  }

  @Override
  @Transient
  public Long getImageId() {

    if (this.image == null) {
      return null;
    }
    return this.image.getId();
  }

  @Override
  public void setImageId(Long imageId) {

    if (imageId == null) {
      this.image = null;
    } else {
      ImageEntity imageEntity = new ImageEntity();
      imageEntity.setId(imageId);
      this.image = imageEntity;
    }
  }

}
