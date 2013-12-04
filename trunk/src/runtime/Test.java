package runtime;

public class Test {

	public static void shellExecute(String command, boolean wait) {
		try {
			Process p = Runtime.getRuntime().exec(command);
			if (wait) p.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Non e' stato possibile eseguire il comando: '" + command + "'");
		}
	}

	public static void main(String[] args) {
		shellExecute("cmd /c c:\\Programmi\\ToolSelca\\sp6000.vbs 13", false);
	}

}
