package org.codebyalejandro.bacman.persistence;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class MigrationManager {
	private final Database database;
	private final String migrationsResourcePath;

	public MigrationManager(Database database, String migrationsResourcePath) {
		this.database = database;
		this.migrationsResourcePath = migrationsResourcePath;
	}

	public void migrate() {
		var allMigrationFiles = listMigrationFiles();
		var migrationFilesToApply = new ArrayList<>(allMigrationFiles);

		try {
			if (Files.isRegularFile(database.getDatabasePath())) {
				var lastAppliedMigrationFileOpt = getLastAppliedMigrationFile();
				lastAppliedMigrationFileOpt.ifPresent(lastAppliedMigrationFileName ->
						migrationFilesToApply.removeIf(filename ->
								filename.compareTo(lastAppliedMigrationFileName) <= 0));
			}

			for (var migrationFileName : migrationFilesToApply) {
				System.out.println("Applying migration file: " + migrationFileName);
				database.runStatementsFromSqlResource(migrationsResourcePath + "/" + migrationFileName);
			}
		} catch (SQLException e) {
			System.err.println("Failed to apply migrations");
			e.printStackTrace();
		}
	}

	private List<String> listMigrationFiles() {
		URL dirUrl = Database.class.getResource(migrationsResourcePath);
		if (dirUrl == null) {
			throw new IllegalStateException("Resource directory not found: " + migrationsResourcePath);
		}

		try {
			Path dir = Paths.get(dirUrl.toURI());

			try (Stream<Path> stream = Files.list(dir)) {
				return stream
						.filter(Files::isRegularFile)
						.map(path -> path.getFileName().toString())
						.filter(filename -> filename.endsWith(".sql"))
						.sorted()
						.toList();
			}
		} catch (URISyntaxException | IOException e) {
			throw new RuntimeException("Failed to list migration files", e);
		}
	}

	private Optional<String> getLastAppliedMigrationFile() throws SQLException {
		return database.runQuery(
				"SELECT migration_file FROM db_migrations ORDER BY applied_at DESC, migration_file DESC LIMIT 1",
				rs -> rs.next() ? Optional.of(rs.getString("migration_file")) : Optional.empty()
		);
	}
}
