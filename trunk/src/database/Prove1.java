package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Prove1 {

	static final class Db {
		String driver;
		String protocol;
		protected Db(String driver, String protocol) {
			this.driver = driver;
			this.protocol = protocol;
		}
		Db H2() {
			return new Db("org.h2.Driver", "jdbc:h2:");
		}
		Db HSQLDB() {
			return new Db("org.hsqldb.jdbcDriver", "jdbc:hsqldb:");
		}
	}

	public static void main(String[] a) throws Exception {
		System.out.println("starting");
		Class.forName("org.h2.Driver");
		System.out.println("Driver loaded");
		Connection conn = DriverManager.getConnection("jdbc:h2:db/h2/test", "sa", "");
		System.out.println("Database connected");

		Statement stmt = conn.createStatement();
		stmt.execute("CREATE TABLE Tags(ID INT PRIMARY KEY, NAME VARCHAR(32))");
		ResultSet rs = stmt.executeQuery("SELECT * FROM Tags");
		while (rs.next()) {
			int x = rs.getInt("ID");
			String s = rs.getString("NAME");
			System.out.println("ID=" + x + ", NAME=" + s);
		}
	}

}
