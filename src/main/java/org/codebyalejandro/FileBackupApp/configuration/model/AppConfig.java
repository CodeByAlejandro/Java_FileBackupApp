package org.codebyalejandro.FileBackupApp.configuration.model;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AppConfig {
	private List<BackupConfig> backups;
	private Path archiveDir;
	private Path databaseFile;
	private Path databaseBackupFile;

	// ----------- CONSTRUCTOR
	public AppConfig() {
		backups = new ArrayList<>();
		archiveDir = Paths.get("recycle_bin");
		databaseFile = Paths.get("FileBackupApp.db");
		databaseBackupFile = null;
	}

	// ----------- GETTERS & SETTERS
	public List<BackupConfig> getBackups() {
		return backups;
	}

	public void setBackups(List<BackupConfig> backups) {
		this.backups = backups;
	}

	public Path getArchiveDir() {
		return archiveDir;
	}

	public void setArchiveDir(Path archiveDir) {
		this.archiveDir = archiveDir;
	}

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
