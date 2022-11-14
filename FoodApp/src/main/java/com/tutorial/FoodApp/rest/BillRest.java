package com.tutorial.FoodApp.rest;

import java.util.List;
import java.util.Map;

import com.tutorial.FoodApp.model.Bill;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(path = "/bill")
public interface BillRest {
	
	@PostMapping(path = "/generateReport")
	ResponseEntity<String> generateReport(@RequestBody Map<String, Object> requestMap);

	@GetMapping(path = "/getBills")
	ResponseEntity<List<Bill>> getBills();
	
	@PostMapping(path = "/getPdf")
	ResponseEntity<byte[]> getPdf(@RequestBody Map<String, Object> requestMap);
}
