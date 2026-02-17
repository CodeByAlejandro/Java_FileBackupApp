package org.codebyalejandro.bacman.persistence.function;

import org.codebyalejandro.bacman.persistence.StatementExecutor;

import java.sql.SQLException;

@FunctionalInterface
public interface StatementExecutorConsumer {
	void accept(StatementExecutor stmtExecutor) throws SQLException;
}
