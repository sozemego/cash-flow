package com.soze.cashflow.auth.controller;

import com.soze.cashflow.auth.dto.CreateUserDTO;
import com.soze.cashflow.auth.dto.UserDTO;
import com.soze.cashflow.auth.repository.UserRepository;
import com.soze.common.json.JsonUtils;
import io.micronaut.core.io.buffer.ByteBuffer;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MicronautTest;
import io.reactivex.Flowable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.nio.charset.Charset;

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
		Flowable<HttpResponse<ByteBuffer>> exchange = client.exchange(post);
		UserDTO userDTO = parse(exchange, UserDTO.class);
		System.out.println(userDTO);
		Assertions.assertEquals("user", userDTO.name);
		Assertions.assertNotNull(userDTO.createTime);
		Assertions.assertNotNull(userDTO.id);
		Assertions.assertNotNull(userDTO.token);
	}

	private <T> T parse(Flowable<HttpResponse<ByteBuffer>> flowable, Class<T> clazz) {
		HttpResponse<ByteBuffer> httpResponse = flowable.blockingFirst();
		String responseBody = httpResponse.body().toString(Charset.defaultCharset());
		return JsonUtils.parse(responseBody, clazz);
	}

}