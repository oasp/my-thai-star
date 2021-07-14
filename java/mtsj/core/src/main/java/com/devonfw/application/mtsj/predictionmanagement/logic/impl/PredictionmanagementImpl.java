package com.devonfw.application.mtsj.predictionmanagement.logic.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.devonfw.application.mtsj.dishmanagement.dataaccess.api.DishEntity;
import com.devonfw.application.mtsj.dishmanagement.dataaccess.api.repo.DishRepository;
import com.devonfw.application.mtsj.general.common.impl.security.ApplicationAccessControlConfig;
import com.devonfw.application.mtsj.general.logic.base.AbstractComponentFacade;
import com.devonfw.application.mtsj.predictionmanagement.common.api.PredictionDayData;
import com.devonfw.application.mtsj.predictionmanagement.common.api.to.PredictionDataTo;
import com.devonfw.application.mtsj.predictionmanagement.common.api.to.PredictionDayDataEto;
import com.devonfw.application.mtsj.predictionmanagement.common.api.to.PredictionSearchCriteriaTo;
import com.devonfw.application.mtsj.predictionmanagement.dataaccess.api.PredictionDayDataEntity;
import com.devonfw.application.mtsj.predictionmanagement.dataaccess.api.PredictionForecastDataEntity;
import com.devonfw.application.mtsj.predictionmanagement.dataaccess.api.repo.PredictionDayDataRepository;
import com.devonfw.application.mtsj.predictionmanagement.dataaccess.api.repo.PredictionModelDataRepository;
import com.devonfw.application.mtsj.predictionmanagement.logic.api.Predictionmanagement;

/**
 * Implementation of component interface of predictionmanagement
 */
@Named
@Transactional
public class PredictionmanagementImpl extends AbstractComponentFacade implements Predictionmanagement {

  /**
   * Logger instance.
   */
  private static final Logger LOG = LoggerFactory.getLogger(PredictionmanagementImpl.class);

  private PredictionDayDataRepository predictionDayDataRepository;

  /**
   * @param predictionDayDataRepository new value of {@link #getpredictionDayDataRepository}.
   */
  @Inject
  public void setPredictionDayDataRepository(PredictionDayDataRepository predictionDayDataRepository) {

    this.predictionDayDataRepository = predictionDayDataRepository;
  }

  private PredictionModelDataRepository predictionModelDataRepository;

  /**
   * @param predictionModelDataRepository new value of {@link #getpredictionModelDataRepository}.
   */
  @Inject
  public void setPredictionModelDataRepository(PredictionModelDataRepository predictionModelDataRepository) {

    this.predictionModelDataRepository = predictionModelDataRepository;
  }

  private DishRepository dishRepository;

  /**
   * @param dishRepository new value of {@link #getdishRepository}.
   */
  @Inject
  public void setDishRepository(DishRepository dishRepository) {

    this.dishRepository = dishRepository;
  }

  private EntityManager entityManager;

  /**
   * @param entityManager new value of {@link #getentityManager}.
   */
  @PersistenceContext
  public void setEntityManager(EntityManager entityManager) {

    this.entityManager = entityManager;
  }

  /**
   * The constructor.
   */
  public PredictionmanagementImpl() {

    super();
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_FIND_NEXT_WEEK_PREDICTION)
  public PredictionDataTo getNextWeekPrediction(PredictionSearchCriteriaTo criteria) {

    LOG.debug("Generate predictions for next week.");

    // delete old forecast data
    this.predictionDayDataRepository.deletePredictionForecastData();

    // add new forecast data
    for (int i = 0; i < criteria.getTemperatures().size(); i++) {
      PredictionForecastDataEntity forecastData = new PredictionForecastDataEntity();
      forecastData.setTimestamp(i + 1);
      forecastData.setTemperature(criteria.getTemperatures().get(i) == null ? 0 : criteria.getTemperatures().get(i));
      forecastData.setHoliday(criteria.getHolidays().get(i) == null ? 0 : 1);
      this.entityManager.persist(forecastData);
    }

    PredictionDataTo predictionDataTo = new PredictionDataTo();
    List<PredictionDayData> predictionDayDataEtos = new ArrayList<>();

    List<DishEntity> dishes = this.dishRepository.findAll();

    for (DishEntity dish : dishes) {
      train(dish, criteria.getStartBookingdate());
    }

    for (DishEntity dish : dishes) {
      generatePredictionFor(dish);
    }

    for (PredictionDayDataEntity entity : this.predictionDayDataRepository.getPrediction(criteria)) {
      PredictionDayDataEto predictionDayDataEto = new PredictionDayDataEto();
      predictionDayDataEto.setId(entity.getId());
      predictionDayDataEto.setDishId(entity.getDishId());
      predictionDayDataEto.setDishName(entity.getDishName());
      predictionDayDataEto.setTimestamp(entity.getTimestamp());
      predictionDayDataEto.setForecast(entity.getForecast());
      predictionDayDataEtos.add(predictionDayDataEto);
    }

    predictionDataTo.setData(predictionDayDataEtos);

    return predictionDataTo;
  }

  private void generatePredictionFor(DishEntity dish) {

    // Prepare model parameters
    this.predictionModelDataRepository.deleteTmpPredictionModel();

    this.predictionModelDataRepository.prepareModelPredictions(dish.getId());
    // Do prediction
    this.predictionDayDataRepository.deleteTmpPredictionForecast();
    this.entityManager
        .createNativeQuery("CALL _SYS_AFL.PAL_ARIMA_FORECAST(PREDICTION_FORECAST_DATA, TMP_PREDICTION_MODEL, "
            + "PREDICTION_ARIMA_PARAMS, TMP_PREDICTION_FORECAST) WITH OVERVIEW")
        .executeUpdate();
    // Save prediction
    this.predictionDayDataRepository.deletePredictionDayDatabyDish(dish.getId());
    this.predictionDayDataRepository.savePredictions(dish.getId());
  }

  private void train(DishEntity dish, Timestamp startDate) {

    // is training necessary
    boolean alreadyTrained = this.predictionModelDataRepository.isTrainingNecessary(dish.getId(),
        startDate.toString()) > 0;
    if (alreadyTrained) {
      return;
    }
    // is training possible
    boolean existsTrainingData = this.predictionModelDataRepository.isTrainingPossible() > 0;
    if (!existsTrainingData) {
      return;
    }
    // prepare training data
    this.predictionModelDataRepository.deleteTmpPredictionData();
    this.predictionModelDataRepository.prepareTrainingData(dish.getId(), startDate);

    // Estimate model parameters
    this.predictionModelDataRepository.deleteTmpPredictionModel();
    this.predictionModelDataRepository.deleteTmpPredictionFit();
    this.entityManager
        .createNativeQuery("CALL _SYS_AFL.PAL_AUTOARIMA(TMP_PREDICTION_DATA, PREDICTION_AUTOARIMA_PARAMS, "
            + "TMP_PREDICTION_MODEL, TMP_PREDICTION_FIT) WITH OVERVIEW")
        .executeUpdate();

    // Save model
    this.predictionModelDataRepository.deletePreditionDataModelbyDishId(dish.getId());
    this.predictionModelDataRepository.addPredictionModel(dish.getId());

  }

}
