package com.soze.cashflow.apigateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private final AuthService authService;

	@Autowired
	public SecurityConfiguration(AuthService authService) {
		this.authService = authService;
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.cors()
				.and()
				.csrf()
				.disable()
				.authorizeRequests()
				.antMatchers("/auth-service/**")
				.permitAll()
				.anyRequest()
				.authenticated()
				.and()
				.addFilter(new JWTBasicAuthenticationFilter(authenticationManager(), authService))
				// this disables session creation on Spring Security
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

}
