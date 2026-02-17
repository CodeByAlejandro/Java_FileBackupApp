package org.codebyalejandro.bacman.persistence.function;

import org.codebyalejandro.bacman.persistence.StatementExecutor;

import java.sql.SQLException;

@FunctionalInterface
public interface StatementExecutorFunction<R> {
	R apply(StatementExecutor stmtExecutor) throws SQLException;
}
