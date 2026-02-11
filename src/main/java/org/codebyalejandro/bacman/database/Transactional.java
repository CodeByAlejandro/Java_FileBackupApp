package org.codebyalejandro.bacman.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * JDBC transaction helper.
 *
 * <p>JDBC transactions don't require manual "BEGIN" SQL. You typically use:
 * <ul>
 *   <li>{@code conn.setAutoCommit(false)}</li>
 *   <li>{@code conn.commit()}</li>
 *   <li>{@code conn.rollback()}</li>
 * </ul>
 *
 * <p>This class wraps that pattern and guarantees:
 * <ul>
 *   <li>rollback on any failure</li>
 *   <li>autoCommit is restored to its previous value</li>
 * </ul>
 */
public final class Transactional {
	private Transactional() {
	}

	@FunctionalInterface
	interface SqlRunnable {
		void run() throws SQLException, IOException;
	}

	@FunctionalInterface
	interface DataSupplier<R> {
		R get() throws SQLException, IOException;
	}

	static void inTransaction(Connection conn, SqlRunnable sql) throws SQLException, IOException {
		inTransaction(conn, () -> {
			sql.run();
			return null;
		});
	}

	static <R> R inTransaction(Connection conn, DataSupplier<R> supplier) throws SQLException, IOException {
		boolean previousAutoCommit = conn.getAutoCommit();
		conn.setAutoCommit(false);
		try {
			R result = supplier.get();
			conn.commit();
			return result;
		} catch (SQLException | IOException | RuntimeException e) {
			rollbackQuietly(conn, e);
			throw e;
		} finally {
			conn.setAutoCommit(previousAutoCommit);
		}
	}

	private static void rollbackQuietly(Connection conn, Exception original) {
		try {
			conn.rollback();
		} catch (SQLException rollbackEx) {
			original.addSuppressed(rollbackEx);
		}
	}
}
