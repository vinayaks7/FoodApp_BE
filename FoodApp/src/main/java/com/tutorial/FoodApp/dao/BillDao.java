package com.tutorial.FoodApp.dao;

import java.util.List;

import com.tutorial.FoodApp.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface BillDao extends JpaRepository<Bill, Integer>{

	List<Bill> getAllBills();

	List<Bill> getBillByUserName(@Param("username") String currentUser);

}
