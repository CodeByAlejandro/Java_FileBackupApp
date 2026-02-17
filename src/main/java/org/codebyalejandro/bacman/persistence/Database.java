package org.codebyalejandro.bacman.persistence;

import org.codebyalejandro.bacman.persistence.function.PreparedStatementConsumer;
import org.codebyalejandro.bacman.persistence.function.ResultSetMapperFunction;
import org.codebyalejandro.bacman.persistence.function.StatementExecutorConsumer;
import org.codebyalejandro.bacman.persistence.function.StatementExecutorFunction;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

public class Database {
	private final Path path;
	private final DataSource dataSource;

	public Database(String pathString) {
		path = Paths.get(pathString);
		dataSource = SQLiteDataSourceFactory.create(path);
	}

	public Path getDatabasePath() {
		return path;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void runStatement(String sql) throws SQLException {
		try (var conn = dataSource.getConnection()) {
			new StatementExecutor(conn).runStatement(sql);
		}
	}

	public <R> R runQuery(String sql, ResultSetMapperFunction<R> resultMapper) throws SQLException {
		try (var conn = dataSource.getConnection()) {
			return new StatementExecutor(conn).runQuery(sql, resultMapper);
		}
	}

	public <R> R runQuery(
			String sql,
			PreparedStatementConsumer stmtConsumer,
			ResultSetMapperFunction<R> resultMapper) throws SQLException {
		try (var conn = dataSource.getConnection()) {
			return new StatementExecutor(conn).runQuery(sql, stmtConsumer, resultMapper);
		}
	}

	public <R> R runQueryFromSqlResource(String sqlResourcePath, ResultSetMapperFunction<R> resultMapper) throws IOException, SQLException {
		try (var conn = dataSource.getConnection()) {
			return new StatementExecutor(conn).runQueryFromSqlResource(sqlResourcePath, resultMapper);
		}
	}

	public <R> R runQueryFromSqlResource(
			String sqlResourcePath,
			PreparedStatementConsumer stmtConsumer,
			ResultSetMapperFunction<R> resultMapper) throws IOException, SQLException {
		try (var conn = dataSource.getConnection()) {
			return new StatementExecutor(conn).runQueryFromSqlResource(sqlResourcePath, stmtConsumer, resultMapper);
		}
	}

	public int runUpdate(String sql) throws SQLException {
		try (var conn = dataSource.getConnection()) {
			return new StatementExecutor(conn).runUpdate(sql);
		}
	}

	public int runUpdate(String sql, PreparedStatementConsumer stmtConsumer) throws SQLException {
		try (var conn = dataSource.getConnection()) {
			return new StatementExecutor(conn).runUpdate(sql, stmtConsumer);
		}
	}

	public void inTranaction(StatementExecutorConsumer stmtExecutorConsumer) throws SQLException {
		try (var conn = dataSource.getConnection()) {
			Transactional.inTransaction(conn, (Transactional.ConnectionConsumer)
					connection -> stmtExecutorConsumer.accept(new StatementExecutor(connection)));
		}
	}

	public <R> R inTranaction(StatementExecutorFunction<R> stmtExecutorFunction) throws SQLException {
		try (var conn = dataSource.getConnection()) {
			return Transactional.inTransaction(conn, (Transactional.ConnectionFunction<R>)
					connection -> stmtExecutorFunction.apply(new StatementExecutor(connection)));
		}
	}
}
