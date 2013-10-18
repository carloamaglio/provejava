package jOpenDoc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Set;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

public class ODS {

	public static void main(String[] args) throws Exception {
		ODS ods = new ODS();
		ods.create3(
				"it_IT", 
				"da_DK", 
				"de_DE", 
				"en_EN", 
				"es_ES", 
				"fr_FR", 
				"hu_HU", 
				"nl_NL", 
				"pl_PL", 
				"pt_PT", 
				"ru_RU", 
				"sv_SE", 
				"tr_TR", 
				"zh_CN"
		);
//		ods.create3("it_IT", "de_DE", "en_EN", "es_ES", "fr_FR");//, "nl_NL", "pl_PL", "pt_PT", "ru_RU", "sv_SE", "tr_TR", "zh_CN");
	}

	public void create() throws Exception {
		// Create the data to save.
		final Object[][] data = new Object[6][2];
		data[0] = new Object[] { "January", 1 };
		data[1] = new Object[] { "February", 3 };
		data[2] = new Object[] { "March", 8 };
		data[3] = new Object[] { "April", 10 };
		data[4] = new Object[] { "May", 15 };
		data[5] = new Object[] { "June", 18 };

		String[] columns = new String[] { "Month", "Temp" };

		TableModel model = new DefaultTableModel(data, columns);

		// Save the data to an ODS file and open it.
		final File file = new File("temperature.ods");
		SpreadSheet.createEmpty(model).saveAs(file);

//		OOUtils.open(file);
	}

	static public class Prop extends Properties {
		private static final long serialVersionUID = -2574886094390168354L;

		public Prop() {
			super();
		}

		class Item implements Comparable<Item> {
			public String k;
			public String v;
			public Item(String k, String v) {
				this.k = k;
				this.v = (v!=null) ? v : "";
			}
			@Override
			public int compareTo(Item o) {
				return k.compareToIgnoreCase(o.k);
			}
		}

		public void storeToFile(String filename) throws Exception {
			store(new FileOutputStream(filename), "");
//			try {
//				PrintStream stream = new PrintStream(new FileOutputStream(filename));
//				Item[] t = new Item[this.size()];
//				Set<Object> keys = keySet();
//				int i = 0;
//				for (Object k : keys) {
//					t[i++] = new Item((String)k, this.getProperty((String)k));
//				}
//				Arrays.sort(t);
//				for (i=0; i<t.length; i++) {
//					stream.println(t[i].k + "=" + t[i].v);
//				}
//				stream.flush();
//				stream.close();
//			} catch (Exception e) {}
		}
	}

	public static Properties getLanguage(String fn) throws Exception {
		Prop src = new Prop();
		src.load(new FileInputStream(fn));
		return src;
	}

	public void create2() throws Exception {

		// Create the data to save.
		final Object[][] data = new Object[1][2];
		data[0] = new Object[] { "k", "t" };
		String[] columns = new String[] { "key", "ru_RU" };
		TableModel model = new DefaultTableModel(data, columns);

		SpreadSheet ss = SpreadSheet.createEmpty(model);
		Sheet s = ss.getSheet(0);

		Properties l = getLanguage("language_ru_RU.properties");
		Set<Object> keys = l.keySet();
		s.setRowCount(l.size());
		int row = 1;
		for (Object k_ : keys) {
			if (k_!=null && (k_ instanceof String) && ((String)k_).length()>0) {
				String k = (String)k_;
				String v = l.getProperty(k);
//				System.out.println("k='" + k + "', v='" + v + "'");
				s.setValueAt(k, 0, row);
				s.setValueAt(v, 1, row);
				row++;
			}
		}


		// Save the data to an ODS file and open it.
		final File file = new File("language.ods");
		ss.saveAs(file);

//		OOUtils.open(file);
	}

	public final static class Language {
		String name;
		Properties l;
		public Language(String name) throws Exception {
			this.name = name;
			l = getLanguage(fn());
		}
		String fn() {
			return "language_" + name + ".properties";
		}
	    public Set<Object> keySet() {
	    	return l.keySet();
	    }
	    public String get(Object k) {
	    	return (String)l.get(k);
	    }
	}

