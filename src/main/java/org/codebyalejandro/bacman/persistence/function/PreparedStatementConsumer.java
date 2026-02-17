package org.codebyalejandro.bacman.persistence.function;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface PreparedStatementConsumer {
	void accept(PreparedStatement stmt) throws SQLException;
}
