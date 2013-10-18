package dataLogger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileDataLogger extends PrintStream implements DataLogger {

	static final long maxSize = 10000000L;

	FileOutputStream fos;

	FileDataLogger(FileOutputStream fos) {
		super(fos, true);
		this.fos = fos;
	}

	public static FileDataLogger create(String filename) {
		FileDataLogger rv = null;
		FileOutputStream fos;
		try {
			File f = new File(filename);
			boolean append = f.length() < maxSize;
			if (!append) {
				File bak = new File(filename + ".bak");
				if (bak.exists()) {
					bak.delete();
				}
				f.renameTo(bak);
			}
			fos = new FileOutputStream(f, append);
			rv = new FileDataLogger(fos);
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}
		return rv;
	}


	String line = "";

	@Override
	public synchronized void print(String s) {
		if (fos != null && s != null) {
			line += s;
			if (s.endsWith("\n")) {
				String m;

				m = now() + " " + line;
				super.print(m);
				line = "";
			}
		}
	}

	@Override
	public void println(String s) {
		if (s==null) s="[null]";
		if (!s.endsWith("\n")) s += "\n";
		print(s); //avoid additional line feed
	}

	private static String now(DateFormat format) {
		String rv;
		Date date = new Date();
		rv = format.format(date);
		return rv;
	}

	private static final DateFormat dfltNowFormat = new SimpleDateFormat("yyMMdd HHmmss");
	private static String now() {
		return now(dfltNowFormat);
	}

}
