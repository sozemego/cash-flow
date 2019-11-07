package com.soze.cashflow.auth.controller;

import com.soze.cashflow.auth.AuthException;
import com.soze.cashflow.auth.domain.tables.records.UserRecord;
import com.soze.cashflow.auth.dto.CreateUserDTO;
import com.soze.cashflow.auth.dto.UserDTO;
import com.soze.cashflow.auth.service.AuthService;
import com.soze.cashflow.auth.service.TokenService;
import com.soze.common.json.JsonUtils;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.annotation.*;
import io.micronaut.http.hateoas.JsonError;
import org.jooq.exception.DataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

@Controller("/auth")
public class AuthController {

	private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

	private final AuthService authService;
	private final TokenService tokenService;

	@Inject
	public AuthController(AuthService authService, TokenService tokenService) {
		this.authService = authService;
		this.tokenService = tokenService;
	}

	@Post("/create")
	@Produces(MediaType.APPLICATION_JSON)
	public UserDTO createUser(@Body String body) {
		CreateUserDTO createUserDTO = JsonUtils.parse(body, CreateUserDTO.class);
		UserRecord userRecord = authService.createUser(createUserDTO.username, createUserDTO.password);
		return convert(userRecord);
	}

	@Post("/login")
	@Produces(MediaType.APPLICATION_JSON)
	public UserDTO login(@Body String body) {
		CreateUserDTO createUserDTO = JsonUtils.parse(body, CreateUserDTO.class);
		UserRecord userRecord = authService.findUserByName(createUserDTO.username);
		if (userRecord == null) {
			throw new AuthException("Invalid username or password");
		}
		return convert(userRecord);
	}

	private UserDTO convert(UserRecord userRecord) {
		UserDTO userDTO = new UserDTO();
		userDTO.id = userRecord.getId();
		userDTO.createTime = LocalDateTime.ofInstant(
			Instant.ofEpochMilli(userRecord.getCreateTime().getTime()), ZoneId.systemDefault());
		userDTO.name = userRecord.getName();
		userDTO.token = tokenService.createToken(userRecord.getName());
		return userDTO;
	}

	@Error
	public HttpResponse<JsonError> onAuthException(HttpRequest request, AuthException e) {
		LOG.info("Handling exception to {}", request.getMethod(), e);
		Objects.requireNonNull(e.getMessage());
		JsonError error = new JsonError(e.getMessage());
		return HttpResponse.<JsonError>status(HttpStatus.BAD_REQUEST).body(error);
	}

	@Error
	public HttpResponse<JsonError> onDataAccessException(HttpRequest request, DataAccessException e) {
		LOG.info("Handling exception to {}", request.getMethod(), e);
		Objects.requireNonNull(e.getMessage());
		JsonError error = new JsonError("Invalid username or password");
		return HttpResponse.<JsonError>status(HttpStatus.BAD_REQUEST).body(error);
	}

}
