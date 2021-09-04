package com.viniciusleitemperg.rest.services;

import java.util.List;
import java.util.UUID;

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
		Customer customer = new Customer();

		customer.setEmail(customerData.getEmail());
		customer.setFirstName(customerData.getFirstName());
		customer.setLastName(customerData.getLastName());

		Customer customerToReturn = customerRepository.save(customer);
		System.out.println(customerToReturn);
		System.out.println(customerToReturn.getId().toString());

		return customerToReturn;
	}

	public void removeCustomer(UUID id) {
		customerRepository.deleteById(id);
	}

	public List<Customer> getCustomers() {
		return customerRepository.findAll();
	}

	public Customer getCustomer(UUID id) {
		return customerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Customer not found"));
	}
}