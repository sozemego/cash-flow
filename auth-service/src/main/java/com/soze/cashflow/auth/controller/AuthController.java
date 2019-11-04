package com.soze.cashflow.auth.controller;

import com.soze.cashflow.auth.domain.tables.records.UserRecord;
import com.soze.cashflow.auth.dto.CreateUserDTO;
import com.soze.cashflow.auth.dto.UserDTO;
import com.soze.cashflow.auth.service.AuthService;
import com.soze.cashflow.auth.service.TokenService;
import com.soze.common.json.JsonUtils;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;

import javax.inject.Inject;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Controller("/auth")
public class AuthController {

	private final AuthService authService;
	private final TokenService tokenService;

	@Inject
	public AuthController(AuthService authService, TokenService tokenService) {
		this.authService = authService;
		this.tokenService = tokenService;
	}

	@Post("/create")
	public UserDTO createUser(@Body String body) {
		CreateUserDTO createUserDTO = JsonUtils.parse(body, CreateUserDTO.class);
		UserRecord userRecord = authService.createUser(createUserDTO.username, createUserDTO.password);
		UserDTO userDTO = new UserDTO();
		userDTO.id = userRecord.getId();
		userDTO.createTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(userRecord.getCreateTime().getTime()), ZoneId.systemDefault());
		userDTO.name = userRecord.getName();
		userDTO.token = tokenService.createToken(userRecord.getName());
		return userDTO;
	}

	@Post("/login")
	@Produces(MediaType.TEXT_PLAIN)
	public String login(@Body String body) {
		CreateUserDTO createUserDTO = JsonUtils.parse(body, CreateUserDTO.class);
		return authService.login(createUserDTO);
	}
}
