package gui.tables.bigtable;

import javax.swing.table.AbstractTableModel;

public class BigTableModel extends AbstractTableModel {
	private static final long serialVersionUID = -1118937855310094834L;

	private final static int rows = 100000;
	private final static int cols = 15;
	private Object[][] values;
	private int cntr;

	public BigTableModel() {
		super();
		values = new Object[rows][cols];
	}

	public int getRowCount() {
		return rows;
	}

	public int getColumnCount() {
		return cols;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		Object rv = values[rowIndex][columnIndex];
		if (rv == null) {
			rv = values[rowIndex][columnIndex] = String.valueOf(rowIndex * cols + columnIndex);
//			System.out.println("" + cntr++ + ", " + rowIndex + ", " + columnIndex);
		}
		return rv;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		values[rowIndex][columnIndex] = aValue;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}
}
