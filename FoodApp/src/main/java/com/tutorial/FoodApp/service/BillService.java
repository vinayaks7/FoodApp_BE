package com.tutorial.FoodApp.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.tutorial.FoodApp.model.Bill;

public interface BillService {

	ResponseEntity<String> generateReport(Map<String, Object> requestMap);

	ResponseEntity<List<Bill>> getBills();

	ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap);

}
