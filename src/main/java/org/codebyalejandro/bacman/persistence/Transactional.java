package org.codebyalejandro.bacman.persistence;

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
final class Transactional {
	private Transactional() {
	}

	@FunctionalInterface
	interface ConnectionConsumer {
		void accept(Connection conn) throws SQLException;
	}

	@FunctionalInterface
	interface ConnectionFunction<R> {
		R apply(Connection conn) throws SQLException;
	}

	static void inTransaction(Connection conn, ConnectionConsumer statements) throws SQLException {
		inTransaction(conn, connection -> {
			statements.accept(connection);
			return null;
		});
	}

	static <R> R inTransaction(Connection conn, ConnectionFunction<R> statements) throws SQLException {
		boolean previousAutoCommit = conn.getAutoCommit();
		conn.setAutoCommit(false);
		Throwable primary = null;
		try {
			R result = statements.apply(conn);
			conn.commit();
			return result;
		} catch (SQLException | RuntimeException e) {
			primary = e;
			rollbackQuietly(conn, e);
			throw e;
		} finally {
			try {
				conn.setAutoCommit(previousAutoCommit);
			} catch (SQLException e) {
				if (primary != null) {
					primary.addSuppressed(e);
				} else {
					throw e;
				}
			}
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
