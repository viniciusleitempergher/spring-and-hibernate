package com.viniciusleitemperg.rest.services;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viniciusleitemperg.rest.models.Customer;
import com.viniciusleitemperg.rest.repositories.CustomerRepository;

@Service("customerService")
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	public Customer createCustomer(Customer customerData) {

		boolean emailExists = customerRepository.existsByEmail(customerData.getEmail());

		if (emailExists) {
			throw new EntityExistsException("Email already exists!");
		}

		Customer customer = new Customer();

		customer.setEmail(customerData.getEmail());
		customer.setFirstName(customerData.getFirstName());
		customer.setLastName(customerData.getLastName());

		Customer customerToReturn = customerRepository.save(customer);

		return customerToReturn;
	}

	public void removeCustomer(UUID id) {
		Customer customer = customerRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Customer not found"));
		customerRepository.delete(customer);
	}

	public List<Customer> getCustomers() {
		return customerRepository.findAll();
	}

	public Customer getCustomer(UUID id) {
		return customerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Customer not found"));
	}
}