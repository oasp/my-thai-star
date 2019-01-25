package com.devonfw.application.mtsj.dishmanagement.logic.api.to;

import java.math.BigDecimal;
import java.util.List;

import com.devonfw.application.mtsj.general.common.api.to.AbstractSearchCriteriaTo;
import com.devonfw.module.basic.common.api.query.StringSearchConfigTo;

/**
 * used to find {@link com.devonfw.application.mtsj.dishmanagement.common.api.Dish}s.
 *
 */
public class DishSearchCriteriaTo extends AbstractSearchCriteriaTo {

  private static final long serialVersionUID = 1L;

  private List<CategoryEto> categories;

  private BigDecimal maxPrice;

  private int minLikes;

  private String searchBy;

  private int showOrder;

  private boolean isFav;

  private StringSearchConfigTo searchByOption;

  /**
   * The constructor.
   */
  public DishSearchCriteriaTo() {

    super();
  }

  /**
   * @return categories
   */
  public List<CategoryEto> getCategories() {

    return this.categories;
  }

  /**
   * @param categories new value of {@link #getCategories}.
   */
  public void setCategories(List<CategoryEto> categories) {

    this.categories = categories;
  }

  /**
   * @return maxPrice
   */
  public BigDecimal getMaxPrice() {

    return this.maxPrice;
  }

  /**
   * @param maxPrice new value of {@link #getMaxPrice}.
   */
  public void setMaxPrice(BigDecimal maxPrice) {

    this.maxPrice = maxPrice;
  }

  /**
   * @return minLikes
   */
  public int getMinLikes() {

    return this.minLikes;
  }

  /**
   * @param minLikes new value of {@link #getMinLikes}.
   */
  public void setMinLikes(int minLikes) {

    this.minLikes = minLikes;
  }

  /**
   * @return searchBy
   */
  public String getSearchBy() {

    return this.searchBy;
  }

  /**
   * @param searchBy new value of {@link #getSearchBy}.
   */
  public void setSearchBy(String searchBy) {

    this.searchBy = searchBy;
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

  /**
   * @return isFav
   */
  public boolean isFav() {

    return this.isFav;
  }

  /**
   * @param isFav new value of {@link #isFav}.
   */
  public void setFav(boolean isFav) {

    this.isFav = isFav;
  }

  /**
   * @return searchByOption
   */
  public StringSearchConfigTo getSearchByOption() {

    return this.searchByOption;
  }

  /**
   * @param searchByOption new value of {@link #getsearchByOption}.
   */
  public void setSearchByOption(StringSearchConfigTo searchByOption) {

    this.searchByOption = searchByOption;
  }



}
