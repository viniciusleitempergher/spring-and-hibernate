package com.viniciusleitemperg.rest.controllers;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viniciusleitemperg.rest.models.Customer;
import com.viniciusleitemperg.rest.services.CustomerService;

@RestController
@RequestMapping("/customers")
public class CustomerController {

	@Resource(name = "customerService")
	private CustomerService customerService;

	@GetMapping
	public List<Customer> getCustomers() {
		return customerService.getCustomers();
	}

	@GetMapping("/{id}")
	public Customer getCustomer(@PathVariable UUID id) {
		System.out.println(id);
		return customerService.getCustomer(id);
	}

	@PostMapping
	public Customer createCustomer(final @RequestBody Customer customerData) {
		return customerService.createCustomer(customerData);
	}

	@DeleteMapping("/{id}")
	public void deleteCustomer(@PathVariable UUID id) {

		customerService.removeCustomer(id);
	}
}
