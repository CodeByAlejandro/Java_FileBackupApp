package org.codebyalejandro.bacman.database.sql;

import java.io.*;

/**
 * Reads SQL statements from a classpath resource, splitting on semicolons that are not inside quotes.
 *
 * <p>Returns {@code null} on EOF.
 */
final class SqlFileReader implements Closeable {
	private final BufferedReader reader;
	private String remainingContentAfterLastStatementEndingChar;

	SqlFileReader(String sqlFilePath) {
		InputStream in = getClass().getClassLoader().getResourceAsStream(sqlFilePath);
		if (in == null) {
			throw new IllegalArgumentException("SQL file not found: " + sqlFilePath);
		}
		reader = new BufferedReader(new InputStreamReader(in));
	}

	String readNextStatement() throws IOException {
		boolean statementComplete = false;
		StringBuilder statementBuilder = new StringBuilder();

		for (String line; !statementComplete; ) {
			line = fetchNextSqlLine();
			if (line == null) break;

			String trimmedLine = line.trim();
			if (isWhitespaceOnlyOrCommentLine(trimmedLine)) {
				continue;
			}

			statementComplete = parseSqlLine(trimmedLine, statementBuilder);
		}

		return statementComplete ? statementBuilder.toString() : null;
	}

	private String fetchNextSqlLine() throws IOException {
		if (remainingContentAfterLastStatementEndingChar != null) {
			String line = remainingContentAfterLastStatementEndingChar;
			remainingContentAfterLastStatementEndingChar = null;
			return line;
		}
		return reader.readLine();
	}

	private static boolean isWhitespaceOnlyOrCommentLine(String sqlLine) {
		return sqlLine.isEmpty() || sqlLine.startsWith("--");
	}

	private boolean parseSqlLine(String sqlLine, StringBuilder statementBuilder) {
		String sqlLineWithoutComment = removeEndingCommentFromSqlLine(sqlLine);
		int statementEndingCharIdx = indexOfStatementEndingChar(sqlLineWithoutComment);
		if (statementEndingCharIdx != -1) {
			statementBuilder.append(sqlLineWithoutComment, 0, statementEndingCharIdx + 1)
					.append(System.lineSeparator());
			setRemainingContentAfterLastStatementEndingChar(sqlLineWithoutComment, statementEndingCharIdx);
			return true;
		}

		statementBuilder.append(sqlLineWithoutComment).append(System.lineSeparator());
		return false;
	}

	private void setRemainingContentAfterLastStatementEndingChar(String sqlLine, int statementEndingCharIdx) {
		remainingContentAfterLastStatementEndingChar = sqlLine.substring(statementEndingCharIdx + 1);
	}

	private static String removeEndingCommentFromSqlLine(String sqlLine) {
		int inlineCommentIdx = sqlLine.indexOf("--");
		if (inlineCommentIdx != -1) {
			return sqlLine.substring(0, inlineCommentIdx);
		}
		return sqlLine;
	}

	private static int indexOfStatementEndingChar(String sqlLine) {
		int fromIdx = 0;
		for (int semicolonIdx; (semicolonIdx = sqlLine.indexOf(';', fromIdx)) != -1; fromIdx = semicolonIdx + 1) {
			if (!isSemicolonInStringLiteral(sqlLine, semicolonIdx)) {
				return semicolonIdx;
			}
		}
		return -1;
	}

	private static boolean isSemicolonInStringLiteral(String sqlLine, int semicolonIdx) {
		boolean inSingleQuotes = false;
		boolean inDoubleQuotes = false;
		for (int i = 0; i < semicolonIdx; i++) {
			char c = sqlLine.charAt(i);
			if (c == '\'') {
				inSingleQuotes = !inSingleQuotes;
			} else if (c == '"') {
				inDoubleQuotes = !inDoubleQuotes;
			}
		}
		return inSingleQuotes || inDoubleQuotes;
	}

	@Override
	public void close() throws IOException {
		reader.close();
	}
}
