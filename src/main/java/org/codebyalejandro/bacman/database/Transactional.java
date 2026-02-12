package org.codebyalejandro.bacman.database;

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

	static void inTransaction(Connection conn, ConnectionConsumer statements) throws SQLException {
		boolean previousAutoCommit = conn.getAutoCommit();
		conn.setAutoCommit(false);
		try {
			statements.accept(conn);
			conn.commit();
		} catch (SQLException | RuntimeException e) {
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
