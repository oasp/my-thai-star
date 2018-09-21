package io.oasp.application.mtsj.predictionmanagement.dataaccess.api;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import io.oasp.application.mtsj.dishmanagement.dataaccess.api.DishEntity;
import io.oasp.application.mtsj.predictionmanagement.common.api.PredictionModelData;

/**
 * The persistent entity for {@link PredictionModelDataEntity}.
 */
@Entity
@Table(name = "PREDICTION_ALL_MODELS")
@IdClass(PredictionModelDataId.class)
public class PredictionModelDataEntity implements PredictionModelData {

  private static final long serialVersionUID = 1L;

  private DishEntity dish;
  private String key;
  private String value;

  @Id
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "IDDISH")
  public DishEntity getDish() {

    return this.dish;
  }

  public void setDish(DishEntity dish) {

    this.dish = dish;
  }

  @Override
  @Transient
  public Long getDishId() {

    if (this.dish == null) {
      return null;
    }
    return this.dish.getId();
  }

  @Id
  @Override
  public String getKey() {

    return this.key;
  }

  @Override
  public void setKey(String key) {

    this.key = key;
  }

  @Override
  public String getValue() {

    return this.value;
  }

  @Override
  public void setValue(String value) {

    this.value = value;
  }

}
