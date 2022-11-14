package com.tutorial.FoodApp.JWT;

import java.util.ArrayList;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tutorial.FoodApp.dao.UserDao;

@Service
public class CustomerUserDetailsService implements UserDetailsService {

	@Autowired
	UserDao userDao;

	private com.tutorial.FoodApp.model.User userDetail;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		userDetail = userDao.findByEmailId(username);

		if (!Objects.isNull(userDetail))
			return new User(userDetail.getEmail(), userDetail.getPassword(), new ArrayList<>());
		else
			throw new UsernameNotFoundException("User not found.");
	}

	public com.tutorial.FoodApp.model.User getUserDetail() {
		return userDetail;
	}

}
