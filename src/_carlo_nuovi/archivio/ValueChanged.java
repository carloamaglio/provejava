/*
 * Created on 16-set-2005
 *
 */
package _carlo_nuovi.archivio;

import _carlo_nuovi.DBResult;

/**
 * @author Dott. Ing. Carlo Amaglio - BMB S.p.A.
 *
 */
public class ValueChanged extends MachineEvent {
	private String what;		// tipicamente nome della variabile
	private String oldValue;
	private String newValue;

	public ValueChanged(MachineEventType type, String what, String oldValue, String newValue) {
		super(type);
		this.what = what;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	static String f(String[] r) {
		System.out.println("ValueChanged: " + r[0] + ", " + r[1] + ", " + r[2]);
		return r[1];
	}
	ValueChanged(String[] record) {
		super(ValueChanged.f(record), record[2], record[4]);
		this.what = record[3];
		this.oldValue = record[5];
		this.newValue = record[6];
	}

	public final String what() {
		return what;
	}

	public final String oldValue() {
		return oldValue;
	}

	public final String newValue() {
		return newValue;
	}
	
	public static ValueChanged[] convert(DBResult dbr) {
		int i;
		ValueChanged[] rv = new ValueChanged[dbr.size()];
		i = 0;
		for (String[] r : dbr) {
			ValueChanged v = new ValueChanged(r);
			rv[i++] = v;
		}
		return rv;
	}
}
