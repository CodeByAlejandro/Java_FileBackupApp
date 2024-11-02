package org.codebyalejandro.FileBackupApp.configuration;

import org.codebyalejandro.FileBackupApp.configuration.model.AppConfig;

public class Configuration {
	public AppConfig loadConfiguration() { return new AppConfig(); }
	public void validateConfiguration(AppConfig config) {}
	public void saveConfiguration(AppConfig config) {}
}
