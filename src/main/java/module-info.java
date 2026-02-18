module org.codebyalejandro.bacman {
	requires java.sql;
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires org.xerial.sqlitejdbc;
	requires liquibase.core;

	opens org.codebyalejandro.bacman.ui to javafx.graphics;
	opens org.codebyalejandro.bacman.ui.controller to javafx.fxml;
	opens db.changelog;
	opens db.migration;
	exports org.codebyalejandro.bacman;
}