package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Prove {

	static final class DbConfig {
		static final DbConfig derby = 
			new DbConfig("org.apache.derby.jdbc.EmbeddedDriver", "jdbc:derby:");
		static final DbConfig h2 = 
			new DbConfig("org.h2.Driver", "jdbc:h2:");
		final String driver;
		final String protocol;
		protected DbConfig(String driver, String protocol) {
			this.driver = driver;
			this.protocol = protocol;
		}
	}

	public static void main(String[] args) {
		DbConfig db;
		switch (1) {
			case 0:
				db = DbConfig.derby;
				break;
			case 1:
				db = DbConfig.h2;
				break;
			default:
				db = DbConfig.derby;
			break;
		} 

		try {
			Class.forName(db.driver);
			System.out.println("Loaded the appropriate driver: '" + db.driver + "'");

			Connection conn = 
				DriverManager.getConnection(
						db.protocol + "db/test;create=true", "sa", "");
            System.out.println("Connected to and created database");

            Statement s = conn.createStatement();

            try {
                String createTable = "CREATE TABLE Tags " +
                "(ID INTEGER PRIMARY KEY, NAME VARCHAR(32), LOWER VARCHAR(8))";
                s.execute(createTable);
                System.out.println("Created table Tags");
            } catch (Exception e) {
            	e.printStackTrace();
            }

            try {
                s.executeUpdate("INSERT INTO Tags VALUES (1, 'QtAltaPrs', '0')");
                s.executeUpdate("INSERT INTO Tags VALUES (2, 'QtEstr', '0')");
                System.out.println("Insert done");
            } catch (Exception e) {
            	e.printStackTrace();
            }

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
