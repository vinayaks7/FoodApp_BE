package com.tutorial.FoodApp.dao;

import java.util.List;

import com.tutorial.FoodApp.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryDao extends JpaRepository<Category, Integer>{
	
	List<Category> getAllCategory();
	
}
