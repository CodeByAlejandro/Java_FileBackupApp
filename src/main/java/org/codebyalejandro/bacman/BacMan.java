package org.codebyalejandro.bacman;

import org.codebyalejandro.bacman.database.DbManager;

public class BacMan {
	public static void main(String[] args) {
		System.out.println("Hello World!");

		new DbManager().initializeDataBase();
	}
}
