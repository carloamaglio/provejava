package jOpenDoc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class TextOutputFile extends PrintStream {
	FileOutputStream fos;

	TextOutputFile(FileOutputStream fos) {
		super(fos, true);
		this.fos = fos;
	}

	public static TextOutputFile create(String filename) {
		TextOutputFile rv = null;
		FileOutputStream fos;
		try {
			File f = new File(filename);
			boolean append = false;
			fos = new FileOutputStream(f, append);
			rv = new TextOutputFile(fos);
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}
		return rv;
	}

}
