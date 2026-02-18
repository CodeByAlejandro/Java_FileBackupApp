package org.codebyalejandro.bacman.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.codebyalejandro.bacman.persistence.Database;

import java.io.IOException;

public class BacManApplication extends Application {

	private static final String LIQUIBASE_CHANGELOG = "db/changelog/db.changelog-master.xml";

	@Override
	public void start(Stage primaryStage) throws IOException {
		Database database = new Database("BacMan.db");
		runMigrations(database);

		FXMLLoader fxmlLoader = new FXMLLoader(BacManApplication.class.getResource("/view/main-view.fxml"));
		Scene scene = new Scene(fxmlLoader.load(), 320, 240);
		primaryStage.setTitle("Hello!");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private static void runMigrations(Database database) {
		try (var conn = database.getDataSource().getConnection()) {
			// When running as a named module, resources are associated with the module's classloader,
			// not necessarily the application/system classloader.
			ClassLoader moduleClassLoader = BacManApplication.class.getModule().getClassLoader();
			if (moduleClassLoader == null) {
				moduleClassLoader = BacManApplication.class.getClassLoader();
			}

			var liquibaseDatabase = DatabaseFactory.getInstance()
					.findCorrectDatabaseImplementation(new JdbcConnection(conn));
			Liquibase liquibase = new Liquibase(
					LIQUIBASE_CHANGELOG,
					new ClassLoaderResourceAccessor(moduleClassLoader),
					liquibaseDatabase);
			liquibase.update((String) null);
		} catch (Exception e) {
			throw new RuntimeException("Failed to run database migrations with Liquibase", e);
		}
	}
}
