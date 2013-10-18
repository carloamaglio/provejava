package database;

/**
 * 
 * @author Dott. Ing. Carlo Amaglio
 *
 */
public class Derby extends JdbcDba {
	static DbConfig db = new DbConfig("org.apache.derby.jdbc.EmbeddedDriver", "jdbc:derby:");

	static {
		try {
			Class.forName(db.driver);
			System.out.println("Loaded the appropriate driver: '" + db.driver + "'");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Derby(String fileName) throws Exception {
		super(db, fileName);
	}

}
