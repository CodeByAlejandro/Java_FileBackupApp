package org.codebyalejandro.bacman.database;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public final class DbInitializer {
	private static final String MIGRATIONS_DIR = "db/migrations";

	private final Database database;

	public DbInitializer(Database database) {
		this.database = database;
	}

	public void initializeDataBase() {
		List<Path> allMigrationFiles = listMigrationFiles();
		List<Path> migrationFilesToApply = new ArrayList<>(allMigrationFiles);

		if (Files.isRegularFile(database.getDatabasePath())) {
			Optional<Path> lastAppliedMigrationFileOpt = getLastAppliedMigrationFile();
			if (lastAppliedMigrationFileOpt.isPresent()) {
				String lastAppliedMigrationFileName = lastAppliedMigrationFileOpt.get().getFileName().toString();
				migrationFilesToApply.removeIf(p -> p.getFileName().toString().compareTo(lastAppliedMigrationFileName) <= 0);
			}
		}

		try {
			for (Path migrationFile : migrationFilesToApply) {
				database.runDdlFile(MIGRATIONS_DIR + "/" + migrationFile.getFileName().toString());
			}
		} catch (SQLException e) {
			System.err.println("Failed to apply migrations");
			e.printStackTrace();
		}
	}

	private static List<Path> listMigrationFiles() {
		URL dirUrl = Database.class.getClassLoader().getResource(MIGRATIONS_DIR);
		if (dirUrl == null) {
			throw new IllegalStateException("Resource directory not found: " + MIGRATIONS_DIR);
		}

		try {
			Path dir = Paths.get(dirUrl.toURI());

			try (Stream<Path> stream = Files.list(dir)) {
				return stream
						.filter(Files::isRegularFile)
						.filter(p -> p.getFileName().toString().endsWith(".sql"))
						.sorted(Comparator.comparing(p -> p.getFileName().toString()))
						.toList();
			}
		} catch (URISyntaxException | IOException e) {
			throw new RuntimeException("Failed to list migration files", e);
		}
	}

	private Optional<Path> getLastAppliedMigrationFile() {
		try {
			return database.runQuery(
					"SELECT migration_file FROM db_migrations ORDER BY applied_at DESC, migration_file DESC LIMIT 1",
					rs -> rs.next() ? Optional.of(Paths.get(rs.getString("migration_file"))) : Optional.empty()
			);
		} catch (SQLException e) {
			System.err.println("Failed to retrieve last applied migration");
			e.printStackTrace();
			return Optional.empty();
		}
	}
}
