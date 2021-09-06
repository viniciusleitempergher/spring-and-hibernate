package com.viniciusleitemperg.rest.services;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.UUID;

import javax.annotation.Resource;
import javax.persistence.EntityExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.viniciusleitemperg.rest.configs.JwtTokenUtil;
import com.viniciusleitemperg.rest.models.Customer;
import com.viniciusleitemperg.rest.models.RefreshToken;
import com.viniciusleitemperg.rest.repositories.CustomerRepository;
import com.viniciusleitemperg.rest.repositories.RefreshTokenRepository;

@Service("authService")
@Component
public class AuthService {

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private JwtTokenUtil jwt;

	@Resource(name = "customerService")
	private CustomerService customerService;

	@Value("${google.client.id}")
	String CLIENT_ID;

	public String[][] login(String googleId) throws GeneralSecurityException, IOException {

		HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(httpTransport, jsonFactory)
				.setAudience(Collections.singletonList(CLIENT_ID)).build();

		GoogleIdToken idToken = verifier.verify(googleId);

		String[][] tokens;

		if (idToken != null) {
			Payload payload = idToken.getPayload();

			// Print user identifier
			String userId = payload.getSubject();
			System.out.println("User ID: " + userId);

			// Get profile information from payload
			String email = payload.getEmail();
			String name = (String) payload.get("name");
			// String pictureUrl = (String) payload.get("picture");
			// String locale = (String) payload.get("locale");
			String familyName = (String) payload.get("family_name");
			// String givenName = (String) payload.get("given_name");

			Customer customer = new Customer();
			customer.setEmail(email);
			customer.setFirstName(name);
			customer.setLastName(familyName);

			if (!customerService.customerExists(email)) {
				tokens = handleCreateCustomer(customer);
			} else {
				tokens = handleExistingCustomer(customer);
			}
		} else {
			System.out.println("Invalid ID token.");
			throw new EntityExistsException();
		}
		return tokens;
	}

	private String[][] handleExistingCustomer(Customer customerData) {
		Customer customer = customerRepository.findByEmail(customerData.getEmail());

		RefreshToken dataRefreshToken = refreshTokenRepository.findByCustomer(customer);

		String refreshToken = dataRefreshToken.getToken().toString();

		if (jwt.validateToken(refreshToken, customer)) {
			String accessToken = createAccessToken(dataRefreshToken);
			return new String[][] { { "token", accessToken + "" }, { "refreshToken", refreshToken + "" } };
		} else {
			String tokens[] = createRefreshToken(customer);
			return new String[][] { { "token", tokens[0] }, { "refreshToken", tokens[1] } };
		}
	}

	private String[][] handleCreateCustomer(Customer customerData) {
		Customer customer = customerService.createCustomer(customerData);

		String[] tokens = createRefreshToken(customer);
		String[][] tokensJson = { { "token", tokens[0] + "" }, { "refreshToken", tokens[1] + "" } };

		return tokensJson;
	}

	private String[] createRefreshToken(Customer customer) {
		String refreshToken = jwt.generateRefeshToken(customer);

		RefreshToken refreshTokenEntity = new RefreshToken();

		refreshTokenEntity.setCustomer(customer);
		refreshTokenEntity.setExpiresIn(jwt.JWT_REFRESH_TOKEN_VALIDITY);
		refreshTokenEntity.setToken(refreshToken);

		if (refreshTokenRepository.existsByCustomer(customer)) {
			RefreshToken refreshToDelete = refreshTokenRepository.findByCustomer(customer);
			refreshTokenRepository.delete(refreshToDelete);
		}
		refreshTokenRepository.save(refreshTokenEntity);

		String accessToken = createAccessToken(refreshTokenEntity);

		return new String[] { refreshToken, accessToken };
	}

	private String createAccessToken(RefreshToken refreshToken) {
		String accessToken = jwt.generateAccessToken(refreshToken);
		return accessToken;
	}
}
