package org.codebyalejandro.bacman.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.codebyalejandro.bacman.database.Database;
import org.codebyalejandro.bacman.database.DbInitializer;

import java.io.IOException;

public class BacManApplication extends Application {

	@Override
	public void start(Stage primaryStage) throws IOException {
		Database database = new Database();
		DbInitializer dbInitializer = new DbInitializer(database);
		dbInitializer.initializeDataBase();

		FXMLLoader fxmlLoader = new FXMLLoader(BacManApplication.class.getResource("/view/main-view.fxml"));
		Scene scene = new Scene(fxmlLoader.load(), 320, 240);
		primaryStage.setTitle("Hello!");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
