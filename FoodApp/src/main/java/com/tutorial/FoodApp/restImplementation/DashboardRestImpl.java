package com.tutorial.FoodApp.restImplementation;

import java.util.Map;

import com.tutorial.FoodApp.rest.DashboardRest;
import com.tutorial.FoodApp.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardRestImpl implements DashboardRest {

	@Autowired
    DashboardService dashboardService;

	@Override
	public ResponseEntity<Map<String, Object>> getCount() {

		return dashboardService.getCount();

	}

}
