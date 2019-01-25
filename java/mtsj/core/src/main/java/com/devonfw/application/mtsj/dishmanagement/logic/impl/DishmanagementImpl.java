package com.devonfw.application.mtsj.dishmanagement.logic.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.devonfw.application.mtsj.dishmanagement.common.api.Category;
import com.devonfw.application.mtsj.dishmanagement.dataaccess.api.CategoryEntity;
import com.devonfw.application.mtsj.dishmanagement.dataaccess.api.DishEntity;
import com.devonfw.application.mtsj.dishmanagement.dataaccess.api.IngredientEntity;
import com.devonfw.application.mtsj.dishmanagement.dataaccess.api.repo.CategoryRepository;
import com.devonfw.application.mtsj.dishmanagement.dataaccess.api.repo.DishRepository;
import com.devonfw.application.mtsj.dishmanagement.dataaccess.api.repo.IngredientRepository;
import com.devonfw.application.mtsj.dishmanagement.logic.api.Dishmanagement;
import com.devonfw.application.mtsj.dishmanagement.logic.api.to.CategoryEto;
import com.devonfw.application.mtsj.dishmanagement.logic.api.to.CategorySearchCriteriaTo;
import com.devonfw.application.mtsj.dishmanagement.logic.api.to.DishCto;
import com.devonfw.application.mtsj.dishmanagement.logic.api.to.DishEto;
import com.devonfw.application.mtsj.dishmanagement.logic.api.to.DishSearchCriteriaTo;
import com.devonfw.application.mtsj.dishmanagement.logic.api.to.IngredientEto;
import com.devonfw.application.mtsj.dishmanagement.logic.api.to.IngredientSearchCriteriaTo;
import com.devonfw.application.mtsj.general.logic.base.AbstractComponentFacade;
import com.devonfw.application.mtsj.imagemanagement.logic.api.to.ImageEto;

/**
 * Implementation of component interface of dishmanagement
 */
@Named
@Transactional
public class DishmanagementImpl extends AbstractComponentFacade implements Dishmanagement {

	/**
	 * Logger instance.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(DishmanagementImpl.class);

	/**
	 * @see #getCategoryDao()
	 */
	@Inject
	private CategoryRepository categoryDao;

	/**
	 * @see #getDishDao()
	 */
	@Inject
	private DishRepository dishDao;

	/**
	 * @see #getIngredientDao()
	 */
	@Inject
	private IngredientRepository ingredientDao;

	/**
	 * The constructor.
	 */
	public DishmanagementImpl() {

		super();
	}

	@Override
	public CategoryEto findCategory(Long id) {

		LOG.debug("Get Category with id {} from database.", id);
		return getBeanMapper().map(getCategoryDao().find(id), CategoryEto.class);
	}

	@Override
	public Page<CategoryEto> findCategoryEtos(CategorySearchCriteriaTo criteria) {

		Page<CategoryEntity> categorys = getCategoryDao().findCategorys(criteria);
		return mapPaginatedEntityList(categorys, CategoryEto.class);
	}

	@Override
	public boolean deleteCategory(Long categoryId) {

		CategoryEntity category = getCategoryDao().find(categoryId);
		getCategoryDao().delete(category);
		LOG.debug("The category with id '{}' has been deleted.", categoryId);
		return true;
	}

	@Override
	public CategoryEto saveCategory(CategoryEto category) {

		Objects.requireNonNull(category, "category");
		CategoryEntity categoryEntity = getBeanMapper().map(category, CategoryEntity.class);

		// initialize, validate categoryEntity here if necessary
		CategoryEntity resultEntity = getCategoryDao().save(categoryEntity);
		LOG.debug("Category with id '{}' has been created.", resultEntity.getId());

		return getBeanMapper().map(resultEntity, CategoryEto.class);
	}

	/**
	 * Returns the field 'categoryDao'.
	 *
	 * @return the {@link CategoryDao} instance.
	 */
	public CategoryRepository getCategoryDao() {

		return this.categoryDao;
	}

	@Override
	public DishCto findDish(Long id) {

		LOG.debug("Get Dish with id {} from database.", id);
		DishEntity entity = getDishDao().find(id);
		DishCto cto = new DishCto();
		cto.setCategories(getBeanMapper().mapList(entity.getCategories(), CategoryEto.class));
		cto.setImage(getBeanMapper().map(entity.getImage(), ImageEto.class));
		cto.setDish(getBeanMapper().map(entity, DishEto.class));
		cto.setExtras(getBeanMapper().mapList(entity.getExtras(), IngredientEto.class));
		return cto;
	}

