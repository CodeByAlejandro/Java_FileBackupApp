package org.codebyalejandro.bacman.persistence;

import org.codebyalejandro.bacman.persistence.function.PreparedStatementConsumer;
import org.codebyalejandro.bacman.persistence.function.ResultSetMapperFunction;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

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

	public <R> R runQuery(String sql, ResultSetMapperFunction<R> resultMapper) throws SQLException {
		try (var stmt = connection.createStatement(); var rs = stmt.executeQuery(sql)) {
			return resultMapper.apply(rs);
		}
	}

	public <R> R runQuery(
			String sql,
			PreparedStatementConsumer stmtConsumer,
			ResultSetMapperFunction<R> resultMapper) throws SQLException {
		try (var stmt = connection.prepareStatement(sql)) {
			stmtConsumer.accept(stmt);
			try (var rs = stmt.executeQuery()) {
				return resultMapper.apply(rs);
			}
		}
	}

	public <R> R runQueryFromSqlResource(String sqlResourcePath, ResultSetMapperFunction<R> resultMapper) throws SQLException {
		var sql = readStatementFromSqlResource(sqlResourcePath);
		return runQuery(sql, resultMapper);
	}

	public <R> R runQueryFromSqlResource(
			String sqlResourcePath,
			PreparedStatementConsumer stmtConsumer,
			ResultSetMapperFunction<R> resultMapper) throws SQLException {
		var sql = readStatementFromSqlResource(sqlResourcePath);
		return runQuery(sql, stmtConsumer, resultMapper);
	}

	private String readStatementFromSqlResource(String sqlResourcePath) throws SQLException {
		InputStream in = StatementExecutor.class.getResourceAsStream(sqlResourcePath);
		if (in == null) {
			throw new IllegalStateException("SQL resource not found on classpath: " + sqlResourcePath);
		}
		Optional<String> sqlOpt;
		try (var sqlFileReader = new SqlFileReader(in)) {
			sqlOpt = sqlFileReader.readNextStatement();
		} catch (IOException e) {
			throw new SQLException("Error reading SQL resource: " + sqlResourcePath, e);
		}
		return sqlOpt.orElseThrow(() ->
				new IllegalStateException("SQL resource does not contain a valid statement: " + sqlResourcePath));
	}

	public int runUpdate(String sql) throws SQLException {
		try (var stmt = connection.createStatement()) {
			return stmt.executeUpdate(sql);
		}
	}

	public int runUpdate(String sql, PreparedStatementConsumer stmtConsumer) throws SQLException {
		try (var stmt = connection.prepareStatement(sql)) {
			stmtConsumer.accept(stmt);
			return stmt.executeUpdate();
		}
	}

	public void runStatementsFromSqlResource(String sqlResourcePath) throws SQLException {
		InputStream in = StatementExecutor.class.getResourceAsStream(sqlResourcePath);
		if (in == null) {
			throw new IllegalStateException("SQL resource not found on classpath: " + sqlResourcePath);
		}
		try (var sqlFile = new SqlFile(new SqlFileReader(in))) {
			sqlFile.forEachStatement(this::runStatement);
		} catch (IOException e) {
			throw new SQLException("Error reading SQL resource: " + sqlResourcePath, e);
		}
	}
}
