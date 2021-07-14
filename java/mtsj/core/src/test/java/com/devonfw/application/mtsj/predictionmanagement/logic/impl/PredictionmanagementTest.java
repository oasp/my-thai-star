package com.devonfw.application.mtsj.predictionmanagement.logic.impl;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.ReactorContextTestExecutionListener;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;

import com.devonfw.application.mtsj.SpringBootApp;
import com.devonfw.application.mtsj.dishmanagement.dataaccess.api.DishEntity;
import com.devonfw.application.mtsj.dishmanagement.dataaccess.api.repo.DishRepository;
import com.devonfw.application.mtsj.general.common.api.constants.Roles;
import com.devonfw.application.mtsj.predictionmanagement.common.api.to.PredictionDataTo;
import com.devonfw.application.mtsj.predictionmanagement.common.api.to.PredictionDayDataEto;
import com.devonfw.application.mtsj.predictionmanagement.common.api.to.PredictionSearchCriteriaTo;
import com.devonfw.application.mtsj.predictionmanagement.dataaccess.api.PredictionDayDataEntity;
import com.devonfw.application.mtsj.predictionmanagement.dataaccess.api.PredictionForecastDataEntity;
import com.devonfw.application.mtsj.predictionmanagement.dataaccess.api.PredictionModelDataEntity;
import com.devonfw.application.mtsj.predictionmanagement.dataaccess.api.repo.PredictionDayDataRepository;
import com.devonfw.application.mtsj.predictionmanagement.dataaccess.api.repo.PredictionModelDataRepository;
import com.devonfw.application.mtsj.predictionmanagement.logic.api.Predictionmanagement;
import com.devonfw.module.test.common.base.ComponentTest;

/**
 * Test for {@link Predictionmanagement}
 *
 */
// @SpringBootTest(classes = { SpringBootApp.class, PredictionmanagementTest.TestConfig.class })
@Profile("hana")

@TestExecutionListeners(listeners = { WithSecurityContextTestExecutionListener.class,
ReactorContextTestExecutionListener.class })

@SpringBootTest(classes = SpringBootApp.class)
// @RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class PredictionmanagementTest extends ComponentTest {

  private PredictionmanagementImpl predictionManagement = new PredictionmanagementImpl();

  @Mock
  private PredictionDayDataRepository predictionDayDataRepository;

  @Mock
  private PredictionModelDataRepository predictionModelDataRepository;

  @Mock
  private EntityManager entityManager;

  @Mock
  private PredictionForecastDataEntity forecastData;

  @Mock
  private PredictionDayDataEntity predictionDayDataEntity;

  @Mock
  private PredictionModelDataEntity entity;

  @Mock
  private DishRepository dishRepository;

  @Mock
  private Query mockedQuery;

  static PredictionDayDataEntity predictionDayData;

  /**
   * Creation of needed objects
   */
  @Override
  public void doSetUp() {

    predictionDayData = new PredictionDayDataEntity();
    predictionDayData.setDishName("Dish 1");
    predictionDayData.setForecast(Double.valueOf(0.8));
    predictionDayData.setId(Long.valueOf(1));
    predictionDayData.setModificationCounter(0);
    predictionDayData.setTimestamp(Integer.valueOf(5));
  }

  /**
   * Tests the prediction
   */
  @Test
  @WithMockUser(username = "manager", authorities = { Roles.MANAGER })
  public void getNextWeekPrediction() {

    PredictionSearchCriteriaTo eto = new PredictionSearchCriteriaTo();
    eto.setHolidays(List.of("0"));
    eto.setTemperatures(List.of(Double.valueOf(20.5d)));
    eto.setStartBookingdate(new Timestamp(System.currentTimeMillis()));
    eto.setPageable(PageRequest.of(0, 100));

    this.predictionManagement.setPredictionDayDataRepository(this.predictionDayDataRepository);
    this.predictionManagement.setDishRepository(this.dishRepository);
    this.predictionManagement.setEntityManager(this.entityManager);
    this.predictionManagement.setPredictionModelDataRepository(this.predictionModelDataRepository);

    when(this.entityManager.createNativeQuery(anyString())).thenReturn(mock(Query.class));
    when(this.dishRepository.findAll()).thenReturn(returnDishes());
    when(this.predictionDayDataRepository.getPrediction(eto)).thenReturn(returnPagePredictionDayData());

    PredictionDataTo nextWeekPrediction = this.predictionManagement.getNextWeekPrediction(eto);

    assertThat(nextWeekPrediction).isNotNull();
    assertThat(nextWeekPrediction.getData()).isNotNull();
    assertThat(nextWeekPrediction.getData().size()).isEqualTo(1);

    PredictionDayDataEto expected = new PredictionDayDataEto();
    expected.setDishName(predictionDayData.getDishName());
    expected.setForecast(predictionDayData.getForecast());
    expected.setId(predictionDayData.getId());
    expected.setModificationCounter(predictionDayData.getModificationCounter());
    expected.setTimestamp(predictionDayData.getTimestamp());
    assertThat(nextWeekPrediction.getData().get(0)).isEqualToComparingFieldByField(expected);

  }

  private List<DishEntity> returnDishes() {

    List<DishEntity> dishEntities = new ArrayList<DishEntity>();
    DishEntity dishEntity = new DishEntity();
    dishEntity.setId(123l);
    dishEntity.setName("test");
    dishEntity.setPrice(new BigDecimal(12));
    dishEntities.add(dishEntity);

    return dishEntities;
  }

  private Page<PredictionDayDataEntity> returnPagePredictionDayData() {

    Page<PredictionDayDataEntity> predictionDayDataPage = null;
    List<PredictionDayDataEntity> predictionDayDataList = new ArrayList<PredictionDayDataEntity>();
    PredictionDayDataEntity predictionDayDataEntity = new PredictionDayDataEntity();
    predictionDayDataEntity.setId(Long.valueOf(1));
    predictionDayDataEntity.setTimestamp(Integer.valueOf(5));
    predictionDayDataEntity.setForecast(Double.valueOf(0.8));
    predictionDayDataEntity.setDishName("Dish 1");
    predictionDayDataEntity.setModificationCounter(0);
    predictionDayDataList.add(predictionDayDataEntity);

    predictionDayDataPage = new PageImpl<PredictionDayDataEntity>(predictionDayDataList);
    return predictionDayDataPage;
  }

}
