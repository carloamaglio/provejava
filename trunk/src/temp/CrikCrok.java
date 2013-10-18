package temp;

import java.io.File;

public class CrikCrok {

	static void foo() {
		String padre = new File(System.getProperty("user.dir")).getParent();
		File myDir = new File(padre, File.separator + "IMMAGINI");
//		File myDir = new File(padre);
		File[] files = myDir.listFiles();
		String[] cat;
		int h, i;

		if (files!=null && files.length>0) {
			cat = new String[files.length];
			for (i=h=0; i<files.length; i++) {
				if (files[i].isDirectory()) {
					cat[h] = files[i].getName();
					System.out.println(h + ": " + cat[h]);
					h++;
				}
			}
		}
	}

	static String formatted(double v) {
		return String.format("%.2f", v);		
	}


	public static void main(String[] args) {
		String f = formatted(123456.3456);
		System.out.println("formatted(): '" + f + "'");
	}
}
