package org.codebyalejandro.FileBackupApp.configuration.model.validation;

public class ValidationModeConfig {
	private boolean active;

	// ----------- CONSTRUCTOR
	public ValidationModeConfig() {
		active = true;
	}

	// ----------- GETTERS & SETTERS
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
