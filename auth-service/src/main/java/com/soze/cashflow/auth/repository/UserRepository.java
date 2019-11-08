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

	public UserRecord findUserByName(String username) {
		return context.selectFrom(User.USER).where(User.USER.NAME.eq(username)).fetchAny();
	}

	public List<UserRecord> findUnconfirmed() {
		Result<UserRecord> result = context.selectFrom(User.USER).where(User.USER.CONFIRMED.eq(false)).fetch();
		return result.map(TableRecordImpl::original);
	}

	public void confirmUser(String username) {
		context.update(User.USER).set(User.USER.CONFIRMED, true).where(User.USER.NAME.eq(username));
	}

	public void deleteAll() {
		context.deleteFrom(User.USER).execute();
	}

}
