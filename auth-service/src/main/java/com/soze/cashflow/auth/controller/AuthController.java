package com.soze.cashflow.auth.controller;

import com.soze.cashflow.auth.dto.UserDTO;
import com.soze.cashflow.auth.service.AuthService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import javax.inject.Inject;
import java.util.UUID;

@Controller("/auth")
public class AuthController {

	private final AuthService authService;

	@Inject
	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@Get("/create")
	public UserDTO hello() {
		System.out.println("HELLO");
		UserDTO userDTO = new UserDTO();
		userDTO.uuid = UUID.randomUUID();
		return userDTO;
	}
}