	@Override
	public Page<DishCto> findDishCtos(DishSearchCriteriaTo criteria) {

		List<DishCto> ctos = new ArrayList<>();
		Page<DishCto> pagListTo = null;
		Page<DishEntity> searchResult = getDishDao().findDishs(criteria);

		for (DishEntity dish : searchResult.getContent()) {
			DishCto cto = new DishCto();
			cto.setDish(getBeanMapper().map(dish, DishEto.class));
			cto.setImage(getBeanMapper().map(dish.getImage(), ImageEto.class));
			cto.setCategories(getBeanMapper().mapList(dish.getCategories(), CategoryEto.class));
			cto.setExtras(getBeanMapper().mapList(dish.getExtras(), IngredientEto.class));
			ctos.add(cto);
		}

		if (!criteria.getCategories().isEmpty()) {
			ctos = categoryFilter(ctos, criteria.getCategories());
		}
		if (!ctos.isEmpty()) {
			Pageable pagResultTo = PageRequest.of(criteria.getPageable().getPageNumber(), ctos.size());
			pagListTo = new PageImpl<>(ctos, pagResultTo, pagResultTo.getPageSize());
		}
		return pagListTo;

	}

	@Override
	public boolean deleteDish(Long dishId) {

		DishEntity dish = getDishDao().find(dishId);
		getDishDao().delete(dish);
		LOG.debug("The dish with id '{}' has been deleted.", dishId);
		return true;
	}

	@Override
	public DishEto saveDish(DishEto dish) {

		Objects.requireNonNull(dish, "dish");
		DishEntity dishEntity = getBeanMapper().map(dish, DishEntity.class);

		// initialize, validate dishEntity here if necessary
		DishEntity resultEntity = getDishDao().save(dishEntity);
		LOG.debug("Dish with id '{}' has been created.", resultEntity.getId());

		return getBeanMapper().map(resultEntity, DishEto.class);
	}

	public Page<DishCto> findDishesByCategory(DishSearchCriteriaTo criteria, String categoryName) {

		List<DishCto> ctos = new ArrayList<>();
		Page<DishCto> searchResult = findDishCtos(criteria);
		for (DishCto dish : searchResult.getContent()) {
			for (CategoryEto category : dish.getCategories()) {
				if (category.getName().equals(categoryName)) {
					ctos.add(dish);
				}
			}
		}
		Pageable pagResultTo = PageRequest.of(criteria.getPageable().getPageNumber(), ctos.size());
		return new PageImpl<>(ctos, pagResultTo, pagResultTo.getPageSize());
	}

	private List<DishCto> categoryFilter(List<DishCto> dishes, List<CategoryEto> categories) {

		List<DishCto> dishFiltered = new ArrayList<>();
		for (DishCto dish : dishes) {

			List<CategoryEto> entityCats = dish.getCategories();
			for (Category entityCat : entityCats) {
				for (Category category : categories) {
					if (category.getId() == entityCat.getId() && !dishAlreadyAdded(dishFiltered, dish)) {
						dishFiltered.add(dish);
						break;

					}
				}
			}

		}

		return dishFiltered;
	}

	private boolean dishAlreadyAdded(List<DishCto> dishEntitiesFiltered, DishCto dishEntity) {

		boolean result = false;
		for (DishCto entity : dishEntitiesFiltered) {
			if (entity.getDish().getId() == dishEntity.getDish().getId()) {
				result = true;
				break;
			}
		}
		return result;
	}

	/**
	 * Returns the field 'dishDao'.
	 *
	 * @return the {@link DishDao} instance.
	 */
	public DishRepository getDishDao() {

		return this.dishDao;
	}

	@Override
	public IngredientEto findIngredient(Long id) {

		LOG.debug("Get Ingredient with id {} from database.", id);
		return getBeanMapper().map(getIngredientDao().find(id), IngredientEto.class);
	}

	@Override
	public Page<IngredientEto> findIngredientEtos(IngredientSearchCriteriaTo criteria) {

		Page<IngredientEntity> ingredients = getIngredientDao().findIngredients(criteria);
		return mapPaginatedEntityList(ingredients, IngredientEto.class);
	}

	@Override
	public boolean deleteIngredient(Long ingredientId) {

		IngredientEntity ingredient = getIngredientDao().find(ingredientId);
		getIngredientDao().delete(ingredient);
		LOG.debug("The ingredient with id '{}' has been deleted.", ingredientId);
		return true;
	}

	@Override
	public IngredientEto saveIngredient(IngredientEto ingredient) {

		Objects.requireNonNull(ingredient, "ingredient");
		IngredientEntity ingredientEntity = getBeanMapper().map(ingredient, IngredientEntity.class);

		// initialize, validate ingredientEntity here if necessary
		IngredientEntity resultEntity = getIngredientDao().save(ingredientEntity);
		LOG.debug("Ingredient with id '{}' has been created.", resultEntity.getId());

		return getBeanMapper().map(resultEntity, IngredientEto.class);
	}

	/**
	 * Returns the field 'ingredientDao'.
	 *
	 * @return the {@link IngredientDao} instance.
	 */
	public IngredientRepository getIngredientDao() {

		return this.ingredientDao;
	}

}
