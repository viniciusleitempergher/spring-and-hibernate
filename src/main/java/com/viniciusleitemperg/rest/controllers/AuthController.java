package com.viniciusleitemperg.rest.controllers;

import javax.annotation.Resource;

import org.apache.tomcat.util.json.JSONParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viniciusleitemperg.rest.services.AuthService;

@CrossOrigin
@RestController
@RequestMapping(value = "/auth")
public class AuthController {

	@Resource(name = "authService")
	private AuthService authService;

	@PostMapping(value = "/login")
	public ResponseEntity<?> login(@RequestBody String jsonRequest) throws Exception {

		JSONParser json = new JSONParser(jsonRequest);

		Object obj = json.parse();

		String googleId = obj.toString().split("=")[1];
		googleId = googleId.substring(0, googleId.length() - 1);

		try {
			System.out.println(googleId);
			String[][] tokens = authService.login(googleId);

			return new ResponseEntity(tokens, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
