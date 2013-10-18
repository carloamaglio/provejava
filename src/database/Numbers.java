/*
 * Created on 28-set-2005
 *
 */
package database;

/**
 * @author Dott. Ing. Carlo Amaglio - BMB S.p.A.
 *
 */
public class Numbers {

	public static boolean parseBoolean(String s) {
		try {
			return (parseInteger(s)!=0);
		} catch (Exception e) {
			return false;
		}
	}

	public static double parseDouble(String s) {
		try {
			return Double.parseDouble(s);
		} catch (Exception e) {
			return 0;
		}
	}

	public static int parseInteger(String s) {
		return (int)parseDouble(s);
	}

	public static long parseLong(String s) {
		return (long)parseDouble(s);
	}
}
