package temp;

import java.util.Map;
import java.util.Properties;

public class SystemProperties {

	static void foo() {
		Map<String, String> env = System.getenv();
		for (String k : env.keySet()) {
			System.out.println("key='" + k + "', value='" + env.get(k) + "'");
		}

		System.out.println("\n\n\n==============================================");
		Properties p = System.getProperties();
		for (Object ko : p.keySet()) {
			String k = (String)ko;
			System.out.println("key='" + k + "', value='" + env.get(k) + "'");
		}
	}

	public static void main(String[] args) {
		foo();
	}
}
