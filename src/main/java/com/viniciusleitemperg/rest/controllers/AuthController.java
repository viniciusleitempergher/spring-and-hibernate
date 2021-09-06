package com.viniciusleitemperg.rest.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@PostMapping("/login")
	public String[][] login(final @RequestBody String googleId) {
		

		String[][] tokens = { { "token", "" }, { "refreshToken", "" } };

		return tokens;
	}
}
