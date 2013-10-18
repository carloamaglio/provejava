/**
 * @author Dott. Ing. Carlo Amaglio - BMB S.p.A.
 *
 */
package database;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;

public class Console {
	private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
	static boolean enabled = true;
	static int cntr;
	static long startDate = new Date().getTime();
	static long lastDate = startDate;

	public static void print(String s) {
		if (enabled) {
			long dd;
			Date date = new Date();
			String m = timeFormat.format(date);
			long d = (dd=date.getTime()) - lastDate;
			lastDate = dd;
			System.out.printf("\t%s %06.0f %05.1f %03d %s\n", m, new Double((dd-startDate)/1000.0), new Double(d/1000.0), new Integer(cntr++), s);
		}
	}
	
	public static void print() {
		print("");
	}

	public static String sprintf(String format, Object... args) {
		StringBuilder buf = new StringBuilder(100);
		Formatter f = new Formatter(buf);
		f.format(format, args);
		return buf.toString();
	}

	public static void printf(String format, Object... args) {
		print(sprintf(format, args));
	}

	public static void setEnabled(boolean enabled) {
		Console.enabled = enabled;
	}

	public static void printWithTrace(String msg) {
		Exception e = new Exception();
		e.printStackTrace();
		System.err.println(msg);
	}
}
