package database;

import java.util.Vector;

public interface DbaInterface {

	public DBResult executeSql(String sql);
	public void clearTable(String table);
	public void update(String table, String fields[], String[] values, String where) throws Exception;
	public DBResult executeInsert(String table, String fields, String... values);
	public long numOfRecords(String table);
	public boolean tableExist(String table);
	public boolean tableDrop(String table);
	public boolean tableDelete(String table);
	public void vacuum(String table);
	public Vector<String[]> getAllRecords(String table);

	public void setDebug(int lvl);
}
