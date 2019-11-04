package com.soze.cashflow.auth.repository;

import com.soze.cashflow.auth.domain.tables.User;
import com.soze.cashflow.auth.domain.tables.records.UserRecord;
import org.jooq.DSLContext;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserRepository {

	@Inject
	DSLContext context;

	public void saveUser(UserRecord userRecord) {
		context.insertInto(User.USER)
					 .values(userRecord.getId(), userRecord.getCreateTime(), userRecord.getName(), userRecord.getHash())
					 .execute();
	}

	public UserRecord findUserByName(String username) {
		return context.selectFrom(User.USER).where(User.USER.NAME.eq(username)).fetchAny();
	}

	public void deleteAll() {
		context.deleteFrom(User.USER).execute();
	}

}
