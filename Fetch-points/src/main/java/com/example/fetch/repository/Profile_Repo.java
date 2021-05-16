package com.example.fetch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.fetch.model.Transaction;

@Repository
public interface Profile_Repo extends JpaRepository<Transaction , Long> { 
	
	
	 @Query(value = "Select sum(points) from fetch.profile", nativeQuery = true)
	 int get_total();
	

}
