package com.soze.cashflow.auth.controller;

import io.micronaut.core.io.buffer.ByteBuffer;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MicronautTest;
import io.reactivex.Flowable;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@MicronautTest
class CreateUserControllerTest {

	@Inject
	@Client("/auth")
	RxHttpClient client;

	@Test
	public void testHello() {
		Flowable<HttpResponse<ByteBuffer>> exchange = client.exchange("/create");
		HttpResponse<ByteBuffer> httpResponse = exchange.blockingFirst();
		System.out.println(new String(httpResponse.body().toByteArray()));
	}

}