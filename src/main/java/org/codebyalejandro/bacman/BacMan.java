package org.codebyalejandro.bacman;

import org.codebyalejandro.bacman.database.Database;
import org.codebyalejandro.bacman.database.DbInitializer;

public class BacMan {
	public static void main(String[] args) {
		System.out.println("Hello World!");

		Database database = new Database();
		DbInitializer dbInitializer = new DbInitializer(database);
		dbInitializer.initializeDataBase();
	}
}
