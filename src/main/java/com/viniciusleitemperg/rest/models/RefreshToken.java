package com.viniciusleitemperg.rest.models;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Type;

@Entity(name = "refresh_token")
public class RefreshToken {

	@Id
	@GeneratedValue(generator = "uuid4")
	@Type(type = "org.hibernate.type.PostgresUUIDType")
	@Column(name = "id", updatable = false, unique = true, nullable = false, columnDefinition = "uuid")
	private UUID id;

	@Column(name = "token", nullable = false, unique = true)
	private String token;

	@Column(name = "expires_in", nullable = false)
	private long expiresIn;

	@OneToOne
	@JoinColumn(name = "customer_id", referencedColumnName = "id")
	private Customer customer;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(long expiresIn) {
		this.expiresIn = expiresIn;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public UUID getId() {
		return id;
	}
}
