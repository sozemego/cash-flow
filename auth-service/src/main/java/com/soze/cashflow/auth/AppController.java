package com.soze.cashflow.auth;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

@Controller("/hi")
public class AppController {

	@Get("/")
	@Produces(MediaType.TEXT_PLAIN)
	public String hello() {
		return "why hello!";
	}

}
