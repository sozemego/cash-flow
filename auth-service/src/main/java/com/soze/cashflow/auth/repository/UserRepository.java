package com.soze.cashflow.auth.repository;

import io.micronaut.context.annotation.Context;
import org.jooq.*;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Context
public class UserRepository {

	@Inject
	DSLContext context;

}
