package _carlo;

public class Varargs {

	void f1(String[] s) {
		System.err.println("f1::s.len=" + s.length + ", s=[" + s + "]");
		f2(s);
	}

	void f2(String[] s) {
		System.err.println("f2::s.len=" + s.length + ", s=[" + s + "]");
	}

	void f3(String...s) {
		System.err.println("f3::s.len=" + s.length + ", s=[" + s + "]");
		for (String e : s) {
			System.err.println("\tf3::s[i]='" + e + "'");
		}
	}

	public static void main(String[] args) {
		Varargs v = new Varargs();
		v.f1(new String[] { "ciao", "io", "sono", "Carlo" });
	}
}
