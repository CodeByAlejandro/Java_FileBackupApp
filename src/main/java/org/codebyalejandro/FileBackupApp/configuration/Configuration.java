package org.codebyalejandro.FileBackupApp.configuration;

import org.codebyalejandro.FileBackupApp.configuration.model.AppConfig;

public class Configuration {
	public static AppConfig loadConfiguration() { return new AppConfig(); }
	public static void validateConfiguration(AppConfig config) {}
	public static void saveConfiguration(AppConfig config) {}
}
