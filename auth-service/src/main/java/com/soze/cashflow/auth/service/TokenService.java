package com.soze.cashflow.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.soze.cashflow.auth.domain.tables.records.UserRecord;
import io.micronaut.context.annotation.Value;

import javax.inject.Singleton;

@Singleton
public class TokenService {

	private final Algorithm algorithm;

	public TokenService(@Value("${auth.secret}")String secret) {
		algorithm = Algorithm.HMAC256(secret);
	}

	public String createToken(UserRecord userRecord) {
		try {
			return JWT.create()
								.withIssuer("cashflow")
								.withClaim("name", userRecord.getName())
								.withClaim("id", userRecord.getId().toString())
								.sign(algorithm);
		} catch (JWTCreationException e) {
			throw new IllegalStateException(e);
		}
	}


}
