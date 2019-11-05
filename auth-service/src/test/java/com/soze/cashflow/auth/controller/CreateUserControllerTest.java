package com.soze.cashflow.auth.controller;

import com.soze.cashflow.auth.dto.CreateUserDTO;
import com.soze.cashflow.auth.dto.UserDTO;
import com.soze.cashflow.auth.repository.UserRepository;
import com.soze.common.json.JsonUtils;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@MicronautTest
class CreateUserControllerTest {

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
	public void createUser() {
		CreateUserDTO createUserDTO = new CreateUserDTO("user", "password".toCharArray());
		MutableHttpRequest<CreateUserDTO> post = HttpRequest.POST("/create", createUserDTO);
		String payload = client.toBlocking().retrieve(post);
		UserDTO userDTO = JsonUtils.parse(payload, UserDTO.class);
		Assertions.assertEquals("user", userDTO.name);
		Assertions.assertNotNull(userDTO.createTime);
		Assertions.assertNotNull(userDTO.id);
		Assertions.assertNotNull(userDTO.token);
	}

	@Test
	public void createUser_nullUsername() {
		CreateUserDTO createUserDTO = new CreateUserDTO(null, "password".toCharArray());
		try {
			client.toBlocking().retrieve(HttpRequest.POST("/create", createUserDTO));
		} catch (HttpClientResponseException e) {
			e.printStackTrace();
			Assertions.assertTrue(((String) e.getResponse().body()).contains("Username cannot be empty"));
		}
	}

	@Test
	public void createUser_emptyUsername() {
		CreateUserDTO createUserDTO = new CreateUserDTO("", "password".toCharArray());
		try {
			client.toBlocking().retrieve(HttpRequest.POST("/create", createUserDTO));
		} catch (HttpClientResponseException e) {
			e.printStackTrace();
			Assertions.assertTrue(((String) e.getResponse().body()).contains("Username cannot be empty"));
		}
	}

	@Test
	public void createUser_passwordTooShort() {
		CreateUserDTO createUserDTO = new CreateUserDTO("username", "aps".toCharArray());
		try {
			client.toBlocking().retrieve(HttpRequest.POST("/create", createUserDTO));
		} catch (HttpClientResponseException e) {
			e.printStackTrace();
			Assertions.assertTrue(((String) e.getResponse().body()).contains("Password cannot be shorter than 6 characters"));
		}
	}

}