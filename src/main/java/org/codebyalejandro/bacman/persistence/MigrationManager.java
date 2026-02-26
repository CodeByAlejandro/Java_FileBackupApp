package org.codebyalejandro.bacman.persistence;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MigrationManager {
	private static final ClassPathResource MIGRATION_INDEX = new ClassPathResource("migration_index.txt");

	private final Database database;
	private final ClassPathResource migrationsResourcePath;

	public MigrationManager(Database database, String migrationsResourcePath) {
		this.database = database;
		this.migrationsResourcePath = new ClassPathResource(migrationsResourcePath);
	}

	public void migrate() {
		try {
			var allMigrations = listMigrationResources();
			var migrationsToApply = new ArrayList<>(allMigrations);

			if (Files.isRegularFile(database.getDatabasePath())) {
				var lastAppliedMigrationResourceOpt = getLastAppliedMigrationResource();
				lastAppliedMigrationResourceOpt.ifPresent(lastAppliedMigrationResource ->
						migrationsToApply.removeIf(migrationResource ->
								migrationResource.compareTo(lastAppliedMigrationResource) <= 0));
			}

			for (var migrationResource : migrationsToApply) {
				System.out.println("Applying migration file: " + migrationResource);
				database.runStatementsFromSqlResource(migrationResource.toString());
			}
		} catch (IOException | SQLException e) {
			System.err.println("Failed to apply migrations");
			e.printStackTrace();
		}
	}

	private List<ClassPathResource> listMigrationResources() throws IOException {
		var migrationIndexResourcePath = migrationsResourcePath.resolve(MIGRATION_INDEX);
		InputStream in = getClass().getClassLoader().getResourceAsStream(migrationIndexResourcePath.toString());
		if (in == null) {
			throw new IllegalStateException("Migration index not found: " + migrationIndexResourcePath);
		}
		try (var reader = new BufferedReader(new InputStreamReader(in))) {
			var migrationFiles = new ArrayList<ClassPathResource>();
			for (String line; (line = reader.readLine()) != null; ) {
				line = line.trim();
				if (!line.isEmpty() && !line.startsWith("#")) {
					migrationFiles.add(new ClassPathResource(line));
				}
			}
			return migrationFiles;
		}
	}

	private Optional<ClassPathResource> getLastAppliedMigrationResource() throws SQLException {
		return database.runQuery(
				"SELECT migration_file FROM db_migrations ORDER BY applied_at DESC, migration_file DESC LIMIT 1",
				rs -> rs.next()
						? Optional.of(new ClassPathResource(rs.getString("migration_file")))
						: Optional.empty()
		);
	}
}
