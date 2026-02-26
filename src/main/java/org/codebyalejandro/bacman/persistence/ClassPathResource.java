package org.codebyalejandro.bacman.persistence;

import java.util.regex.Pattern;

class ClassPathResource implements Comparable<ClassPathResource> {
	private static final Pattern VALID_PATH_PATTERN = Pattern.compile("/?[a-zA-Z0-9.\\-_]+(?:/[a-zA-Z0-9.\\-_]+)*+/?");

	private final String path;

	ClassPathResource(String path) {
		rejectNullOrBlank(path);
		rejectInvalidPath(path);
		this.path = normalizePath(path);
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || getClass() != other.getClass()) {
			return false;
		}
		ClassPathResource that = (ClassPathResource) other;
		return path.equals(that.path);
	}

	@Override
	public int hashCode() {
		return path.hashCode();
	}

	@Override
	public String toString() {
		return path;
	}

	@Override
	public int compareTo(ClassPathResource other) {
		return path.compareTo(other.path);
	}

	public ClassPathResource resolve(String other) {
		return resolve(new ClassPathResource(other));
	}

	public ClassPathResource resolve(ClassPathResource other) {
		return new ClassPathResource(path + "/" + other.path);
	}

	private void rejectNullOrBlank(String path) {
		if (path == null || path.isBlank()) {
			throw new IllegalArgumentException("Path must not be null or blank");
		}
	}

	private void rejectInvalidPath(String path) {
		if (!VALID_PATH_PATTERN.matcher(path).matches()) {
			throw new IllegalArgumentException("Invalid path: '" + path + "'." +
					" Path must match pattern: '" + VALID_PATH_PATTERN + "'");
		}
	}

	private String normalizePath(String path) {
		return removeTrailingForwardSlash(removeLeadingForwardSlash(path));
	}

	private String removeLeadingForwardSlash(String path) {
		if (path.startsWith("/")) {
			return path.substring(1);
		}
		return path;
	}

	private String removeTrailingForwardSlash(String path) {
		if (path.endsWith("/")) {
			return path.substring(0, path.length() - 1);
		}
		return path;
	}
}
