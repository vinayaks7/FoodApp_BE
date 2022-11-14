package com.tutorial.FoodApp.serviceImplementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.tutorial.FoodApp.JWT.JwtFilter;
import com.tutorial.FoodApp.dao.ProductDao;
import com.tutorial.FoodApp.model.Category;
import com.tutorial.FoodApp.model.Product;
import com.tutorial.FoodApp.service.ProductService;
import com.tutorial.FoodApp.utils.FoodAppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tutorial.FoodApp.constants.FoodAppConstants;
import com.tutorial.FoodApp.wrapper.ProductWrapper;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
    ProductDao productDao;

	@Autowired
    JwtFilter jwtFilter;

	@Override
	public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
		try {
			if (jwtFilter.isBrancManager()) {
				if (validateProductMap(requestMap, false)) {
					productDao.save(getProductFromMap(requestMap, false));
					return FoodAppUtils.getResponseEntity("Product added successfully", HttpStatus.OK);
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

	private boolean validateProductMap(Map<String, String> requestMap, boolean validateId) {
		if (requestMap.containsKey("name")) {
			if (requestMap.containsKey("id") && validateId) {
				return true;
			} else if (!validateId) {
				return true;
			}
		}
		return false;
	}

	private Product getProductFromMap(Map<String, String> requestMap, boolean isAdd) {

		Category category = new Category();

		category.setId(Integer.parseInt(requestMap.get("categoryId")));

		Product product = new Product();

		if (isAdd) {
			product.setId(Integer.parseInt(requestMap.get("id")));
		} else {
			product.setStatus("true");
		}
		product.setCategory(category);
		product.setName(requestMap.get("name"));
		product.setDescription(requestMap.get("description"));
		product.setPrice(Integer.parseInt(requestMap.get("price")));
		return product;
	}

	@Override
	public ResponseEntity<List<ProductWrapper>> getAllProducts() {
		try {
			return new ResponseEntity<>(productDao.getAllProduct(), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
		try {
			if (jwtFilter.isBrancManager()) {
				if (validateProductMap(requestMap, true)) {
					Optional<Product> optional = productDao.findById(Integer.parseInt(requestMap.get("id")));
					if (!optional.isEmpty()) {
						Product product = getProductFromMap(requestMap, true);
						product.setStatus(optional.get().getStatus());
						productDao.save(product);
						return FoodAppUtils.getResponseEntity("Product updated successfully", HttpStatus.OK);
					} else {
						return FoodAppUtils.getResponseEntity("Product ID does not exist", HttpStatus.OK);
					}
				} else {
					return FoodAppUtils.getResponseEntity(FoodAppConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
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

	@Override
	public ResponseEntity<String> deleteProduct(Integer id) {
		try {
			if (jwtFilter.isBrancManager()) {
				Optional<Product> optional = productDao.findById(id);
				if (!optional.isEmpty()) {
					productDao.deleteById(id);
					return FoodAppUtils.getResponseEntity("Product deleted successfully", HttpStatus.OK);
				} else {
					return FoodAppUtils.getResponseEntity("Product ID does not exist", HttpStatus.OK);
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

	@Override
	public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
		try {
			if (jwtFilter.isBrancManager()) {
				Optional<Product> optional = productDao.findById(Integer.parseInt(requestMap.get("id")));
				if (!optional.isEmpty()) {
					productDao.updateProductStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
					return FoodAppUtils.getResponseEntity("Product status updated successfully", HttpStatus.OK);
				} else {
					return FoodAppUtils.getResponseEntity("Product ID does not exist", HttpStatus.OK);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return FoodAppUtils.getResponseEntity(FoodAppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<List<ProductWrapper>> getByCategory(Integer id) {
		try {
			return new ResponseEntity<>(productDao.getProductByCategory(id), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<ProductWrapper> getProductById(Integer id) {
		try {
			return new ResponseEntity<>(productDao.getProductById(id), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ProductWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
