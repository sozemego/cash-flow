package com.soze.cashflow.logaggregator.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "log_aggregator.log")
public class LogEventEntity {

	@Id
	private UUID id;
	@Column
	private String application;
	@Column
	private String level;
	@Column
	private String message;
	@Column
	private Timestamp timestamp;

	public LogEventEntity() {

	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "LogEventEntity{" + "id=" + id + ", application='" + application + '\'' + ", level='" + level + '\'' + ", message='" + message + '\'' + ", timestamp=" + timestamp + '}';
	}
}
