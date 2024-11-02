package org.codebyalejandro.FileBackupApp.configuration.model.mode;

public class ValidationModeConfig {
	private boolean active;

	// ----------- CONSTRUCTOR
	public ValidationModeConfig() {
		setActive(false);
	}

	// ----------- GETTERS & SETTERS
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
