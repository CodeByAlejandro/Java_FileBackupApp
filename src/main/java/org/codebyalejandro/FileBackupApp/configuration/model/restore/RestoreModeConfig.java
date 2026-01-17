package org.codebyalejandro.FileBackupApp.configuration.model.restore;

public class RestoreModeConfig {
	private boolean active;

	// ----------- CONSTRUCTOR
	public RestoreModeConfig() {
		active = false;
	}

	// ----------- GETTERS & SETTERS
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
