package com.tutorial.FoodApp.serviceImplementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.tutorial.FoodApp.JWT.JwtFilter;
import com.tutorial.FoodApp.dao.CategoryDao;
import com.tutorial.FoodApp.model.Category;
import com.tutorial.FoodApp.service.CategoryService;
import com.tutorial.FoodApp.utils.FoodAppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tutorial.FoodApp.constants.FoodAppConstants;
import com.google.common.base.Strings;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
    CategoryDao categoryDao;

	@Autowired
    JwtFilter jwtFilter;

	@Override
	public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
		try {
			if (jwtFilter.isBrancManager()) {
				if (validateCategoryMap(requestMap, false)) {
					categoryDao.save(getCategoryFromMap(requestMap, false));
					return FoodAppUtils.getResponseEntity("Category added successfully", HttpStatus.OK);
				} else {

				}
			} else {
				return FoodAppUtils.getResponseEntity(FoodAppConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return FoodAppUtils.getResponseEntity(FoodAppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private boolean validateCategoryMap(Map<String, String> requestMap, boolean validateId) {
		if (requestMap.containsKey("name")) {
			if (requestMap.containsKey("id") && validateId) {
				return true;
			} else if (!validateId) {
				return true;
			}
		}
		return false;
	}

	private Category getCategoryFromMap(Map<String, String> requestMap, Boolean isAdd) {
		Category category = new Category();

		if (isAdd) {
			category.setId(Integer.parseInt(requestMap.get("id")));
		}
		category.setName(requestMap.get("name"));
		return category;
	}

	@Override
	public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
		try {
			if (!Strings.isNullOrEmpty(filterValue) && filterValue.equals("true")) {
				return new ResponseEntity<List<Category>>(categoryDao.getAllCategory(), HttpStatus.OK);
			}
			return new ResponseEntity<List<Category>>(categoryDao.findAll(), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
		try {
			if(jwtFilter.isBrancManager()) {
				if(validateCategoryMap(requestMap, true)) {
					Optional<Category> optional = categoryDao.findById(Integer.parseInt(requestMap.get("id")));
					if(!optional.isEmpty()) {
						categoryDao.save(getCategoryFromMap(requestMap, true));
						return FoodAppUtils.getResponseEntity("Category updated successfully", HttpStatus.OK);
					} else {
						return FoodAppUtils.getResponseEntity("Category ID does not exist", HttpStatus.OK);
					}
				}
				return FoodAppUtils.getResponseEntity(FoodAppConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
			} else {
				return FoodAppUtils.getResponseEntity(FoodAppConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return FoodAppUtils.getResponseEntity(FoodAppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
