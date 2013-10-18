package temp;

import java.text.NumberFormat;
import java.util.Locale;

public class Printf {

	public static String round(String value, int ndigits) {
		NumberFormat formatter = NumberFormat.getNumberInstance(Locale.ENGLISH);
		formatter.setMaximumFractionDigits(2);
		Double numero = new Double(value);
		String s = formatter.format(numero);
		System.out.println("s='" + s + "'");
		numero = Double.parseDouble(s);
		System.out.println("numero='" + numero + "'");
		return s;
	}

	public static void main(String[] args) {
		String s = Character.toString((char)('A'+11-10));
		System.out.println(s);

//		double v = 59.786;
//		
//		System.out.println(String.format(Locale.ENGLISH, "%.2f", v));
//		String s = String.valueOf((char)('A' + 8));
//		System.out.println(s);
//
//		round("123.12589", 2);
//		Rounder r = new Rounder();
//		r.round("123.12589", 2);
	}

}

class Rounder {
	NumberFormat formatter;

	public Rounder() {
		formatter = NumberFormat.getNumberInstance(Locale.ENGLISH);
	}

	public String round(String value, int ndigits) {
		formatter.setMaximumFractionDigits(2);
		double d = 125.12589;
		String s = formatter.format(d);
		System.out.println("s='" + s + "'");
		Double numero = new Double(value);
		numero = Double.parseDouble(s);
		System.out.println("numero='" + numero + "'");
		return s;
	}
}
