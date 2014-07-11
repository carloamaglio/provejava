package internet.http;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class SaveURL {
	public static void saveUrl(String filename, String urlString) throws MalformedURLException, IOException {
		BufferedInputStream in = null;
		FileOutputStream fout = null;
		try {
			in = new BufferedInputStream(new URL(urlString).openStream());
			fout = new FileOutputStream(filename);

			byte data[] = new byte[1024];
			int count;
			while ((count = in.read(data, 0, 1024)) != -1) {
				fout.write(data, 0, count);
			}
		} finally {
			if (in != null) in.close();
			if (fout != null) fout.close();
		}
	}

	public static void main(String[] args) throws MalformedURLException, IOException {
		for (int i=2189; i<3000; i++) {
			String fn = String.valueOf(i) + ".pdf";
			try {
				saveUrl("c:/temp/carlo/" + fn, "http://hfs1.duytan.edu.vn/upload/ebooks/" + fn);
				System.out.println(fn + " ok");
			} catch (Exception e) {
				System.out.print(".");
			}
		}
	}
}
