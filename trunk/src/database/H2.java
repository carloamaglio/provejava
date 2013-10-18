/*
 * Created on 15-set-2005
 *
 */
package database;



/**
 * @author Dott. Ing. Carlo Amaglio - BMB S.p.A.
 *
 */
public class H2 extends JdbcDba {
	static DbConfig db = new DbConfig("org.h2.Driver", "jdbc:h2:");

	static {
		try {
			Class.forName(db.driver);
			System.out.println("Loaded the appropriate driver: '" + db.driver + "'");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public H2(String fileName) throws Exception {
		super(db, fileName);
	}

//	public void closeDB() {
//		try {
//			super.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

}
