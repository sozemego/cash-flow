package com.soze.cashflow.auth.controller;

import com.soze.cashflow.auth.dto.CreateUserDTO;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.nio.charset.Charset;

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
		client.exchange(HttpRequest.POST("/create", createUserDTO)).blockingFirst();
		Flowable<HttpResponse<ByteBuffer>> flowable = client.exchange(HttpRequest.POST("/login", createUserDTO));
		String token = parse(flowable, String.class);
		System.out.println(token);
	}

	private <T> T parse(Flowable<HttpResponse<ByteBuffer>> flowable, Class<T> clazz) {
		HttpResponse<ByteBuffer> httpResponse = flowable.blockingFirst();
		System.out.println(httpResponse.body().toByteArray().length);
		if (clazz == String.class) {
			return (T) new String(httpResponse.body().toByteArray());
		}
		String responseBody = httpResponse.body().toString(Charset.defaultCharset());
		return JsonUtils.parse(responseBody, clazz);
	}

}
