/*
 * Created on 15-set-2005
 *
 */
package _carlo_nuovi;

import java.util.Vector;

import tools.Numbers;
import SQLite.FunctionContext;
import SQLite.Vm;

/**
 * @author Dott. Ing. Carlo Amaglio - BMB S.p.A.
 *
 */
public class DataBase extends SQLite.Database {
	private String fileName;
//	private SQLite.Database _db = new SQLite.Database();
	private Object _obj = new Object();
	private int debugLvl = 0;
		
	static {
		try {
			System.loadLibrary("sqlite_jni");
		} catch (UnsatisfiedLinkError ule) {
			System.err.println(ule);
		}
	}

	public DataBase(String filename) throws Exception {
		super();
		fileName = filename;
		int mode = 0666;
		super.open(fileName, mode);
	}

	public void setDebug(int lvl) {
		debugLvl = lvl;
		if (debugLvl!=0)
			System.out.println("DataBase '" + fileName + "': debug is ON");
	}

	class SQLiteCallbacks implements SQLite.Callback, SQLite.Function,
					SQLite.Authorizer, SQLite.Trace, SQLite.ProgressHandler {

		private StringBuffer acc = new StringBuffer();
		public DBResult result;

		public SQLiteCallbacks(String sql) {
			result = new DBResult(sql);
		}

		// SQLite.Callback interface
		public void columns(String col[]) {
		}
		public void types(String types[]) {
		}
		public boolean newrow(String data[]) {
			result.add(data);
			return false;
		}

		public void function(FunctionContext fc, String args[]) {
			for (int i=0; i<args.length; i++) {
				System.out.println("arg[" + i + "]=" + args[i]);
			}
			if (args.length > 0) {
				fc.set_result(args[0].toLowerCase());
			}
		}

		public void step(FunctionContext fc, String args[]) {
			System.out.println("step:");
			for (int i = 0; i < args.length; i++) {
				acc.append(args[i]);
				acc.append(" ");
			}
		}
	
		public void last_step(FunctionContext fc) {
			System.out.println("last_step");
			fc.set_result(acc.toString());
			acc.setLength(0);
		}
	
		public int authorize(int what, String arg1, String arg2, String arg3,
							 String arg4) {
			return SQLite.Constants.SQLITE_OK;
		}
	
		public void trace(String stmt) {
			System.out.println("TRACE: " + stmt);
		}
	
		public boolean progress() {
			System.out.println("PROGRESS");
			return true;
		}
	}

	public Vm compile(String sql) {
		try {
			synchronized (_obj) {
				return super.compile(sql);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}	
	}

	public DBResult executeSql(String sql) {
		SQLiteCallbacks cb = new SQLiteCallbacks(sql);
		cb.result.status = DBResult.States.EXECUTING;
		try {
			super.exec(sql, cb);
			cb.result.status = DBResult.States.EXECUTED;
		} catch (SQLite.Exception e) {
			if (debugLvl!=0) e.printStackTrace(System.out);
			cb.result.status = DBResult.States.ERROR;
		}
		if (debugLvl!=0) cb.result.debug();
		return cb.result;
	}
	
	public synchronized void clearTable(String table) {
		if (table == null) return;
		executeSql("DELETE FROM " + table);
	}

	public synchronized void update(String table, String fields[], String[] values, String where) throws Exception {
		String SQL = "UPDATE " + table + " SET ";
		for (int i=0; i<fields.length; i++) {
			SQL += fields[i] + " = " + values[i];
			if (i < fields.length-1) SQL+=", ";
		}
		SQL += " WHERE id=" + where;
		executeSql(SQL);
	}

	/**
	 * Restituisce il numero di record presenti nella tabella specificata
	 * @param table tabella di cui si desidera conoscere il numero di records
	 * @return numero di record presenti in table
	 */
	public long numOfRecords(String table) {
		String SQL = "SELECT COUNT(*) from " + table;
		Vector<String[]> r = executeSql(SQL);
		String[] snum = r.firstElement();
		return Numbers.parseInteger(snum[0]);
	}
	
	public synchronized boolean insertFields(String table, String[] fields, String[] values) {
		String SQL = "INSERT INTO " + table + " (";
		for (int i=0; i<fields.length; i++) {
			SQL += fields[i];
			if (i < fields.length-1) SQL+=", ";
		}
		SQL += ") VALUES ((SELECT COUNT(*) from " + table + ")+1, ";
		for (int i=0; i<values.length; i++) {
			SQL += "\"" + values[i] + "\"";
			if (i < values.length-1) SQL+=", ";
		}
		SQL += ")";

		SQLiteCallbacks cb = new SQLiteCallbacks(SQL);
		super.set_authorizer(cb);

		//Debug.println(SQL);
		try {
			Vm vm = compile(SQL);
			synchronized (_obj) {	
				int stmt = 0;
				do {
					++stmt;
					if (stmt > 3) {
						System.out.println("setting progress handler");
						super.progress_handler(3, cb);
					}
					//Debug.println("---- STMT #" + stmt + " ----");
					while (vm.step(cb)) {
					}
				} while (vm.compile());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean tableExist(String table) {
		DBResult r = executeSql("SELECT * FROM sqlite_master WHERE type='table' and tbl_name='" + table + "'");
		return r.size()>0;
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
	
	public static void main(String[] args) throws Exception {
		DataBase db;
		DBResult r;

		System.out.println("Apro il database...");
		try {
			db = new DataBase("db/test.sqlite");
		} catch (Exception e) {
			System.out.println("Errore apertura database");
			throw e;
		}
		System.out.println("ok, database aperto.");

		db.setDebug(1);

		if (db.tableExist("Test")) {
			db.tableDrop("Test");
			System.out.println("Cancello l'indice...");
			db.executeSql("DROP INDEX idxTestByDate");
		}

		System.out.println("Creo la tabella 'Test'...");
		db.executeSql(
			"CREATE TABLE Test (" + 
				"Id INTEGER PRIMARY KEY, " + 
				"Date INTEGER, " +
				"Desc TEXT" + 
			")"
		);

		System.out.println("Creo l'indice sul campo Date...");
		db.executeSql("CREATE INDEX idxTestByDate on Test (Date)");
		
		System.out.println("Inserisco i valori nella tabella 'Test'...");
		for (int i=0; i<10; i++)
			db.executeSql("INSERT INTO Test VALUES (NULL, " + Math.random() + ", 'CIAO" + i + "')");
		System.out.println("ok");

		System.out.println("Numero di records=" + db.numOfRecords("Test"));

		System.out.println("SENZA ORDINAMENTO");
		r = db.executeSql("SELECT * FROM Test");

		System.out.println("CON ORDINAMENTO SU Date");
		r = db.executeSql("SELECT * FROM Test ORDER BY Date");

		System.out.println("CON ORDINAMENTO SU Desc");
		r = db.executeSql("SELECT * FROM Test ORDER BY Desc");

		r = db.executeSql("SELECT * FROM sqlite_master WHERE type='table' ORDER BY name");
		r = db.executeSql("SELECT * FROM sqlite_master ORDER BY name");
	}
}
