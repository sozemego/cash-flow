package com.soze.factory.store;

import com.vladmihalcea.hibernate.type.json.JsonNodeBinaryType;
import org.hibernate.dialect.PostgreSQL95Dialect;

import java.sql.Types;

public class PostgreSQL95JsonDialect extends PostgreSQL95Dialect {

	public PostgreSQL95JsonDialect() {
		this.registerHibernateType(Types.OTHER, JsonNodeBinaryType.class.getName());
	}
}