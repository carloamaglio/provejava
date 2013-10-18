/*
 * Created on 19-set-2005
 *
 */
package _carlo_nuovi.archivio;

import java.util.Observable;

import _carlo_nuovi.DBResult;
import _carlo_nuovi.DataBase;

/**
 * @author Dott. Ing. Carlo Amaglio - BMB S.p.A.
 *
 */
public class Archivio extends Observable {

	DataBase db;

	public Archivio(DataBase db) {
		this.db = db;
	}

	void notifyNow() {
		setChanged();
		notifyObservers(this);
	}

	public DBResult executeSql(String sql) {
		DBResult rv = db.executeSql(sql);
		notifyNow();
		return rv;
	}

	/**
	 * Cancella tutti gli eventi presenti nel database
	 *
	 */
	public void delete(String tbl) {
		db.tableDelete(tbl);
		notifyNow();
	}

	public static void main(String[] args) throws Exception {
		DataBase db;
		DBResult r;
		Archivio a;

		System.out.println("Apro il database...");
		try {
			db = new DataBase("db/test.sqlite");
		} catch (Exception e) {
			System.out.println("Errore apertura database");
			throw e;
		}
		System.out.println("ok, database aperto.");

//		db.setDebug(1);		// abilito le stampe di debug

		a = new Archivio(db);

		a.delete("archivio");
		for (int i=0; i<100; i++) {
			a.executeSql("INSERT INTO archivio ");
		}

		System.out.println(a.toString());
	}
}
