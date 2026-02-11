package org.codebyalejandro.BacMan.database.sql;

import java.io.IOException;
import java.sql.SQLException;

public class SqlFile implements AutoCloseable {
	private final String sqlFilePath;
	private SqlFileReader sqlFileReader;
	private boolean isClosed = false;

	public SqlFile(String sqlFilePath) {
		this.sqlFilePath = sqlFilePath;
	}

	/**
	 * Iterates through SQL statements in the file and calls {@code consumer} for each one.
	 *
	 * <p>This avoids {@link java.util.Iterator} because it can't throw checked {@link IOException}.
	 */
	public void forEachStatement(StatementConsumer consumer) throws IOException, SQLException {
		ensureOpen();
		for (String stmt; (stmt = sqlFileReader.readNextStatement()) != null; ) {
			consumer.accept(stmt);
		}
	}

	@FunctionalInterface
	public interface StatementConsumer {
		void accept(String sqlStatement) throws SQLException;
	}

	private void ensureOpen() {
		if (isClosed) {
			throw new IllegalStateException("SqlFile is closed and cannot be reused: " + sqlFilePath);
		}
		if (sqlFileReader == null) {
			sqlFileReader = new SqlFileReader(sqlFilePath);
		}
	}

	@Override
	public void close() throws IOException {
		isClosed = true;
		if (sqlFileReader != null) {
			sqlFileReader.close();
		}
	}
}