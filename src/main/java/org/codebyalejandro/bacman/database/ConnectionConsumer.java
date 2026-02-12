package org.codebyalejandro.bacman.database;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface ConnectionConsumer {
	void accept(Connection conn) throws SQLException;
}
