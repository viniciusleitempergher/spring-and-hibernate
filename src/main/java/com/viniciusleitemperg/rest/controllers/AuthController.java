package com.viniciusleitemperg.rest.controllers;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viniciusleitemperg.rest.services.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Resource(name = "authService")
	private AuthService authService;

	@PostMapping("/login")
	public String[][] login(final @RequestBody String googleId) throws Exception {
		try {
			String[][] tokens = authService.login(googleId);

			return tokens;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
