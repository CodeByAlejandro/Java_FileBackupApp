package org.codebyalejandro.bacman.database;

import org.codebyalejandro.bacman.database.sql.SqlFile;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {
	private final Path databasePath;
	private final DataSource dataSource;

	public Database() {
		this(Paths.get("BacMan.db"));
	}

	public Database(Path databasePath) {
		this(databasePath, SQLiteDataSourceFactory.create(databasePath));
	}

	public Database(Path databasePath, DataSource dataSource) {
		this.databasePath = databasePath;
		this.dataSource = dataSource;
	}

	public Path getDatabasePath() {
		return databasePath;
	}

	public void runStatement(String sql) throws SQLException {
		try (var conn = dataSource.getConnection();
			 var stmt = conn.createStatement()) {
			stmt.execute(sql);
		}
	}

	public <R> R runQuery(String sql, ResultSetMapperFunction<R> resultMapper) throws SQLException {
		try (var conn = dataSource.getConnection();
			 var stmt = conn.createStatement();
			 var rs = stmt.executeQuery(sql)) {
			return resultMapper.apply(rs);
		}
	}

	public <R> R runQuery(String sql, PreparedStatementConsumer stmtConsumer, ResultSetMapperFunction<R> resultMapper) throws SQLException {
		try (var conn = dataSource.getConnection();
			 var stmt = conn.prepareStatement(sql)) {
			stmtConsumer.accept(stmt);
			try (var rs = stmt.executeQuery()) {
				return resultMapper.apply(rs);
			}
		}
	}

	public int runUpdate(String sql) throws SQLException {
		try (var conn = dataSource.getConnection();
			 var stmt = conn.createStatement()) {
			return stmt.executeUpdate(sql);
		}
	}

	public int runUpdate(String sql, PreparedStatementConsumer stmtConsumer) throws SQLException {
		try (var conn = dataSource.getConnection();
			 var stmt = conn.prepareStatement(sql)) {
			stmtConsumer.accept(stmt);
			return stmt.executeUpdate();
		}
	}

	public void inTransaction(ConnectionConsumer statements) throws SQLException {
		try (var conn = dataSource.getConnection()) {
			Transactional.inTransaction(conn, statements);
		}
	}

	public void runDdlFile(String ddlFilePath) throws SQLException {
		inTransaction(connection -> runDdlFile(ddlFilePath, connection));
	}

	private void runDdlFile(String ddlFilePath, Connection conn) throws SQLException {
		try (var sqlFile = new SqlFile(ddlFilePath)) {
			sqlFile.forEachStatement(sqlStatement -> {
				try (var stmt = conn.createStatement()) {
					stmt.execute(sqlStatement);
				}
			});
			try (var stmt = conn.prepareStatement("INSERT INTO db_migrations (migration_file) VALUES (?)")) {
				stmt.setString(1, ddlFilePath);
				stmt.executeUpdate();
			}
		} catch (SQLException | IOException e) {
			throw new SQLException("Failed to run DDL file: " + ddlFilePath, e);
		}
	}

	@FunctionalInterface
	public interface PreparedStatementConsumer {
		void accept(PreparedStatement stmt) throws SQLException;
	}

	@FunctionalInterface
	public interface ResultSetMapperFunction<R> {
		R apply(ResultSet rs) throws SQLException;
	}
}
