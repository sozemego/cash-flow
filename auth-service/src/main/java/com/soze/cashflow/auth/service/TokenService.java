package com.soze.cashflow.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import io.micronaut.context.annotation.Value;

import javax.inject.Singleton;

@Singleton
public class TokenService {

	private final Algorithm algorithm;

	public TokenService(@Value("${auth.secret}")String secret) {
		algorithm = Algorithm.HMAC256(secret);
	}

	public String createToken(String username) {
		try {
			return JWT.create()
								.withIssuer("cashflow")
								.withClaim("name", username)
								.sign(algorithm);
		} catch (JWTCreationException e) {
			throw new IllegalStateException(e);
		}
	}


}
