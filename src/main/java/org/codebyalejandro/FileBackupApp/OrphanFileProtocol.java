package org.codebyalejandro.FileBackupApp;

public enum OrphanFileProtocol {
	RETAIN,      // Keep orphaned destination files
	DELETE,      // Remove orphaned destination files
	ARCHIVE,     // Move orphaned destination files to a archiveDir
}
