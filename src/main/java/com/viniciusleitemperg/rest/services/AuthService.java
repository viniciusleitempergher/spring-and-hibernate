package com.viniciusleitemperg.rest.services;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import javax.annotation.Resource;

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
import com.viniciusleitemperg.rest.models.Customer;
import com.viniciusleitemperg.rest.repositories.RefreshTokenRepository;

@Service("authService")
@Component
public class AuthService {

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

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
		if (idToken != null) {
			Payload payload = idToken.getPayload();

			// Print user identifier
			String userId = payload.getSubject();
			System.out.println("User ID: " + userId);

			// Get profile information from payload
			String email = payload.getEmail();
			String name = (String) payload.get("name");
			String pictureUrl = (String) payload.get("picture");
			String locale = (String) payload.get("locale");
			String familyName = (String) payload.get("family_name");
			String givenName = (String) payload.get("given_name");

			System.out.println(email + "\n" + name + "\n" + pictureUrl + "\n" + locale + "\n" + familyName + givenName);

			Customer customer = new Customer();

			customer.setEmail(email);
			customer.setFirstName(name);
			customer.setLastName(familyName);

			customerService.createCustomer(customer);
		} else {
			System.out.println("Invalid ID token.");
		}

		String[][] tokens = { { "token", "" }, { "refreshToken", "" } };

		return tokens;
	}
}
