package language.collections;

import java.util.HashSet;

public class C {

	public static void main(String[] args) {
		HashSet<String> s = new HashSet<String>();
		String k = "Carlo";
		s.add("Carlo");
		System.out.println("" + s.contains(k));
		System.out.println("" + s.contains("Carlo"));
		System.out.println("" + s.contains("carlo"));
		System.out.println("" + s.contains("Ciao"));
	}
}
