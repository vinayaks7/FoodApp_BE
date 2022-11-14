package com.tutorial.FoodApp.restImplementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tutorial.FoodApp.rest.UserRest;
import com.tutorial.FoodApp.service.UserService;
import com.tutorial.FoodApp.utils.FoodAppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.tutorial.FoodApp.constants.FoodAppConstants;
import com.tutorial.FoodApp.wrapper.UserWrapper;

@RestController
public class UserRestImpl implements UserRest {

	@Autowired
    UserService userService;
	
	@Override
	public ResponseEntity<String> signUp(Map<String, String> requestMap) {
		try {
			return userService.signUp(requestMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return FoodAppUtils.getResponseEntity(FoodAppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> login(Map<String, String> requestMap) {
		try {
			return userService.login(requestMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return FoodAppUtils.getResponseEntity(FoodAppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<List<UserWrapper>> getAllUsers() {
		try {
			return userService.getAllUser();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> update(Map<String, String> requestMap) {
		try {
			return userService.update(requestMap);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return FoodAppUtils.getResponseEntity(FoodAppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> checkToken() {
		try {
			return userService.checkToken();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return FoodAppUtils.getResponseEntity(FoodAppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
		try {
			return userService.changePassword(requestMap);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return FoodAppUtils.getResponseEntity(FoodAppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
		try {
			return userService.forgetPassword(requestMap);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return FoodAppUtils.getResponseEntity(FoodAppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
