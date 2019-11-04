package com.soze.cashflow.auth.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserDTO {

	public UUID uuid;
	public LocalDateTime createTime;
	public String name;
	public String token;

}
