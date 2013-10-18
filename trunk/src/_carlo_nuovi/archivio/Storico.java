/*
 * Created on 16-set-2005
 *
 */
package _carlo_nuovi.archivio;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Observable;
import java.util.Observer;

import language.Translator;
import tools.Numbers;
import _carlo_nuovi.DBResult;
import _carlo_nuovi.DataBase;
import debug.Console;

/**
 * @author Dott. Ing. Carlo Amaglio - BMB S.p.A.
 *
 * Classe per la gestione dello storico degli eventi.
 */
public class Storico extends Archivio {
	static final String TBLEVENTI = "Eventi";
	static final String INSERT_TBLEVENTI = "INSERT INTO " + TBLEVENTI;

	public Storico(DataBase db) {
		super(db);

		System.out.println("Creo la tabella '" + TBLEVENTI + "'...");
		if (!db.tableExist(TBLEVENTI)) {
			db.executeSql(
					"CREATE TABLE " + TBLEVENTI + " (" + 
						"ID INTEGER PRIMARY KEY" + 
						", TIPO TEXT" +
						", QUANDO TEXT" +
						", COSA TEXT" +
						", CHI TEXT" +
						", VVAL TEXT" +
						", NVAL TEXT" +
					")"
				);

			System.out.println("Creo l'indice sul campo TIPO...");
			db.executeSql("CREATE INDEX idx" + TBLEVENTI + "ByTipo on " + TBLEVENTI + " (TIPO)");

			System.out.println("Creo l'indice sul campo QUANDO...");
			db.executeSql("CREATE INDEX idx" + TBLEVENTI + "ByQuando on " + TBLEVENTI + " (QUANDO)");

			System.out.println("Creo l'indice sul campo COSA...");
			db.executeSql("CREATE INDEX idx" + TBLEVENTI + "ByCosa on " + TBLEVENTI + " (COSA)");
		}
	}

	public void add(MachineEvent event) {
		executeSql(
			INSERT_TBLEVENTI + 
			"(ID, TIPO, QUANDO, COSA, CHI, VVAL, NVAL) VALUES (null" 
			+ ", '" + event.type() + "'" 
			+ ", '" + event.when().getTimeInMillis() + "'" 
			+ ", '" + event.what() + "'" 
			+ ", '" + event.who() + "'" 
			+ ", " + event.oldValue() 
			+ ", " + event.newValue()
			+ ")"
		);
	}

	/**
	 * Restituisce gli ultimi n eventi dall'archivio storico
	 * @param n
	 * @return
	 */
	public ValueChanged[] queryLastDays(int nDays) {
		ValueChanged[] rv;
		Calendar d = new GregorianCalendar(Translator._locale);
		Console.print("Current date is " + d);
		d.add(Calendar.DAY_OF_YEAR, -nDays);
		Console.print("Current date minus " + nDays + " days is " + d);
		DBResult r = db.executeSql(
			"SELECT * FROM " + TBLEVENTI 
			+ " WHERE QUANDO>=" + d.getTimeInMillis() 
			+ " ORDER BY ID");
		rv = ValueChanged.convert(r);
		return rv;
	}


	/**
	 * Cancella tutti gli eventi presenti nel database
	 *
	 */
	public void delete() {
		db.tableDelete(TBLEVENTI);
		notifyNow();
	}

	public String toString() {
		DBResult r = db.executeSql("SELECT * FROM " + TBLEVENTI + " ORDER BY COSA");
		StringBuffer s = new StringBuffer(1000);
		
		if (r.size()!=0) {
			int i = 1;
			for (String[] record : r) {
				s.append(
					"\n" 
					+ "TIPO=" + record[1] 
					+ ", QUANDO=" + new Date(Numbers.parseLong(record[2]))
					+ ", COSA=" + record[3] + " "
				);
			}
		}
		s.append("\n\n");
		return s.toString();
	}

	public static void main(String[] args) throws Exception {
		final String[] vars = { "QUOTAAP", "QUOTACH", "SETPT01" };
		DataBase db;
		DBResult r;
		final Storico storico;

		System.out.println("Apro il database...");
		try {
			db = new DataBase("db/test.sqlite");
		} catch (Exception e) {
			System.out.println("Errore apertura database");
			throw e;
		}
		System.out.println("ok, database aperto.");

//		db.setDebug(1);		// abilito le stampe di debug

		storico = new Storico(db);
		storico.addObserver(new Observer() {
			public void update(Observable o, Object arg) {
				System.err.println("Observer is notified: " + arg.getClass());
			}
		});

		Runnable task = new Runnable() {
			public void run() {
				storico.delete();
				for (int i=0; i<100; i++) {
					storico.add(new TagChanged(vars[(int)(Math.random()*3)], Math.random()*100, Math.random()*100));
				}

				System.out.println(storico.toString());

				System.out.println("queryLastDays()...");
				ValueChanged[] tbl = storico.queryLastDays(5);
				System.out.println(tbl.length + " records.");
				for (ValueChanged v : tbl) {
					System.out.println(v.what() + ", " + v.oldValue() + ", " + v.when());
				}

			}
		};
		
		task.run();
	}
}
