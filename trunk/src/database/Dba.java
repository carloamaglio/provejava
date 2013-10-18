package database;

import java.util.Vector;

abstract class Dba implements DbaInterface {

	String fileName;

	int debugLvl = 0;

	public Dba(String fileName) throws Exception {
		this.fileName = fileName;
	}

	public synchronized void clearTable(String table) {
		if (table!=null && table.length()>0) {
			executeSql("DELETE FROM " + table);
		}
	}

	public synchronized void update(String table, String fields[], String[] values, String where) throws Exception {
		String SQL = "UPDATE " + table + " SET ";
		for (int i=0; i<fields.length; i++) {
			SQL += fields[i] + " = " + _q(values[i]);
			if (i < fields.length-1) SQL+=", ";
		}
		SQL += " WHERE " + where;
		executeSql(SQL);
	}

	public DBResult executeInsert(String table, String fields, String... values) {
		String SQL = insertSQL(table, fields, values);
		DBResult dbr = executeSql(SQL);
		return dbr;
	}

	/**
	 * Restituisce il numero di record presenti nella tabella specificata
	 * @param table tabella di cui si desidera conoscere il numero di records
	 * @return numero di record presenti in table
	 */
	public long numOfRecords(String table) {
		String SQL = "SELECT COUNT(*) FROM " + table;
		DBResult r = executeSql(SQL);
		if (r==null || r.isEmpty()) return 0L;
		String[] snum = r.firstElement();
		return Numbers.parseLong(snum[0]);
	}

	/**
	 * Elimina la tabella specificata (e tutti i suoi indici) dal database
	 * @param table
	 * @return false se errore, true se ok.
	 */
	public boolean tableDrop(String table) {
		if (debugLvl!=0) System.out.println("tableDrop(" + table + ")...");
		DBResult r = executeSql("DROP TABLE " + table);
		return r.status!=DBResult.States.EXECUTED;
	}

	/**
	 * Elimina tutti i record della tabella specificata
	 * @param table
	 * @return false se errore, true se ok.
	 */
	public boolean tableDelete(String table) {
		if (debugLvl!=0) System.out.println("tableDelete(" + table + ")...");
		DBResult r = executeSql("DELETE FROM " + table);
		return r.status!=DBResult.States.EXECUTED;
	}
	
	public Vector<String[]> getAllRecords(String table) {
		String SQL = "SELECT * FROM " + table;
		DBResult dbr = executeSql(SQL);

		if (dbr.status == DBResult.States.ERROR)
			Console.printWithTrace("ERRORE: " + dbr.status + ": " + SQL);

		return dbr;
	}



	public void setDebug(int lvl) {
		debugLvl = lvl;
		if (debugLvl!=0) {
			System.out.println("DataBase '" + fileName + "': debug is ON");
		}
	}

	/**
	 * _q - mette le virgolette prima e dopo la stringa passata
	 * Da usare per comporre i comandi SQL
	 * @param str
	 * @return
	 */
	public static String _q(String str) {
		String rv;
		if (str==null) str="";
		String qch = (str.indexOf("'")>=0) ? "\"" : "'";
		rv = qch + str + qch;
		return rv;
	}

	public static String insertSQL(String table, String fields, String... values) {
		int i;
		String SQL = "INSERT INTO " + table + " (" + fields + ") VALUES (";
		for (i=0; i<values.length-1; i++)
			SQL = SQL + _q(values[i]) + ", ";
		SQL = SQL + _q(values[i]) + ")";
		return SQL;
	}


}
