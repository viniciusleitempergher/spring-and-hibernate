package com.viniciusleitemperg.rest.controllers;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.viniciusleitemperg.rest.services.AuthService;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {

	@Resource(name = "authService")
	private AuthService authService;

	@PostMapping("/login")
	@ResponseBody
	public String[][] login(@RequestBody Object requestBody) throws Exception {
		try {
			String googleId = requestBody.toString();
			System.out.println(googleId);
			String[][] tokens = authService.login(googleId);

			return tokens;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
