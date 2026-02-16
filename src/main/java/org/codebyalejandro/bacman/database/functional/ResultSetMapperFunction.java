package org.codebyalejandro.bacman.database.functional;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultSetMapperFunction<R> {
	R apply(ResultSet rs) throws SQLException;
}
