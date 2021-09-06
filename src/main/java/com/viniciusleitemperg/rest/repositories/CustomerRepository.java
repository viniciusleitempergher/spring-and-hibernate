package com.viniciusleitemperg.rest.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viniciusleitemperg.rest.models.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
	Boolean existsByEmail(String email);
}