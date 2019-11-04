package com.soze.cashflow.auth.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserDTO {

	public UUID id;
	public LocalDateTime createTime;
	public String name;
	public String token;

	@Override
	public String toString() {
		return "UserDTO{" + "id=" + id + ", createTime=" + createTime + ", name='" + name + '\'' + ", token='" + token + '\'' + '}';
	}
}
