package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * 
 * @author Dott. Ing. Carlo Amaglio
 *
 */
abstract class JdbcDba extends Dba {

	Connection conn; 

	public JdbcDba(DbConfig dbcfg, String fileName) throws Exception {
		super(fileName);
		conn = DriverManager.getConnection(dbcfg.protocol + fileName + ";create=true", "", "");
		System.out.println("Connected to database '" + fileName + "'");
	}

	synchronized public DBResult executeSql(String sql) {
		DBResult result = new DBResult(sql);
		result.status = DBResult.States.EXECUTING;

		try {
            Statement s = conn.createStatement();
            if (s.execute(sql)==true) {
            	ResultSet rs = s.getResultSet();
            	int nc = rs.getMetaData().getColumnCount();
            	while (rs.next()) {
                	String[] row = new String[nc];
                	for (int i=0; i<nc; i++) {
                		row[i] = rs.getString(i+1);
                	}
                	result.add(row);
            	}
            } else {
//            	int c = s.getUpdateCount();
            }
			result.status = DBResult.States.EXECUTED;
		} catch (Exception e) {
//			if (debugLvl!=0) e.printStackTrace(System.out);
			e.printStackTrace(System.out);
			result.status = DBResult.States.ERROR;
		}
		if (debugLvl!=0) result.debug();
		return result;
	}

	public boolean tableExist(String table) {
        ResultSet rs = null;
		try {
            Statement s = conn.createStatement();
			rs = s.executeQuery("SELECT COUNT(*) FROM " + table);
		} catch (Exception e) {
			rs = null;
		}
		System.out.println("rs=" + rs);
		return rs!=null;
	}

	public void vacuum(String table) {
		String SQL = "VACUUM " + table;
		DBResult dbr = executeSql(SQL);
		if (dbr.status == DBResult.States.ERROR)
			Console.printWithTrace("ERRORE: " + SQL);
	}


	static final class DbConfig {
		final String driver;
		final String protocol;
		protected DbConfig(String driver, String protocol) {
			this.driver = driver;
			this.protocol = protocol;
		}
	}

}
