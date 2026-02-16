package org.codebyalejandro.bacman.database;

import javax.sql.DataSource;
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

	public <R> R runQuery(String sql, StatementExecutor.ResultSetMapperFunction<R> resultMapper) throws SQLException {
		try (var conn = dataSource.getConnection()) {
			return new StatementExecutor(conn).runQuery(sql, resultMapper);
		}
	}

	public <R> R runQuery(
			String sql,
			StatementExecutor.PreparedStatementConsumer stmtConsumer,
			StatementExecutor.ResultSetMapperFunction<R> resultMapper) throws SQLException {
		try (var conn = dataSource.getConnection()) {
			return new StatementExecutor(conn).runQuery(sql, stmtConsumer, resultMapper);
		}
	}

	public int runUpdate(String sql) throws SQLException {
		try (var conn = dataSource.getConnection()) {
			return new StatementExecutor(conn).runUpdate(sql);
		}
	}

	public int runUpdate(String sql, StatementExecutor.PreparedStatementConsumer stmtConsumer) throws SQLException {
		try (var conn = dataSource.getConnection()) {
			return new StatementExecutor(conn).runUpdate(sql, stmtConsumer);
		}
	}

	@FunctionalInterface
	public interface StatementExecutorConsumer {
		void accept(StatementExecutor stmtExecutor) throws SQLException;
	}

	public void inTranaction(StatementExecutorConsumer stmtExecutorConsumer) throws SQLException {
		try (var conn = dataSource.getConnection()) {
			Transactional.inTransaction(conn, (Transactional.ConnectionConsumer)
					connection -> stmtExecutorConsumer.accept(new StatementExecutor(connection)));
		}
	}

	@FunctionalInterface
	public interface StatementExecutorFunction<R> {
		R apply(StatementExecutor stmtExecutor) throws SQLException;
	}

	public <R> R inTranaction(StatementExecutorFunction<R> stmtExecutorFunction) throws SQLException {
		try (var conn = dataSource.getConnection()) {
			return Transactional.inTransaction(conn, (Transactional.ConnectionFunction<R>)
					connection -> stmtExecutorFunction.apply(new StatementExecutor(connection)));
		}
	}
}
