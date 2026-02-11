package org.codebyalejandro.bacman.database;

import org.codebyalejandro.bacman.database.sql.SqlFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Statement;
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

		for (Path migrationFile : migrationFilesToApply) {
			runDdlFile(MIGRATIONS_DIR + "/" + migrationFile.getFileName().toString());
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
			return database.withConnection(conn -> {
				try (Statement stmt = conn.createStatement()) {
					return stmt.executeQuery(
							"SELECT migration_file FROM db_migrations ORDER BY applied_at DESC, migration_file DESC LIMIT 1"
					);
				}
			}, rs -> {
				if (rs.next()) {
					return Optional.of(Paths.get(rs.getString("migration_file")));
				} else {
					return Optional.empty();
				}
			});
		} catch (SQLException | IOException e) {
			System.err.println("Failed to retrieve last applied migration");
			e.printStackTrace();
			return Optional.empty();
		}
	}

	private void runDdlFile(String ddlFilePath) {
		try {
			database.withConnectionAndTransaction(conn -> {
				try (SqlFile sqlFile = new SqlFile(ddlFilePath)) {
					sqlFile.forEachStatement(sqlStatement -> {
						try (Statement stmt = conn.createStatement()) {
							stmt.execute(sqlStatement);
						}
					});
				}

				try (Statement stmt = conn.createStatement()) {
					stmt.executeUpdate(
							"INSERT INTO db_migrations (migration_file) VALUES ('" + ddlFilePath + "')"
					);
				}
			});
		} catch (SQLException | IOException e) {
			System.err.println("Failed to execute DDL file: " + ddlFilePath);
			e.printStackTrace();
		}
	}
}
