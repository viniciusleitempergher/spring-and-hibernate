package com.viniciusleitemperg.rest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viniciusleitemperg.rest.repositories.RefreshTokenRepository;

@Service("authService")
public class AuthService {

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	public String[][] login(String googleId) {
		
		
		
		String[][] tokens = { { "token", "" }, { "refreshToken", "" } };

		return tokens;
	}
}
