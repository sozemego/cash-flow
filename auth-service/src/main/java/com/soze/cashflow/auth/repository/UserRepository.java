package com.soze.cashflow.auth.repository;

import com.soze.cashflow.auth.domain.tables.User;
import com.soze.cashflow.auth.domain.tables.records.UserRecord;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.TableField;
import org.jooq.impl.TableRecordImpl;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.UUID;

@Singleton
public class UserRepository {

	@Inject
	DSLContext context;

	public UserRecord userRecord() {
		return context.newRecord(User.USER);
	}

	public void saveUser(UserRecord userRecord) {
		userRecord.store();
	}

	public UserRecord findUserById(UUID id) {
		return context.selectFrom(User.USER).where(User.USER.ID.eq(id)).fetchAny();
	}

	public UserRecord findUserByName(String username) {
		return context.selectFrom(User.USER).where(User.USER.NAME.eq(username)).fetchAny();
	}

	public void deleteAll() {
		context.deleteFrom(User.USER).execute();
	}

}
