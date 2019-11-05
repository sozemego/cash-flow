package com.soze.cashflow.auth.controller;

import com.soze.cashflow.auth.dto.CreateUserDTO;
import com.soze.cashflow.auth.dto.UserDTO;
import com.soze.cashflow.auth.repository.UserRepository;
import com.soze.common.json.JsonUtils;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@MicronautTest
public class LoginControllerTest {

	@Inject
	@Client("/auth")
	RxHttpClient client;

	@Inject
	private UserRepository userRepository;

	@BeforeEach
	void setup() {
		userRepository.deleteAll();
	}

	@Test
	public void login() {
		CreateUserDTO createUserDTO = new CreateUserDTO("user", "password".toCharArray());
		client.toBlocking().retrieve(HttpRequest.POST("/create", createUserDTO));

		String payload = client.toBlocking().retrieve(HttpRequest.POST("/login", createUserDTO));
		UserDTO userDTO = JsonUtils.parse(payload, UserDTO.class);
		Assertions.assertEquals("user", userDTO.name);
		Assertions.assertNotNull(userDTO.token);
	}

	@Test
	public void login_nullUsername() {
		CreateUserDTO createUserDTO = new CreateUserDTO(null, "password".toCharArray());
		try {
			client.toBlocking().retrieve(HttpRequest.POST("/login", createUserDTO));
		} catch (HttpClientResponseException e) {
			e.printStackTrace();
			Assertions.assertTrue(((String) e.getResponse().body()).contains("Invalid username or password"));
		}
	}

	@Test
	public void loginUser_emptyUsername() {
		CreateUserDTO createUserDTO = new CreateUserDTO("", "password".toCharArray());
		try {
			client.toBlocking().retrieve(HttpRequest.POST("/login", createUserDTO));
		} catch (HttpClientResponseException e) {
			e.printStackTrace();
			Assertions.assertTrue(((String) e.getResponse().body()).contains("Invalid username or password"));
		}
	}

	@Test
	public void loginUser_passwordTooShort() {
		CreateUserDTO createUserDTO = new CreateUserDTO("username", "aps".toCharArray());
		try {
			client.toBlocking().retrieve(HttpRequest.POST("/login", createUserDTO));
		} catch (HttpClientResponseException e) {
			e.printStackTrace();
			Assertions.assertTrue(((String) e.getResponse().body()).contains("Invalid username or password"));
		}
	}

}
