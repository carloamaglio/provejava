package temp;

public class ExceptionTest {

	static void foo() throws Exception {
		try {
			int i = 7;
//			System.out.println("provo a dividere...");
			i /= 0;
//			System.out.println("divisione eseguita!");
		} catch (Exception e) {
			System.out.println("eccezione");
//			e.printStackTrace();
			throw e;
		} finally {
			System.out.println("c8ao");
		}
	}

	public static void main(String[] args) {
		try {
			foo();
			System.out.println("ok.");
		} catch (Exception e) {
			System.out.println("eccezione 2");
		}
	}

}
