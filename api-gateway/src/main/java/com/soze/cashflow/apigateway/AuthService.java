package com.soze.cashflow.apigateway;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AuthService {

	private static final String USER_NAME_CLAIM = "name";

	public boolean validateToken(String token) {
		try {
			decodeToken(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String getUsernameClaim(String token) {
		DecodedJWT decodedJWT = decodeToken(token);
		Claim claim = decodedJWT.getClaim(USER_NAME_CLAIM);
		if (claim.isNull()) {
			//TODO make own exception
			throw new IllegalArgumentException("NO USERNAME CLAIM");
		}
		return claim.asString();
	}

	private DecodedJWT decodeToken(String token) {
		Objects.requireNonNull(token);
		return JWT.decode(token);
	}

	public List<? extends GrantedAuthority> getUserAuthorities(String username) {
		return new ArrayList<>();
	}
}
