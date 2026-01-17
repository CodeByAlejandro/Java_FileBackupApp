package org.codebyalejandro.FileBackupApp.configuration.model;

import org.codebyalejandro.FileBackupApp.configuration.model.backup.BackupModeConfig;
import org.codebyalejandro.FileBackupApp.configuration.model.restore.RestoreModeConfig;
import org.codebyalejandro.FileBackupApp.configuration.model.validation.ValidationModeConfig;

import java.nio.file.Path;

public class BackupConfig {
	private Path sourcePath;
	private Path destinationPath;
	private BackupModeConfig backupMode;
	private RestoreModeConfig restoreMode;
	private ValidationModeConfig validationMode;

	// ----------- GETTERS & SETTERS
	public Path getSourcePath() {
		return sourcePath;
	}

	public void setSourcePath(Path sourcePath) {
		this.sourcePath = sourcePath;
	}

	public Path getDestinationPath() {
		return destinationPath;
	}

	public void setDestinationPath(Path destinationPath) {
		this.destinationPath = destinationPath;
	}

	public BackupModeConfig getBackupMode() {
		return backupMode;
	}

	public void setBackupMode(BackupModeConfig backupMode) {
		this.backupMode = backupMode;
	}

	public RestoreModeConfig getRestoreMode() {
		return restoreMode;
	}

	public void setRestoreMode(RestoreModeConfig restoreMode) {
		this.restoreMode = restoreMode;
	}

	public ValidationModeConfig getValidationMode() {
		return validationMode;
	}

	public void setValidationMode(ValidationModeConfig validationMode) {
		this.validationMode = validationMode;
	}

}
