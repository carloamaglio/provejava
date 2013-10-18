package language.collections;

import java.util.LinkedList;

public class LL {

	public static void dump(LinkedList<String> l) {
		for (String s : l) {
			System.out.println(s);
		}
		System.out.println("");
	}

	public static void main(String[] args) {
		LinkedList<String> l = new LinkedList<String>();
		l.add("ciao");
		l.add("carlo");
		l.add("bello");
		dump(l);
		l.remove();
		dump(l);
		l.removeLast();
		dump(l);
	}
}
