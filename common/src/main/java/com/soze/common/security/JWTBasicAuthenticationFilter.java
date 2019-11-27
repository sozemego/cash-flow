package com.soze.common.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class JWTBasicAuthenticationFilter extends BasicAuthenticationFilter {

	private static final Logger LOG = LoggerFactory.getLogger(JWTBasicAuthenticationFilter.class);

	private static final String AUTHORIZATION = "Authorization";
	private static final String AUTHENTICATION_SCHEME = "Bearer";

	private final AuthService authService;

	public JWTBasicAuthenticationFilter(final AuthenticationManager authenticationManager, final AuthService authService
																		 ) {
		super(authenticationManager);
		this.authService = Objects.requireNonNull(authService);
	}

	@Override
	protected void doFilterInternal(final HttpServletRequest req, final HttpServletResponse res, final FilterChain chain
																 ) throws IOException, ServletException {
		LOG.info("Checking token from {} to {}", req.getRemoteAddr(), req.getRequestURL());
		String header = req.getHeader(AUTHORIZATION);
		LOG.info("Authorization header = {}", header);
		if (header == null) {
			header = getQueryParamToken(req);
		}

		if (header != null) {
			final String[] tokens = header.split(" ");
			if (tokens.length == 2 && ("null".equalsIgnoreCase(tokens[1]) || tokens[1].isEmpty())) {
				chain.doFilter(req, res);
				return;
			}
		}

		if (header == null || !header.startsWith(AUTHENTICATION_SCHEME)) {
			chain.doFilter(req, res);
			return;
		}

		final UsernamePasswordAuthenticationToken authentication = getAuthentication(header);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		chain.doFilter(req, res);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(final String header) {
		final String token = header.substring(AUTHENTICATION_SCHEME.length()).trim();

		if (!authService.validateToken(token)) {
			return null;
		}

		final String username = authService.getUsernameClaim(token);
		final List<? extends GrantedAuthority> grantedAuthorities = authService.getUserAuthorities(username);
		return new UsernamePasswordAuthenticationToken(username, null, grantedAuthorities);
	}

	private String getQueryParamToken(final HttpServletRequest request) {
	  LOG.info("Getting token from query param");
		final String token = ServletRequestUtils.getStringParameter(request, "token", "");
		LOG.info("Token from query param is [{}]", token);
		if (token.isEmpty()) {
			return null;
		}

		return "Bearer " + token;
	}

}
