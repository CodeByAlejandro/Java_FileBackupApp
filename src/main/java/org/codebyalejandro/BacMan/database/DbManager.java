package org.codebyalejandro.BacMan.database;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class DbManager {
	private static final Path DATABASE_PATH = Paths.get("BacMan.db");
	private static final String MIGRATIONS_DIR = "db/migrations";

	private final DataSource dataSource;

	public DbManager() {
		this(SQLiteDataSourceFactory.create(DATABASE_PATH));
	}

	public DbManager(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void initializeDataBase() {
		List<Path> allMigrationFiles = listMigrationFiles();
		List<Path> migrationFilesToApply = new ArrayList<>(allMigrationFiles);

		if (Files.isRegularFile(DATABASE_PATH)) {
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

	private Optional<Path> getLastAppliedMigrationFile() {
		try (Connection conn = dataSource.getConnection();
			 Statement stmt = conn.createStatement()) {

			ResultSet rs = stmt.executeQuery(
					"SELECT migration_file FROM db_migrations ORDER BY applied_at DESC, migration_file DESC LIMIT 1"
			);
			if (rs.next()) {
				return Optional.of(Paths.get(rs.getString("migration_file")));
			} else {
				return Optional.empty();
			}

		} catch (SQLException e) {
			System.err.println("Failed to retrieve last applied migration");
			e.printStackTrace();
			return Optional.empty();
		}
	}

	private void runDdlFile(String ddlFilePath) {
		try (Connection conn = dataSource.getConnection();
			 SqlFile sqlFile = new SqlFile(ddlFilePath)) {

			Transactional.inTransaction(conn, c -> {
				for (String sqlStatement : sqlFile) {
					try (Statement stmt = c.createStatement()) {
						stmt.execute(sqlStatement);
					}
				}

				try (Statement stmt = c.createStatement()) {
					stmt.executeUpdate(
							"INSERT INTO db_migrations (migration_file) VALUES ('" + ddlFilePath + "')"
					);
				}
			});

		} catch (Exception e) {
			System.err.println("Failed to execute DDL file: " + ddlFilePath);
			e.printStackTrace();
		}
	}

	private static List<Path> listMigrationFiles() {
		URL dirUrl = DbManager.class.getClassLoader().getResource(MIGRATIONS_DIR);
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

}