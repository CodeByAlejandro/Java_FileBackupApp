package org.codebyalejandro.FileBackupApp.configuration.model.mode;

import org.codebyalejandro.FileBackupApp.CopyStrategy;
import org.codebyalejandro.FileBackupApp.OrphanFileProtocol;

public class BackupModeConfig {
	private boolean active;
	private CopyStrategy copyStrategy;
	private OrphanFileProtocol orphanFileProtocol;
	//TODO backup schedule

	// ----------- CONSTRUCTOR
	public BackupModeConfig() {
		setActive(true);
		setCopyStrategy(CopyStrategy.ALL_FILES);
		setOrphanFileProtocol(OrphanFileProtocol.ARCHIVE);
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

	public OrphanFileProtocol getOrphanFileProtocol() {
		return orphanFileProtocol;
	}

	public void setOrphanFileProtocol(OrphanFileProtocol orphanFileProtocol) {
		this.orphanFileProtocol = orphanFileProtocol;
	}
}
