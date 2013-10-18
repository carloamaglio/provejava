/*
 * Created on 19-apr-2006
 *
 * TODO Tutto
 */
package _carlo;

import tools.Numbers;

public class ProvaRegularExpression {

	static int index(String s) {
		int rv = 0;
		if (s.matches(".+\\(\\d+\\)")) {
			System.out.print("Tag::index() '" + s + "' matches: ");
			String n = s.substring(s.indexOf('(')+1, s.length()-1);
			System.out.print("n='" + n + "', ");
			rv = Numbers.parseInteger(n);
			System.out.println("rv=" + rv);
		} else {
			System.out.println("Tag::index() '" + s + "' does'nt matches: ");
		}
		return rv;
	}

	public static void main(String[] args) {
        System.out.println(Character.isWhitespace('_'));
		
//		index("abcd(1)");
//		index("abcd(1");
//		index("abcd1)");
//		index("abcd(123)");
//		index("abcd(0)");
//		index("abcd(4)");
//		index("abcd(-1)");
//		index("12");
	}
}
