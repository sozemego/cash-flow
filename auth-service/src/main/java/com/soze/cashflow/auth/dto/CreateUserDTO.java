package com.soze.cashflow.auth.dto;

public class CreateUserDTO {

	public String username;
	public char[] password;

	public CreateUserDTO() {
	}

	public CreateUserDTO(String username, char[] password) {
		this.username = username;
		this.password = password;
	}
}
