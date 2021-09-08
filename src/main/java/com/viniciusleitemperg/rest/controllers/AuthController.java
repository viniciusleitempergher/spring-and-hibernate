package com.viniciusleitemperg.rest.controllers;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.viniciusleitemperg.rest.services.AuthService;

@CrossOrigin
@RestController
@RequestMapping(value = "/auth")
public class AuthController {

	@Resource(name = "authService")
	private AuthService authService;

	class RequestBodyData {
		String googleId;
	}

	@PostMapping(value = "/login")
	@ResponseBody
	public String[][] login(HttpServletRequest request, HttpServletResponse response,
			@RequestBody RequestBodyData requestBody) throws Exception {

		String googleId = requestBody.googleId;
		try {
			System.out.println(googleId);
			String[][] tokens = authService.login(googleId);

			return tokens;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
