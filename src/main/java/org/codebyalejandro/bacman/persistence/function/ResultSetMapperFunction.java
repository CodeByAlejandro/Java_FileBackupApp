package org.codebyalejandro.bacman.persistence.function;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultSetMapperFunction<R> {
	R apply(ResultSet rs) throws SQLException;
}
