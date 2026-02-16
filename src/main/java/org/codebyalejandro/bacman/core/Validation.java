package org.codebyalejandro.bacman.core;

public final class Validation {
	private Validation() {
	}

	public static <T> T requireNonNull(T obj, String name) {
		if (obj == null) {
			throw new IllegalArgumentException(name + " is null");
		}
		return obj;
	}
}
