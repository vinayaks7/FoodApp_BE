package com.tutorial.FoodApp.restImplementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tutorial.FoodApp.rest.ProductRest;
import com.tutorial.FoodApp.service.ProductService;
import com.tutorial.FoodApp.utils.FoodAppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.tutorial.FoodApp.constants.FoodAppConstants;
import com.tutorial.FoodApp.wrapper.ProductWrapper;

@RestController
public class ProductRestImpl implements ProductRest {

	@Autowired
    ProductService productService;
	
	@Override
	public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
		try {
			return productService.addNewProduct(requestMap);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return FoodAppUtils.getResponseEntity(FoodAppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<List<ProductWrapper>> getAllProduct(String filterValue) {
		try {
			return productService.getAllProducts();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
		try {
			return productService.updateProduct(requestMap);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return FoodAppUtils.getResponseEntity(FoodAppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> deleteProduct(Integer id) {
		try {
			return productService.deleteProduct(id);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return FoodAppUtils.getResponseEntity(FoodAppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
		try {
			return productService.updateStatus(requestMap);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return FoodAppUtils.getResponseEntity(FoodAppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<List<ProductWrapper>> getByCategory(Integer id) {
		try {
			return productService.getByCategory(id);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<ProductWrapper> getProductById(Integer id) {
		try {
			return productService.getProductById(id);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ProductWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
