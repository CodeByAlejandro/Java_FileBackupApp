package org.codebyalejandro.bacman.database;

import java.sql.Connection;
import java.sql.SQLException;

import static org.codebyalejandro.bacman.core.Validation.requireNonNull;

public class StatementExecutor {
	private final Connection connection;

	StatementExecutor(Connection connection) {
		this.connection = requireNonNull(connection, "connection");
	}

	public void runStatement(String sql) throws SQLException {
		try (var stmt = connection.createStatement()) {
			stmt.execute(sql);
		}
	}

	public <R> R runQuery(String sql, Database.ResultSetMapperFunction<R> resultMapper) throws SQLException {
		try (var stmt = connection.createStatement(); var rs = stmt.executeQuery(sql)) {
			return resultMapper.apply(rs);
		}
	}

	public <R> R runQuery(String sql, Database.PreparedStatementConsumer stmtConsumer, Database.ResultSetMapperFunction<R> resultMapper) throws SQLException {
		try (var stmt = connection.prepareStatement(sql)) {
			stmtConsumer.accept(stmt);
			try (var rs = stmt.executeQuery()) {
				return resultMapper.apply(rs);
			}
		}
	}

	public int runUpdate(String sql) throws SQLException {
		try (var stmt = connection.createStatement()) {
			return stmt.executeUpdate(sql);
		}
	}

	public int runUpdate(String sql, Database.PreparedStatementConsumer stmtConsumer) throws SQLException {
		try (var stmt = connection.prepareStatement(sql)) {
			stmtConsumer.accept(stmt);
			return stmt.executeUpdate();
		}
	}
}
