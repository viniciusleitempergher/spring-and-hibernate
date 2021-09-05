package com.viniciusleitemperg.rest.configs;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.viniciusleitemperg.rest.models.Customer;
import com.viniciusleitemperg.rest.models.RefreshToken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable {

	private static final long serialVersionUID = 5282239508767377003L;

	@Value("${jwt.refreshtoken.validity}")
	public static long JWT_REFESH_TOKEN_VALIDITY;
	@Value("${jwt.token.validity}")
	public static long JWT_TOKEN_VALIDITY;

	@Value("${jwt.refreshtoken.secret}")
	String REFRESH_TOKEN_SECRET;

	/**
	 * Returns the id of the customer token
	 * 
	 * @param token - the customer login token
	 */
	public String getIdFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	/**
	 * Returns the expiration date from the customer token
	 * 
	 * @param token - the customer login token
	 */
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	/**
	 * Returns a JSON with the key values inside of the customer token
	 * 
	 * @param token - the customer login token
	 */
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(REFRESH_TOKEN_SECRET).parseClaimsJws(token).getBody();
	}

	/**
	 * Check if the token has expired
	 * 
	 * @param token - the customer login token
	 */
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	/**
	 * Generates the refresh token for the customer
	 * 
	 * @param customer - the customer object
	 */
	public String generateRefeshToken(Customer customer) {
		return doGenerateToken(customer.getId().toString(), JWT_REFESH_TOKEN_VALIDITY);
	}

	/**
	 * Generates the access token for the customer
	 * 
	 * @param customer - the customer object
	 */
	public String generateAccessToken(RefreshToken refreshToken) {
		return doGenerateToken(refreshToken.getId().toString(), JWT_TOKEN_VALIDITY);
	}

	/**
	 * Creates the token and defines its time of expiration
	 * 
	 * @param subject  - the id of the customer/refreshToken
	 * @param validity - the time in seconds to expire
	 */
	private String doGenerateToken(String subject, Long validity) {
		Map<String, Object> claims = new HashMap<String, Object>();

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + validity * 1000))
				.signWith(SignatureAlgorithm.HS512, REFRESH_TOKEN_SECRET).compact();
	}

	/**
	 * Checks if the token is valid for this customer
	 * 
	 * @param token    - the login token
	 * @param customer - the customer's object
	 */
	public Boolean validateToken(String token, Customer customer) {
		final String username = getIdFromToken(token);
		return (username.equals(customer.getId()) && !isTokenExpired(token));
	}
}
