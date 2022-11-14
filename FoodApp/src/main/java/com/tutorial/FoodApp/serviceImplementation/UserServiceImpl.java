package com.tutorial.FoodApp.serviceImplementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.tutorial.FoodApp.JWT.CustomerUserDetailsService;
import com.tutorial.FoodApp.JWT.JwtFilter;
import com.tutorial.FoodApp.JWT.JwtUtil;
import com.tutorial.FoodApp.dao.UserDao;
import com.tutorial.FoodApp.model.User;
import com.tutorial.FoodApp.service.UserService;
import com.tutorial.FoodApp.utils.FoodAppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.tutorial.FoodApp.constants.FoodAppConstants;
import com.tutorial.FoodApp.wrapper.UserWrapper;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
    UserDao userDao;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
    CustomerUserDetailsService customerUserDetailsService;

	@Autowired
    JwtUtil jwtUtil;

	@Autowired
    JwtFilter jwtFilter;

	@Override
	public ResponseEntity<String> signUp(Map<String, String> requestMap) {

		try {
			if (validateSignUpMap(requestMap)) {

				User user = userDao.findByEmailId(requestMap.get("email"));

				if (Objects.isNull(user)) {

					userDao.save(getUserFromMap(requestMap));
					return FoodAppUtils.getResponseEntity("Successfully registerd", HttpStatus.OK);

				} else {

					return FoodAppUtils.getResponseEntity("Email already exists", HttpStatus.BAD_REQUEST);
				}

			} else {

				return FoodAppUtils.getResponseEntity(FoodAppConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return FoodAppUtils.getResponseEntity(FoodAppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private boolean validateSignUpMap(Map<String, String> requestMap) {

		if (requestMap.containsKey("name") && requestMap.containsKey("email") && requestMap.containsKey("password")) {
			return true;

		} else {

			return false;
		}
	}

	private User getUserFromMap(Map<String, String> requestMap) {

		User user = new User();
		user.setName(requestMap.get("name"));
		user.setEmail(requestMap.get("email"));
		user.setPassword(requestMap.get("password"));
		user.setRole(requestMap.get("role"));
		user.setStatus("false");
		user.setRole("user");
		return user;
	}

	@Override
	public ResponseEntity<String> login(Map<String, String> requestMap) {
		try {
			Authentication auth = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password")));

			if (auth.isAuthenticated()) {
				if (customerUserDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")) {
					return new ResponseEntity<String>(
							"{\"token\":\""
									+ jwtUtil.generateToken(customerUserDetailsService.getUserDetail().getEmail(),
											customerUserDetailsService.getUserDetail().getRole())
									+ "\"}",
							HttpStatus.OK);
				}
			} else {
				return new ResponseEntity<String>("{\"message\":\"" + "You are not authorized" + "\"}",
						HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<String>("{\"message\":\"" + "You are not authorized" + "\"}", HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<List<UserWrapper>> getAllUser() {
		try {
			if (jwtFilter.isBrancManager()) {
				return new ResponseEntity<>(userDao.getAllUser(), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> update(Map<String, String> requestMap) {
		try {
			if (jwtFilter.isBrancManager()) {
				Optional<User> optional = userDao.findById(Integer.parseInt(requestMap.get("id")));

				if (!optional.isEmpty()) {
					userDao.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
					return FoodAppUtils.getResponseEntity("User status updated successfully", HttpStatus.OK);
				} else {
					return FoodAppUtils.getResponseEntity("User ID doesn't exist", HttpStatus.OK);
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
	public ResponseEntity<String> checkToken() {
		return FoodAppUtils.getResponseEntity("true", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
		try {
			User user = userDao.findByEmail(jwtFilter.getCurrentUser());
			if (!user.equals(null)) {
				if (user.getPassword().equals(requestMap.get("oldPassword"))) {
					user.setPassword(requestMap.get("newPassword"));
					userDao.save(user);
					return FoodAppUtils.getResponseEntity("Password updated successfully",
							HttpStatus.OK);
				} else {
					return FoodAppUtils.getResponseEntity("Incorrect old password",
							HttpStatus.BAD_REQUEST);
				}
			} else {
				return FoodAppUtils.getResponseEntity(FoodAppConstants.SOMETHING_WENT_WRONG,
						HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return FoodAppUtils.getResponseEntity(FoodAppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> forgetPassword(Map<String, String> requestMap) {				
		return FoodAppUtils.getResponseEntity(FoodAppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
