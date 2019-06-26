package com.devonfw.application.mtsj.dishmanagement.dataaccess.api.repo;

import java.math.BigDecimal;
import java.util.List;

import com.devonfw.module.jpa.dataaccess.api.RevisionMetadata;
import com.devonfw.module.jpa.dataaccess.api.data.DefaultRevisionedRepository;
import org.springframework.data.domain.Page;

import com.devonfw.application.mtsj.dishmanagement.common.api.to.DishSearchCriteriaTo;
import com.devonfw.application.mtsj.dishmanagement.dataaccess.api.DishEntity;
import com.devonfw.module.jpa.dataaccess.api.QueryUtil;
import com.devonfw.module.jpa.dataaccess.api.data.DefaultRepository;
import com.querydsl.core.alias.Alias;
import com.querydsl.jpa.impl.JPAQuery;

/**
 * {@link DefaultRepository} for {@link DishEntity}.
 */
public interface DishRepository extends DefaultRevisionedRepository<DishEntity> {

  /**
   * @param criteria the {@link DishSearchCriteriaTo} with the criteria to search.
   * @return the {@link Page} of the {@link DishEntity} objects that matched the search.
   */
  default Page<DishEntity> findDishs(DishSearchCriteriaTo criteria) {

    DishEntity alias = newDslAlias();
    JPAQuery<DishEntity> query = newDslQuery(alias);

    String searchBy = criteria.getSearchBy();
    if (searchBy != null) {
      query.where(Alias.$(alias.getName()).contains(searchBy).or(Alias.$(alias.getDescription()).contains(searchBy)));
    }

    BigDecimal price = criteria.getMaxPrice();
    if (price != null) {
      query.where(Alias.$(alias.getPrice()).lt(price));
    }

    return QueryUtil.get().findPaginated(criteria.getPageable(), query, false);
  }
}
