package org.codebyalejandro.FileBackupApp.configuration.model.mode;

import org.codebyalejandro.FileBackupApp.CopyStrategy;

public class RestoreModeConfig {
	private boolean active;
	private CopyStrategy copyStrategy;

	// ----------- CONSTRUCTOR
	public RestoreModeConfig() {
		setActive(false);
		setCopyStrategy(CopyStrategy.ALL_FILES);
	}

	// ----------- GETTERS & SETTERS
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public CopyStrategy getCopyStrategy() {
		return copyStrategy;
	}

	public void setCopyStrategy(CopyStrategy copyStrategy) {
		this.copyStrategy = copyStrategy;
	}
}
