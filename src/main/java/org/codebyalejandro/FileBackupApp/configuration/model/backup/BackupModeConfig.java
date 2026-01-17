package org.codebyalejandro.FileBackupApp.configuration.model.backup;

public class BackupModeConfig {
	private boolean active;

	// ----------- CONSTRUCTOR
	public BackupModeConfig() {
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
