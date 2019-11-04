package com.soze.cashflow.auth.service;

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
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@Singleton
public class AuthService {

	private static final Logger LOG = LoggerFactory.getLogger(AuthService.class);
	private final UserRepository userRepository;
	private final TokenService tokenService;

	@Inject
	public AuthService(UserRepository userRepository, TokenService tokenService) {
		this.userRepository = userRepository;
		this.tokenService = tokenService;
	}

	public UserRecord createUser(String username, char[] password) {
		LOG.info("Creating user = {}", username);
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);

		if (username.isEmpty()) {
			throw new IllegalArgumentException("Username cannot be empty");
		}

		if (password.length < 6) {
			throw new IllegalArgumentException("Password cannot be shorter than 6 characters");
		}

		UserRecord createUserRecord = new UserRecord();
		createUserRecord.values(
			UUID.randomUUID(), Timestamp.from(Instant.now()), username,
			BCrypt.hashpw(new String(password), BCrypt.gensalt())
										 );
		Arrays.fill(password, 'a');

		userRepository.saveUser(createUserRecord);
		LOG.info("User created = {}", username);

		return userRepository.findUserByName(username);
	}

	public String login(CreateUserDTO createUserDTO) {
		Objects.requireNonNull(createUserDTO);
		LOG.info("Logging in user = {}", createUserDTO.username);

		UserRecord userRecord = userRepository.findUserByName(createUserDTO.username);
		String hash = userRecord.getHash();
		boolean passwordMatches = BCrypt.checkpw(new String(createUserDTO.password), hash);
		if (!passwordMatches) {
			Arrays.fill(createUserDTO.password, 'a');
			throw new IllegalArgumentException("Invalid username or password");
		}

		return tokenService.createToken(createUserDTO.username);
	}


}
