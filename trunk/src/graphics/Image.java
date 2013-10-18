package graphics;

import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;


public class Image {

	public static void list() {
		// Get list of unique supported read formats
	    String[] formatNames = ImageIO.getReaderFormatNames();
	    formatNames = unique(formatNames);
	    // e.g. png jpeg gif jpg
	    print("read formats:", formatNames);
	    
	    // Get list of unique supported write formats
	    formatNames = ImageIO.getWriterFormatNames();
	    formatNames = unique(formatNames);
	    // e.g. png jpeg jpg
	    print("write formats:", formatNames);

	    // Get list of unique MIME types that can be read
	    formatNames = ImageIO.getReaderMIMETypes();
	    formatNames = unique(formatNames);
	    // e.g image/jpeg image/png image/x-png image/gif
	    print("MIME read formats:", formatNames);

	    // Get list of unique MIME types that can be written
	    formatNames = ImageIO.getWriterMIMETypes();
	    formatNames = unique(formatNames);
	    // e.g. image/jpeg image/png image/x-png
	    print("MIME write formats:", formatNames);
	}

	static void print(String title, String[] names) {
    	System.out.println(title + "  -----------------");
	    for (String s : names) {
	    	System.out.println(s);
	    }
    	System.out.println();
	}

	// Converts all strings in 'strings' to lowercase
    // and returns an array containing the unique values.
    // All returned values are lowercase.
    public static String[] unique(String[] strings) {
        Set<String> set = new HashSet<String>();
        for (int i=0; i<strings.length; i++) {
            String name = strings[i].toLowerCase();
            set.add(name);
        }
        return (String[])set.toArray(new String[0]);
    }

    public static void main(String[] args) {
    	list();
    }

}
