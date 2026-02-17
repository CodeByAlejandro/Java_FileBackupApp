package org.codebyalejandro.bacman.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.codebyalejandro.bacman.persistence.Database;
import org.flywaydb.core.Flyway;

import java.io.IOException;

public class BacManApplication extends Application {

	@Override
	public void start(Stage primaryStage) throws IOException {
		Database database = new Database("BacMan.db");
		Flyway flyway = Flyway.configure()
				.dataSource(database.getDataSource())
				.load();
		flyway.migrate();

		FXMLLoader fxmlLoader = new FXMLLoader(BacManApplication.class.getResource("/view/main-view.fxml"));
		Scene scene = new Scene(fxmlLoader.load(), 320, 240);
		primaryStage.setTitle("Hello!");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
