package org.codebyalejandro.bacman.database;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
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

	@FunctionalInterface
	public interface ConnectionConsumer {
		void accept(Connection conn) throws SQLException, IOException;
	}

	@FunctionalInterface
	public interface ConnectionFunction {
		ResultSet apply(Connection conn) throws SQLException, IOException;
	}

	@FunctionalInterface
	public interface ResultSetMapperFunction<R> {
		R apply(ResultSet result) throws SQLException;
	}

	public void withConnection(ConnectionConsumer consumer) throws SQLException, IOException {
		try (Connection conn = dataSource.getConnection()) {
			consumer.accept(conn);
		}
	}

	public void withConnectionAndTransaction(ConnectionConsumer consumer) throws SQLException, IOException {
		withConnection((ConnectionConsumer) conn -> Transactional.inTransaction(conn, () -> consumer.accept(conn)));
	}

	public ResultSet withConnection(ConnectionFunction function) throws SQLException, IOException {
		try (Connection conn = dataSource.getConnection()) {
			return function.apply(conn);
		}
	}

	public ResultSet withConnectionAndTransaction(ConnectionFunction function) throws SQLException, IOException {
		return withConnection((ConnectionFunction) conn -> Transactional.inTransaction(conn, () -> function.apply(conn)));
	}

	public <R> R withConnection(ConnectionFunction function, ResultSetMapperFunction<R> mapper) throws SQLException, IOException {
		try (Connection conn = dataSource.getConnection()) {
			ResultSet resultSet = function.apply(conn);
			return mapper.apply(resultSet);
		}
	}

	public <R> R withConnectionAndTransaction(ConnectionFunction function, ResultSetMapperFunction<R> mapper) throws SQLException, IOException {
		return withConnection(conn -> Transactional.inTransaction(conn, () -> function.apply(conn)), mapper);
	}

}
