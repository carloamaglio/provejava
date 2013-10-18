package temp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerializationTest {

	static void dump(double[][] d) {
		if (d!=null) {
		    System.out.println("" + d.length);
		    for (int i=0; i<d.length; i++) {
	    	    System.out.print("" + d[i].length + ": ");
		    	for (int j=0; j<d[i].length; j++) {
		    	    System.out.print("" + d[i][j] + ", ");
		    	}
	    	    System.out.println("---");
		    }
		} else {
		    System.out.println("d is null!");
		}
	}

	static class ReferenceData implements Serializable {
		private static final long serialVersionUID = 1L;

		final String filename;
		boolean dataAvailable;
		boolean dataLoaded;
		double[][] data;

		public ReferenceData(String filename) {
			this.filename = filename;
		}

		public boolean dataAvailable() {
			if (!dataLoaded) getData();
			return dataAvailable;
		}

		public double[][] getData() {
			if (!dataLoaded) {
				try {
				    ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
				    data = (double[][])in.readObject();
				    in.close();
				    dataAvailable = true;
				} catch (Exception e) {
				}
				dataLoaded = true;
			}
			return data;
		}

		public void storeData(double[][] data) {
			try {
				this.data = data;
				ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
			    out.writeObject(data);
			    out.close();
			    dataLoaded = dataAvailable = true;
			} catch (Exception e) {
			}
		}

		public void eraseData() {
			File f = new File(filename);
			f.delete();
			dataLoaded = dataAvailable = false;
			data = null;
		}
	}

	public static void main(String[] args) throws Exception {
		ReferenceData rd = new ReferenceData("sertest.out");
		double[][] data;
//	    rd.eraseData();
		if (!rd.dataAvailable()) {
		    System.out.println("data dosn't exist: generating random data...");
			int np = 30;
			data = new double[2][np];
			for (int i=0; i<np; i++) {
				data[0][i] = 35+i;
				data[1][i] = Math.random() * 30.0 + 30.0;
			}
			rd.storeData(data);
		    dump(data);
		} else {
		    System.out.println("data already exist.");
		}

	    dump(rd.getData());
//	    rd.eraseData();
//	    dump(rd.getData());
	}

}
