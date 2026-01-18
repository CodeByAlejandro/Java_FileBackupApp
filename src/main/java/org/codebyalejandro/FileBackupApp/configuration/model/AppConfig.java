package org.codebyalejandro.FileBackupApp.configuration.model;

import java.nio.file.Path;
import java.nio.file.Paths;

public class AppConfig {
	private Path databaseFile;
	private Path databaseBackupFile;

	// ----------- CONSTRUCTOR
	public AppConfig() {
		databaseFile = Paths.get("FileBackupApp.db");
		databaseBackupFile = null;
	}

	// ----------- GETTERS & SETTERS
	public Path getDatabaseFile() {
		return databaseFile;
	}

	public void setDatabaseFile(Path databaseFile) {
		this.databaseFile = databaseFile;
	}

	public Path getDatabaseBackupFile() {
		return databaseBackupFile;
	}

	public void setDatabaseBackupFile(Path databaseBackupFile) {
		this.databaseBackupFile = databaseBackupFile;
	}

}
