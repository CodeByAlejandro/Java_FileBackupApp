package org.codebyalejandro.bacman.persistence;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

class SqlFile implements AutoCloseable {
	private final SqlFileReader sqlFileReader;

	SqlFile(SqlFileReader sqlFileReader) {
		this.sqlFileReader = sqlFileReader;
	}

	@FunctionalInterface
	interface SqlStringConsumer {
		void accept(String sqlStatement) throws SQLException;
	}

	void forEachStatement(SqlStringConsumer sqlStringConsumer) throws IOException, SQLException {
		for (Optional<String> sqlOpt; (sqlOpt = sqlFileReader.readNextStatement()).isPresent(); ) {
			sqlStringConsumer.accept(sqlOpt.get());
		}
	}

	@Override
	public void close() throws IOException {
		sqlFileReader.close();
	}
}
