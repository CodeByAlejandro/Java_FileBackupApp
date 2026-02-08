package org.codebyalejandro.BacMan.database;

import java.io.*;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;

public class SqlFileIterator implements Iterator<String> {
	private final BufferedReader reader;
	private String nextStatement;
	private String remainingContentAfterLastStatementEndingChar;

	public SqlFileIterator(String sqlFilePath) {
		InputStream in = getClass().getClassLoader().getResourceAsStream(sqlFilePath);
		if (in == null) {
			throw new IllegalArgumentException("SQL file not found: " + sqlFilePath);
		}
		reader = new BufferedReader(new InputStreamReader(in));
	}

	@Override
	public boolean hasNext() {
		Optional<String> nextStatementOpt;
		try {
			nextStatementOpt = readNextStatement();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		if (nextStatementOpt.isPresent()) {
			nextStatement = nextStatementOpt.get();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String next() {
		if (nextStatement == null) {
			if (hasNext()) {
				return usePreFetchedStatement();
			} else {
				throw new NoSuchElementException("No more SQL statements available.");
			}
		} else {
			return usePreFetchedStatement();
		}
	}

	public void close() throws IOException {
		reader.close();
	}

	private String usePreFetchedStatement() {
		String statementToReturn = nextStatement;
		nextStatement = null;
		return statementToReturn;
	}

	private Optional<String> readNextStatement() throws IOException {
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

		return statementComplete ? Optional.of(statementBuilder.toString()) : Optional.empty();
	}

	private String fetchNextSqlLine() throws IOException {
		String line;
		if (remainingContentAfterLastStatementEndingChar != null) {
			line = remainingContentAfterLastStatementEndingChar;
			remainingContentAfterLastStatementEndingChar = null;
		} else {
			line = reader.readLine();
		}
		return line;
	}

	private boolean isWhitespaceOnlyOrCommentLine(String sqlLine) {
		return sqlLine.isEmpty() || sqlLine.startsWith("--");
	}

	private boolean parseSqlLine(String sqlLine, StringBuilder statementBuilder) {
		String sqlLineWithoutComment = removeEndingCommentFromSqlLine(sqlLine);
		int statementEndingCharIdx = indexOfStatementEndingChar(sqlLineWithoutComment);
		if (statementEndingCharIdx != -1) {
			statementBuilder.append(sqlLineWithoutComment, 0, statementEndingCharIdx + 1).append(System.lineSeparator());
			setRemainingContentAfterLastStatementEndingChar(sqlLineWithoutComment, statementEndingCharIdx);
			return true;
		} else {
			statementBuilder.append(sqlLineWithoutComment).append(System.lineSeparator());
			return false;
		}
	}

	private void setRemainingContentAfterLastStatementEndingChar(String sqlLine, int statementEndingCharIdx) {
		remainingContentAfterLastStatementEndingChar = sqlLine.substring(statementEndingCharIdx + 1);
	}

	private static String removeEndingCommentFromSqlLine(String sqlLine) {
		int inlineCommentIdx = sqlLine.indexOf("--");
		if (inlineCommentIdx != -1) {
			return sqlLine.substring(0, inlineCommentIdx);
		} else {
			return sqlLine;
		}
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

}