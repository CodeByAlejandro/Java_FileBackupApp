package org.codebyalejandro.BacMan.database;

import java.util.Iterator;

public class SqlFile implements Iterable<String>, AutoCloseable {
	private final String sqlFilePath;
	private SqlFileIterator sqlFileIterator;

	public SqlFile(String sqlFilePath) {
		this.sqlFilePath = sqlFilePath;
	}

	@Override
	public Iterator<String> iterator() {
		sqlFileIterator = new SqlFileIterator(sqlFilePath);
		return sqlFileIterator;
	}

	@Override
	public void close() throws Exception {
		if (sqlFileIterator != null) {
			sqlFileIterator.close();
		}
	}

}