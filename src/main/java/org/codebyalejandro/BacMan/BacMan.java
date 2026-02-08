package org.codebyalejandro.BacMan;

import org.codebyalejandro.BacMan.database.DbManager;

public class BacMan {
	public static void main(String[] args) {
		System.out.println("Hello World!");

		new DbManager().initializeDataBase();
	}
}
