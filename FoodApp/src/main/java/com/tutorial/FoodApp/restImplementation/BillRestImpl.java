package com.tutorial.FoodApp.restImplementation;

import java.util.List;
import java.util.Map;

import com.tutorial.FoodApp.model.Bill;
import com.tutorial.FoodApp.rest.BillRest;
import com.tutorial.FoodApp.service.BillService;
import com.tutorial.FoodApp.utils.FoodAppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.tutorial.FoodApp.constants.FoodAppConstants;

@RestController
public class BillRestImpl implements BillRest {
	
	@Autowired
    BillService billService;

	@Override
	public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
		try {
			return billService.generateReport(requestMap);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return FoodAppUtils.getResponseEntity(FoodAppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<List<Bill>> getBills() {
		try {
			return billService.getBills();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
		// TODO Auto-generated method stub
		try {
			return billService.getPdf(requestMap);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

}
