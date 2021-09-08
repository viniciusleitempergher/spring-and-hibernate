package com.viniciusleitemperg.rest.controllers;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.viniciusleitemperg.rest.configs.JwtTokenUtil;
import com.viniciusleitemperg.rest.models.Customer;
import com.viniciusleitemperg.rest.services.CustomerService;

@CrossOrigin
@RestController
@RequestMapping("/customers")
public class CustomerController {

	@Resource(name = "customerService")
	private CustomerService customerService;

	@Autowired
	private JwtTokenUtil jwt;

	@GetMapping
	public List<Customer> getCustomers() {
		return customerService.getCustomers();
	}

	@GetMapping("/{id}")
	public Customer getCustomer(@PathVariable UUID id) {
		try {
			return customerService.getCustomer(id);
		} catch (EntityNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
		}
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Customer createCustomer(final @RequestBody Customer customerData) {
		try {
			return customerService.createCustomer(customerData);
		} catch (EntityExistsException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists!");
		}
	}

	@DeleteMapping("/{id}")
	public void deleteCustomer(@PathVariable UUID id) {
		try {
			customerService.removeCustomer(id);
		} catch (EntityNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
		}
	}

	@PostMapping("/customerinfo")
	public Customer customerInfo(HttpServletRequest request) {

		String token = request.getHeader("Authorization").split(" ")[1];
		System.out.println(token);
		Customer customer = jwt.getCustomerFromAccessToken(token);
		return customer;
	}
}
