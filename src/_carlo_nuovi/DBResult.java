/*
 * Created on 16-set-2005
 *
 */
package _carlo_nuovi;

import java.text.DecimalFormat;
import java.util.Vector;

/**
 * @author Dott. Ing. Carlo Amaglio - BMB S.p.A.
 *
 */
public class DBResult extends Vector<String[]> {
	private static final long serialVersionUID = 3257003254909908537L;
	private String sql;
	public enum States { IDLE, EXECUTING, EXECUTED, ERROR }
	public States status = States.IDLE;

	DBResult(String sql) {
		super();
		this.sql = sql;
	}
	
	public String toString() {
		final DecimalFormat f = new DecimalFormat("00000");
		StringBuffer s = new StringBuffer(1000);
		
		s.append("QUERY: '" + sql + "'\nRESULT: status=" + status + ", size=" + size());
		if (size()!=0) {
			s.append(", strings=");

			int i = 1;
			for (String[] record : this) {
				s.append("\n\t" + f.format(i++) + " ");
				for (String v : record) {
					s.append(v + "; ");
				}
			}
		}
		s.append("\n------------------------\n\n");
		return s.toString();
	}
	
	void debug() {
		System.out.println(this);
	}
}

