package com.soze.cashflow.auth.service;

import com.soze.cashflow.auth.AuthException;
import com.soze.cashflow.auth.domain.tables.records.UserRecord;
import com.soze.cashflow.auth.dto.CreateUserDTO;
import com.soze.cashflow.auth.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Singleton
public class AuthService {

	private static final Logger LOG = LoggerFactory.getLogger(AuthService.class);
	private final UserRepository userRepository;
	private final TokenService tokenService;
	private final UserCreatedConfirmationService confirmationService;

	@Inject
	public AuthService(UserRepository userRepository, TokenService tokenService,
										 UserCreatedConfirmationService confirmationService
										) {
		this.userRepository = userRepository;
		this.tokenService = tokenService;
		this.confirmationService = confirmationService;
	}

	public UserRecord createUser(String username, char[] password) {
		LOG.info("Creating user = {}", username);

		if (username == null || username.isEmpty()) {
			throw new AuthException("Username cannot be empty");
		}

		if (password == null || password.length < 6) {
			throw new AuthException("Password cannot be shorter than 6 characters");
		}

		UserRecord existingUser = userRepository.findUserByName(username);
		if (existingUser != null) {
			throw new AuthException("Username already exists!");
		}

		UserRecord createUserRecord = userRepository.userRecord();
		createUserRecord.values(UUID.randomUUID(), Timestamp.from(Instant.now()), username,
														BCrypt.hashpw(new String(password), BCrypt.gensalt())
													 );

		userRepository.saveUser(createUserRecord);
		LOG.info("User created = {}", username);

		UserRecord createdUser = userRepository.findUserByName(username);
		confirmationService.sendUserCreated(createUserRecord);

		return createdUser;
	}

	public String login(CreateUserDTO createUserDTO) {
		Objects.requireNonNull(createUserDTO);
		LOG.info("Logging in user = {}", createUserDTO.username);

		UserRecord userRecord = userRepository.findUserByName(createUserDTO.username);
		String hash = userRecord.getHash();
		boolean passwordMatches = BCrypt.checkpw(new String(createUserDTO.password), hash);
		if (!passwordMatches) {
			Arrays.fill(createUserDTO.password, 'a');
			throw new AuthException("Invalid username or password");
		}

		return tokenService.createToken(userRecord);
	}

	public UserRecord findUserByName(String username) {
		return userRepository.findUserByName(username);
	}

}
