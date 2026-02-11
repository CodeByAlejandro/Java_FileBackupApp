package org.codebyalejandro.bacman.database;

import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;
import java.nio.file.Path;

/**
 * Central place to create a configured SQLite {@link DataSource}.
 *
 * <p>Key idea: you don't want PRAGMA/config scattered across the codebase.
 * A DataSource lets you standardize connection creation and (optionally) pool later.
 */
public final class SQLiteDataSourceFactory {
	private SQLiteDataSourceFactory() {
	}

	static DataSource create(Path databasePath) {
		SQLiteConfig config = new SQLiteConfig();

		// Concurrency-friendly defaults for a multi-threaded app.
		config.setJournalMode(SQLiteConfig.JournalMode.WAL);
		config.setSynchronous(SQLiteConfig.SynchronousMode.FULL);
		config.enforceForeignKeys(true);
		config.setBusyTimeout(5_000);

		SQLiteDataSource ds = new SQLiteDataSource(config);
		ds.setUrl("jdbc:sqlite:" + databasePath.toAbsolutePath());
		return ds;
	}
}
