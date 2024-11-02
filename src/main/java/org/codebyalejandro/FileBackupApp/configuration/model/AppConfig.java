package org.codebyalejandro.FileBackupApp.configuration.model;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AppConfig {
	private Path archiveDir;
	private Path checksumFile;
	private Path lockFile;
	private List<BackupConfig> backups;

	// ----------- CONSTRUCTOR
	public AppConfig() {
		setArchiveDir(Paths.get("recycle_bin"));
		setChecksumFile(Paths.get("checksums.yaml"));
		setLockFile(Paths.get("lock.yaml"));
		setBackups(new ArrayList<>());
	}

	// ----------- GETTERS & SETTERS
	public Path getArchiveDir() {
		return archiveDir;
	}

	public void setArchiveDir(Path archiveDir) {
		this.archiveDir = archiveDir;
	}

	public Path getChecksumFile() {
		return checksumFile;
	}

	public void setChecksumFile(Path checksumFile) {
		this.checksumFile = checksumFile;
	}

	public Path getLockFile() {
		return lockFile;
	}

	public void setLockFile(Path lockFile) {
		this.lockFile = lockFile;
	}

	public List<BackupConfig> getBackups() {
		return backups;
	}

	public void setBackups(List<BackupConfig> backups) {
		this.backups = backups;
	}
}
