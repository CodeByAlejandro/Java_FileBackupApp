package org.codebyalejandro.bacman.database.functional;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface PreparedStatementConsumer {
	void accept(PreparedStatement stmt) throws SQLException;
}