	public final static class Languages {
		static final int MAXLANGUAGES = 15;
		LinkedList<Language> ll;
		Hashtable<String,String[]> tbl;
		public Languages() {
			tbl = new Hashtable<String,String[]>(10000);
			ll = new LinkedList<Language>();
		}

		public void loadLanguages(String[] names) throws Exception {
			for (String name : names) {
				loadLanguage(name);
			}
		}

		public void loadLanguage(String name) throws Exception {
			if (!isLoaded(name)) {
				int i = ll.size();
				Language l = new Language(name);
				ll.add(l);
				for (Object _k : l.keySet()) {
					String t = l.get(_k);
					if (!tbl.containsKey(_k)) {
						String[] a = new String[MAXLANGUAGES];
						tbl.put((String)_k, a);
					}
					tbl.get(_k)[i] = t;
				}
			}
		}

		private boolean isLoaded(String name) {
			for (Language l : ll) {
				if (l.name.equals(name)) return true;
			}
			return false;
		}

	}

	public void create3(String... names) throws Exception {

		// Create the data to save.
		final Object[][] data = new Object[1][2];
		data[0] = new Object[] { "k", "t" };
		String[] columns = new String[] { "key", "ru_RU" };
		TableModel model = new DefaultTableModel(data, columns);

		SpreadSheet ss = SpreadSheet.createEmpty(model);
		Sheet s = ss.getSheet(0);


		s.setColumnCount(names.length+1);
		System.out.println("columnCount=" + (names.length+1));
		Languages languages = new Languages();
		languages.loadLanguages(names);

		// riga di intestazione
		{
			int col = 1;
			for (Language l : languages.ll) {
				s.setValueAt(l.name, col, 0);
				col++;
			}
		}

		Set<String> keys = languages.tbl.keySet();
		s.setRowCount(keys.size()+1);
//		s = null;
		System.out.println("rowCount=" + (keys.size()+1));

		TextOutputFile csv = null;//TextOutputFile.create("languages.csv");
		int row = 1;
		for (String k : keys) {
			if (
					k!=null 
					&& k.length()>0 
					&& !k.startsWith("alarm_h") 
					&& !k.startsWith("alarm_m")
			) {
				String[] a = languages.tbl.get(k);
				String ref = a[0];

				// se la lingua di riferimento e' presente procedo
				if (
						ref!=null 
						&& ref.length()>0 
						&& !ref.startsWith("???")
				) {
					if ((row % 1000)==0) System.out.println("" + row + "...");
					if (s!=null) s.setValueAt(true ? k : "k", 0, row);
					if (csv!=null) csv.print(k + "=");
					for (int i=0, col=1; i<names.length; i++, col++) {
						String v = a[i];
//						System.out.println("k='" + k + "', v='" + v + "'");
						if (v!=null && v.length()>0) {
							if (v.startsWith("???")) v="???";
							if (s!=null) s.setValueAt(true ? v : "v", col, row);
							if (csv!=null) csv.print(v + "\\k");
//							if (v==null || v.startsWith("???")) if (s!=null) s.getCellAt(col, row).setBackgroundColor(Color.red);

//							try {
//							} catch (Exception e) {
//								System.out.println("col=" + col + ", row=" + row + ", k='" + k + "'");
//								throw e;
//							}
						} else {
//							System.out.println("col=" + col + ", row=" + row + ", k='" + k + "', a.len=" + a.length);
//							System.out.println("  v='" + (v==null ? "null" : v) + "'");

//							try {
//								s.setValueAt(new String("-"), col, row);
//								s.getCellAt(col, row).setBackgroundColor(Color.red);
//							} catch (Exception e) {
//								for (int ii=0; ii<=i; ii++) System.out.print("'" + (a[ii]==null?"null":a[ii]) + "', ");
//								System.out.println();
//								System.out.println("col=" + col + ", row=" + row + ", k='" + k + "', a.len=" + a.length);
//								System.out.println("  v='" + (v==null ? "null" : v) + "'");
//								System.out.println();
//								throw e;
//							}
						}
					}
					if (csv!=null) csv.println();
					row++;
				}
			}
		}

		if (csv!=null) csv.close();

		// Save the data to an ODS file and open it.
		final File file = new File("languages.ods");
		ss.saveAs(file);

//		OOUtils.open(file);
	}

}
