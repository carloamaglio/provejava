package database;

public class Test {

	public static void main(String[] args) throws Exception {
		DbaInterface db;
		DBResult r;

		System.out.println("Apro il database...");
		try {
			db = new H2("db/test_h2");
//			db = new Derby("db/test.sqlite");
		} catch (Exception e) {
			System.out.println("Errore apertura database");
			throw e;
		}
		System.out.println("ok, database aperto.");

		db.setDebug(1);

		if (db.tableExist("Test")) {
			System.out.println("Cancello l'indice...");
			db.executeSql("DROP INDEX idxTestByDate");
			db.tableDrop("Test");
		}

		if (!db.tableExist("Test")) {
			System.out.println("Creo la tabella 'Test'...");
			db.executeSql(
				"CREATE TABLE Test (" + 
//				"Id INTEGER generated always as identity PRIMARY KEY, " + 
				"Id INTEGER AUTO_INCREMENT PRIMARY KEY, " + 
					"Date INTEGER, " +
					"Descr VARCHAR(32)" + 
				")"
			);
		}

		System.out.println("Creo l'indice sul campo Date...");
		db.executeSql("CREATE INDEX idxTestByDate on Test (Date)");
		
		System.out.println("Inserisco i valori nella tabella 'Test'...");
		for (int i=0; i<10; i++)
			db.executeSql("INSERT INTO Test VALUES (DEFAULT, " + 100.0*Math.random() + ", 'CIAO" + (100-i) + "')");
		System.out.println("ok");

		System.out.println("Numero di records=" + db.numOfRecords("Test"));

		System.out.println("SENZA ORDINAMENTO");
		r = db.executeSql("SELECT * FROM Test");

		System.out.println("CON ORDINAMENTO SU Date");
		r = db.executeSql("SELECT * FROM Test ORDER BY Date");

		System.out.println("CON ORDINAMENTO SU Descr");
		r = db.executeSql("SELECT * FROM Test ORDER BY Descr");

//		r = db.executeSql("SELECT * FROM sqlite_master WHERE type='table' ORDER BY name");
//		r = db.executeSql("SELECT * FROM sqlite_master ORDER BY name");
		System.out.println("r=" + r);

		System.out.println("insert...");
		db.setDebug(0);
		for (int i=0; i<10000; i++)
			db.executeSql("INSERT INTO Test VALUES (DEFAULT, " + 100.0*Math.random() + ", 'CIAO" + i + "')");
		System.out.println("insert...end");
	}

}
