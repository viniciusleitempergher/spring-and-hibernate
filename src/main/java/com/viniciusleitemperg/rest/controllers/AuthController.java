package com.viniciusleitemperg.rest.controllers;

import java.io.Serializable;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.viniciusleitemperg.rest.services.AuthService;

@CrossOrigin
@RestController
@RequestMapping(value = "/auth")
public class AuthController {

	@Resource(name = "authService")
	private AuthService authService;

	@PostMapping(value = "/login")
	public String[][] login(@RequestBody RefreshBodyData requestBody) throws Exception {
		String googleIdToken = requestBody.getToken();

		try {
			String[][] tokens = authService.login(googleIdToken);

			return tokens;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@PostMapping(value = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
	public RefreshBodyData refreshAccess(@RequestBody RefreshBodyData requestBody) {
		String token = requestBody.getToken();

		try {
			String accessToken = authService.refreshAccessToken(token);

			RefreshBodyData responseBody = new RefreshBodyData();
			responseBody.setToken(accessToken);

			return responseBody;
		} catch (EntityNotFoundException | AccessDeniedException e) {
			throw new UnauthorizedException();
		}
	}
}

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
class UnauthorizedException extends RuntimeException {
	private static final long serialVersionUID = 5791339699901421376L;
}

class RefreshBodyData implements Serializable {
	private static final long serialVersionUID = -3495239241091692730L;

	private String token;

	public RefreshBodyData() {
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}